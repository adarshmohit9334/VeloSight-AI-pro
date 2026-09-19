import React from 'react';

export const TrafficLogo = ({ size = 28, animated = true }) => {
  return (
    <div 
      style={{ 
        display: 'inline-flex', 
        alignItems: 'center', 
        justify: 'center',
        position: 'relative',
        width: `${size}px`,
        height: `${size}px`,
        flexShrink: 0
      }}
    >
      <svg 
        width={size} 
        height={size} 
        viewBox="0 0 36 36" 
        fill="none" 
        xmlns="http://www.w3.org/2000/svg"
      >
        {/* Dark Metallic Traffic Signal Housing */}
        <rect 
          x="9" 
          y="2" 
          width="18" 
          height="32" 
          rx="6" 
          fill="#0f172a" 
          stroke="#38bdf8" 
          strokeWidth="1.5"
        />

        {/* Red Light */}
        <circle cx="18" cy="8" r="3.5" fill="#ef4444" />
        <circle cx="18" cy="8" r="4.5" fill="#ef4444" opacity="0.4" className={animated ? "glow-pulse-red" : ""} />

        {/* Yellow Light */}
        <circle cx="18" cy="18" r="3.5" fill="#f59e0b" />
        <circle cx="18" cy="18" r="4.5" fill="#f59e0b" opacity="0.4" className={animated ? "glow-pulse-yellow" : ""} />

        {/* Green Light */}
        <circle cx="18" cy="28" r="3.5" fill="#10b981" />
        <circle cx="18" cy="28" r="4.5" fill="#10b981" opacity="0.4" className={animated ? "glow-pulse-green" : ""} />
      </svg>
    </div>
  );
};
