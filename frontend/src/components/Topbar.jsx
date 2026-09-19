import React, { useState, useEffect } from 'react';
import { Bell, Activity, LogOut, ToggleLeft, ToggleRight, CheckCircle2, AlertCircle } from 'lucide-react';
import { TrafficLogo } from './TrafficLogo';
import { useAuth } from '../context/AuthContext';
import API from '../services/api';

export const Topbar = () => {
  const { user, logout, demoMode, toggleDemoMode } = useAuth();
  const [aiOnline, setAiOnline] = useState(true);
  const [notificationsOpen, setNotificationsOpen] = useState(false);

  useEffect(() => {
    const checkAiStatus = async () => {
      try {
        await fetch('http://localhost:8000/ai/health', { method: 'GET' });
        setAiOnline(true);
      } catch (err) {
        setAiOnline(false);
      }
    };
    checkAiStatus();
    const interval = setInterval(checkAiStatus, 15000);
    return () => clearInterval(interval);
  }, []);

  return (
    <header className="topbar">
      <div style={{ display: 'flex', alignItems: 'center', gap: '14px' }}>
        <TrafficLogo size={24} />
        <h2 style={{ fontSize: '1.1rem', fontWeight: 700, color: '#fff' }}>
          VeloSight Control Center
        </h2>
        {demoMode && (
          <span className="badge badge-info" style={{ animation: 'pulse 2s infinite' }}>
            DEMO MODE ENABLED
          </span>
        )}
      </div>

      <div style={{ display: 'flex', alignItems: 'center', gap: '20px' }}>
        {/* System AI Engine Status */}
        <div style={{ display: 'flex', alignItems: 'center', gap: '8px', fontSize: '0.8rem', background: 'var(--bg-card)', padding: '6px 12px', borderRadius: '20px', border: '1px solid var(--border-color)' }}>
          <Activity size={16} color={aiOnline ? '#10b981' : '#ef4444'} />
          <span style={{ color: 'var(--text-muted)' }}>AI Engine:</span>
          <span style={{ fontWeight: 700, color: aiOnline ? '#10b981' : '#ef4444' }}>
            {aiOnline ? '● ONLINE' : '● OFFLINE'}
          </span>
        </div>

        {/* Demo Mode Switch */}
        <button
          onClick={toggleDemoMode}
          style={{ display: 'flex', alignItems: 'center', gap: '6px', background: 'transparent', border: 'none', color: varDemoColor(demoMode), cursor: 'pointer', fontSize: '0.8rem', fontWeight: 600 }}
          title="Toggle Demo Mode for presentations"
        >
          {demoMode ? <ToggleRight size={24} color="#38bdf8" /> : <ToggleLeft size={24} color="#64748b" />}
          <span>Demo Mode</span>
        </button>

        {/* Notifications Icon */}
        <div style={{ position: 'relative' }}>
          <button
            onClick={() => setNotificationsOpen(!notificationsOpen)}
            style={{ background: 'var(--bg-card)', border: '1px solid var(--border-color)', color: '#fff', width: '38px', height: '38px', borderRadius: '8px', display: 'flex', alignItems: 'center', justifyContent: 'center', cursor: 'pointer' }}
          >
            <Bell size={18} />
            <span style={{ position: 'absolute', top: '4px', right: '4px', width: '8px', height: '8px', borderRadius: '50%', backgroundColor: '#ef4444' }}></span>
          </button>

          {notificationsOpen && (
            <div style={{ position: 'absolute', right: 0, top: '48px', width: '320px', backgroundColor: 'var(--bg-card)', border: '1px solid var(--border-color)', borderRadius: '12px', padding: '16px', boxShadow: '0 10px 30px rgba(0,0,0,0.5)', zIndex: 60 }}>
              <div style={{ fontSize: '0.85rem', fontWeight: 700, marginBottom: '12px', color: '#fff' }}>Recent Notifications</div>
              <div style={{ display: 'flex', flexDirection: 'column', gap: '10px', fontSize: '0.8rem' }}>
                <div style={{ padding: '8px', background: 'var(--bg-sidebar)', borderRadius: '6px', borderLeft: '3px solid #ef4444' }}>
                  <div style={{ fontWeight: 600, color: '#ef4444' }}>High Congestion Alert</div>
                  <div style={{ color: 'var(--text-muted)' }}>Central Avenue North approach reached 78% capacity.</div>
                </div>
                <div style={{ padding: '8px', background: 'var(--bg-sidebar)', borderRadius: '6px', borderLeft: '3px solid #38bdf8' }}>
                  <div style={{ fontWeight: 600, color: '#38bdf8' }}>Signal Timing Adjusted</div>
                  <div style={{ color: 'var(--text-muted)' }}>N-S green duration extended by 30 seconds.</div>
                </div>
              </div>
            </div>
          )}
        </div>

        {/* Logout */}
        <button
          onClick={logout}
          style={{ background: 'rgba(239, 68, 68, 0.1)', border: '1px solid rgba(239, 68, 68, 0.3)', color: '#ef4444', padding: '8px 12px', borderRadius: '8px', display: 'flex', alignItems: 'center', gap: '6px', cursor: 'pointer', fontSize: '0.8rem', fontWeight: 600 }}
        >
          <LogOut size={16} />
          <span>Logout</span>
        </button>
      </div>
    </header>
  );
};

const varDemoColor = (demo) => (demo ? '#38bdf8' : '#94a3b8');
