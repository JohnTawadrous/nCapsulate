import React from 'react';
import { useParams } from 'react-router-dom';
import Navbar from '../Navbar/Navbar';

const UserPage = () => {
  const { username } = useParams();

  return (
    <div>
      <h1>Welcome {username}</h1>
      {/* Add content for the user page */}
      {/* Include the Navbar component */}
      <Navbar />
    </div>
  );
};

export default UserPage;