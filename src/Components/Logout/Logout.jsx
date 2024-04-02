import React, { useState } from 'react';
import { useHistory } from 'react-router-dom';
import './Logout.css'

const Logout = ({ isOpen, onClose, onLogout }) => {
  const handleLogout = () => {
    onLogout();
    onClose();
  };

  return (
    <div id="logout-modal" className={`logout-modal-container ${isOpen ? 'open' : ''}`}>
      <div className="logout-modal-content">
        <h2>Logout Confirmation</h2>
        <p>Are you sure you want to log out?</p>
        <div>
          <button className='logout-buttons' onClick={handleLogout}>Yes</button>
          <button className='logout-buttons' onClick={onClose}>No</button>
        </div>
      </div>
    </div>
  );
};

export default Logout;