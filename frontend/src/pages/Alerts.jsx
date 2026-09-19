import React, { useState, useEffect } from 'react';
import { AlertTriangle, CheckCircle, Filter } from 'lucide-react';
import { AlertCard } from '../components/AlertCard';
import API from '../services/api';

export const Alerts = () => {
  const [alerts, setAlerts] = useState([]);
  const [filter, setFilter] = useState('ALL');

  useEffect(() => {
    fetchAlerts();
  }, []);

  const fetchAlerts = async () => {
    try {
      const res = await API.get('/alerts');
      setAlerts(res.data);
    } catch (e) {
      setAlerts([
        { id: 1, type: 'HIGH_CONGESTION', severity: 'CRITICAL', message: 'Severe traffic queue buildup detected at Central Avenue North Approach (Score: 84%).', intersectionName: 'Central Crossing', status: 'ACTIVE', timestamp: new Date() },
        { id: 2, type: 'POTENTIAL_INCIDENT', severity: 'HIGH', message: 'Unusual stationary vehicle cluster detected near IT Corridor Phase 1 lane 2.', intersectionName: 'North Plaza', status: 'ACTIVE', timestamp: new Date() },
        { id: 3, type: 'CAMERA_OFFLINE', severity: 'MEDIUM', message: 'Camera CAM-WEST-01 lost RTSP signal ping.', intersectionName: 'West Intersect', status: 'ACKNOWLEDGED', timestamp: new Date(Date.now() - 3600000) }
      ]);
    }
  };

  const handleAcknowledge = async (id) => {
    try {
      await API.put(`/alerts/${id}/acknowledge`);
      fetchAlerts();
    } catch (e) {
      setAlerts(alerts.map(a => a.id === id ? { ...a, status: 'ACKNOWLEDGED' } : a));
    }
  };

  const handleResolve = async (id) => {
    try {
      await API.put(`/alerts/${id}/resolve`);
      fetchAlerts();
    } catch (e) {
      setAlerts(alerts.map(a => a.id === id ? { ...a, status: 'RESOLVED' } : a));
    }
  };

  const filteredAlerts = alerts.filter(a => filter === 'ALL' || a.status === filter);

  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: '20px' }}>
      
      <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between' }}>
        <div>
          <h1 style={{ fontSize: '1.4rem', fontWeight: 800, color: '#fff' }}>Traffic Incident & Congestion Alerts</h1>
          <p style={{ fontSize: '0.8rem', color: 'var(--text-muted)' }}>Automated AI anomaly detection, critical density triggers, and resolution workflow</p>
        </div>

        {/* Filter Tabs */}
        <div style={{ display: 'flex', gap: '8px', background: 'var(--bg-sidebar)', padding: '4px', borderRadius: '8px', border: '1px solid var(--border-color)' }}>
          {['ALL', 'ACTIVE', 'ACKNOWLEDGED', 'RESOLVED'].map((status) => (
            <button
              key={status}
              onClick={() => setFilter(status)}
              className="btn"
              style={{
                padding: '6px 12px',
                fontSize: '0.75rem',
                backgroundColor: filter === status ? 'var(--bg-card)' : 'transparent',
                color: filter === status ? '#38bdf8' : 'var(--text-muted)'
              }}
            >
              {status}
            </button>
          ))}
        </div>
      </div>

      <div>
        {filteredAlerts.length > 0 ? (
          filteredAlerts.map(a => (
            <AlertCard key={a.id} alert={a} onAcknowledge={handleAcknowledge} onResolve={handleResolve} />
          ))
        ) : (
          <div className="card" style={{ textAlign: 'center', padding: '40px' }}>
            <CheckCircle size={36} color="#10b981" style={{ margin: '0 auto 12px auto' }} />
            <h3 style={{ fontSize: '1rem', fontWeight: 700, color: '#fff' }}>No Alerts Match Filter</h3>
            <p style={{ fontSize: '0.8rem', color: 'var(--text-muted)', marginTop: '4px' }}>All traffic corridors operating within expected parameters.</p>
          </div>
        )}
      </div>

    </div>
  );
};
