import React, { useState, useEffect } from 'react';
import AuthService from '../Utils/AuthService';
import UserService from '../Utils/UserService';

const FriendlyMatch = () => {
    const [opponentUsername, setOpponentUsername] = useState('');
    const [selectedBetslip, setSelectedBetslip] = useState(null);
    const [betSlips, setBetSlips] = useState([]);

    useEffect(() => {
        // Fetch user's saved betslips
        const fetchSavedBetslips = async () => {
            try {
                const currentUser = AuthService.getCurrentUser();
                const betSlipsData = await UserService.getSavedBetSlips(currentUser.username);
                setBetSlips(betSlipsData.data);
            } catch (error) {
                console.error("Error getting Bet Slips");
            }
        };

        fetchSavedBetslips();
    }, []);


    const handleOpponentUsernameChange = (e) => {
        setOpponentUsername(e.target.value);
    };

    const handleBetslipSelection = (e) => {
        setSelectedBetslip(e.target.value);
    };

    const handleSendMatchupRequest = async () => {
        try {
            const matchupRequestData = {
                userUsername: AuthService.getCurrentUser().username,
                opponentUsername: opponentUsername,
                selectedBetSlipId: selectedBetslip
            };
            await UserService.sendMatchupRequest(matchupRequestData);
            // Handle success: Display a success message or redirect the user
            console.log("Matchup request sent successfully");
        } catch (error) {
            // Handle error: Display an error message to the user
            console.error("Error sending matchup request:", error.message);
        }
    };

    return (
        <div>
            <h1>Friendly Match</h1>
            <label>
                Opponent's Username:
                <input type="text" value={opponentUsername} onChange={handleOpponentUsernameChange} />
            </label>
            <br />
            <label>
                Select Your Betslip:
                <select value={selectedBetslip} onChange={handleBetslipSelection}>
                    <option value="">Select Betslip</option>
                    {betSlips.map((betslip) => (
                        <option key={betslip.id} value={betslip.id}>
                            {betslip.id} {/* Adjust this based on the structure of your betslip object */}
                        </option>
                    ))}
                </select>
            </label>
            <br />
            <button onClick={handleSendMatchupRequest}>Send Matchup Request</button>
        </div>
    );
};

export default FriendlyMatch;