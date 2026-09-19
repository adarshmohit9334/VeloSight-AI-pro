import React from 'react';

export const TrafficStatusBadge = ({ status }) => {
  const s = (status || 'MODERATE').toUpperCase();

  let className = 'badge-moderate';
  let dotColor = 'var(--color-moderate)';

  if (s === 'LOW' || s === 'NORMAL') {
    className = 'badge-low';
    dotColor = 'var(--color-low)';
  } else if (s === 'HIGH') {
    className = 'badge-high';
    dotColor = 'var(--color-high)';
  } else if (s === 'CRITICAL' || s === 'SEVERE') {
    className = 'badge-critical';
    dotColor = 'var(--color-critical)';
  }

  return (
    <span className={`badge ${className}`}>
      <span style={{ width: '6px', height: '6px', borderRadius: '50%', backgroundColor: dotColor }}></span>
      {s}
    </span>
  );
};
