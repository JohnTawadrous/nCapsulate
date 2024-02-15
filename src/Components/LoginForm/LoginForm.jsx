
import React, { useState } from 'react';
import './LoginForm.css';
import { FaUser } from "react-icons/fa";
import { FaLock } from "react-icons/fa";
import { FaEye, FaEyeSlash } from 'react-icons/fa';
import { useNavigate } from 'react-router-dom';



const LoginForm = () => {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [isPasswordVisible, setIsPasswordVisible] = useState(false);
  const navigate = useNavigate();

  const handleTogglePasswordVisibility = () => {
    setIsPasswordVisible((prev) => !prev);
  };

  const handleLogin = () => {

    const credentials = {
        username: username,
        password: password,
      };
  
      fetch('http://localhost:8080/api/auth/signin', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(credentials),
      })
        .then(response => {
            console.log('Response status:', response.status);
            console.log('Response status text:', response.statusText);

          if (!response.ok) {
            throw new Error('Network response was not ok');
        }

        // Assuming the server sends the token in a field named 'jwtCookie'
        const jwtCookie = response.headers.get('myJwtCookie');

        if (jwtCookie) {
          // Save the JWT token in a secure way, e.g., HttpOnly cookie or localStorage
          document.cookie = `myJwtCookie=${jwtCookie}; path=/; max-age=86400;`;
          console.log('JWT token received:', jwtCookie);
        }

        return response.json();
      })
      .then(data => {
        // Handle the successful login response
        console.log('Login successful:', data);
        // Redirect the user to their personal account page
        navigate(`/user/${data.username}`);
      })
      .catch(error => {
        // Handle errors during login
        console.error('Error during login:', error.message);
        // You might want to display an error message to the user
      });
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
                <p>Don't have an account? <a href="#">Register</a></p>
            </div>
      </form>
    </div>
  );
};

export default LoginForm;