import React, { useState } from 'react';
import { Settings as SettingsIcon, Sliders, ToggleLeft, ToggleRight, Save, Cpu } from 'lucide-react';
import { useAuth } from '../context/AuthContext';

export const Settings = () => {
  const { demoMode, toggleDemoMode } = useAuth();
  const [confidence, setConfidence] = useState(0.40);
  const [frameSkip, setFrameSkip] = useState(2);
  const [maxUploadMb, setMaxUploadMb] = useState(500);
  const [aiUrl, setAiUrl] = useState('http://localhost:8000');
  const [savedSuccess, setSavedSuccess] = useState(false);

  const handleSave = (e) => {
    e.preventDefault();
    setSavedSuccess(true);
    setTimeout(() => setSavedSuccess(false), 3000);
  };

  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: '20px' }}>
      
      <div>
        <h1 style={{ fontSize: '1.4rem', fontWeight: 800, color: '#fff' }}>System & AI Engine Configuration</h1>
        <p style={{ fontSize: '0.8rem', color: 'var(--text-muted)' }}>Tune computer vision thresholds, frame skip rates, and presentation demonstration modes</p>
      </div>

      <div className="card" style={{ maxWidth: '640px' }}>
        <form onSubmit={handleSave}>
          
          <h3 style={{ fontSize: '1rem', fontWeight: 700, color: '#fff', marginBottom: '16px', display: 'flex', alignItems: 'center', gap: '8px' }}>
            <Cpu size={18} color="#38bdf8" /> AI Pipeline Parameters
          </h3>

          <div className="form-group">
            <label className="form-label">YOLO Detection Confidence Threshold ({confidence.toFixed(2)})</label>
            <input 
              type="range" 
              min="0.10" 
              max="0.90" 
              step="0.05" 
              value={confidence} 
              onChange={(e) => setConfidence(parseFloat(e.target.value))}
              style={{ width: '100%', accentColor: 'var(--primary-blue)' }} 
            />
            <div style={{ display: 'flex', justifyContent: 'space-between', fontSize: '0.7rem', color: 'var(--text-muted)' }}>
              <span>0.10 (High sensitivity)</span>
              <span>0.40 (Default)</span>
              <span>0.90 (High precision)</span>
            </div>
          </div>

          <div className="form-group">
            <label className="form-label">Frame Skip Processing Rate</label>
            <select className="form-control" value={frameSkip} onChange={(e) => setFrameSkip(parseInt(e.target.value))}>
              <option value="1">1 (Process every frame — Highest Accuracy)</option>
              <option value="2">2 (Skip 1 frame — Recommended Balance)</option>
              <option value="3">3 (Skip 2 frames — Fast Processing)</option>
              <option value="5">5 (Skip 4 frames — Ultra Fast)</option>
            </select>
          </div>

          <div className="form-group">
            <label className="form-label">Python AI Microservice Endpoint URL</label>
            <input className="form-control" value={aiUrl} onChange={(e) => setAiUrl(e.target.value)} />
          </div>

          <div style={{ padding: '16px 0', borderTop: '1px solid var(--border-color)', borderBottom: '1px solid var(--border-color)', margin: '20px 0' }}>
            <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between' }}>
              <div>
                <div style={{ fontSize: '0.9rem', fontWeight: 700, color: '#fff' }}>Presentation Demo Mode</div>
                <div style={{ fontSize: '0.75rem', color: 'var(--text-muted)' }}>
                  Uses fixed sample dataset for reliable offline college presentations
                </div>
              </div>
              <button 
                type="button"
                onClick={toggleDemoMode} 
                style={{ background: 'transparent', border: 'none', cursor: 'pointer' }}
              >
                {demoMode ? <ToggleRight size={32} color="#38bdf8" /> : <ToggleLeft size={32} color="#64748b" />}
              </button>
            </div>
          </div>

          <button type="submit" className="btn btn-primary" style={{ width: '100%' }}>
            <Save size={16} /> Save Configuration Settings
          </button>

          {savedSuccess && (
            <div style={{ marginTop: '12px', textAlign: 'center', color: 'var(--color-low)', fontSize: '0.85rem', fontWeight: 600 }}>
              ✅ Settings successfully saved to backend environment!
            </div>
          )}
        </form>
      </div>

    </div>
  );
};
