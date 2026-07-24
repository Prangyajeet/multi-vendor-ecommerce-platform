import { useEffect, useState } from "react";
import CustomerLayout from "../../layouts/CustomerLayout";
import CategoryCard from "../../components/category/CategoryCard";
import { getAllCategories } from "../../services/categoryService";

const Shop = () => {
    const [categories, setCategories] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState("");

    useEffect(() => {
        const loadCategories = async () => {
            try {
                setLoading(true);

                const data = await getAllCategories();
                setCategories(data);
            } catch (err) {
                setError(err || "Unable to load categories.");
            } finally {
                setLoading(false);
            }
        };

        loadCategories();
    }, []);

    const handleCategoryClick = (category) => {
        console.log("Selected Category:", category);

        // Product module
        // Navigate to products by category here later.
    };

    if (loading) {
        return (
            <CustomerLayout>
                <div className="flex justify-center items-center h-[70vh]">
                    <h2 className="text-2xl font-semibold">
                        Loading Categories...
                    </h2>
                </div>
            </CustomerLayout>
        );
    }

    if (error) {
        return (
            <CustomerLayout>
                <div className="flex justify-center items-center h-[70vh]">
                    <div className="bg-red-100 text-red-700 px-6 py-4 rounded-xl">
                        {error}
                    </div>
                </div>
            </CustomerLayout>
        );
    }

    return (
        <CustomerLayout>
            <div className="max-w-7xl mx-auto px-6 py-8">

                {/* Header */}

                <div className="mb-10">

                    <h1 className="text-4xl font-bold text-gray-800">
                        Shop by Category
                    </h1>

                    <p className="text-gray-500 mt-2">
                        Explore all available product categories.
                    </p>

                </div>

                {/* Category Grid */}

                {categories.length === 0 ? (

                    <div className="bg-white rounded-xl shadow p-8 text-center">
                        <h2 className="text-xl font-semibold text-gray-700">
                            No Categories Available
                        </h2>

                        <p className="text-gray-500 mt-2">
                            Categories will appear here once added by the administrator.
                        </p>
                    </div>

                ) : (

                    <div className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-4 gap-6">

                        {categories.map((category) => (

                            <CategoryCard
                                key={category.id}
                                category={category}
                                onClick={handleCategoryClick}
                            />

                        ))}

                    </div>

                )}

            </div>
        </CustomerLayout>
    );
};

export default Shop;