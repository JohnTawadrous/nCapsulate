import React, { useState, useEffect } from 'react';
import axios from 'axios';
import AuthService from '../Utils/AuthService';
import MenuBar from '../MenuBar/MenuBar';
import './Matchups.css';
import UserService from '../Utils/UserService';
import BetSlipDetailsModal from '../Modal/BetSlipDetailsModal';

const Matchups = ({ userId }) => {
  const [activeMatchups, setActiveMatchups] = useState([]);
  const [completedMatchups, setCompletedMatchups] = useState([]);
  const [username, setUsername] = useState('');
  const [selectedMatchup, setSelectedMatchup] = useState(null);
  const [showBetSlip1Details, setShowBetSlip1Details] = useState(false);
  const [showBetSlip2Details, setShowBetSlip2Details] = useState(false);

  useEffect(() => {
      const fetchActiveMatchups = async () => {
          try {
              const currentUser = AuthService.getCurrentUser();
              setUsername(currentUser.username);
              const response = await axios.get(`/api/matchups/active/${currentUser.id}`);
              setActiveMatchups(response.data);
          } catch (error) {
              console.error('Error fetching active matchups:', error);
          }
      };

      const fetchCompletedMatchups = async () => {
          try {
              const currentUser = AuthService.getCurrentUser();
              setUsername(currentUser.username);
              const response = await axios.get(`/api/matchups/completed/${currentUser.id}`);
              setCompletedMatchups(response.data);
          } catch (error) {
              console.error('Error fetching completed matchups:', error);
          }
      };

      fetchActiveMatchups();
      fetchCompletedMatchups();
  }, []);

  const handleViewMatchupDetails = async (matchup) => {
    setSelectedMatchup(matchup);

    const betSlip1Details = await UserService.getBetSlip(matchup.betSlipUser1.id)
    const betSlip2Details = await UserService.getBetSlip(matchup.betSlipUser2.id)
    
    setShowBetSlip1Details(betSlip1Details);
    setShowBetSlip2Details(betSlip2Details);
};

  return (
        <div>
        <MenuBar username={username} />
        <div className='matchups-container'>
          
          <div className='details-modal1'>
            {/* Render modals for bet slip details */}
            <BetSlipDetailsModal isOpen={showBetSlip1Details} betSlip={selectedMatchup ? selectedMatchup.betSlipUser1.id : null} onClose={() => setShowBetSlip1Details(false)} />
          </div>
          
          <div className='matchups-grid'>
            <h1>Active Matchups</h1>
            {activeMatchups.map(matchup => (
              <div className='matchup-box' key={matchup.id}>
                <div className="usernames-container">
                  <h5>{matchup.user1.username}</h5>
                  
                  <h5>{matchup.user2.username}</h5>
                </div>
                <h4 className="vs">vs</h4>
                <h3>{matchup.user1CorrectBets}  :  {matchup.user2CorrectBets}</h3>
                <button className='matchup-details-button' onClick={() => handleViewMatchupDetails(matchup)}>Matchup Details</button>
              </div>
            ))}
          
            <h1>Completed Matchups</h1>
            {completedMatchups.map(matchup => (
              <div className='matchup-box' key={matchup.id}>
                <div className="usernames-container">
                  <h5>{matchup.user1.username}</h5>
                  
                  <h5>{matchup.user2.username}</h5>
                </div>
                <h4 className="vs">vs</h4>
                <h3>{matchup.user1CorrectBets}  :  {matchup.user2CorrectBets}</h3>
                <button className='matchup-details-button' onClick={() => handleViewMatchupDetails(matchup)}>Matchup Details</button>
              </div>
            ))}
          </div>
      
          <div className='details-modal2'>
            <BetSlipDetailsModal isOpen={showBetSlip2Details} betSlip={selectedMatchup ? selectedMatchup.betSlipUser2.id : null} onClose={() => setShowBetSlip2Details(false)} />
          </div>
        </div>
      </div>
    );
  };

export default Matchups;