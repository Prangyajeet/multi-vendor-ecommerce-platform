import React from "react";

const CategoryCard = ({ category, onClick }) => {
    return (
        <div
            onClick={() => onClick(category)}
            className="group cursor-pointer bg-white rounded-2xl border border-gray-200 shadow-sm hover:shadow-xl hover:-translate-y-1 transition-all duration-300 p-6"
        >
            <div className="flex flex-col items-center justify-center">

                <div className="w-16 h-16 rounded-full bg-blue-100 flex items-center justify-center text-3xl group-hover:bg-blue-600 group-hover:text-white transition-all duration-300">
                    🛍️
                </div>

                <h3 className="mt-5 text-lg font-semibold text-gray-800 text-center">
                    {category.name}
                </h3>

                <p className="mt-2 text-sm text-gray-500 text-center">
                    {category.description || "Browse products in this category"}
                </p>

            </div>
        </div>
    );
};

export default CategoryCard;