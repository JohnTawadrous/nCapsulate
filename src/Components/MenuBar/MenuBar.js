import React from 'react';
import { Link } from 'react-router-dom';
import './MenuBar.css';

const MenuBar = ({ username }) => {
  return (
    <div className="menu-bar">
      <div className="left">
        <Link to="/user/:username">Profile</Link>
      </div>
      <div className="right">
        <span>{username}</span>
        {/* <span>Funds: {funds}</span> */}
      </div>
    </div>
  );
};

export default MenuBar;