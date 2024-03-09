import React, { useState, useEffect } from 'react';
import { useParams } from 'react-router-dom';
import AuthService from '../Utils/AuthService';
import Navbar from '../Navbar/Navbar';
import UserService from '../Utils/UserService';

const UserPage = () => {
  // const { username } = useParams();
  const [username, setUsername] = useState('');
  const [matchupRequests, setMatchupRequests] = useState([]);
  const [loading, setLoading] = useState(true);
  const [betSlips, setBetSlips] = useState([]);
  const [selectedBetslip, setSelectedBetslip] = useState(null);
  const [selectedBetSlipId, setSelectedBetSlipId] = useState(null);

  const fetchMatchupRequests = async () => {
    try {
        const currentUser = AuthService.getCurrentUser();
        setUsername(currentUser.username);
        const response = await UserService.getPendingMatchupRequests(currentUser.username);
        // Ensure lazy-loaded associations are initialized
        const initializedMatchupRequests = response.data.map(matchup => ({
          ...matchup,
          user1: { ...matchup.user1 },
          betSlipUser1: { ...matchup.betSlipUser1 }
          // Repeat for other lazy-loaded associations if necessary
        }));
        setMatchupRequests(initializedMatchupRequests);
        setLoading(false);
    } catch (error) {
        console.error("Error getting pending matchup requests:", error.message);
    }
  };
    useEffect(() => {
      fetchMatchupRequests();
    }, []);

  const handleAcceptMatchup = async (matchupId, selectedBetSlipId) => {
      try {
          setSelectedBetSlipId(selectedBetslip.id);
          await UserService.acceptMatchup(matchupId, selectedBetSlipId);
          // Reload matchup requests after accepting
          fetchMatchupRequests();
      } catch (error) {
          console.error("Error accepting matchup request:", error.message);
      }
  };

  useEffect(() => {
    // Fetch user's saved betslips
    const fetchSavedBetslips = async () => {
        try {
            const currentUser = AuthService.getCurrentUser();
            const betSlipsData = await UserService.getSavedBetSlips(currentUser.username);
            setBetSlips(betSlipsData.data);
        } catch (error) {
            console.error("Error getting Bet Slips");
        }
    };

    fetchSavedBetslips();
}, []);

  // const handleBetslipSelection = (e) => {
  //   setSelectedBetslip(e.target.value);
  // };

  const handleBetslipSelection = (e) => {
    const selectedId = e.target.value;
    setSelectedBetslip(selectedId);
    setSelectedBetSlipId(selectedId);
  };

  const handleDeclineMatchup = async (matchupId, selectedBetSlipId) => {
      try {
          await UserService.declineMatchup(matchupId, selectedBetSlipId);
          // Reload matchup requests after declining
          fetchMatchupRequests();
      } catch (error) {
          console.error("Error declining matchup request:", error.message);
      }
  };

  const getUsernameById = async (userId) => {
    try {
      const response = await UserService.getUsernameById(userId);
      return response.data.username;
    } catch (error) {
      console.error("Error getting username by ID:", error.message);
      return null;
    }
  };

  return (
      <div>
          <h1>Welcome {username}</h1>
          <Navbar />
          <h2>Pending Matchup Requests</h2>
          {loading ? (
              <p>Loading...</p>
          ) : (
              <ul>
                  {matchupRequests.length === 0 ? (
                      <p>No pending matchup requests.</p>
                  ) : (
                      matchupRequests.map((request) => (
                          <li key={request.id}>
                              <div>From: {request.user1.username}</div>

                              <label>
                                  Select Your Betslip:
                                  <select value={selectedBetslip} onChange={handleBetslipSelection}>
                                      <option value="">Select Betslip</option>
                                      {betSlips.map((betslip) => (
                                          <option key={betslip.id} value={betslip.id}>
                                              {betslip.id} {/* Adjust this based on the structure of your betslip object */}
                                          </option>
                                      ))}
                                  </select>
                              </label>
                              <div>Bet Slip: {request.betSlipUser1.id}</div>
                              <button onClick={() => handleAcceptMatchup(request.id, selectedBetSlipId)}>Accept</button>
                              <button onClick={() => handleDeclineMatchup(request.id)}>Decline</button>
                          </li>
                      ))
                  )}
              </ul>
          )}
      </div>
  );
};

export default UserPage;