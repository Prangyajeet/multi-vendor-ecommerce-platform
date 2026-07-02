import { create } from "zustand";

const useAuthStore = create((set) => ({

  token: localStorage.getItem("token") || null,
  userId: localStorage.getItem("userId") || null,
  email: localStorage.getItem("email") || null,
  role: localStorage.getItem("role") || null,

  login: ({ token, userId, email, role }) => {

    localStorage.setItem("token", token);
    localStorage.setItem("userId", userId);
    localStorage.setItem("email", email);
    localStorage.setItem("role", role);

    set({
      token,
      userId,
      email,
      role,
    });

  },

  logout: () => {

    localStorage.removeItem("token");
    localStorage.removeItem("userId");
    localStorage.removeItem("email");
    localStorage.removeItem("role");

    set({
      token: null,
      userId: null,
      email: null,
      role: null,
    });

  },

}));

export default useAuthStore;