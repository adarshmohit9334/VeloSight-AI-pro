import React from 'react';

export const StatCard = ({ label, value, icon: Icon, color = '#38bdf8', subtext }) => {
  return (
    <div className="stat-card">
      <div>
        <div className="stat-label">{label}</div>
        <div className="stat-value">{value}</div>
        {subtext && (
          <div style={{ fontSize: '0.7rem', color: 'var(--text-muted)', marginTop: '4px' }}>
            {subtext}
          </div>
        )}
      </div>
      <div 
        className="stat-icon-wrapper"
        style={{ background: `${color}15`, color: color }}
      >
        <Icon size={24} />
      </div>
    </div>
  );
};
