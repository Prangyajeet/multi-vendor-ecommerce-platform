import axiosInstance from "../api/axiosInstance";

/**
 * Fetch logged-in customer profile
 * GET /api/customer/me
 */
export const getCustomerProfile = async () => {
    try {
        const response = await axiosInstance.get("/api/customer/me");

        return response.data.data;
    } catch (error) {
        console.error("Error fetching customer profile:", error);

        throw (
            error.response?.data?.message ||
            "Unable to fetch customer profile."
        );
    }
};

/**
 * Fetch customer dashboard message
 * GET /api/customer/dashboard
 */
export const getCustomerDashboard = async () => {
    try {
        const response = await axiosInstance.get("/api/customer/dashboard");

        return response.data.data;
    } catch (error) {
        console.error("Error fetching dashboard:", error);

        throw (
            error.response?.data?.message ||
            "Unable to load dashboard."
        );
    }
};