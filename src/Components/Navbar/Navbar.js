import React from 'react';
import NavbarItem from './NavbarItem';
import './Navbar.css'; 

const Navbar = () => {
  return (
    <div className="navbar">
        <ul>
            <li><NavbarItem to="/live-odds" label="Browse Odds" /></li>
            <li><NavbarItem to="/friendly-match" label="Friendly Match" /></li>
            <li><NavbarItem to="/matchups" label="Matchups" /></li>
            <li><NavbarItem to="/savedslips" label="Saved Slips" /></li>
            <li><NavbarItem to="/logout" label="Settings" /></li>
        </ul>
    </div>
  );
};

export default Navbar;