import { createContext, useContext, useEffect, useState, useCallback } from 'react';
import * as api from '../api';

const AuthContext = createContext(null);

export function AuthProvider({ children }) {
  const [user, setUser] = useState(null);
  const [isInitializing, setIsInitializing] = useState(true);
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState(null);

  useEffect(() => {
    const token = api.getAuthToken();
    if (!token) {
      setIsInitializing(false);
      return;
    }
    api
      .me()
      .then(setUser)
      .catch(() => api.clearAuthToken())
      .finally(() => setIsInitializing(false));
  }, []);

  const login = useCallback(async (username, password) => {
    setIsLoading(true);
    setError(null);
    try {
      const { token, user } = await api.login(username, password);
      api.setAuthToken(token);
      setUser(user);
      return user;
    } catch (err) {
      setError(err.message);
      throw err;
    } finally {
      setIsLoading(false);
    }
  }, []);

  const logout = useCallback(() => {
    api.clearAuthToken();
    setUser(null);
  }, []);

  return (
    <AuthContext.Provider
      value={{ user, isAuthenticated: !!user, isInitializing, isLoading, error, login, logout }}
    >
      {children}
    </AuthContext.Provider>
  );
}

export function useAuth() {
  const ctx = useContext(AuthContext);
  if (!ctx) throw new Error('useAuth must be used within AuthProvider');
  return ctx;
}
