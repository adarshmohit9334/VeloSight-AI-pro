import React, { useState, useEffect } from 'react';
import { SlidersHorizontal, Zap, ShieldAlert, CheckCircle, Clock } from 'lucide-react';
import API from '../services/api';

export const SignalRecommendations = () => {
  const [recommendations, setRecommendations] = useState([]);
  const [simulationActive, setSimulationActive] = useState(false);

  useEffect(() => {
    fetchRecommendations();
  }, []);

  const fetchRecommendations = async () => {
    try {
      const res = await API.get('/recommendations');
      setRecommendations(res.data);
    } catch (e) {
      setRecommendations([
        {
          id: 1,
          intersectionName: 'Central Avenue Crossing',
          nsScore: 82.5,
          ewScore: 34.0,
          nsGreenDuration: 55,
          ewGreenDuration: 25,
          reason: 'North-South traffic corridor currently exhibits 2.4x higher density compared to East-West. Extending N-S green phase duration.',
          confidence: 0.94,
          createdAt: new Date()
        },
        {
          id: 2,
          intersectionName: 'North Plaza Junction',
          nsScore: 48.0,
          ewScore: 78.2,
          nsGreenDuration: 30,
          ewGreenDuration: 50,
          reason: 'East-West IT corridor traffic density is significantly elevated. Reallocating green duration to E-W phase.',
          confidence: 0.91,
          createdAt: new Date(Date.now() - 1800000)
        }
      ]);
    }
  };

  const runSimulation = () => {
    setSimulationActive(true);
    setTimeout(() => setSimulationActive(false), 3000);
  };

  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: '24px' }}>
      
      <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between' }}>
        <div>
          <h1 style={{ fontSize: '1.4rem', fontWeight: 800, color: '#fff' }}>AI Signal Phase Duration Optimization</h1>
          <p style={{ fontSize: '0.8rem', color: 'var(--text-muted)' }}>Automated signal green-light split recommendations (Decision Support Engine)</p>
        </div>

        <div style={{ background: 'rgba(56, 189, 248, 0.1)', color: '#38bdf8', padding: '6px 12px', borderRadius: '8px', border: '1px solid rgba(56, 189, 248, 0.3)', fontSize: '0.75rem', fontWeight: 700 }}>
          🛡️ DECISION SUPPORT ONLY — NO DIRECT HARDWARE CONTROL
        </div>
      </div>

      <div className="grid-2">
        {recommendations.map((rec) => (
          <div key={rec.id} className="card" style={{ border: '1px solid rgba(56, 189, 248, 0.2)' }}>
            
            <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between', marginBottom: '16px' }}>
              <div>
                <h3 style={{ fontSize: '1.1rem', fontWeight: 700, color: '#fff' }}>{rec.intersectionName}</h3>
                <div style={{ fontSize: '0.75rem', color: 'var(--text-muted)' }}>AI Model Confidence: {(rec.confidence * 100).toFixed(0)}%</div>
              </div>
              <span className="badge badge-info"><Zap size={12} /> Active Split</span>
            </div>

            {/* Split Visualizer */}
            <div style={{ display: 'flex', gap: '16px', marginBottom: '16px' }}>
              <div style={{ flex: 1, backgroundColor: 'var(--bg-sidebar)', padding: '16px', borderRadius: '10px', borderLeft: '4px solid #10b981' }}>
                <div style={{ fontSize: '0.7rem', color: 'var(--text-muted)', textTransform: 'uppercase', fontWeight: 700 }}>North-South Green Phase</div>
                <div style={{ fontSize: '1.8rem', fontWeight: 800, color: '#10b981', fontFamily: 'var(--font-mono)', marginTop: '4px' }}>
                  {rec.nsGreenDuration} <span style={{ fontSize: '0.9rem' }}>sec</span>
                </div>
                <div style={{ fontSize: '0.75rem', color: 'var(--text-muted)', marginTop: '4px' }}>
                  Corridor Traffic Score: <strong>{rec.nsScore}%</strong>
                </div>
              </div>

              <div style={{ flex: 1, backgroundColor: 'var(--bg-sidebar)', padding: '16px', borderRadius: '10px', borderLeft: '4px solid #f59e0b' }}>
                <div style={{ fontSize: '0.7rem', color: 'var(--text-muted)', textTransform: 'uppercase', fontWeight: 700 }}>East-West Green Phase</div>
                <div style={{ fontSize: '1.8rem', fontWeight: 800, color: '#f59e0b', fontFamily: 'var(--font-mono)', marginTop: '4px' }}>
                  {rec.ewGreenDuration} <span style={{ fontSize: '0.9rem' }}>sec</span>
                </div>
                <div style={{ fontSize: '0.75rem', color: 'var(--text-muted)', marginTop: '4px' }}>
                  Corridor Traffic Score: <strong>{rec.ewScore}%</strong>
                </div>
              </div>
            </div>

            {/* Rationale Explanation */}
            <div style={{ backgroundColor: 'var(--bg-sidebar)', padding: '12px', borderRadius: '8px', fontSize: '0.8rem', color: 'var(--text-muted)', marginBottom: '16px' }}>
              💡 <strong>AI Analysis Rationale:</strong> {rec.reason}
            </div>

            <div style={{ display: 'flex', justifyContent: 'flex-end', gap: '8px' }}>
              <button 
                className="btn btn-outline" 
                onClick={runSimulation}
                disabled={simulationActive}
              >
                {simulationActive ? 'Running Traffic Sim...' : 'Apply to Traffic Simulation'}
              </button>
            </div>

          </div>
        ))}
      </div>

    </div>
  );
};
