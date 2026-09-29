import React from 'react';
import { NavLink } from 'react-router-dom';
import { 
  LayoutDashboard, 
  Video, 
  FileSearch, 
  BarChart3, 
  MapPin, 
  Camera, 
  AlertTriangle, 
  SlidersHorizontal, 
  History, 
  FileText, 
  Settings, 
  User as UserIcon,
  ShieldCheck
} from 'lucide-react';
import { TrafficLogo } from './TrafficLogo';
import { useAuth } from '../context/AuthContext';

export const Sidebar = () => {
  const { user } = useAuth();

  const userRole = user?.role?.replace('ROLE_', '').toUpperCase() || 'ADMIN';

  const navItems = [
    { label: 'Dashboard', path: '/dashboard', icon: LayoutDashboard },
    { label: 'Live Monitoring', path: '/live-monitoring', icon: Video, roles: ['ADMIN', 'OPERATOR'] },
    { label: 'Video Analysis', path: '/video-analysis', icon: FileSearch, roles: ['ADMIN', 'ANALYST'] },
    { label: 'Analytics', path: '/analytics', icon: BarChart3, roles: ['ADMIN', 'ANALYST'] },
    { label: 'Intersections', path: '/intersections', icon: MapPin, roles: ['ADMIN'] },
    { label: 'Cameras', path: '/cameras', icon: Camera, roles: ['ADMIN'] },
    { label: 'Alerts', path: '/alerts', icon: AlertTriangle, roles: ['ADMIN', 'OPERATOR'] },
    { label: 'Signal AI', path: '/signal-recommendations', icon: SlidersHorizontal, roles: ['ADMIN', 'ANALYST'] },
    { label: 'History', path: '/history', icon: History },
    { label: 'Reports', path: '/reports', icon: FileText, roles: ['ADMIN', 'ANALYST'] },
  ];

  return (
    <aside className="sidebar">
      <div style={{ padding: '20px', borderBottom: '1px solid var(--border-color)' }}>
        <div className="brand-title">
          <TrafficLogo size={32} />
          <span>VeloSight AI</span>
          <span className="brand-badge">PRO</span>
        </div>
        <div style={{ fontSize: '0.7rem', color: 'var(--text-muted)', marginTop: '4px' }}>
          Intelligent Traffic Management
        </div>
      </div>

      <div className="nav-section" style={{ flex: 1, overflowY: 'auto' }}>
        {navItems.filter(item => !item.roles || item.roles.includes(userRole)).map((item) => {
          const Icon = item.icon;
          return (
            <NavLink
              key={item.path}
              to={item.path}
              className={({ isActive }) => `nav-item ${isActive ? 'active' : ''}`}
            >
              <Icon size={18} />
              <span>{item.label}</span>
            </NavLink>
          );
        })}

        {userRole === 'ADMIN' && (
          <>
            <div style={{ margin: '16px 0 8px 12px', fontSize: '0.65rem', textTransform: 'uppercase', color: 'var(--text-dim)', fontWeight: 700 }}>
              System Administration
            </div>

            <NavLink to="/settings" className={({ isActive }) => `nav-item ${isActive ? 'active' : ''}`}>
              <Settings size={18} />
              <span>Settings</span>
            </NavLink>
          </>
        )}

      </div>

      <NavLink 
        to="/profile"
        style={{ 
          padding: '16px', 
          borderTop: '1px solid var(--border-color)', 
          backgroundColor: 'rgba(0,0,0,0.2)',
          display: 'block',
          textDecoration: 'none',
          cursor: 'pointer'
        }}
      >
        <div style={{ display: 'flex', alignItems: 'center', gap: '10px' }}>
          <div style={{ width: '36px', height: '36px', borderRadius: '50%', background: 'var(--primary-glow)', display: 'flex', alignItems: 'center', justifyContent: 'center', color: 'var(--primary-blue)', fontWeight: 'bold', overflow: 'hidden' }}>
            {user?.profilePhoto ? (
              <img src={user.profilePhoto} alt="Profile" style={{ width: '100%', height: '100%', objectFit: 'cover' }} />
            ) : (
              user?.name ? user.name[0] : 'A'
            )}
          </div>
          <div style={{ overflow: 'hidden' }}>
            <div style={{ fontSize: '0.85rem', fontWeight: 600, color: '#fff', whiteSpace: 'nowrap', textOverflow: 'ellipsis' }}>
              {user?.name || 'Admin'}
            </div>
            <div style={{ fontSize: '0.7rem', color: 'var(--primary-blue)', fontWeight: 700, textTransform: 'uppercase' }}>
              {userRole}
            </div>
          </div>
        </div>
      </NavLink>
    </aside>
  );
};
