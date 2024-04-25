import React, { useState, useEffect } from 'react';
import { useParams } from 'react-router-dom';
import AuthService from '../Utils/AuthService';
import Navbar from '../Navbar/Navbar';
import UserService from '../Utils/UserService';
import './UserPage.css' ;
import BetSlipDetailsModal from '../Modal/BetSlipDetailsModal';

const UserPage = () => {
  // const { username } = useParams();
  const [username, setUsername] = useState('');
  const [matchupRequests, setMatchupRequests] = useState([]);
  const [loading, setLoading] = useState(true);
  const [betSlips, setBetSlips] = useState([]);
  const [selectedBetslip, setSelectedBetslip] = useState(null);
  const [selectedBetSlipId, setSelectedBetSlipId] = useState(null);
  const [showBetSlipDetails, setShowBetSlipDetails] = useState(false);
  const [selectedBetSlipDetails, setSelectedBetSlipDetails] = useState(null);

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
            const currentDate = new Date();
            const betSlipsData = await UserService.getSavedBetSlipsToday(currentUser.username, currentDate);
            setBetSlips(betSlipsData.data);
        } catch (error) {
            console.error("Error getting Bet Slips");
        }
    };

    fetchSavedBetslips();
}, []);

  const handleBetslipSelection = (e) => {
    const selectedId = e.target.value;
    setSelectedBetslip(selectedId);
    setSelectedBetSlipId(selectedId);
  };

  const handleViewBetSlipDetails = (betslip) => {
    setSelectedBetSlipDetails(betslip);
    setShowBetSlipDetails(true);
};

    // Function to close bet slip details modal
    const handleCloseBetSlipDetails = () => {
        setShowBetSlipDetails(false);
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
        <img src="/Okay Bet mate (8).png" alt="Logo" className='logo' />
          <h3>{username}</h3>
        
          <Navbar />
          <h2>Pending Matchup Requests</h2>
          {loading ? (
              <p>Loading...</p>
          ) : (
              <div className='request-container'>
                  {matchupRequests.length === 0 ? (
                      <p>No pending matchup requests.</p>
                  ) : (
                      matchupRequests.map((request) => (
                          <div className='matchup-request-box' key={request.id}>
                              <h3>From: {request.user1.username}</h3>

                              <label className="select">
                                  {/* Select Your Betslip: */}
                                  <select value={selectedBetslip} onChange={handleBetslipSelection}>
                                      <option value="">Select Betslip</option>
                                      {betSlips.map((betslip) => (
                                          <option key={betslip.id} value={betslip.id}>
                                              {betslip.id} {/* Adjust this based on the structure of your betslip object */}
                                          </option>
                                      ))}
                                  </select>
                              </label>
                                {selectedBetslip && (
                                    <button className='view-slip-button' onClick={() => handleViewBetSlipDetails(selectedBetslip)}>View Bet Slip Details</button>
                                )}
                              <button className='accept-match-button' onClick={() => handleAcceptMatchup(request.id, selectedBetSlipId)}>Accept</button>
                              <button className='decline-match-button' onClick={() => handleDeclineMatchup(request.id)}>Decline</button>

                              {/* Modal to display bet slip details */}
                              
                          </div>
                      ))
                  )}
                 
                    
              </div>
          )}
           <div className='user-slip-modal'>
                        <BetSlipDetailsModal
                            isOpen={showBetSlipDetails}
                            betSlip={selectedBetSlipDetails}
                            onClose={handleCloseBetSlipDetails}
                         />
                        </div>
                    
      </div>
  );
};

export default UserPage;