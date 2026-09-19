import React, { useState, useEffect } from 'react';
import { Camera as CameraIcon, Plus, Edit, Trash2, Video } from 'lucide-react';
import { Modal } from '../components/Modal';
import API from '../services/api';

export const Cameras = () => {
  const [cameras, setCameras] = useState([]);
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [formData, setFormData] = useState({ cameraName: '', cameraIdStr: '', direction: 'North', streamUrl: '', status: 'ONLINE', intersectionId: '1' });

  useEffect(() => {
    fetchCameras();
  }, []);

  const fetchCameras = async () => {
    try {
      const res = await API.get('/cameras');
      setCameras(res.data);
    } catch (e) {
      setCameras([
        { id: 1, cameraName: 'Central North Cam-01', cameraIdStr: 'CAM-CENTRAL-01', direction: 'North', status: 'ONLINE', intersectionName: 'Central Crossing' },
        { id: 2, cameraName: 'Central East Cam-02', cameraIdStr: 'CAM-CENTRAL-02', direction: 'East', status: 'ONLINE', intersectionName: 'Central Crossing' },
        { id: 3, cameraName: 'North Plaza Cam-01', cameraIdStr: 'CAM-NORTH-01', direction: 'South', status: 'ONLINE', intersectionName: 'North Plaza Junction' }
      ]);
    }
  };

  const handleCreate = async (e) => {
    e.preventDefault();
    try {
      await API.post('/cameras', formData);
      setIsModalOpen(false);
      fetchCameras();
    } catch (e) {
      setCameras([...cameras, { ...formData, id: Date.now(), intersectionName: 'Central Crossing' }]);
      setIsModalOpen(false);
    }
  };

  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: '20px' }}>
      
      <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between' }}>
        <div>
          <h1 style={{ fontSize: '1.4rem', fontWeight: 800, color: '#fff' }}>Traffic Camera Management</h1>
          <p style={{ fontSize: '0.8rem', color: 'var(--text-muted)' }}>Register video surveillance hardware, stream endpoints, and directional alignment</p>
        </div>
        <button className="btn btn-primary" onClick={() => setIsModalOpen(true)}>
          <Plus size={16} /> Register Camera
        </button>
      </div>

      <div className="card">
        <div className="table-responsive">
          <table className="custom-table">
            <thead>
              <tr>
                <th>Camera ID</th>
                <th>Camera Name</th>
                <th>Intersection</th>
                <th>Direction</th>
                <th>Stream Protocol</th>
                <th>Status</th>
                <th>Actions</th>
              </tr>
            </thead>
            <tbody>
              {cameras.map((c) => (
                <tr key={c.id}>
                  <td style={{ fontFamily: 'var(--font-mono)', fontWeight: 600, color: '#38bdf8' }}>{c.cameraIdStr}</td>
                  <td style={{ fontWeight: 600, color: '#fff' }}>{c.cameraName}</td>
                  <td>{c.intersectionName}</td>
                  <td>{c.direction} Approach</td>
                  <td style={{ fontSize: '0.8rem', color: 'var(--text-muted)' }}>RTSP / H.264 MP4</td>
                  <td><span className="badge badge-low">{c.status}</span></td>
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

      <Modal isOpen={isModalOpen} onClose={() => setIsModalOpen(false)} title="Register Traffic Camera Feed">
        <form onSubmit={handleCreate}>
          <div className="form-group">
            <label className="form-label">Camera Label Name</label>
            <input className="form-control" required value={formData.cameraName} onChange={(e) => setFormData({ ...formData, cameraName: e.target.value })} placeholder="East Highway Cam 01" />
          </div>
          <div className="form-group">
            <label className="form-label">Hardware ID Code</label>
            <input className="form-control" required value={formData.cameraIdStr} onChange={(e) => setFormData({ ...formData, cameraIdStr: e.target.value })} placeholder="CAM-EAST-01" />
          </div>
          <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '12px' }}>
            <div className="form-group">
              <label className="form-label">Approach Direction</label>
              <select className="form-control" value={formData.direction} onChange={(e) => setFormData({ ...formData, direction: e.target.value })}>
                <option value="North">North Approach</option>
                <option value="South">South Approach</option>
                <option value="East">East Approach</option>
                <option value="West">West Approach</option>
              </select>
            </div>
            <div className="form-group">
              <label className="form-label">Target Intersection</label>
              <select className="form-control" value={formData.intersectionId} onChange={(e) => setFormData({ ...formData, intersectionId: e.target.value })}>
                <option value="1">Central Avenue Crossing</option>
                <option value="2">North Plaza Junction</option>
              </select>
            </div>
          </div>
          <button type="submit" className="btn btn-primary" style={{ width: '100%', marginTop: '12px' }}>Save Camera Config</button>
        </form>
      </Modal>

    </div>
  );
};
