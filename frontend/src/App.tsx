import React, { useState } from 'react';
import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import Header from './components/Header';
import Footer from './components/Footer';
import Login from './components/Login';
import Register from './components/Register';
import BMICalculator from './components/BMICalculator';
import Recommendations from './components/Recommendations';
import { UserProvider, useUser } from './components/UserContext';
import Chatbot from './components/Chatbot';
import Dashboard from './components/Dashboard';
import { FaUserCircle } from 'react-icons/fa';
import DetailsPage from './components/DetailsPage';
import Homepage from './components/Homepage';
import BMRCalculator from './components/BMRCalculator';
import ProgressEntries from './components/ProgressEntries';
import MyWorkouts from './components/MyWorkouts';
import PersonalRecords from './components/PersonalRecords';

const AppContent = ({ navigateToDetails }: { navigateToDetails: () => void }) => {
  const { user, setUser, logout } = useUser();
  const [fitnessReport, setFitnessReport] = useState<any>(null);
  const [showDashboard, setShowDashboard] = useState(false);
  const [showEditProfile, setShowEditProfile] = useState(false);

  // Pass these as props to Login/Register
  const handleLogin = (userData: any) => {
    setUser(userData);
  };
  const handleLogout = () => {
    logout();
    setFitnessReport(null);
  };
  const handleFitnessReport = (report: any) => {
    setFitnessReport(report);
  };
  const handleDetailsSubmit = (updatedUser: any) => {
    setUser(updatedUser);
    setShowEditProfile(false);
  };

  return (
    <div className="min-h-screen bg-gradient-to-b from-blue-50 to-white flex flex-col relative">
      <Header user={user} onProfileClick={() => setShowDashboard(true)} />
      {/* Dashboard Overlay */}
      {showDashboard && user && (
        <>
          {/* Backdrop */}
          <div
            className="fixed inset-0 bg-black/30 backdrop-blur-sm z-40"
            onClick={() => setShowDashboard(false)}
            aria-label="Close dashboard"
          />
          {/* Slide-in Panel */}
          <div className="fixed top-0 right-0 h-full w-full max-w-md bg-white/90 shadow-2xl z-50 transition-transform duration-300 flex flex-col">
            <button
              className="absolute top-4 right-4 text-gray-500 hover:text-gray-800 text-2xl"
              onClick={() => setShowDashboard(false)}
              aria-label="Close"
            >
              &times;
            </button>
            <div className="flex-1 overflow-y-auto p-8 pt-12">
              <Dashboard onEditProfile={() => setShowEditProfile(true)} />
            </div>
          </div>
        </>
      )}
      {/* Edit Profile Modal */}
      {showEditProfile && user && (
        <>
          <div
            className="fixed inset-0 bg-black/40 backdrop-blur-sm z-50"
            onClick={() => setShowEditProfile(false)}
            aria-label="Close edit profile"
          />
          <div className="fixed top-1/2 left-1/2 -translate-x-1/2 -translate-y-1/2 w-full max-w-md bg-white/95 rounded-2xl shadow-2xl z-50 p-8">
            <DetailsPage user={user} onDetailsSubmit={handleDetailsSubmit} />
            <button
              className="absolute top-4 right-4 text-gray-500 hover:text-gray-800 text-2xl"
              onClick={() => setShowEditProfile(false)}
              aria-label="Close"
            >
              &times;
            </button>
          </div>
        </>
      )}
      <main className="flex-grow container mx-auto px-4 py-8">
        <Routes>
          <Route path="/login" element={
            user ? <Navigate to="/homepage" /> : <Login onLogin={handleLogin} />
          } />
          <Route path="/register" element={
            user ? <Navigate to="/homepage" /> : <Register onRegister={navigateToDetails} />
          } />
          <Route path="/homepage" element={
            user ? <Homepage /> : <Navigate to="/login" />
          } />
          <Route path="/bmr" element={
            user ? <BMRCalculator /> : <Navigate to="/login" />
          } />
          <Route path="/progress" element={
            user ? <ProgressEntries /> : <Navigate to="/login" />
          } />
          <Route path="/workouts" element={
            user ? <MyWorkouts /> : <Navigate to="/login" />
          } />
          <Route path="/pr" element={
            user ? <PersonalRecords /> : <Navigate to="/login" />
          } />
          <Route path="/details" element={
            user ? <DetailsPage user={user} onDetailsSubmit={handleDetailsSubmit} /> : <Navigate to="/login" />
          } />
          <Route path="/calculator" element={
            user ? <BMICalculator user={user} onFitnessReport={handleFitnessReport} /> : <Navigate to="/login" />
          } />
          <Route path="/recommendations" element={
            user && fitnessReport ? <Recommendations report={fitnessReport} /> : <Navigate to="/calculator" />
          } />
          <Route path="*" element={<Navigate to={user ? "/homepage" : "/login"} />} />
        </Routes>
      </main>
      <Footer />
      <Chatbot />
    </div>
  );
};

const App = () => (
  <UserProvider>
    <Router>
      <AppContent navigateToDetails={() => { window.location.href = '/details'; }} />
    </Router>
  </UserProvider>
);

export default App;