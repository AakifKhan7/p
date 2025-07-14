const BASE_URL = "http://localhost:8080";

/**
 * Wrapper around fetch that automatically adds the Authorization header when a
 * JWT token is saved in localStorage under the key `token`.
 */
export async function apiFetch(path, options = {}) {
  const token = localStorage.getItem("token");
  const headers = {
    "Content-Type": "application/json",
    ...options.headers,
  };
  if (token) {
    headers["Authorization"] = `Bearer ${token}`;
  }
  const res = await fetch(`${BASE_URL}${path}`, {
    ...options,
    headers,
  });
  if (!res.ok) {
    const text = await res.text();
    throw new Error(text || `Request failed with status ${res.status}`);
  }
  // No content
  if (res.status === 204) return null;
  return res.json();
}
