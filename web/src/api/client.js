const AUTH_TOKEN_KEY = 'blueline_token';

let authToken = localStorage.getItem(AUTH_TOKEN_KEY);

export function setAuthToken(token) {
  authToken = token;
  if (token) localStorage.setItem(AUTH_TOKEN_KEY, token);
  else localStorage.removeItem(AUTH_TOKEN_KEY);
}

export function clearAuthToken() {
  setAuthToken(null);
}

export function getAuthToken() {
  return authToken;
}

export async function request(path, { method = 'GET', body } = {}) {
  const headers = { 'Content-Type': 'application/json' };
  if (authToken) headers.Authorization = `Bearer ${authToken}`;

  const res = await fetch(`/api${path}`, {
    method,
    headers,
    body: body !== undefined ? JSON.stringify(body) : undefined,
  });

  if (!res.ok) {
    let message = `Request failed (${res.status})`;
    try {
      const data = await res.json();
      if (data?.message) message = data.message;
    } catch {
      // response had no JSON body
    }
    throw new Error(message);
  }

  if (res.status === 204) return null;
  return res.json();
}
