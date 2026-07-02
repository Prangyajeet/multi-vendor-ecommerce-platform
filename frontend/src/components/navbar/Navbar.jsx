import { useNavigate } from "react-router-dom";
import useAuthStore from "../../store/authStore";

function Navbar() {

  const navigate = useNavigate();

  const logout = useAuthStore((state) => state.logout);
  const role = useAuthStore((state) => state.role);

  const handleLogout = () => {

    logout();

    navigate("/login");

  };

  return (

    <header className="bg-gray-900 text-white px-6 py-4 flex justify-between items-center">

      <h1 className="text-xl font-bold">
        Multi Vendor Ecommerce
      </h1>

      <div className="flex items-center gap-4">

        <span className="text-sm">
          {role}
        </span>

        <button
          onClick={handleLogout}
          className="bg-red-600 hover:bg-red-700 px-4 py-2 rounded-lg"
        >
          Logout
        </button>

      </div>

    </header>

  );
}

export default Navbar;