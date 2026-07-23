import React from "react";

const ProfileCard = ({ profile }) => {
    if (!profile) {
        return (
            <div className="bg-white rounded-2xl shadow-md p-6 border border-gray-100">
                <p className="text-gray-500">Loading profile...</p>
            </div>
        );
    }

    return (
        <div className="bg-white rounded-2xl shadow-md border border-gray-100 p-6">

            <div className="flex items-center gap-4 mb-6">

                <div className="w-16 h-16 rounded-full bg-blue-600 flex items-center justify-center text-white text-2xl font-bold">
                    {profile.firstName?.charAt(0)}
                </div>

                <div>
                    <h2 className="text-xl font-bold text-gray-800">
                        {profile.firstName} {profile.lastName}
                    </h2>

                    <p className="text-gray-500">
                        {profile.role}
                    </p>
                </div>

            </div>

            <div className="space-y-4">

                <div>
                    <p className="text-sm text-gray-500">
                        Email
                    </p>

                    <p className="font-medium text-gray-800">
                        {profile.email}
                    </p>
                </div>

                <div>
                    <p className="text-sm text-gray-500">
                        Phone Number
                    </p>

                    <p className="font-medium text-gray-800">
                        {profile.phoneNumber || "Not Available"}
                    </p>
                </div>

                <div>
                    <p className="text-sm text-gray-500">
                        Customer ID
                    </p>

                    <p className="font-medium text-gray-800">
                        #{profile.id}
                    </p>
                </div>

            </div>

        </div>
    );
};

export default ProfileCard;