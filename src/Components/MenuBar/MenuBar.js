import React from 'react';
import { Link } from 'react-router-dom';
import './MenuBar.css';
import { FaUser } from "react-icons/fa";

const MenuBar = ({ username }) => {
  return (
    <div className="menu-bar">
      <div className="left">
        <Link to="/user/:username">Profile</Link>
      </div>
      <div className="right">
      
        <span><FaUser className='user-logo'/>{username}</span>
        {/* <span>Funds: {funds}</span> */}
      </div>
    </div>
  );
};

export default MenuBar;