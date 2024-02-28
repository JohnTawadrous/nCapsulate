import React, { useState, useEffect } from 'react';
import AuthService from '../Utils/AuthService';
import UserService from '../Utils/UserService';

const BetSlipsComponent = () => {
    const [betSlips, setBetSlips] = useState([]);
    const [username, setUsername] = useState('');

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
            <h1>Bet Slips</h1>
            <ul>
                {betSlips.map(betSlip => (
                    <li key={betSlip.id}>
                        <h2>User: {betSlip.user.username}</h2> {/* Accessing the username */}
                        <ul>
                            {betSlip.selectedBets.map((selectedBet, index) => (
                                <li key={index}>{selectedBet}</li>
                            ))}
                        </ul>
                    </li>
                ))}
            </ul>
        </div>
    );
};

export default BetSlipsComponent;