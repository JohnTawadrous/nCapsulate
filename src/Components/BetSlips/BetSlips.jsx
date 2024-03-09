import React, { useState, useEffect } from 'react';
import AuthService from '../Utils/AuthService';
import UserService from '../Utils/UserService';
import './BetSlips.css';
import MenuBar from '../MenuBar/MenuBar';

const BetSlipsComponent = () => {
    const [betSlips, setBetSlips] = useState([]);
    const [username, setUsername] = useState('');
    const [funds, setFunds] = useState('$1000');

    useEffect(() => {
        const fetchSavedBets = async () => {
            try {
                const currentUser = AuthService.getCurrentUser();
                setUsername(currentUser.username);

                const betSlipsData = await UserService.getSavedBetSlips(currentUser.username);
                setBetSlips(betSlipsData.data);
            } catch (error) {
                console.error("Error getting Bet Slips");
            }
        };

        fetchSavedBets();
    }, []);

    return (
        <div>
            <MenuBar username={username} funds={funds}/>
            <div className='container'>
            <h1>{username}'s Recent Bet Slips</h1>
            <div className='bet-slip-grid'>
                {betSlips.map(betSlip => (
                    <div className='bet-slip-box' key={betSlip.id}>
                        <h2>Bet Slip</h2>
                        <ul>
                            {/* Group selected bets by gameId */}
                            {Object.entries(
                                betSlip.selectedBets.reduce((acc, selectedBet) => {
                                    if (!acc[selectedBet.gameId]) {
                                        acc[selectedBet.gameId] = [];
                                    }
                                    acc[selectedBet.gameId].push(selectedBet);
                                    return acc;
                                }, {})
                            ).map(([gameId, bets]) => (
                                <div key={gameId}>
                                    <h3>{bets[0].awayTeam} @ {bets[0].homeTeam}</h3>
                                    <div>
                                        {/* Render details for each selected bet */}
                                        {bets.map((selectedBet, index) => (
                                            <div key={index}>
                                                <div>{selectedBet.outcome}  {selectedBet.point}</div>
                                                {/* <div>Point: {selectedBet.point}</div> */}
                                                {/* <div>Home Team: {selectedBet.homeTeam}</div>
                                                <div>Away Team: {selectedBet.awayTeam}</div> */}
                                            </div>
                                        ))}
                                    </div>
                                </div>
                            ))}
                        </ul>
                    </div>
                ))}
            </div>
        </div>
    </div>
    );
};

export default BetSlipsComponent;