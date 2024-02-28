import axios from "axios";

const API_URL = "http://localhost:8080/api/auth/";

// let myJwtToken;

const register = (username, email, password) => {
  return axios.post(API_URL + "signup", {
    username,
    email,
    password,
  });
};

const login = (username, password) => {
  return axios
    .post(API_URL + "signin", {
      username,
      password,
    }, {
      headers: {
        'Content-Type': 'application/json',
      }
    })
    .then((response) => {
      if (response.data.username) {
        console.log('Response status:', response.status);
        console.log('Response status text:', response.statusText);
        localStorage.setItem("user", JSON.stringify(response.data));
        const jwtToken = response.data.jwtToken;
        localStorage.setItem("jwtToken", jwtToken);
        
        console.log('Token: ', jwtToken);
      }

      return response.data;
    });
};

const logout = () => {
  localStorage.removeItem("user");
  return axios.post(API_URL + "signout").then((response) => {
    return response.data;
  });
};

const getCurrentUser = () => {
  return JSON.parse(localStorage.getItem("user"));
};

// const fetchBetSlips = async (username) => {
//   try {
//       const response = await axios.get(`/api/betslips/${username}`);
//       return response.data; // Assuming your API returns the bet slips directly
//   } catch (error) {
//       console.error('Error fetching bet slips:', error);
//       throw error; // Handle the error as needed in your React component
//   }
// };

const AuthService = {
  register,
  login,
  logout,
  getCurrentUser,
}



export default AuthService;