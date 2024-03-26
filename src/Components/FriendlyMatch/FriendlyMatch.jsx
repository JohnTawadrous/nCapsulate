import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import AuthService from '../Utils/AuthService';
import UserService from '../Utils/UserService';
import MenuBar from '../MenuBar/MenuBar';
import BetSlipDetailsModal from '../Modal/BetSlipDetailsModal';
import './FriendlyMatch.css' ;

const FriendlyMatch = () => {
    const [username, setUsername] = useState('');
    const [opponentUsername, setOpponentUsername] = useState('');
    const [selectedBetslip, setSelectedBetslip] = useState(null);
    const [betSlips, setBetSlips] = useState([]);
    const [showBetSlipDetails, setShowBetSlipDetails] = useState(false); // State to control the visibility of bet slip details modal
    const [selectedBetSlipDetails, setSelectedBetSlipDetails] = useState(null); // State to store details of the selected bet slip

    useEffect(() => {
        // Fetch user's saved betslips
        const fetchSavedBetslips = async () => {
            try {
                const currentUser = AuthService.getCurrentUser();
                setUsername(currentUser.username);
                const currentDate = new Date();
                const betSlipsData = await UserService.getSavedBetSlipsToday(currentUser.username, currentDate);
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

    // Function to handle viewing bet slip details
    const handleViewBetSlipDetails = (betslip) => {
        setSelectedBetSlipDetails(betslip);
        setShowBetSlipDetails(true);
    };

    // Function to close bet slip details modal
    const handleCloseBetSlipDetails = () => {
        setShowBetSlipDetails(false);
    };


    return (
        <div>
            <MenuBar username={username} />
            <h1>Friendly Match</h1>
            <div className='wrapper-friendly-match'>
                <div className='input-box'>
                    Opponent's Username:
                    <input type="text" placeholder='Enter Opponent Username' value={opponentUsername} onChange={handleOpponentUsernameChange} />
                </div>
                <br />
                <label className="select">
            
                    <select value={selectedBetslip} onChange={handleBetslipSelection}>
                        
                        <option value="">Select Betslip</option>
                        {betSlips.map((betslip) => (
                            <option className='bet-slip-options' key={betslip.id} value={betslip.id}>
                                {betslip.id} {/* Adjust this based on the structure of your betslip object */}
                            </option>
                        ))}
                    </select>
                    
                </label>
                <br />
                <button className='send-match-button' onClick={handleSendMatchupRequest}>Send Matchup Request</button>
                
                {/* Button to view bet slip details */}
                {selectedBetslip && (
                    <button className='view-slip-button' onClick={() => handleViewBetSlipDetails(selectedBetslip)}>View Bet Slip Details</button>
                )}
                <Link to="/live-odds">
                    <button className='create-slip-button' >Create New BetSlip</button>
                </Link>

                {/* Modal to display bet slip details */}
                <BetSlipDetailsModal
                    isOpen={showBetSlipDetails}
                    betSlip={selectedBetSlipDetails}
                    onClose={handleCloseBetSlipDetails}
                />
            </div>
        </div>
    );
};

export default FriendlyMatch;