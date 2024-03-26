import React, { useState, useEffect } from 'react';
import axios from 'axios';
import AuthService from '../Utils/AuthService';
import MenuBar from '../MenuBar/MenuBar';
import './Matchups.css';

const Matchups = ({ userId }) => {
    const [activeMatchups, setActiveMatchups] = useState([]);
    const [username, setUsername] = useState('');
  
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
  
      fetchActiveMatchups();
    }, []);

  return (
    <div>
        <MenuBar username={username} />
        <h2>Active Matchups</h2>
        <div className='matchups-container'>
          <div className='matchups-grid'>
            {activeMatchups.map(matchup => (
              <div className='matchup-box' key={matchup.id}>
                {/* Render matchup details here */}
                <p>{matchup.user1.username} vs {matchup.user2.username}</p>
                <p>{matchup.user1CorrectBets} - {matchup.user2CorrectBets}</p>
              </div>
            ))}
            </div>
      </div>
    </div>
  );
};

export default Matchups;