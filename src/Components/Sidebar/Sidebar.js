import React, { useState, useEffect } from 'react';
import './Sidebar.css';

const Sidebar = ({ selectedSpreadOptions, selectedOverUnderOptions, onConfirmBet }) => {
  const groupedSelections = {};
  const [selectionCount, setSelectionCount] = useState(0);

  // Group selected spread options by game
  for (const gameId in selectedSpreadOptions) {
    const { homeTeam, awayTeam, outcome, point } = selectedSpreadOptions[gameId];
    if (!groupedSelections[gameId]) {
      groupedSelections[gameId] = { homeTeam, awayTeam, selections: [] };
    }
    groupedSelections[gameId].selections.push({ type: 'spread', outcome, point });
  }

  // Group selected over/under options by game
  for (const gameId in selectedOverUnderOptions) {
    const { homeTeam, awayTeam, outcome, point } = selectedOverUnderOptions[gameId];
    if (!groupedSelections[gameId]) {
      groupedSelections[gameId] = { homeTeam, awayTeam, selections: [] };
    }
    groupedSelections[gameId].selections.push({ type: 'overUnder', outcome, point });
  }

  useEffect(() => {
    const totalCount = Object.keys(selectedSpreadOptions).length + Object.keys(selectedOverUnderOptions).length;
    setSelectionCount(totalCount);
  }, [selectedSpreadOptions, selectedOverUnderOptions]);

  return (
    <div className='sidebar'>
    <div className={`sidebar-content ${selectionCount > 0 ? 'has-content' : ''}`} 
      style={{ height: `${selectionCount * 125}px`, minHeight: '300px' , maxHeight: '900px'}}>
        
      <h2>Current Selections</h2>
      <hr className="separator" />
      {Object.keys(groupedSelections).map((gameId) => (
        <div key={gameId} className="game-selection">
          <h3>{groupedSelections[gameId].homeTeam} vs {groupedSelections[gameId].awayTeam}</h3>
          <ul>
            {groupedSelections[gameId].selections.map(({ type, outcome, point }) => (
              <li key={type}>
                {type === 'spread' ? `Spread: ${outcome} : ${point}` : `Over/Under: ${outcome} - ${point}`}
              </li>
            ))}
          </ul>
          <hr className="separator" />
        </div>
      ))}
      
    </div>
      <div>
          <button className="confirm-button" onClick={onConfirmBet}>Confirm Selections</button>
        </div>
    </div>
      
  );
};

export default Sidebar;