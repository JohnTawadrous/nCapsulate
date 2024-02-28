import axios from "axios";

const API_URL = "http://localhost:8080/api/test/";

const getPublicContent = () => {
  return axios.get(API_URL + "all");
};

const getUserBoard = () => {
  return axios.get(API_URL + "user");
};

const getModeratorBoard = () => {
  return axios.get(API_URL + "mod");
};

const getAdminBoard = () => {
  return axios.get(API_URL + "admin");
};

const getLiveOdds = () => {
    return axios.get("http://localhost:8080/api/odds/live");
};

const getSavedBetSlips = (username) => {
  return axios.get("http://localhost:8080/api/betslips/saved", {
    params: { username: username }
  });
};



const UserService = {
  getPublicContent,
  getUserBoard,
  getModeratorBoard,
  getAdminBoard,
  getLiveOdds,
  getSavedBetSlips,
}

export default UserService;