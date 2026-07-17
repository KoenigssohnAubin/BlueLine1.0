import { NavLink, Outlet } from 'react-router-dom';
import { useAuth } from '../auth/AuthContext';
import logo from '../assets/logo.png';

const NAV_ITEMS = [
  { to: '/map', label: 'Map' },
  { to: '/missions', label: 'Missions' },
  { to: '/ambulances', label: 'Ambulances' },
  { to: '/hospitals', label: 'Hospitals' },
  { to: '/alerts', label: 'Alerts' },
  { to: '/stats', label: 'Stats' },
  { to: '/users', label: 'Users', adminOnly: true },
];

export default function DashboardLayout() {
  const { user, logout } = useAuth();
  const isAdmin = user?.role === 'ADMIN';

  return (
    <div className="app-shell">
      <aside className="sidebar">
        <div className="brand">
          <img src={logo} alt="" className="brand-logo" />
          Blue Line IA
        </div>
        <nav>
          {NAV_ITEMS.filter((item) => !item.adminOnly || isAdmin).map((item) => (
            <NavLink
              key={item.to}
              to={item.to}
              className={({ isActive }) => `nav-link${isActive ? ' active' : ''}`}
            >
              {item.label}
            </NavLink>
          ))}
        </nav>
      </aside>
      <div className="main">
        <header className="topbar">
          <span>{user?.name || user?.username}</span>
          <span className="role-badge">{user?.role}</span>
          <button className="btn-link" onClick={logout}>Sign out</button>
        </header>
        <main className="content">
          <Outlet />
        </main>
      </div>
    </div>
  );
}
