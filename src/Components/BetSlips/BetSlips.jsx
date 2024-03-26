import React, { useState, useEffect } from 'react';
import AuthService from '../Utils/AuthService';
import UserService from '../Utils/UserService';
import './BetSlips.css';
import MenuBar from '../MenuBar/MenuBar';
import BetSlipDetailsModal from '../Modal/BetSlipDetailsModal';

const BetSlipsComponent = () => {
    const [betSlips, setBetSlips] = useState([]);
    const [username, setUsername] = useState('');
    const [selectedBetSlip, setSelectedBetSlip] = useState(null);
    const [showBetSlipDetails, setShowBetSlipDetails] = useState(false);

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

    const handleViewBetSlipDetails = (betSlip) => {
        setSelectedBetSlip(betSlip);
        setShowBetSlipDetails(true);
    };

    const handleCloseBetSlipDetails = () => {
        setShowBetSlipDetails(false);
    };

    return (
        <div>
            <MenuBar username={username} />
            <h1>{username}'s Recent Bet Slips</h1>
            <div className='container'>
                <div className='bet-slip-grid'>
                    {betSlips.map(betSlip => (
                        <div className='bet-slip-box' key={betSlip.id}>
                            <h2>Bet Slip ID: {betSlip.id}</h2>
                            <button className='view-betslip-details-button' onClick={() => handleViewBetSlipDetails(betSlip.id)}>View Bet Slip Details</button>
                        </div>
                    ))}
                    
                </div>
                <div className='details-modal'> 
                {/* Modal to display bet slip details */}
                <BetSlipDetailsModal
                                isOpen={showBetSlipDetails}
                                betSlip={selectedBetSlip}
                                onClose={handleCloseBetSlipDetails}
                            />
                </div>
            </div>
            
        </div>
    );
};

export default BetSlipsComponent;