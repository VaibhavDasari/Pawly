const BASE_URL = import.meta.env.VITE_API_BASE_URL || "http://localhost:8080";
export const TOKEN_KEY = "pawly_token";

export async function api(path, options = {}) {
  const headers = new Headers(options.headers || {});
  if (options.body && !(options.body instanceof FormData)) headers.set("Content-Type", "application/json");
  const token = localStorage.getItem(TOKEN_KEY);
  if (token) headers.set("Authorization", `Bearer ${token}`);
  let response;
  try {
    response = await fetch(`${BASE_URL}${path}`, { ...options, headers });
  } catch {
    throw new Error("Could not reach Pawly. Check that the backend is running and try again.");
  }
  if (response.status === 401) {
    localStorage.removeItem(TOKEN_KEY);
    window.dispatchEvent(new Event("pawly:unauthorized"));
    throw new Error("Your session has expired. Please log in again.");
  }
  if (!response.ok) {
    let message = response.status === 403 ? "You do not have permission to perform this action." : `Request failed (${response.status}).`;
    try { const data = await response.json(); message = data.message || data.error || message; } catch { /* response has no JSON body */ }
    throw new Error(message);
  }
  if (response.status === 204) return null;
  return response.json();
}

export const authApi = {
  register: (data) => api("/api/auth/register", { method: "POST", body: JSON.stringify(data) }),
  login: (data) => api("/api/auth/login", { method: "POST", body: JSON.stringify(data) }),
  me: () => api("/api/users/me"),
};
export const petApi = {
  list: () => api("/api/pets"), get: (id) => api(`/api/pets/${id}`), stats: (id) => api(`/api/pets/${id}/stats`),
  create: (data) => api("/api/pets", { method: "POST", body: JSON.stringify(data) }),
  update: (id, data) => api(`/api/pets/${id}`, { method: "PUT", body: JSON.stringify(data) }),
  remove: (id) => api(`/api/pets/${id}`, { method: "DELETE" }),
};
export const appointmentApi = {
  list: (query = "") => api(`/api/appointments${query ? `?${query}` : ""}`),
  create: (data) => api("/api/appointments", { method: "POST", body: JSON.stringify(data) }),
  update: (id, data) => api(`/api/appointments/${id}`, { method: "PUT", body: JSON.stringify(data) }),
  remove: (id) => api(`/api/appointments/${id}`, { method: "DELETE" }),
};
export const healthApi = {
  list: (query = "") => api(`/api/health-records${query ? `?${query}` : ""}`),
  create: (data) => api("/api/health-records", { method: "POST", body: JSON.stringify(data) }),
  update: (id, data) => api(`/api/health-records/${id}`, { method: "PUT", body: JSON.stringify(data) }),
  remove: (id) => api(`/api/health-records/${id}`, { method: "DELETE" }),
};
export const dashboardApi = () => api("/api/dashboard");
export const notificationApi = () => api("/api/notifications");



