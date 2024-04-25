import React, { useState, useEffect } from 'react';
import AuthService from '../Utils/AuthService';
import UserService from '../Utils/UserService';
import "./BetSlipDetailsModal.css";
import teamLogos from '../Assets/teamLogos/teamLogos';

const BetSlipDetailsModal = ({ isOpen, betSlip, onClose }) => {

    const [betSlipDetails, setBetSlipDetails] = useState([]);
    const [username, setUsername] = useState('');

    // If the modal is not open, don't render anything
    useEffect(() => {
        const fetchBetSlip = async () => {
            try {
                const currentUser = AuthService.getCurrentUser();
                setUsername(currentUser.username);

                const betSlipData = await UserService.getBetSlip(betSlip);
                setBetSlipDetails(betSlipData.data);
            } catch (error) {
                console.error("Error getting Bet Slips", error);
            }
        };

        if (isOpen && betSlip) {
            fetchBetSlip();
        }
    }, [isOpen, betSlip]);

    if (!isOpen) {
        return null;
    }

     // Group selected bets by game ID
     const groupedBets = {};
    if (betSlipDetails && betSlipDetails.selectedBets) {
        betSlipDetails.selectedBets.forEach(selectedBet => {
            if (!groupedBets[selectedBet.gameId]) {
                groupedBets[selectedBet.gameId] = [];
            }
            groupedBets[selectedBet.gameId].push(selectedBet);
         });
        }

        const getColor = (correct) => {
            if (correct === true) {
                return 'green';
            } else if (correct === false) {
                return 'red';
            } else {
                return 'white';
            }
        };

    return (
        <div className="modal">
        <div className="modal-content">
            <div className='modal-scrollable-content'>
                <div className='vertical-space'></div>
                <h2>Bet Slip Details</h2>
                {betSlipDetails && (
                    <div>
                        <h4>Bet Slip ID: {betSlipDetails.id}</h4>
                        {/* Here is where I want the username*/}
                        <h4>Bet Slip ID: {betSlipDetails.user}</h4>
                        <div className='vertical-space'></div>
                        {/* <p>Selected Bets:</p> */}
                        {Object.keys(groupedBets).map(gameId => {
                            const gameBets = groupedBets[gameId];
                            const homeTeam = gameBets[0].homeTeam;
                            const awayTeam = gameBets[0].awayTeam;
                            return (
                                <div key={gameId}>
                                    <div className='teams'> 
                                        <div className='away-team'>
                                            <img src={teamLogos[awayTeam]} alt={awayTeam} className="team-logo" />
                                            <h4>{awayTeam}</h4>
                                        </div>
                                        @
                                        <div className='home-team'>
                                            <img src={teamLogos[homeTeam]} alt={homeTeam} className="team-logo" />
                                            <br></br>    
                                            <h4>{homeTeam}</h4>
                                        </div>
                                    </div>
                                    <div className='vertical-space'></div>
                                    <div className='details-box'>
                                        {gameBets.map((selectedBet, index) => (
                                            <div className='game-details-box' key={index}>
                                                <h5 style={{ color: getColor(selectedBet.correct) }}>{selectedBet.outcome} : {selectedBet.point}</h5>
                                            </div>
                                        ))}
                                    </div>
                                    <hr className="game-separator" />
                                </div>
                            );
                        })}
                    </div>
                )}
            </div>
            <button className='close-button' onClick={onClose}>Close</button>
        </div>
    </div>
);
};


export default BetSlipDetailsModal;