import React, { useEffect, useState } from 'react';
import './LiveOdds.css';
import Sidebar from '../Sidebar/Sidebar';
import teamLogos from '../Assets/teamLogos/teamLogos';
import MenuBar from '../MenuBar/MenuBar';
import AuthService from '../Utils/AuthService';
import axios from 'axios';
import UserService from '../Utils/UserService';

const LiveOdds = () => {
  const [liveOdds, setLiveOdds] = useState(null);
  const [content, setContent] = useState("");
  const [selectedSpreadOptions, setSelectedSpreadOptions] = useState({});
  const [selectedOverUnderOptions, setSelectedOverUnderOptions] = useState({});
  const [username, setUsername] = useState('');
  const [funds, setFunds] = useState('$1000');
  const jwtToken = localStorage.getItem("jwtToken")

  useEffect(() => {
    // Fetch live odds
    const fetchLiveOdds = async () => {
      try {
        // Get JWT token from local storage
        const currentUser = AuthService.getCurrentUser();

        // Fetch user data based on the current user
        // const userData = await UserService.getUserByUsername(currentUser.username);

        // Set username and funds based on user data
        setUsername(currentUser.username);
        setFunds(1000);
        

        // Fetch live odds with authorization header
        const response = await UserService.getLiveOdds();

        // Set live odds state
        setLiveOdds(response.data);
      } catch (error) {
        console.error('Error fetching live odds:', error);
      }
    };

    fetchLiveOdds();
  }, []);

  const handleSpreadSelection = (gameId, outcome, homeTeam, awayTeam, selected) => {
    setSelectedSpreadOptions((prevOptions) => {
        const newOptions = {
            ...prevOptions,
            [gameId]: {
                outcome: outcome.name,
                point: outcome.point,
                homeTeam,
                awayTeam,
                selected: selected // Toggle the selected status
            },
        };
        console.log('Spread option selected:', newOptions);
        return newOptions;
    });
};
  
const handleOverUnderSelection = (gameId, outcome, homeTeam, awayTeam, selected) => {
  setSelectedOverUnderOptions((prevOptions) => {
      const newOptions = {
          ...prevOptions,
          [gameId]: {
              outcome: outcome.name,
              point: outcome.point,
              homeTeam,
              awayTeam,
              selected: selected
          },
      };
      console.log('Over/Under option selected:', newOptions);
      return newOptions;
  });
};

const handleConfirmBet = async () => {
  if (Object.keys(selectedSpreadOptions).length === 0 && Object.keys(selectedOverUnderOptions).length === 0) {
    console.log('No selections made. Please select some options before confirming the bet.');
    return;
  }

  console.log('Confirmed selections:', selectedSpreadOptions, selectedOverUnderOptions);

  try {
    const selectedBets = [
      ...Object.values(selectedSpreadOptions).map(option => option.selected),
      ...Object.values(selectedOverUnderOptions).map(option => option.selected)
    ];

    await axios.post('http://localhost:8080/api/betslips/save', {
      username,
      selectedBets,
    });

    console.log('Bet slip saved successfully');
  } catch (error) {
    console.error('Error saving bet slip:', error);
  }
};

  return (

<div>
    <MenuBar username={username} funds={funds} />
    <div className='container'>
        <div className="games-grid">
          {liveOdds !== null ? (
            liveOdds.map((game) => (
              <div key={game.id} className="game-box">
                <h3>{game.sport_title}</h3>
                <p>
                  {new Date(game.commence_time).toLocaleDateString('en-US', {
                    day: 'numeric',
                    month: 'long',
                  })},{' '}
                  {new Date(game.commence_time).toLocaleTimeString([], {
                    hour: 'numeric',
                    minute: '2-digit',
                  })}
                </p>
                <p className='teams'> 
                  <img src={teamLogos[game.home_team]} alt={game.home_team} className="team-logo" />
                  {game.home_team} <br /> vs <br />
                  <img src={teamLogos[game.away_team]} alt={game.away_team} className="team-logo" />
                  {game.away_team}
                </p>
                {game.bookmakers.map((bookmaker) => (
                  <div key={bookmaker.key}>
                    {bookmaker.markets.map((market) => (
                      <div key={market.key}>
                        <h4>{market.key}</h4>
                        {market.outcomes.map((outcome) => (
                          <button 
                            key={outcome.name}
                            className={`select-button ${selectedSpreadOptions[game.id]?.selected === outcome.name || selectedOverUnderOptions[game.id]?.selected === outcome.name ? 'selected' : ''}`}
                            onClick={() => {
                              if (market.key === 'spreads') {
                                handleSpreadSelection(game.id, outcome, game.home_team, game.away_team, outcome.name);
                              } else {
                                handleOverUnderSelection(game.id, outcome, game.home_team, game.away_team, outcome.name);
                              }
                            }}
                          >
                            {outcome.name}: {outcome.point}
                          </button>
                        ))}
                      </div>
                    ))}
                  </div>
                ))}
              </div>
            ))
          ) : (
            <p>Loading live odds...</p>
          )}
        </div>
        <Sidebar
          selectedSpreadOptions={selectedSpreadOptions}
          selectedOverUnderOptions={selectedOverUnderOptions}
          onConfirmBet={handleConfirmBet}
        />
    </div>
  </div>
  );
};

export default LiveOdds;