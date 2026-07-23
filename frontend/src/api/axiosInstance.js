import axios from "axios";
import { API_BASE_URL } from "../constants/apiConstants";

const axiosInstance = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    "Content-Type": "application/json",
  },
});

axiosInstance.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem("token");

    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }

    return config;
  },
  (error) => Promise.reject(error)
);

axiosInstance.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response) {
      switch (error.response.status) {
        case 401:
          localStorage.removeItem("token");
          localStorage.removeItem("userId");
          localStorage.removeItem("email");
          localStorage.removeItem("role");

          window.location.href = "/login";
          break;

        case 403:
          console.error("Access Denied");
          break;

        default:
          break;
      }
    }

    return Promise.reject(error);
  }
);

export default axiosInstance;