import React, { useState, useEffect } from 'react';
import { MapPin, Plus, Edit, Trash2 } from 'lucide-react';
import { Modal } from '../components/Modal';
import API from '../services/api';

export const Intersections = () => {
  const [intersections, setIntersections] = useState([]);
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [formData, setFormData] = useState({ name: '', location: '', latitude: '', longitude: '', status: 'ACTIVE' });

  useEffect(() => {
    fetchIntersections();
  }, []);

  const fetchIntersections = async () => {
    try {
      const res = await API.get('/intersections');
      setIntersections(res.data);
    } catch (e) {
      setIntersections([
        { id: 1, name: 'Central Avenue Crossing', location: 'Downtown Sector 4', latitude: 18.5204, longitude: 73.8567, status: 'ACTIVE', cameraCount: 2 },
        { id: 2, name: 'North Plaza Junction', location: 'IT Corridor Phase 1', latitude: 18.5314, longitude: 73.8446, status: 'ACTIVE', cameraCount: 2 }
      ]);
    }
  };

  const handleCreate = async (e) => {
    e.preventDefault();
    try {
      await API.post('/intersections', formData);
      setIsModalOpen(false);
      fetchIntersections();
    } catch (e) {
      setIntersections([...intersections, { ...formData, id: Date.now(), cameraCount: 0 }]);
      setIsModalOpen(false);
    }
  };

  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: '20px' }}>
      
      <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between' }}>
        <div>
          <h1 style={{ fontSize: '1.4rem', fontWeight: 800, color: '#fff' }}>Intersection Management</h1>
          <p style={{ fontSize: '0.8rem', color: 'var(--text-muted)' }}>Configure smart city traffic monitoring junctions & geographic coordinates</p>
        </div>
        <button className="btn btn-primary" onClick={() => setIsModalOpen(true)}>
          <Plus size={16} /> Add Intersection
        </button>
      </div>

      <div className="card">
        <div className="table-responsive">
          <table className="custom-table">
            <thead>
              <tr>
                <th>ID</th>
                <th>Intersection Name</th>
                <th>Location / District</th>
                <th>Coordinates (Lat, Long)</th>
                <th>Cameras</th>
                <th>Status</th>
                <th>Actions</th>
              </tr>
            </thead>
            <tbody>
              {intersections.map((item) => (
                <tr key={item.id}>
                  <td style={{ fontFamily: 'var(--font-mono)' }}>#{item.id}</td>
                  <td style={{ fontWeight: 600, color: '#fff' }}>{item.name}</td>
                  <td>{item.location}</td>
                  <td style={{ fontFamily: 'var(--font-mono)', fontSize: '0.8rem' }}>
                    {item.latitude || '18.520'}, {item.longitude || '73.856'}
                  </td>
                  <td>{item.cameraCount || 0} active</td>
                  <td>
                    <span className="badge badge-low">{item.status}</span>
                  </td>
                  <td>
                    <div style={{ display: 'flex', gap: '8px' }}>
                      <button className="btn btn-outline" style={{ padding: '4px 8px' }}><Edit size={14} /></button>
                      <button className="btn btn-danger" style={{ padding: '4px 8px' }}><Trash2 size={14} /></button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      </div>

      <Modal isOpen={isModalOpen} onClose={() => setIsModalOpen(false)} title="Register New Traffic Intersection">
        <form onSubmit={handleCreate}>
          <div className="form-group">
            <label className="form-label">Intersection Name</label>
            <input className="form-control" required value={formData.name} onChange={(e) => setFormData({ ...formData, name: e.target.value })} placeholder="East Highway Junction" />
          </div>
          <div className="form-group">
            <label className="form-label">Location / City Zone</label>
            <input className="form-control" required value={formData.location} onChange={(e) => setFormData({ ...formData, location: e.target.value })} placeholder="Sector 12 Avenue" />
          </div>
          <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '12px' }}>
            <div className="form-group">
              <label className="form-label">Latitude</label>
              <input type="number" step="any" className="form-control" value={formData.latitude} onChange={(e) => setFormData({ ...formData, latitude: e.target.value })} placeholder="18.5204" />
            </div>
            <div className="form-group">
              <label className="form-label">Longitude</label>
              <input type="number" step="any" className="form-control" value={formData.longitude} onChange={(e) => setFormData({ ...formData, longitude: e.target.value })} placeholder="73.8567" />
            </div>
          </div>
          <button type="submit" className="btn btn-primary" style={{ width: '100%', marginTop: '12px' }}>Save Intersection</button>
        </form>
      </Modal>

    </div>
  );
};
