import { Routes, Route } from "react-router-dom";

import Login from "../pages/auth/Login";
import Register from "../pages/auth/Register";

import AdminDashboard from "../pages/admin/Dashboard";
import VendorDashboard from "../pages/vendor/Dashboard";

import Home from "../pages/customer/Home";

function AppRoutes() {
  return (
    <Routes>

      <Route path="/login" element={<Login />} />

      <Route path="/register" element={<Register />} />

      <Route
        path="/admin/dashboard"
        element={<AdminDashboard />}
      />

      <Route
        path="/vendor/dashboard"
        element={<VendorDashboard />}
      />

      <Route
        path="/customer/home"
        element={<Home />}
      />

    </Routes>
  );
}

export default AppRoutes;