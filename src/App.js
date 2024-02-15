
import './App.css';
import { BrowserRouter as Router, Route, Routes} from 'react-router-dom';
import LoginForm from './Components/LoginForm/LoginForm.jsx'; // Use forward slashes
import SignupForm from './Components/SignupForm/SignupForm.jsx'; // Use forward slashes
import UserPage from './Components/UserPage/UserPage.jsx'
import LiveOdds from './Components/LiveOdds/LiveOdds.jsx';

function App() {
  return (
    <Router>
    <div className="App">
      <Routes>
        <Route path="/login" element={<LoginForm />} />
        <Route path="/signup" element={<SignupForm />} />
        <Route path="/user/:username" element={<UserPage />} />
        <Route path="/live-odds" element={<LiveOdds />} />
        {/* Add other routes if needed */}
      </Routes>
    </div>
  </Router>
  );
}

export default App;
