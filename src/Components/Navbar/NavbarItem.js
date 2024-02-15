import React from 'react';
import { Link } from 'react-router-dom';

const NavbarItem = ({ to, label }) => {
  return (
    <Link to={to} className="navbar-item">
      {label}
    </Link>
  );
};

export default NavbarItem;