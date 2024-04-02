
import React, { useState } from 'react';
import './LoginForm.css';
import { FaUser } from "react-icons/fa";
import { FaLock } from "react-icons/fa";
import { FaEye, FaEyeSlash } from 'react-icons/fa';
import { useNavigate } from 'react-router-dom';
import fetchLiveOdds from '../LiveOdds/LiveOdds';
import Cookies from 'js-cookie';
import AuthService from '../Utils/AuthService';



const LoginForm = () => {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [isPasswordVisible, setIsPasswordVisible] = useState(false);
  const navigate = useNavigate();

  const handleTogglePasswordVisibility = () => {
    setIsPasswordVisible((prev) => !prev);
  };

  const handleLogin = async () => {
    try {
      // Call login function from AuthService
      await AuthService.login(username, password);
      
      // Redirect to user's personal account page
      navigate(`/user/${username}`);
      // window.location.reload();
    } catch (error) {
      console.error('Error during login:', error.message);
      // Handle error
    }
  };


  return (
    <div className='wrapper'>

        <form>
            <h1>Login</h1>
            <div className='input-box'>
                <input type="text" placeholder='Username' value={username} onChange={(e) => setUsername(e.target.value)} />
                <FaUser className='icon'/>
            </div>
            <div className='input-box'>
                <input
                    type={isPasswordVisible ? 'text' : 'password'}
                    placeholder='Password'
                    value={password}
                    onChange={e => setPassword(e.target.value)}
                />
                <button
                    type="button"
                    onClick={handleTogglePasswordVisibility}
                    className="password-toggle"
                >
                    {isPasswordVisible ? <FaEyeSlash /> : <FaEye />}
                </button>
            </div>
            <div className='remember-forgot'>
                <label><input type="checkbox" />Remember me</label>
                <a href="#">Forgot password?</a>
            </div>
            
            <button type="button" onClick={handleLogin}>Login</button>

            <div className='register-link'>
                <p>Don't have an account? <a href="/signup">Register</a></p>
            </div>
      </form>
    </div>
  );
};

export default LoginForm;