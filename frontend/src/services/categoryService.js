import axiosInstance from "../api/axiosInstance";

/**
 * Fetch all product categories
 * GET /api/categories
 */
export const getAllCategories = async () => {
    try {
        const response = await axiosInstance.get("/api/categories");

        return response.data;
    } catch (error) {
        console.error("Error fetching categories:", error);

        throw (
            error.response?.data?.message ||
            "Unable to fetch categories."
        );
    }
};

/**
 * Fetch category by ID
 * GET /api/categories/{id}
 */
export const getCategoryById = async (categoryId) => {
    try {
        const response = await axiosInstance.get(
            `/api/categories/${categoryId}`
        );

        return response.data;
    } catch (error) {
        console.error("Error fetching category:", error);

        throw (
            error.response?.data?.message ||
            "Unable to fetch category."
        );
    }
};