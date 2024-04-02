import React, { useState, useEffect } from 'react';
import NavbarItem from './NavbarItem';
import './Navbar.css'; 
import Logout from '../Logout/Logout';
import AuthService from '../Utils/AuthService';
// import { useHistory } from 'react-router-dom';

const Navbar = () => {
  const [logoutModalOpen, setLogoutModalOpen] = useState(false); // State to manage the visibility of the Logout modal

  // Function to handle logout
  const handleLogout = () => {
    AuthService.logout() // Call the logout function from AuthService
      .then(() => {
        // After successful logout, redirect to the login page
        window.location.href = '/login';
      })
      .catch(error => {
        // Handle logout error
        console.error('Error during logout:', error);
        // Optionally, display an error message to the user
      });
  };

  return (
    <div className="navbar">
      <ul>
        <li><NavbarItem to="/live-odds" label="Browse Odds" /></li>
        <li><NavbarItem to="/friendly-match" label="Friendly Match" /></li>
        <li><NavbarItem to="/matchups" label="Matchups" /></li>
        <li><NavbarItem to="/savedslips" label="Saved Slips" /></li>
        <li><NavbarItem to="/how-to-play" label="How to Play" /></li>
        {/* Add a button to open the Logout modal */}
        <li><button className="navbar-item" onClick={() => setLogoutModalOpen(true)}>Logout</button></li>
      </ul>
      {/* Render the Logout modal only when logoutModalOpen is true */}
      {logoutModalOpen && <Logout isOpen={logoutModalOpen} onClose={() => setLogoutModalOpen(false)} onLogout={handleLogout} />}
    </div>
  );
};

export default Navbar;