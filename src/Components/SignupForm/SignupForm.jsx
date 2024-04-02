import React, { useState } from 'react';
import './SignupForm.css';
import { FaUser } from "react-icons/fa";
import { FaLock } from "react-icons/fa";
import { MdEmail } from "react-icons/md";
import { FaEye, FaEyeSlash } from 'react-icons/fa';

const SignupForm = () => {
  const [username, setUsername] = useState('');
  const [email, setEmail] = useState('');
  const [confirmEmail, setConfirmEmail] = useState('');
  const [password, setPassword] = useState('');
  const [confirmPassword, setConfirmPassword] = useState('');
  const [usernameError, setUsernameError] = useState('');
  const [emailError, setEmailError] = useState('');
  const [isPasswordVisible, setIsPasswordVisible] = useState(false);
  const [successMessage, setSuccessMessage] = useState('');

  const handleTogglePasswordVisibility = () => {
    setIsPasswordVisible((prev) => !prev);
  };

  const handleSignup = () => {
    setUsernameError('');
    setEmailError('');

    // Check if passwords match
    if (password !== confirmPassword) {
      console.error('Passwords do not match');
      alert('Passwords do not match');
      return;
    }

    // Check if emails match
    if (email !== confirmEmail) {
      console.error('Emails do not match');
      alert('Emails do not match');
      return;
    }

    // Logic to handle signup, send a request to the backend
    const userData = {
      username: username,
      email: email,
      password: password,
      confirmPassword: confirmPassword,
      confirmEmail: confirmEmail,
    };

    fetch('http://localhost:8080/api/auth/signup', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(userData),
    })
      .then((response) => {
        if (!response.ok) {
          throw response;
        }
        return response.json();
      })
      .then((data) => {
        // Handle the successful signup response
        console.log('Signup successful:', data);
        setSuccessMessage('Signup successful! You can now login:');
        // Reset form fields
        setUsername('');
        setEmail('');
        setConfirmEmail('');
        setPassword('');
        setConfirmPassword('');
      })
      .catch((error) => {
        // Handle errors during signup
        console.error('Error during signup:', error.message);

        // Check if the status code is 400, indicating a bad request
        if (error.status === 400) {
          // Handle specific error messages based on server response
          return error.json().then((errorMessage) => {
            if (errorMessage.message.includes('Username is already taken')) {
              setUsernameError('Username is already taken.');
            } else if (errorMessage.message.includes('Email is already in use')) {
              setEmailError('Email is already in use.');
            } else {
              alert('Error during signup. Please try again.');
            }
          });
        } else {
          // Handle other types of errors
          alert('Error during signup. Please try again.');
        }
      });
  };

  return (
    <div className='wrapper'>
      <form>
        <h1>Signup</h1>
        <div className='signup-success-message'>
          {successMessage && <p>{successMessage}</p>}
          <a href="/login">Login</a>
        </div>
        <div className='input-box'>
          <input type="text" placeholder='Username' value={username} onChange={e => setUsername(e.target.value)} />
          {/* Display username error message */}
          {usernameError && <span style={{ color: 'red' }}>{usernameError}</span>}
          <FaUser className='icon' />
        </div>

        <div className='input-box'>
          <input type="email" placeholder='Email' value={email} onChange={e => setEmail(e.target.value)} />
          {/* Display email error message */}
          {emailError && <span style={{ color: 'red' }}>{emailError}</span>}
          <MdEmail className='icon' />
        </div>

        <div className='input-box'>
          <input type="email" placeholder='Confirm Email' value={confirmEmail} onChange={(e) => setConfirmEmail(e.target.value)} />
          <MdEmail className='icon' />
        </div>

        <div className='input-box'>
          <input type={isPasswordVisible ? 'text' : 'password'} placeholder='Password' value={password} onChange={e => setPassword(e.target.value)} />
          <button type="button" onClick={handleTogglePasswordVisibility} className="password-toggle">
            {isPasswordVisible ? <FaEyeSlash /> : <FaEye />}
          </button>
        </div>

        <div className='input-box'>
          <input type="password" placeholder='Confirm Password' value={confirmPassword} onChange={(e) => setConfirmPassword(e.target.value)} />
          <FaLock className='icon' />
        </div>

        <button type="button" onClick={handleSignup}>Create Account</button>
        <div className='login-link'>
          <p>Already have an account? <a href="/login">Login</a></p>
        </div>
      </form>
    </div>
  );
};

export default SignupForm;