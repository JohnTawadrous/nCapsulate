import React, { useEffect, useState } from 'react';
import './LiveOdds.css';
import Sidebar from '../Sidebar/Sidebar';
import teamLogos from '../Assets/teamLogos/teamLogos';
import MenuBar from '../MenuBar/MenuBar';

const LiveOdds = () => {
  const [liveOdds, setLiveOdds] = useState(null);
  const [selectedSpreadOptions, setSelectedSpreadOptions] = useState({});
  const [selectedOverUnderOptions, setSelectedOverUnderOptions] = useState({});
  // const selectedOptions = { ...selectedSpreadOptions, ...selectedOverUnderOptions };
  const username = 'John';
  const funds = '$1000';

  useEffect(() => {
    const fetchLiveOdds = async () => {
      try {
        const response = await fetch('http://localhost:8080/api/odds/live');

        if (!response.ok) {
          throw new Error('Network response was not ok');
        }

        const data = await response.json();
        setLiveOdds(data);
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

  const handleConfirmBet = () => {
    // Logic to handle confirmation of bet
  
    // Here, you can implement the logic to handle the confirmation of the bet.
    // For example, you can send the selected options to the server to process the bet.
  
    // First, you may want to check if the user has made any selections.
    if (Object.keys(selectedSpreadOptions).length === 0 && Object.keys(selectedOverUnderOptions).length === 0) {
      console.log('No selections made. Please select some options before confirming the bet.');
      // You can show a message to the user indicating that they need to select options before confirming the bet.
      return; // Exit the function if no selections have been made.
    }
  
    // If selections have been made, you can proceed with confirming the bet.
    console.log('Confirmed selections:', selectedSpreadOptions, selectedOverUnderOptions);
  
    // Now, you can send the selected options to the server to process the bet.
    // You can make an API call here to send the selected options to the backend.
    // Example API call using fetch:
    /*
    fetch('http://example.com/api/confirm-bet', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({
        selectedSpreadOptions,
        selectedOverUnderOptions,
      }),
    })
    .then(response => {
      if (!response.ok) {
        throw new Error('Network response was not ok');
      }
      // Handle successful response from server
      console.log('Bet confirmed successfully.');
    })
    .catch(error => {
      console.error('Error confirming bet:', error);
      // Handle error from server or network failure
      // You can show an error message to the user
    });
    */
  };
  // const groupedSelections = {};

  // // Group spread selections
  // for (const gameId in selectedSpreadOptions) {
  //   const { team, point } = selectedSpreadOptions[gameId];
  //   if (!groupedSelections[gameId]) {
  //     groupedSelections[gameId] = { game: { homeTeam: '', awayTeam: '' }, selections: [] };
  //   }
  //   groupedSelections[gameId].game.homeTeam = team || '';
  //   groupedSelections[gameId].selections.push({ type: 'spread', point });
  // }

  // // Group over/under selections
  // for (const gameId in selectedOverUnderOptions) {
  //   const { team, point } = selectedOverUnderOptions[gameId];
  //   if (!groupedSelections[gameId]) {
  //     groupedSelections[gameId] = { game: { homeTeam: '', awayTeam: '' }, selections: [] };
  //   }
  //   groupedSelections[gameId].game.awayTeam = team || '';
  //   groupedSelections[gameId].selections.push({ type: 'overUnder', point });
  // }

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