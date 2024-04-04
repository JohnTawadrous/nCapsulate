import React, { useEffect, useState } from 'react';
import MenuBar from '../MenuBar/MenuBar';
import AuthService from '../Utils/AuthService';
import './HowToPlay.css';


function HowToPlay() {
    const [username, setUsername] = useState('');

    useEffect(() => {
        const currentUser = AuthService.getCurrentUser();
        setUsername(currentUser.username);
    }, []);

    return (
        <div>
            <MenuBar username={username} />
            <div id="how-to-play">
            <h1>How to Play</h1>
            <br></br>

            <h2>Introduction</h2>
            <p>Welcome to the first-ever 1v1 betting matchup app! Here's how you can get started:</p>
            <br></br>
                            
            <h2>Viewing Today's Games</h2>
            <p>To view today's slate of games, navigate to the homepage and click on the "Browse Odds" option in the menu bar.</p>
            <br></br>
                            
            <h2>Creating a Bet Slip</h2>
            <p>Once on the "Browse Odds" page, you will see all the games of today's slate.</p>
            <p>Each game has 2 selections to make, the spread and the total points.</p>
            <p>To create a bet slip, select a predicted spread and total points for each game of the day.</p>
            <p>Once you have completed all of your predictions, click on the "Confirm Selections" button on the sidebar.</p>
            <p>If all the games have been predicted and confirmed, you will see your Bet Slip ID at the top of the page.</p>            
            <br></br>

            <h2>Sending and Accepting a Matchup</h2>
            <p>Now you have saved Bet Slip! Let's send a matchup request to one of your friends.</p>
            <p>Navigate to the "Friendly Match" option on the menu bar in your user page.</p>
            <p>Here you can type the username of the opponent you wish to face.</p>
            <p>Now select the Bet Slip you wish to use for the Matchup.</p>
            <p>If you wish to review your Bet Slip, click on the "View Bet Slip Details" button.</p>
            <p>When you are ready, click on the "Send Matchup Request" button to send the request.</p>
            <br></br>
            <p>If you are receiving a Matchup request, you will see your pending request in your user page</p>
            <p>Here you can select the Bet Slip you wish to use for the Matchup</p>
            <p>Now you can either select the "Accept" or "Decline" button</p>
            <p>Note: If you or your opponent's Bet Slip is not from the same date, the request will automatically get declined.</p>
            <br></br>

            <h2>Monitoring your Matchups</h2>
            <p>To view your Matchups, navigate to the "Matchups" option in the menu bar in your user page</p>
            <p>Here you view your Active Matchups as well as your Completed Matchups</p>
            <p>If you wish to see the details of the Matchup, click on the "Matchup Details" button</p>
            <p>This will allow you to see your very own Bet Slip as well as your opponent's</p>
            <br></br>

            <h2>Viewing Previous Bet Slips</h2>
            <p>If you ever wish to view your previously saved Bet Slips, you can!</p>
            <p>Navigate to the "Saved Slips" option in the menu bar in your user page</p>
            <p>Here you will see all your previously saved Bet Slips</p>
            <p>Click on "View Bet Slip Details" button on the desired Bet Slip you wish to view</p>
            <p>You will now see your chosen Bet Slip details.</p>
            <br></br>

            <h3>If you see any issues, bugs or have any suggestions, please send it to john.tawadrous96@gmail.com</h3>


                
                {/* Include more sections for saving a bet slip, sending a matchup request, accepting a matchup request, etc. */}
            </div>
        </div>
    );
}

export default HowToPlay;