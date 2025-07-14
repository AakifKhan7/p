import { Navigate, Outlet } from "react-router-dom";

/**
 * Prevents access to child routes when no auth token is present in localStorage.
 * Usage:
 * <Route element={<ProtectedRoute />}>
 *   <Route path="/dashboard" element={<Dashboard />} />
 * </Route>
 */
const ProtectedRoute = () => {
  const token = localStorage.getItem("token");
  return token ? <Outlet /> : <Navigate to="/login" replace />;
};

export default ProtectedRoute;
