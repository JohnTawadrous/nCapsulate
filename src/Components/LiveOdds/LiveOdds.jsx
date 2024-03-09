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
        // Get curent user
        const currentUser = AuthService.getCurrentUser();

        // Set username and funds based on user data
        setUsername(currentUser.username);
        setFunds(1000);
        
        const response = await UserService.getLiveOdds();

        // Set live odds state
        setLiveOdds(response.data);
      } catch (error) {
        console.error('Error fetching live odds:', error);
      }
    };

    fetchLiveOdds();
  }, []);

  const handleSpreadSelection = (gameId, outcome, homeTeam, awayTeam, selected, marketKey) => {
    setSelectedSpreadOptions((prevOptions) => {
        const newOptions = {
            ...prevOptions,
            [gameId]: {
                gameId,
                outcome: outcome.name,
                point: outcome.point,
                homeTeam,
                awayTeam,
                selected: selected, // Toggle the selected status
                marketKey: marketKey
            },
        };
        console.log('Spread option selected:', newOptions);
        return newOptions;
    });
};
  
const handleOverUnderSelection = (gameId, outcome, homeTeam, awayTeam, selected, marketKey) => {
  setSelectedOverUnderOptions((prevOptions) => {
      const newOptions = {
          ...prevOptions,
          [gameId]: {
              gameId,
              outcome: outcome.name,
              point: outcome.point,
              homeTeam,
              awayTeam,
              selected: selected,
              marketKey: marketKey
          },
      };
      console.log('Over/Under option selected:', newOptions);
      return newOptions;
  });
};

const handleConfirmBet = async () => {
  // if (Object.keys(selectedSpreadOptions).length === 0 && Object.keys(selectedOverUnderOptions).length === 0) {
  //   console.log('No selections made. Please select some options before confirming the bet.');
  //   return;
  // }

  // const unselectedOptions = Object.values(selectedSpreadOptions)
  //   .concat(Object.values(selectedOverUnderOptions))
  //   .filter(option => !option.selected);

  // // If there are unselected options, display an error message
  // if (unselectedOptions.length > 0) {
  //   console.log('Please select options for all games before confirming the bet.');
  //   return;
  // }

  // Check if there are any games without selections
  // const gamesWithoutSelections = liveOdds.filter(game =>
  //   !(selectedSpreadOptions[game.id]?.selected || selectedOverUnderOptions[game.id]?.selected)
  // );

  // // If there are games without selections, display an error message
  // if (gamesWithoutSelections.length > 0) {
  //   console.log('Please select options for all games before confirming the bet.');
  //   return;
  // }

  const gamesWithoutSelections = liveOdds.filter(game => {
    const spreadSelected = selectedSpreadOptions[game.id]?.selected;
    const overUnderSelected = selectedOverUnderOptions[game.id]?.selected;
    return !(spreadSelected && overUnderSelected);
  });

  // If there are games without both spread and over/under selections, display an error message
  if (gamesWithoutSelections.length > 0) {
    console.log('Please select both spread and over/under options for all games before confirming the bet.');
    return;
  }

  console.log('Confirmed selections:', selectedSpreadOptions, selectedOverUnderOptions);

  try {
    const selectedBets = [
      ...Object.values(selectedSpreadOptions).map(option => ({
        gameId: option.gameId,
        outcome: option.selected,
        point: option.point,
        homeTeam: option.homeTeam,
        awayTeam: option.awayTeam,
        marketKey: option.marketKey
      })),
      ...Object.values(selectedOverUnderOptions).map(option => ({
        gameId: option.gameId,
        outcome: option.selected,
        point: option.point,
        homeTeam: option.homeTeam,
        awayTeam: option.awayTeam,
        marketKey: option.marketKey
      }))
    ];

    const betSlipRequest = {
      username,
      selectedBets
    };

    await axios.post('http://localhost:8080/api/betslips/save', betSlipRequest);

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
                                handleSpreadSelection(game.id, outcome, game.home_team, game.away_team, outcome.name, market.key);
                              } else {
                                handleOverUnderSelection(game.id, outcome, game.home_team, game.away_team, outcome.name, market.key );
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