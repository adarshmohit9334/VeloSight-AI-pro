import React, { useState, useEffect } from 'react';
import { History as HistoryIcon, Search, Eye, Download, Trash2, Calendar } from 'lucide-react';
import { TrafficStatusBadge } from '../components/TrafficStatusBadge';
import API from '../services/api';

export const History = () => {
  const [sessions, setSessions] = useState([]);
  const [searchTerm, setSearchTerm] = useState('');

  useEffect(() => {
    fetchHistory();
  }, []);

  const fetchHistory = async () => {
    try {
      const res = await API.get('/analysis');
      setSessions(res.data);
    } catch (e) {
      setSessions([
        { id: 1, analysisId: 'VS-DEMO-001', videoFilename: 'traffic_peak_hour.mp4', totalVehicles: 156, densityLevel: 'HIGH', congestionLevel: 'HIGH', status: 'COMPLETED', createdAt: new Date() },
        { id: 2, analysisId: 'VS-DEMO-002', videoFilename: 'highway_flow_morning.mp4', totalVehicles: 88, densityLevel: 'MODERATE', congestionLevel: 'LOW', status: 'COMPLETED', createdAt: new Date(Date.now() - 7200000) }
      ]);
    }
  };

  const filteredSessions = sessions.filter(s => 
    s.analysisId?.toLowerCase().includes(searchTerm.toLowerCase()) ||
    s.videoFilename?.toLowerCase().includes(searchTerm.toLowerCase())
  );

  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: '20px' }}>
      
      <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between' }}>
        <div>
          <h1 style={{ fontSize: '1.4rem', fontWeight: 800, color: '#fff' }}>Historical Traffic Analysis Sessions</h1>
          <p style={{ fontSize: '0.8rem', color: 'var(--text-muted)' }}>Audit trail of processed video streams, vehicle metrics, and annotated videos</p>
        </div>

        {/* Search */}
        <div style={{ position: 'relative', width: '280px' }}>
          <input
            type="text"
            className="form-control"
            style={{ paddingLeft: '36px' }}
            placeholder="Search analysis ID or filename..."
            value={searchTerm}
            onChange={(e) => setSearchTerm(e.target.value)}
          />
          <Search size={16} style={{ position: 'absolute', left: '12px', top: '12px', color: 'var(--text-dim)' }} />
        </div>
      </div>

      <div className="card">
        <div className="table-responsive">
          <table className="custom-table">
            <thead>
              <tr>
                <th>Analysis ID</th>
                <th>Video File</th>
                <th>Total Vehicles</th>
                <th>Density Level</th>
                <th>Congestion Level</th>
                <th>Status</th>
                <th>Timestamp</th>
                <th>Actions</th>
              </tr>
            </thead>
            <tbody>
              {filteredSessions.map((s) => (
                <tr key={s.id}>
                  <td style={{ fontFamily: 'var(--font-mono)', fontWeight: 600, color: '#38bdf8' }}>{s.analysisId}</td>
                  <td style={{ fontWeight: 600, color: '#fff' }}>{s.videoFilename}</td>
                  <td style={{ fontFamily: 'var(--font-mono)' }}>{s.totalVehicles}</td>
                  <td><TrafficStatusBadge status={s.densityLevel} /></td>
                  <td><TrafficStatusBadge status={s.congestionLevel} /></td>
                  <td><span className="badge badge-low">{s.status}</span></td>
                  <td style={{ fontSize: '0.8rem', color: 'var(--text-muted)' }}>
                    {s.createdAt ? new Date(s.createdAt).toLocaleString() : 'Recent'}
                  </td>
                  <td>
                    <div style={{ display: 'flex', gap: '8px' }}>
                      <button className="btn btn-outline" style={{ padding: '4px 8px' }} title="View Analysis Details">
                        <Eye size={14} />
                      </button>
                      <button className="btn btn-danger" style={{ padding: '4px 8px' }} title="Delete Session">
                        <Trash2 size={14} />
                      </button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      </div>

    </div>
  );
};
