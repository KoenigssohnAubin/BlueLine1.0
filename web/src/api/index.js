import { request, setAuthToken, clearAuthToken, getAuthToken } from './client';

export { setAuthToken, clearAuthToken, getAuthToken };

export function login(username, password) {
  return request('/auth/login', { method: 'POST', body: { username, password } });
}

export function me() {
  return request('/auth/me');
}

export function fetchMissions(status) {
  const query = status ? `?status=${encodeURIComponent(status)}` : '';
  return request(`/missions${query}`);
}

export function acceptMission(missionId, ambulanceCode) {
  return request(`/missions/${missionId}/accept`, {
    method: 'PATCH',
    body: { ambulanceCode },
  });
}

export function completeMission(missionId) {
  return request(`/missions/${missionId}/complete`, { method: 'PATCH' });
}

export function createMission(payload) {
  return request('/missions', { method: 'POST', body: payload });
}

export function fetchAmbulances() {
  return request('/ambulances');
}

export function updateAmbulanceStatus(id, status) {
  return request(`/ambulances/${id}/status`, { method: 'PATCH', body: { status } });
}

export function fetchHospitals() {
  return request('/hospitals');
}

export function updateHospitalCapacity(id, department, available, total) {
  return request(`/hospitals/${id}/capacity`, {
    method: 'PATCH',
    body: { department, available, total },
  });
}

export function fetchAlerts() {
  return request('/alerts');
}

export function createAlert(payload) {
  return request('/alerts', { method: 'POST', body: payload });
}

export function dismissAlert(id) {
  return request(`/alerts/${id}/dismiss`, { method: 'PATCH' });
}

export function fetchStats(period) {
  return request(`/stats?period=${period}`);
}

export function fetchUsers() {
  return request('/users');
}

export function updateUser(id, payload) {
  return request(`/users/${id}`, { method: 'PUT', body: payload });
}
