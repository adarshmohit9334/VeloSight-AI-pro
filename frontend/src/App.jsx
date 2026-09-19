import React from 'react';
import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import { AuthProvider, useAuth } from './context/AuthContext';
import { Sidebar } from './components/Sidebar';
import { Topbar } from './components/Topbar';

import { Login } from './pages/Login';
import { Register } from './pages/Register';
import { Dashboard } from './pages/Dashboard';
import { LiveMonitoring } from './pages/LiveMonitoring';
import { VideoAnalysis } from './pages/VideoAnalysis';
import { Analytics } from './pages/Analytics';
import { Intersections } from './pages/Intersections';
import { Cameras } from './pages/Cameras';
import { Alerts } from './pages/Alerts';
import { SignalRecommendations } from './pages/SignalRecommendations';
import { History } from './pages/History';
import { Reports } from './pages/Reports';
import { Settings } from './pages/Settings';
import { Profile } from './pages/Profile';

const ProtectedLayout = ({ children }) => {
  const { token } = useAuth();
  if (!token) {
    return <Navigate to="/login" replace />;
  }

  return (
    <div className="app-container">
      <Sidebar />
      <div className="main-content">
        <Topbar />
        <main className="page-body">
          {children}
        </main>
      </div>
    </div>
  );
};

export default function App() {
  return (
    <AuthProvider>
      <Router>
        <Routes>
          <Route path="/login" element={<Login />} />
          <Route path="/register" element={<Register />} />

          <Route path="/dashboard" element={<ProtectedLayout><Dashboard /></ProtectedLayout>} />
          <Route path="/live-monitoring" element={<ProtectedLayout><LiveMonitoring /></ProtectedLayout>} />
          <Route path="/video-analysis" element={<ProtectedLayout><VideoAnalysis /></ProtectedLayout>} />
          <Route path="/analytics" element={<ProtectedLayout><Analytics /></ProtectedLayout>} />
          <Route path="/intersections" element={<ProtectedLayout><Intersections /></ProtectedLayout>} />
          <Route path="/cameras" element={<ProtectedLayout><Cameras /></ProtectedLayout>} />
          <Route path="/alerts" element={<ProtectedLayout><Alerts /></ProtectedLayout>} />
          <Route path="/signal-recommendations" element={<ProtectedLayout><SignalRecommendations /></ProtectedLayout>} />
          <Route path="/history" element={<ProtectedLayout><History /></ProtectedLayout>} />
          <Route path="/reports" element={<ProtectedLayout><Reports /></ProtectedLayout>} />
          <Route path="/settings" element={<ProtectedLayout><Settings /></ProtectedLayout>} />
          <Route path="/profile" element={<ProtectedLayout><Profile /></ProtectedLayout>} />

          <Route path="*" element={<Navigate to="/dashboard" replace />} />
        </Routes>
      </Router>
    </AuthProvider>
  );
}
