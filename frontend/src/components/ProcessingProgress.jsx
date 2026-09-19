import React from 'react';
import { Loader2, CheckCircle2, Cpu, Eye, BarChart } from 'lucide-react';

export const ProcessingProgress = ({ progress = 0, currentStep = 'Processing...' }) => {
  const steps = [
    { label: 'Video Uploaded', minPct: 0 },
    { label: 'Model Initialized', minPct: 15 },
    { label: 'Vehicle Detection & Tracking', minPct: 40 },
    { label: 'Density & Congestion Analytics', minPct: 75 },
    { label: 'Report & Overlay Rendered', minPct: 95 }
  ];

  return (
    <div className="card" style={{ padding: '24px' }}>
      <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between', marginBottom: '16px' }}>
        <div style={{ display: 'flex', alignItems: 'center', gap: '10px' }}>
          <Loader2 className="spin" size={20} color="#38bdf8" />
          <span style={{ fontSize: '0.95rem', fontWeight: 600, color: '#fff' }}>
            AI Computer Vision Pipeline Active
          </span>
        </div>
        <span style={{ fontSize: '1.25rem', fontWeight: 800, color: '#38bdf8', fontFamily: 'var(--font-mono)' }}>
          {Math.min(Math.round(progress), 100)}%
        </span>
      </div>

      {/* Progress Bar */}
      <div style={{ width: '100%', height: '8px', backgroundColor: 'var(--bg-sidebar)', borderRadius: '4px', overflow: 'hidden', marginBottom: '16px' }}>
        <div 
          style={{ 
            height: '100%', 
            width: `${progress}%`, 
            background: 'linear-gradient(90deg, #0284c7, #38bdf8)', 
            transition: 'width 0.4s ease' 
          }} 
        />
      </div>

      <div style={{ fontSize: '0.8rem', color: 'var(--text-muted)', marginBottom: '20px' }}>
        Current Phase: <strong style={{ color: '#fff' }}>{currentStep}</strong>
      </div>

      {/* Steps checklist */}
      <div style={{ display: 'flex', flexDirection: 'column', gap: '10px' }}>
        {steps.map((step, idx) => {
          const isDone = progress >= step.minPct + 15;
          const isCurrent = progress >= step.minPct && !isDone;
          return (
            <div key={idx} style={{ display: 'flex', alignItems: 'center', gap: '10px', fontSize: '0.85rem' }}>
              {isDone ? (
                <CheckCircle2 size={16} color="#10b981" />
              ) : isCurrent ? (
                <Loader2 className="spin" size={16} color="#38bdf8" />
              ) : (
                <div style={{ width: '16px', height: '16px', borderRadius: '50%', border: '1px solid var(--text-dim)' }} />
              )}
              <span style={{ color: isDone ? '#fff' : isCurrent ? '#38bdf8' : 'var(--text-dim)', fontWeight: isCurrent ? 600 : 400 }}>
                {step.label}
              </span>
            </div>
          );
        })}
      </div>
    </div>
  );
};
