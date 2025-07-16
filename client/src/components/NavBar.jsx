import { Link, useNavigate } from "react-router-dom";

const NavBar = () => {
  const navigate = useNavigate();
  const handleLogout = () => {
    localStorage.removeItem("token");
    navigate("/login");
  };

  return (
    <nav className="bg-white border-b border-gray-200 px-4 py-3 flex items-center justify-between">
      <div className="flex items-center gap-6 text-sm font-semibold">
        <Link to="/" className="hover:text-yellow-600">
          Home
        </Link>
        <Link to="/profile" className="hover:text-yellow-600">
          Profile
        </Link>
        <Link to="/sales" className="hover:text-yellow-600">
          Sales
        </Link>
      </div>
      <button
        onClick={handleLogout}
        className="text-xs bg-yellow-500 hover:bg-yellow-600 text-black font-semibold px-3 py-2 rounded-md"
      >
        Logout
      </button>
    </nav>
  );
};

export default NavBar;
