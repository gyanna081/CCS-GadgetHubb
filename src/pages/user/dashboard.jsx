import { useNavigate, Link, useLocation } from "react-router-dom";
import { auth } from "../../firebaseconfig";
import { signOut } from "firebase/auth";
import logo from "../../assets/CCSGadgetHub1.png";

const Dashboard = () => {
  const navigate = useNavigate();
  const location = useLocation();

  const handleLogout = async () => {
    try {
      await signOut(auth);
      navigate("/");
    } catch (err) {
      console.error("Logout failed:", err);
    }
  };

  const navLinks = [
    { label: "Dashboard", to: "/userdashboard" },
    { label: "Items", to: "/useritems" },
    { label: "My Requests", to: "/my-requests" },
    { label: "Profile", to: "/userprofile" },
  ];

  const featuredItems = [
    { id: 1, name: "Dell Laptop", available: true, rating: 5, image: "https://via.placeholder.com/100" },
    { id: 2, name: "Huawei D15 Matebook", available: false, rating: 4, image: "https://via.placeholder.com/100" },
    { id: 3, name: "Dell Laptop 2", available: true, rating: 3, image: "https://via.placeholder.com/100" },
  ];

  return (
    <div className="dashboard-page">
      {/* Navigation Bar */}
      <div className="navbar">
        <img src={logo} alt="CCS Gadget Hub Logo" />
        <nav>
          {navLinks.map((link) => (
            <Link
              key={link.to}
              to={link.to}
              className={
                location.pathname === link.to
                  ? "navbar-link active-link"
                  : "navbar-link"
              }
            >
              {link.label}
            </Link>
          ))}
        </nav>
        <div style={{ marginLeft: "auto" }}>
          <Link to="/" className="logout-link">Log Out</Link>
        </div>
      </div>

      {/* Main Content */}
      <div className="dashboard-container">
        <h1 className="dashboard-greeting">Welcome back, Mica!</h1>
        <p className="dashboard-subtext">Here’s a quick overview of your gadget hub activity.</p>

        {/* Summary Cards */}
        <div className="summary-cards">
          <div className="summary-card">
            <h3>3</h3>
            <p>Items Borrowed</p>
          </div>
          <div className="summary-card">
            <h3>1</h3>
            <p>Pending Requests</p>
          </div>
          <div className="summary-card">
            <h3>0</h3>
            <p>Overdue</p>
          </div>
        </div>

        {/* Featured Items Header with Buttons */}
        <div className="featured-header">
          <h2 className="featured-title">Featured Items</h2>
          <div className="featured-buttons">
            <button className="custom-button" onClick={() => navigate("/useritems")}>
              Borrow Item
            </button>
            <button className="custom-button" onClick={() => navigate("/my-requests")}>
              View Requests
            </button>
          </div>
        </div>

        {/* Featured Items */}
        <div className="items-grid">
          {featuredItems.map((item) => (
            <div key={item.id} className="item-box">
              <img src={item.image} alt={item.name} className="item-image" />
              <h4>{item.name}</h4>
              <p className="item-status">{item.available ? "Available" : "Not Available"}</p>
              <p className="item-rating">⭐ {item.rating}</p>
            </div>
          ))}
        </div>
      </div>
    </div>
  );
};

export default Dashboard;
