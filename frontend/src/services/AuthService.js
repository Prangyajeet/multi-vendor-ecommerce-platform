import axiosInstance
from "../api/axiosInstance";

export const registerUser = (
  userData
) => {
  return axiosInstance.post(
    "/api/auth/register",
    userData
  );
};

export const loginUser = (
  credentials
) => {
  return axiosInstance.post(
    "/api/auth/login",
    credentials
  );
};