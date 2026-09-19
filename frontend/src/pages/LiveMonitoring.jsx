import React, { useState } from 'react';
import { Camera, Video, Activity, Eye, Play, Radio } from 'lucide-react';
import { TrafficStatusBadge } from '../components/TrafficStatusBadge';

export const LiveMonitoring = () => {
  const [selectedCamera, setSelectedCamera] = useState('CAM-CENTRAL-01');

  const cameras = [
    { id: 'CAM-CENTRAL-01', name: 'Central North Approach', location: 'Central Junction', vehicles: 42, speed: 28, status: 'HIGH' },
    { id: 'CAM-CENTRAL-02', name: 'Central East Approach', location: 'Central Junction', vehicles: 18, speed: 45, status: 'LOW' },
    { id: 'CAM-NORTH-01', name: 'North Plaza Approach', location: 'IT Corridor', vehicles: 31, speed: 36, status: 'MODERATE' }
  ];

  const currentCam = cameras.find(c => c.id === selectedCamera) || cameras[0];

  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: '20px' }}>
      <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between' }}>
        <div>
          <h1 style={{ fontSize: '1.4rem', fontWeight: 800, color: '#fff' }}>Live Traffic Video Stream Monitoring</h1>
          <p style={{ fontSize: '0.8rem', color: 'var(--text-muted)' }}>Real-time camera feed inspection & HUD analytics overlay</p>
        </div>
        <div style={{ display: 'flex', alignItems: 'center', gap: '8px', background: 'rgba(239, 68, 68, 0.15)', color: '#ef4444', padding: '6px 12px', borderRadius: '20px', border: '1px solid rgba(239, 68, 68, 0.3)', fontSize: '0.8rem', fontWeight: 700 }}>
          <Radio size={16} className="spin" />
          <span>LIVE STREAM ACTIVE</span>
        </div>
      </div>

      <div className="grid-3" style={{ gridTemplateColumns: '2.2fr 1fr' }}>
        
        {/* Main Stream Player View */}
        <div className="card" style={{ padding: 0, overflow: 'hidden', position: 'relative', backgroundColor: '#000', minHeight: '420px', display: 'flex', flexDirection: 'column' }}>
          
          {/* Stream Overlay HUD Header */}
          <div style={{ position: 'absolute', top: 0, left: 0, right: 0, backgroundColor: 'rgba(15, 23, 42, 0.85)', padding: '12px 16px', display: 'flex', alignItems: 'center', justifyContent: 'space-between', zIndex: 10, backdropFilter: 'blur(8px)', borderBottom: '1px solid rgba(255,255,255,0.1)' }}>
            <div style={{ display: 'flex', alignItems: 'center', gap: '10px' }}>
              <span className="badge badge-critical">● REC</span>
              <span style={{ fontSize: '0.9rem', fontWeight: 700, color: '#fff' }}>{currentCam.name}</span>
            </div>
            <div style={{ fontSize: '0.75rem', color: '#38bdf8', fontFamily: 'var(--font-mono)' }}>
              1920x1080 @ 30 FPS • H.264
            </div>
          </div>

          {/* Simulated Video Feed Area */}
          <div style={{ flex: 1, display: 'flex', alignItems: 'center', justifyContent: 'center', position: 'relative', background: 'radial-gradient(circle at center, #1e293b 0%, #090d16 100%)' }}>
            <div style={{ textAlign: 'center', padding: '40px' }}>
              <Camera size={48} color="#38bdf8" style={{ margin: '0 auto 12px auto', opacity: 0.8 }} />
              <div style={{ fontSize: '1.1rem', fontWeight: 700, color: '#fff' }}>
                {currentCam.name} Video Stream Feed
              </div>
              <div style={{ fontSize: '0.8rem', color: 'var(--text-muted)', marginTop: '6px' }}>
                Mode: Live Camera Stream Processing Engine
              </div>
              <div style={{ marginTop: '20px', display: 'inline-flex', alignItems: 'center', gap: '8px', background: 'var(--primary-glow)', color: 'var(--primary-blue)', padding: '8px 16px', borderRadius: '8px', fontSize: '0.8rem', fontWeight: 600 }}>
                <Eye size={16} /> OpenCV Frame Analyzer Running
              </div>
            </div>

            {/* Live Bounding Boxes Simulation Overlay */}
            <div style={{ position: 'absolute', bottom: '20px', left: '20px', backgroundColor: 'rgba(15, 23, 42, 0.9)', padding: '12px 16px', borderRadius: '8px', border: '1px solid var(--border-color)', fontSize: '0.8rem' }}>
              <div style={{ color: 'var(--text-muted)' }}>Real-time Detection Stats:</div>
              <div style={{ display: 'flex', gap: '16px', marginTop: '4px', fontWeight: 700, color: '#fff' }}>
                <span>Cars: 28</span>
                <span>Motorcycles: 11</span>
                <span>Buses: 3</span>
              </div>
            </div>
          </div>

          {/* Bottom Feed HUD */}
          <div style={{ backgroundColor: 'var(--bg-sidebar)', padding: '12px 16px', display: 'flex', alignItems: 'center', justifyContent: 'space-between', borderTop: '1px solid var(--border-color)' }}>
            <div style={{ display: 'flex', gap: '20px', fontSize: '0.8rem' }}>
              <div><span style={{ color: 'var(--text-muted)' }}>Live Vehicles:</span> <strong style={{ color: '#fff' }}>{currentCam.vehicles}</strong></div>
              <div><span style={{ color: 'var(--text-muted)' }}>Avg Flow Speed:</span> <strong style={{ color: '#fff' }}>{currentCam.speed} km/h</strong></div>
            </div>
            <TrafficStatusBadge status={currentCam.status} />
          </div>

        </div>

        {/* Camera Selector Sidebar */}
        <div style={{ display: 'flex', flexDirection: 'column', gap: '12px' }}>
          <div style={{ fontSize: '0.9rem', fontWeight: 700, color: '#fff' }}>Available Camera Feeds</div>

          {cameras.map((cam) => (
            <div
              key={cam.id}
              onClick={() => setSelectedCamera(cam.id)}
              className="card"
              style={{
                cursor: 'pointer',
                borderColor: selectedCamera === cam.id ? 'var(--primary-blue)' : 'var(--border-color)',
                backgroundColor: selectedCamera === cam.id ? 'var(--primary-glow)' : 'var(--bg-card)',
                transition: 'all 0.2s ease'
              }}
            >
              <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between' }}>
                <div style={{ display: 'flex', alignItems: 'center', gap: '10px' }}>
                  <Camera size={20} color={selectedCamera === cam.id ? '#38bdf8' : '#94a3b8'} />
                  <div>
                    <div style={{ fontSize: '0.85rem', fontWeight: 600, color: '#fff' }}>{cam.name}</div>
                    <div style={{ fontSize: '0.7rem', color: 'var(--text-muted)' }}>{cam.location} • {cam.id}</div>
                  </div>
                </div>
                <TrafficStatusBadge status={cam.status} />
              </div>
            </div>
          ))}

          <div className="card" style={{ backgroundColor: 'var(--bg-sidebar)', marginTop: 'auto' }}>
            <div style={{ fontSize: '0.8rem', fontWeight: 600, color: '#fff', marginBottom: '6px' }}>
              🎥 Camera Stream Architecture
            </div>
            <p style={{ fontSize: '0.75rem', color: 'var(--text-muted)' }}>
              Supports uploaded traffic video clips as well as live RTSP H.264 camera streams over HTTP.
            </p>
          </div>
        </div>

      </div>
    </div>
  );
};
