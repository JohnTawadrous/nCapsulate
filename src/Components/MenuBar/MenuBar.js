import React from 'react';
import { Link } from 'react-router-dom';
import './MenuBar.css';

const MenuBar = ({ username, funds }) => {
  return (
    <div className="menu-bar">
      <div className="left">
        <Link to="/profile">Profile</Link>
        <Link to="/settings">Settings</Link>
      </div>
      <div className="right">
        <span>{username}</span>
        <span>Funds: {funds}</span>
      </div>
    </div>
  );
};

export default MenuBar;