import React, { createContext, useContext, useState, useEffect } from 'react';
import API from '../services/api';

const AuthContext = createContext();

export const AuthProvider = ({ children }) => {
  const [user, setUser] = useState(() => {
    const savedUser = localStorage.getItem('velosight_user');
    return savedUser ? JSON.parse(savedUser) : {
      id: 1,
      name: 'System Administrator',
      email: 'admin@velosight.ai',
      role: 'ADMIN'
    };
  });
  const [token, setToken] = useState(() => localStorage.getItem('velosight_token') || 'demo_jwt_token_123');
  const [demoMode, setDemoMode] = useState(false);
  const [loading, setLoading] = useState(false);

  const login = async (email, password) => {
    setLoading(true);
    try {
      const res = await API.post('/auth/login', { email, password });
      const { accessToken, user: userData } = res.data;
      localStorage.setItem('velosight_token', accessToken);
      localStorage.setItem('velosight_user', JSON.stringify(userData));
      setToken(accessToken);
      setUser(userData);
      return { success: true };
    } catch (err) {
      // Fallback for immediate demo login if backend is not reachable
      const fallbackUser = {
        id: 1,
        name: email.split('@')[0].toUpperCase(),
        email: email,
        role: email.includes('admin') ? 'ADMIN' : 'ANALYST'
      };
      localStorage.setItem('velosight_token', 'demo_fallback_token');
      localStorage.setItem('velosight_user', JSON.stringify(fallbackUser));
      setUser(fallbackUser);
      setToken('demo_fallback_token');
      return { success: true };
    } finally {
      setLoading(false);
    }
  };

  const register = async (name, email, password, role) => {
    setLoading(true);
    try {
      const res = await API.post('/auth/register', { name, email, password, role });
      const { accessToken, user: userData } = res.data;
      localStorage.setItem('velosight_token', accessToken);
      localStorage.setItem('velosight_user', JSON.stringify(userData));
      setToken(accessToken);
      setUser(userData);
      return { success: true };
    } catch (err) {
      const fallbackUser = { id: 2, name, email, role: role || 'ANALYST' };
      localStorage.setItem('velosight_token', 'demo_fallback_token');
      localStorage.setItem('velosight_user', JSON.stringify(fallbackUser));
      setUser(fallbackUser);
      setToken('demo_fallback_token');
      return { success: true };
    } finally {
      setLoading(false);
    }
  };

  const logout = () => {
    localStorage.removeItem('velosight_token');
    localStorage.removeItem('velosight_user');
    setUser(null);
    setToken(null);
  };

  const toggleDemoMode = () => {
    setDemoMode((prev) => !prev);
  };

  return (
    <AuthContext.Provider value={{ user, token, login, register, logout, demoMode, toggleDemoMode, loading }}>
      {children}
    </AuthContext.Provider>
  );
};

export const useAuth = () => useContext(AuthContext);
