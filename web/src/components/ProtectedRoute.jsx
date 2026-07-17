import { Navigate } from 'react-router-dom';
import { useAuth } from '../auth/AuthContext';

export default function ProtectedRoute({ children }) {
  const { isAuthenticated, isInitializing } = useAuth();

  if (isInitializing) return <div className="page-loading">Chargement…</div>;
  if (!isAuthenticated) return <Navigate to="/login" replace />;

  return children;
}
