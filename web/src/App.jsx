import { Navigate, Route, Routes } from 'react-router-dom';
import { AuthProvider } from './auth/AuthContext';
import ProtectedRoute from './components/ProtectedRoute';
import DashboardLayout from './components/DashboardLayout';
import LoginPage from './pages/LoginPage';
import MissionsPage from './pages/MissionsPage';
import AmbulancesPage from './pages/AmbulancesPage';
import HospitalsPage from './pages/HospitalsPage';
import AlertsPage from './pages/AlertsPage';
import StatsPage from './pages/StatsPage';
import UsersPage from './pages/UsersPage';

export default function App() {
  return (
    <AuthProvider>
      <Routes>
        <Route path="/login" element={<LoginPage />} />
        <Route
          element={
            <ProtectedRoute>
              <DashboardLayout />
            </ProtectedRoute>
          }
        >
          <Route path="/" element={<Navigate to="/missions" replace />} />
          <Route path="/missions" element={<MissionsPage />} />
          <Route path="/ambulances" element={<AmbulancesPage />} />
          <Route path="/hospitals" element={<HospitalsPage />} />
          <Route path="/alerts" element={<AlertsPage />} />
          <Route path="/stats" element={<StatsPage />} />
          <Route path="/users" element={<UsersPage />} />
        </Route>
        <Route path="*" element={<Navigate to="/" replace />} />
      </Routes>
    </AuthProvider>
  );
}
