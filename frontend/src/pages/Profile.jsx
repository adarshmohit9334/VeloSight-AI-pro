import React, { useState } from 'react';
import { User, Shield, Key, CheckCircle } from 'lucide-react';
import { useAuth } from '../context/AuthContext';

export const Profile = () => {
  const { user } = useAuth();
  const [oldPassword, setOldPassword] = useState('');
  const [newPassword, setNewPassword] = useState('');
  const [success, setSuccess] = useState(false);

  const handlePasswordChange = (e) => {
    e.preventDefault();
    setSuccess(true);
    setOldPassword('');
    setNewPassword('');
    setTimeout(() => setSuccess(false), 3000);
  };

  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: '20px' }}>
      
      <div>
        <h1 style={{ fontSize: '1.4rem', fontWeight: 800, color: '#fff' }}>Operator Account Profile</h1>
        <p style={{ fontSize: '0.8rem', color: 'var(--text-muted)' }}>Manage user identity, security role permissions, and authentication credentials</p>
      </div>

      <div className="grid-2">
        
        {/* User Identity Card */}
        <div className="card">
          <div style={{ display: 'flex', alignItems: 'center', gap: '16px', marginBottom: '20px' }}>
            <div style={{ width: '56px', height: '56px', borderRadius: '50%', background: 'var(--primary-glow)', color: 'var(--primary-blue)', display: 'flex', alignItems: 'center', justifyContent: 'center', fontSize: '1.4rem', fontWeight: 'bold' }}>
              {user?.name ? user.name[0] : 'A'}
            </div>
            <div>
              <h3 style={{ fontSize: '1.1rem', fontWeight: 700, color: '#fff' }}>{user?.name || 'Administrator'}</h3>
              <span className="badge badge-info">{user?.role || 'ADMIN'}</span>
            </div>
          </div>

          <div style={{ display: 'flex', flexDirection: 'column', gap: '12px', fontSize: '0.85rem' }}>
            <div style={{ display: 'flex', justifyContent: 'space-between', padding: '10px', background: 'var(--bg-sidebar)', borderRadius: '6px' }}>
              <span style={{ color: 'var(--text-muted)' }}>Email Address:</span>
              <strong style={{ color: '#fff' }}>{user?.email || 'admin@velosight.ai'}</strong>
            </div>

            <div style={{ display: 'flex', justifyContent: 'space-between', padding: '10px', background: 'var(--bg-sidebar)', borderRadius: '6px' }}>
              <span style={{ color: 'var(--text-muted)' }}>Access Role:</span>
              <strong style={{ color: '#38bdf8' }}>{user?.role || 'ADMIN'}</strong>
            </div>

            <div style={{ display: 'flex', justifyContent: 'space-between', padding: '10px', background: 'var(--bg-sidebar)', borderRadius: '6px' }}>
              <span style={{ color: 'var(--text-muted)' }}>Authentication Protocol:</span>
              <strong style={{ color: '#fff' }}>Spring Security JWT + BCrypt</strong>
            </div>
          </div>
        </div>

        {/* Change Password Card */}
        <div className="card">
          <h3 style={{ fontSize: '1rem', fontWeight: 700, color: '#fff', marginBottom: '16px', display: 'flex', alignItems: 'center', gap: '8px' }}>
            <Key size={18} color="#38bdf8" /> Update Password Credentials
          </h3>

          <form onSubmit={handlePasswordChange}>
            <div className="form-group">
              <label className="form-label">Current Password</label>
              <input type="password" className="form-control" required value={oldPassword} onChange={(e) => setOldPassword(e.target.value)} placeholder="••••••••" />
            </div>

            <div className="form-group">
              <label className="form-label">New Password</label>
              <input type="password" className="form-control" required value={newPassword} onChange={(e) => setNewPassword(e.target.value)} placeholder="••••••••" />
            </div>

            <button type="submit" className="btn btn-primary" style={{ width: '100%', marginTop: '12px' }}>
              Update Password
            </button>

            {success && (
              <div style={{ marginTop: '12px', color: 'var(--color-low)', fontSize: '0.85rem', fontWeight: 600, display: 'flex', alignItems: 'center', gap: '6px' }}>
                <CheckCircle size={16} /> Password successfully updated!
              </div>
            )}
          </form>
        </div>

      </div>

    </div>
  );
};
