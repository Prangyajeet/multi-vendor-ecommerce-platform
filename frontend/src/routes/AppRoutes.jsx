import { Routes, Route } from "react-router-dom";

import Login from "../pages/auth/Login";
import Register from "../pages/auth/Register";

import AdminDashboard from "../pages/admin/Dashboard";
import VendorDashboard from "../pages/vendor/Dashboard";
import Home from "../pages/customer/Home";

import ProtectedRoute from "./ProtectedRoute";

function AppRoutes() {
  return (
    <Routes>

      <Route
        path="/login"
        element={<Login />}
      />

      <Route
        path="/register"
        element={<Register />}
      />

      <Route
        path="/admin/dashboard"
        element={
          <ProtectedRoute allowedRoles={["ADMIN"]}>
            <AdminDashboard />
          </ProtectedRoute>
        }
      />

      <Route
        path="/vendor/dashboard"
        element={
          <ProtectedRoute allowedRoles={["VENDOR"]}>
            <VendorDashboard />
          </ProtectedRoute>
        }
      />

      <Route
        path="/customer/home"
        element={
          <ProtectedRoute allowedRoles={["CUSTOMER"]}>
            <Home />
          </ProtectedRoute>
        }
      />

    </Routes>
  );
}

export default AppRoutes;