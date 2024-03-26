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

const sendMatchupRequest = async (matchupRequestData) => {
  try {
    const response = await axios.post(`http://localhost:8080/api/matchups/send-request`, matchupRequestData);
    return response.data;
  } catch (error) {
    throw new Error('Failed to send matchup request');
  }
};

const acceptMatchup = async (matchupId, selectedBetSlipId) => {
  try {
    const response = await axios.post(`http://localhost:8080/api/matchups/accept/${matchupId}?selectedBetSlipId=${selectedBetSlipId}`);
    return response.data;
  } catch (error) {
    throw new Error('Failed to accept matchup');
  }
};

const declineMatchup = async (matchupId) => {
  try {
    const response = await axios.post(`http://localhost:8080/api/matchups/decline/${matchupId}`);
    return response.data;
  } catch (error) {
    throw new Error('Failed to decline matchup');
  }
};

const getSavedBetSlips = (username) => {
  return axios.get("http://localhost:8080/api/betslips/saved", {
    params: { username: username }
  });
};

const getBetSlip = (id) => {
  return axios.get("http://localhost:8080/api/betslips/betslip-details", {
    params: { id: id }
  });
};

const getSavedBetSlipsToday = (username, createdAt) => {
  return axios.get("http://localhost:8080/api/betslips/saved-today", {
    params: { username: username, createdAt: createdAt  }
  });
};

const getPendingMatchupRequests = (username) => {
  return axios.get("http://localhost:8080/api/matchups/pending", {
    params: { username: username }
  });
};

const getUsernameById = async (userId) => {
  try {
      const response = await axios.get(`http://localhost:8080/api/matchups/${userId}/username`);
      return response.data;
  } catch (error) {
      throw new Error('Failed to get username by ID');
  }
};

const UserService = {
  getPublicContent,
  getUserBoard,
  getModeratorBoard,
  getAdminBoard,
  getLiveOdds,
  getSavedBetSlips,
  sendMatchupRequest,
  acceptMatchup,
  declineMatchup,
  getPendingMatchupRequests,
  getUsernameById,
  getSavedBetSlipsToday,
  getBetSlip,
}

export default UserService;