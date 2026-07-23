import { useEffect, useState } from "react";
import CustomerLayout from "../../layouts/CustomerLayout";
import ProfileCard from "../../components/customer/ProfileCard";
import DashboardCard from "../../components/customer/DashboardCard";
import {
    getCustomerDashboard,
    getCustomerProfile,
} from "../../services/customerService";

const Home = () => {
    const [profile, setProfile] = useState(null);
    const [dashboardMessage, setDashboardMessage] = useState("");
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState("");

    useEffect(() => {
        const loadDashboard = async () => {
            try {
                setLoading(true);

                const [profileData, dashboardData] = await Promise.all([
                    getCustomerProfile(),
                    getCustomerDashboard(),
                ]);

                setProfile(profileData);
                setDashboardMessage(dashboardData);
            } catch (err) {
                setError(err || "Failed to load dashboard.");
            } finally {
                setLoading(false);
            }
        };

        loadDashboard();
    }, []);

    if (loading) {
        return (
            <CustomerLayout>
                <div className="flex items-center justify-center h-[70vh]">
                    <div className="text-xl font-semibold text-gray-600">
                        Loading Dashboard...
                    </div>
                </div>
            </CustomerLayout>
        );
    }

    if (error) {
        return (
            <CustomerLayout>
                <div className="flex items-center justify-center h-[70vh]">
                    <div className="bg-red-100 text-red-700 px-6 py-4 rounded-xl shadow">
                        {error}
                    </div>
                </div>
            </CustomerLayout>
        );
    }

    return (
        <CustomerLayout>
            <div className="max-w-7xl mx-auto p-6">

                {/* Welcome Banner */}

                <div className="bg-gradient-to-r from-blue-600 to-indigo-600 rounded-2xl text-white p-8 shadow-lg mb-8">

                    <h1 className="text-4xl font-bold">
                        Welcome Back,
                        {" "}
                        {profile?.firstName} 👋
                    </h1>

                    <p className="mt-3 text-blue-100">
                        {dashboardMessage}
                    </p>

                </div>

                {/* Profile + Overview */}

                <div className="grid grid-cols-1 lg:grid-cols-3 gap-6 mb-8">

                    <div>
                        <ProfileCard profile={profile} />
                    </div>

                    <div className="lg:col-span-2">

                        <div className="grid grid-cols-1 sm:grid-cols-2 gap-5">

                            <DashboardCard
                                title="Orders"
                                value="0"
                                icon="📦"
                                color="bg-blue-500"
                            />

                            <DashboardCard
                                title="Wishlist"
                                value="0"
                                icon="❤️"
                                color="bg-pink-500"
                            />

                            <DashboardCard
                                title="Cart"
                                value="0"
                                icon="🛒"
                                color="bg-green-500"
                            />

                            <DashboardCard
                                title="Addresses"
                                value="0"
                                icon="📍"
                                color="bg-orange-500"
                            />

                        </div>

                    </div>

                </div>

                {/* Quick Actions */}

                <div className="bg-white rounded-2xl shadow-md border border-gray-100 p-6 mb-8">

                    <h2 className="text-2xl font-bold text-gray-800 mb-6">
                        Quick Actions
                    </h2>

                    <div className="grid grid-cols-2 md:grid-cols-5 gap-4">

                        <button className="rounded-xl bg-blue-600 text-white py-3 hover:bg-blue-700 transition">
                            Products
                        </button>

                        <button className="rounded-xl bg-green-600 text-white py-3 hover:bg-green-700 transition">
                            Cart
                        </button>

                        <button className="rounded-xl bg-pink-600 text-white py-3 hover:bg-pink-700 transition">
                            Wishlist
                        </button>

                        <button className="rounded-xl bg-orange-600 text-white py-3 hover:bg-orange-700 transition">
                            Orders
                        </button>

                        <button className="rounded-xl bg-purple-600 text-white py-3 hover:bg-purple-700 transition">
                            Address
                        </button>

                    </div>

                </div>

                {/* Recent Activity */}

                <div className="bg-white rounded-2xl shadow-md border border-gray-100 p-6">

                    <h2 className="text-2xl font-bold text-gray-800 mb-4">
                        Recent Activity
                    </h2>

                    <p className="text-gray-500">
                        Your recent orders, wishlist updates, payments, and
                        notifications will appear here in the upcoming modules.
                    </p>

                </div>

            </div>
        </CustomerLayout>
    );
};

export default Home;