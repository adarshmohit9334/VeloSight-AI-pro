import React from 'react';
import { AlertTriangle, AlertCircle, CheckCircle, Clock } from 'lucide-react';

export const AlertCard = ({ alert, onAcknowledge, onResolve }) => {
  const isCritical = alert.severity === 'CRITICAL';
  const borderColor = isCritical ? 'rgba(239, 68, 68, 0.4)' : 'rgba(249, 115, 22, 0.3)';

  return (
    <div 
      className="card" 
      style={{ 
        borderLeft: `4px solid ${isCritical ? '#ef4444' : '#f97316'}`,
        backgroundColor: 'var(--bg-sidebar)',
        marginBottom: '12px'
      }}
    >
      <div style={{ display: 'flex', alignItems: 'flex-start', justifyContent: 'space-between', gap: '12px' }}>
        <div style={{ display: 'flex', gap: '12px' }}>
          <div style={{ marginTop: '2px' }}>
            {isCritical ? <AlertTriangle size={20} color="#ef4444" /> : <AlertCircle size={20} color="#f97316" />}
          </div>
          <div>
            <div style={{ display: 'flex', alignItems: 'center', gap: '8px' }}>
              <span style={{ fontSize: '0.85rem', fontWeight: 700, color: '#fff' }}>
                {alert.type}
              </span>
              <span className={`badge ${isCritical ? 'badge-critical' : 'badge-high'}`}>
                {alert.severity}
              </span>
            </div>
            <p style={{ fontSize: '0.85rem', color: 'var(--text-muted)', marginTop: '4px' }}>
              {alert.message}
            </p>
            <div style={{ display: 'flex', alignItems: 'center', gap: '12px', fontSize: '0.75rem', color: 'var(--text-dim)', marginTop: '8px' }}>
              <span>📍 {alert.intersectionName || 'Downtown Crossing'}</span>
              <span>•</span>
              <span style={{ display: 'flex', alignItems: 'center', gap: '4px' }}>
                <Clock size={12} /> {alert.timestamp ? new Date(alert.timestamp).toLocaleTimeString() : 'Just now'}
              </span>
            </div>
          </div>
        </div>

        <div style={{ display: 'flex', gap: '8px' }}>
          {alert.status === 'ACTIVE' && (
            <button 
              className="btn btn-outline" 
              style={{ padding: '4px 10px', fontSize: '0.75rem' }}
              onClick={() => onAcknowledge && onAcknowledge(alert.id)}
            >
              Acknowledge
            </button>
          )}
          {alert.status !== 'RESOLVED' && (
            <button 
              className="btn btn-primary" 
              style={{ padding: '4px 10px', fontSize: '0.75rem' }}
              onClick={() => onResolve && onResolve(alert.id)}
            >
              Resolve
            </button>
          )}
          {alert.status === 'RESOLVED' && (
            <span style={{ fontSize: '0.75rem', color: 'var(--color-low)', display: 'flex', alignItems: 'center', gap: '4px' }}>
              <CheckCircle size={14} /> Resolved
            </span>
          )}
        </div>
      </div>
    </div>
  );
};
