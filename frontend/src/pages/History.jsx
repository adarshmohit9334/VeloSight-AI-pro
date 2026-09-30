import React, { useState, useEffect } from 'react';
import { History as HistoryIcon, Search, Eye, Download, Trash2, Calendar } from 'lucide-react';
import { TrafficStatusBadge } from '../components/TrafficStatusBadge';
import { Modal } from '../components/Modal';
import API from '../services/api';

export const History = () => {
  const [sessions, setSessions] = useState([]);
  const [searchTerm, setSearchTerm] = useState('');
  const [selectedSession, setSelectedSession] = useState(null);

  useEffect(() => {
    fetchHistory();
  }, []);

  const fetchHistory = async () => {
    try {
      const res = await API.get('/analysis');
      setSessions(res.data);
    } catch (e) {
      setSessions([]);
    }
  };

  const handleDelete = async (id) => {
    if (window.confirm('Are you sure you want to delete this analysis session?')) {
      try {
        await API.delete(`/analysis/${id}`);
        setSessions(sessions.filter(s => s.analysisId !== id));
      } catch (e) {
        console.error('Failed to delete session', e);
      }
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
                      <button onClick={() => setSelectedSession(s)} className="btn btn-outline" style={{ padding: '4px 8px' }} title="View Analysis Details">
                        <Eye size={14} />
                      </button>
                      <button onClick={() => handleDelete(s.analysisId)} className="btn btn-danger" style={{ padding: '4px 8px' }} title="Delete Session">
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

      <Modal isOpen={!!selectedSession} onClose={() => setSelectedSession(null)} title={`Analysis Details - ${selectedSession?.analysisId}`}>
        {selectedSession && (
          <div style={{ display: 'flex', flexDirection: 'column', gap: '15px' }}>
            <video 
              src={selectedSession.processedVideoUrl} 
              controls 
              style={{ width: '100%', borderRadius: '8px', background: '#000', maxHeight: '400px' }} 
            />
            <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '10px', fontSize: '0.9rem', color: '#fff' }}>
              <div><strong>Status:</strong> <span className="badge badge-low">{selectedSession.status}</span></div>
              <div><strong>Total Vehicles:</strong> {selectedSession.totalVehicles}</div>
              <div><strong>Density:</strong> {selectedSession.densityLevel}</div>
              <div><strong>Congestion:</strong> {selectedSession.congestionLevel}</div>
              <div><strong>File:</strong> {selectedSession.videoFilename}</div>
              <div><strong>Date:</strong> {selectedSession.createdAt ? new Date(selectedSession.createdAt).toLocaleString() : 'N/A'}</div>
            </div>

            {selectedSession.vehicleObservations && selectedSession.vehicleObservations.length > 0 && (
              <div style={{ marginTop: '10px' }}>
                <h3 style={{ fontSize: '1rem', color: '#fff', marginBottom: '10px' }}>Detected Number Plates (ANPR)</h3>
                <div className="table-responsive" style={{ maxHeight: '200px', overflowY: 'auto' }}>
                  <table className="custom-table" style={{ fontSize: '0.85rem' }}>
                    <thead>
                      <tr>
                        <th>Track ID</th>
                        <th>Class</th>
                        <th>Plate Number</th>
                        <th>Status</th>
                        <th>Confidence</th>
                      </tr>
                    </thead>
                    <tbody>
                      {selectedSession.vehicleObservations
                        .filter(obs => obs.plateNumber && obs.plateNumber.trim() !== '')
                        .map((obs, idx) => (
                        <tr key={idx}>
                          <td style={{ color: '#38bdf8' }}>#{obs.trackId}</td>
                          <td>{obs.vehicleType}</td>
                          <td style={{ fontWeight: 'bold', color: '#10b981' }}>{obs.plateNumber}</td>
                          <td>
                            <span className={`badge ${obs.plateStatus === 'READ' ? 'badge-low' : 'badge-moderate'}`}>
                              {obs.plateStatus}
                            </span>
                          </td>
                          <td>{obs.ocrConfidence ? (obs.ocrConfidence * 100).toFixed(1) + '%' : '-'}</td>
                        </tr>
                      ))}
                      {selectedSession.vehicleObservations.filter(obs => obs.plateNumber && obs.plateNumber.trim() !== '').length === 0 && (
                        <tr>
                          <td colSpan="5" style={{ textAlign: 'center', color: 'var(--text-muted)' }}>
                            No plates detected with sufficient confidence.
                          </td>
                        </tr>
                      )}
                    </tbody>
                  </table>
                </div>
              </div>
            )}
          </div>
        )}
      </Modal>

    </div>
  );
};
