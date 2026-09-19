import React, { useState, useEffect } from 'react';
import { 
  Car, 
  Activity, 
  AlertTriangle, 
  SlidersHorizontal, 
  TrendingUp, 
  MapPin, 
  ArrowUpRight, 
  Zap, 
  Layers 
} from 'lucide-react';
import { 
  AreaChart, 
  Area, 
  XAxis, 
  YAxis, 
  Tooltip, 
  ResponsiveContainer, 
  PieChart, 
  Pie, 
  Cell, 
  BarChart, 
  Bar 
} from 'recharts';
import { StatCard } from '../components/StatCard';
import { TrafficStatusBadge } from '../components/TrafficStatusBadge';
import { AlertCard } from '../components/AlertCard';
import API from '../services/api';
import { useAuth } from '../context/AuthContext';

const PIE_COLORS = ['#38bdf8', '#10b981', '#f59e0b', '#ef4444'];

export const Dashboard = () => {
  const { demoMode } = useAuth();
  const [summary, setSummary] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    fetchDashboardData();
  }, [demoMode]);

  const fetchDashboardData = async () => {
    setLoading(true);
    try {
      const res = await API.get('/dashboard/summary');
      setSummary(res.data);
    } catch (err) {
      // Demo dataset fallback
      setSummary({
        totalVehicles: 1284,
        currentTrafficDensity: 'HIGH',
        congestionLevel: 'MODERATE',
        activeAlertsCount: 4,
        volumeTrends: [
          { time: '08:00', vehicles: 320, density: 45 },
          { time: '10:00', vehicles: 450, density: 65 },
          { time: '12:00', vehicles: 390, density: 55 },
          { time: '14:00', vehicles: 410, density: 60 },
          { time: '16:00', vehicles: 580, density: 82 },
          { time: '18:00', vehicles: 640, density: 88 },
          { time: '20:00', vehicles: 420, density: 62 },
        ],
        vehicleDistribution: { Cars: 780, Motorcycles: 340, Buses: 45, Trucks: 119 },
        recentAlerts: [
          { id: 1, type: 'HIGH_CONGESTION', severity: 'CRITICAL', message: 'Central Avenue North corridor congestion peaked at 88%.', intersectionName: 'Central Crossing', status: 'ACTIVE', timestamp: new Date() },
          { id: 2, type: 'POTENTIAL_INCIDENT', severity: 'HIGH', message: 'Sudden vehicle deceleration on Expressway exit ramp 4.', intersectionName: 'IT Corridor', status: 'ACTIVE', timestamp: new Date() }
        ],
        recentRecommendations: [
          { id: 1, intersectionName: 'Central Avenue Crossing', nsScore: 82.5, ewScore: 34.0, nsGreenDuration: 55, ewGreenDuration: 25, reason: 'North-South traffic flow volume is 2.4x higher than East-West. Extending N-S green phase.', confidence: 0.94 }
        ],
        activeIntersections: [
          { id: 1, name: 'Central Avenue Crossing', location: 'Downtown', cameraCount: 4, status: 'ACTIVE' },
          { id: 2, name: 'North Plaza Junction', location: 'IT Corridor', cameraCount: 2, status: 'ACTIVE' }
        ]
      });
    } finally {
      setLoading(false);
    }
  };

  const pieData = summary?.vehicleDistribution 
    ? Object.keys(summary.vehicleDistribution).map((key) => ({ name: key, value: summary.vehicleDistribution[key] }))
    : [];

  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: '24px' }}>
      
      {/* Header Banner */}
      <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between' }}>
        <div>
          <h1 style={{ fontSize: '1.5rem', fontWeight: 800, color: '#fff' }}>
            Smart City Traffic Analytics Command Center
          </h1>
          <p style={{ fontSize: '0.85rem', color: 'var(--text-muted)' }}>
            Real-time AI vehicle detection, density profiling, and signal decision support
          </p>
        </div>
        <button className="btn btn-outline" onClick={fetchDashboardData}>
          Refresh Analytics
        </button>
      </div>

      {/* Row 1: KPI Cards */}
      <div className="grid-4">
        <StatCard
          label="Total Vehicles Processed"
          value={summary?.totalVehicles?.toLocaleString() || '1,284'}
          icon={Car}
          color="#38bdf8"
          subtext="Cumulative count across active cameras"
        />
        <StatCard
          label="Current Traffic Density"
          value={summary?.currentTrafficDensity || 'HIGH'}
          icon={Activity}
          color="#f97316"
          subtext="Occupancy & vehicle concentration score"
        />
        <StatCard
          label="System Congestion Level"
          value={summary?.congestionLevel || 'MODERATE'}
          icon={TrendingUp}
          color="#f59e0b"
          subtext="Calculated flow velocity & delay index"
        />
        <StatCard
          label="Active Alerts"
          value={summary?.activeAlertsCount || '4'}
          icon={AlertTriangle}
          color="#ef4444"
          subtext="Requiring operator acknowledgment"
        />
      </div>

      {/* Row 2: Charts & Traffic Volume */}
      <div className="grid-3" style={{ gridTemplateColumns: '2fr 1fr' }}>
        
        {/* Main Chart */}
        <div className="card">
          <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between', marginBottom: '20px' }}>
            <div>
              <h3 style={{ fontSize: '1rem', fontWeight: 700, color: '#fff' }}>Hourly Traffic Volume & Density Trends</h3>
              <p style={{ fontSize: '0.75rem', color: 'var(--text-muted)' }}>Real-time time-series vehicle aggregation</p>
            </div>
            <div style={{ display: 'flex', gap: '12px', fontSize: '0.75rem' }}>
              <span style={{ color: '#38bdf8', fontWeight: 600 }}>● Vehicles/Hour</span>
              <span style={{ color: '#10b981', fontWeight: 600 }}>● Density Score</span>
            </div>
          </div>

          <div style={{ width: '100%', height: '280px' }}>
            <ResponsiveContainer width="100%" height="100%">
              <AreaChart data={summary?.volumeTrends || []}>
                <defs>
                  <linearGradient id="colorVehicles" x1="0" y1="0" x2="0" y2="1">
                    <stop offset="5%" stopColor="#38bdf8" stopOpacity={0.4}/>
                    <stop offset="95%" stopColor="#38bdf8" stopOpacity={0}/>
                  </linearGradient>
                </defs>
                <XAxis dataKey="time" stroke="#64748b" fontSize={12} />
                <YAxis stroke="#64748b" fontSize={12} />
                <Tooltip 
                  contentStyle={{ backgroundColor: '#0f172a', borderColor: 'rgba(255,255,255,0.1)', borderRadius: '8px', color: '#fff' }}
                />
                <Area type="monotone" dataKey="vehicles" stroke="#38bdf8" strokeWidth={3} fillOpacity={1} fill="url(#colorVehicles)" />
              </AreaChart>
            </ResponsiveContainer>
          </div>
        </div>

        {/* Vehicle Breakdown Pie */}
        <div className="card">
          <h3 style={{ fontSize: '1rem', fontWeight: 700, color: '#fff', marginBottom: '4px' }}>Vehicle Class Distribution</h3>
          <p style={{ fontSize: '0.75rem', color: 'var(--text-muted)', marginBottom: '16px' }}>AI Classification split</p>

          <div style={{ width: '100%', height: '180px' }}>
            <ResponsiveContainer width="100%" height="100%">
              <PieChart>
                <Pie data={pieData} dataKey="value" nameKey="name" cx="50%" cy="50%" outerRadius={65} label>
                  {pieData.map((entry, index) => (
                    <Cell key={`cell-${index}`} fill={PIE_COLORS[index % PIE_COLORS.length]} />
                  ))}
                </Pie>
                <Tooltip contentStyle={{ backgroundColor: '#0f172a', borderRadius: '8px' }} />
              </PieChart>
            </ResponsiveContainer>
          </div>

          <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '8px', marginTop: '12px', fontSize: '0.75rem' }}>
            {pieData.map((item, idx) => (
              <div key={item.name} style={{ display: 'flex', alignItems: 'center', gap: '6px' }}>
                <div style={{ width: '8px', height: '8px', borderRadius: '50%', backgroundColor: PIE_COLORS[idx % PIE_COLORS.length] }} />
                <span style={{ color: 'var(--text-muted)' }}>{item.name}:</span>
                <strong style={{ color: '#fff' }}>{item.value}</strong>
              </div>
            ))}
          </div>
        </div>

      </div>

      {/* Row 3: AI Signal Recommendation Banner & Alerts */}
      <div className="grid-2">
        
        {/* Signal AI Recommendation */}
        <div className="card" style={{ background: 'linear-gradient(135deg, #0f172a 0%, #1e293b 100%)', border: '1px solid rgba(56, 189, 248, 0.2)' }}>
          <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between', marginBottom: '16px' }}>
            <div style={{ display: 'flex', alignItems: 'center', gap: '10px' }}>
              <div style={{ width: '36px', height: '36px', borderRadius: '8px', background: 'var(--primary-glow)', color: 'var(--primary-blue)', display: 'flex', alignItems: 'center', justifyContent: 'center' }}>
                <Zap size={20} />
              </div>
              <div>
                <h3 style={{ fontSize: '1rem', fontWeight: 700, color: '#fff' }}>AI Signal Phase Recommendation</h3>
                <span style={{ fontSize: '0.7rem', color: 'var(--primary-blue)', fontWeight: 600 }}>Decision Support Only</span>
              </div>
            </div>
            <span className="badge badge-info">94% Confidence</span>
          </div>

          {summary?.recentRecommendations?.[0] ? (
            <div>
              <div style={{ display: 'flex', gap: '16px', marginBottom: '16px' }}>
                <div style={{ flex: 1, backgroundColor: 'var(--bg-sidebar)', padding: '12px', borderRadius: '8px', borderLeft: '3px solid #10b981' }}>
                  <div style={{ fontSize: '0.7rem', color: 'var(--text-muted)', textTransform: 'uppercase' }}>North-South Green Phase</div>
                  <div style={{ fontSize: '1.4rem', fontWeight: 800, color: '#10b981', fontFamily: 'var(--font-mono)' }}>
                    {summary.recentRecommendations[0].nsGreenDuration} Seconds
                  </div>
                  <div style={{ fontSize: '0.7rem', color: 'var(--text-muted)', marginTop: '2px' }}>
                    Traffic Score: {summary.recentRecommendations[0].nsScore}%
                  </div>
                </div>

                <div style={{ flex: 1, backgroundColor: 'var(--bg-sidebar)', padding: '12px', borderRadius: '8px', borderLeft: '3px solid #f59e0b' }}>
                  <div style={{ fontSize: '0.7rem', color: 'var(--text-muted)', textTransform: 'uppercase' }}>East-West Green Phase</div>
                  <div style={{ fontSize: '1.4rem', fontWeight: 800, color: '#f59e0b', fontFamily: 'var(--font-mono)' }}>
                    {summary.recentRecommendations[0].ewGreenDuration} Seconds
                  </div>
                  <div style={{ fontSize: '0.7rem', color: 'var(--text-muted)', marginTop: '2px' }}>
                    Traffic Score: {summary.recentRecommendations[0].ewScore}%
                  </div>
                </div>
              </div>

              <p style={{ fontSize: '0.8rem', color: 'var(--text-muted)', background: 'var(--bg-sidebar)', padding: '10px', borderRadius: '6px' }}>
                💡 <strong>AI Rationale:</strong> {summary.recentRecommendations[0].reason}
              </p>
            </div>
          ) : (
            <div style={{ color: 'var(--text-muted)', fontSize: '0.85rem' }}>No active signal recommendations.</div>
          )}
        </div>

        {/* Active Incident Alerts */}
        <div className="card">
          <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between', marginBottom: '16px' }}>
            <h3 style={{ fontSize: '1rem', fontWeight: 700, color: '#fff' }}>Active Incidents & Congestion Alerts</h3>
            <span style={{ fontSize: '0.75rem', color: 'var(--text-muted)' }}>Real-time trigger alerts</span>
          </div>

          <div>
            {summary?.recentAlerts?.length > 0 ? (
              summary.recentAlerts.map((alert) => (
                <AlertCard key={alert.id} alert={alert} />
              ))
            ) : (
              <div style={{ color: 'var(--text-muted)', fontSize: '0.85rem', textAlign: 'center', padding: '20px' }}>
                ✅ No active critical alerts reported.
              </div>
            )}
          </div>
        </div>

      </div>

    </div>
  );
};
