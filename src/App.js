
import './App.css';
import { BrowserRouter as Router, Route, Routes, Navigate} from 'react-router-dom';
import LoginForm from './Components/LoginForm/LoginForm.jsx'; // Use forward slashes
import SignupForm from './Components/SignupForm/SignupForm.jsx'; // Use forward slashes
import UserPage from './Components/UserPage/UserPage.jsx'
import LiveOdds from './Components/LiveOdds/LiveOdds.jsx';
import BetSlipsComponent from './Components/BetSlips/BetSlips.jsx';
import FriendlyMatch from './Components/FriendlyMatch/FriendlyMatch.jsx';
import Matchups from './Components/Matchups/Matchups.jsx';
import Logout from './Components/Logout/Logout.jsx';
import HowToPlay from './Components/HowToPlay/HowToPlay.jsx';


function App() {
  return (
    <Router>
    <div className="App">
      <Routes>
        <Route path="/" element={<Navigate to="/login" />} />
        <Route path="/login" element={<LoginForm />} />
        <Route path="/signup" element={<SignupForm />} />
        <Route path="/user/:username" element={<UserPage />} />
        <Route path="/live-odds" element={<LiveOdds />} />
        <Route path="/savedslips" element={<BetSlipsComponent />} />
        <Route path="/friendly-match" element={<FriendlyMatch />} />
        <Route path="/matchups" element={<Matchups />} />
        <Route path="/how-to-play" element={<HowToPlay />} />
        <Route path="/logout" element={<Logout />} />
        {/* Add other routes if needed */}
      </Routes>
    </div>
  </Router>
  );
}

export default App;
