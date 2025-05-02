import { useEffect, useState } from "react";
import { useNavigate, Link, useLocation } from "react-router-dom";
import { auth } from "../firebaseconfig";
import { signOut } from "firebase/auth";
import axios from "axios";
import logo from "../assets/CCSGadgetHub1.png";

const Dashboard = () => {
  const navigate = useNavigate();
  const location = useLocation();
  const [items, setItems] = useState([]);

  const handleLogout = async () => {
    try {
      await signOut(auth);
      navigate("/");
    } catch (err) {
      console.error("Logout failed:", err);
    }
  };

  const fetchItems = async () => {
    try {
      const response = await axios.get("http://localhost:8080/api/items");
      const allItems = response.data;
      const lastThreeItems = allItems.slice(-3).reverse();
      setItems(lastThreeItems);
    } catch (error) {
      console.error("Failed to fetch items:", error);
    }
  };

  useEffect(() => {
    fetchItems();
  }, []);

  const navLinks = [
    { label: "Dashboard", to: "/dashboard" },
    { label: "Items", to: "/items" },
    { label: "My Requests", to: "/my-requests" },
    { label: "Activity Log", to: "/activity-log" },
    { label: "Profile", to: "/profile" },
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
              style={{
                color: location.pathname === link.to ? "black" : "white",
              }}
            >
              {link.label}
            </Link>
          ))}
        </nav>
        <div style={{ marginLeft: "auto" }}>
          <button className="logout-button" onClick={handleLogout}>
            Log Out
          </button>
        </div>
      </div>

      {/* Main Content */}
      <div className="dashboard-container">
        {/* Search Bar & Buttons */}
        <div className="search-container">
          <input type="text" placeholder="Enter items here" />
          <button className="custom-button">Borrow Item</button>
          <button className="custom-button">View Requests</button>
        </div>

        {/* Featured Items */}
        <h2 className="featured-title">Featured Items</h2>
        <div className="items-grid">
          {items.length > 0 ? (
            items.map((item) => (
              <div
                key={item.id || item.item_id}
                className="item-box"
                style={{
                  padding: "20px",
                  background: "#fff",
                  borderRadius: "8px",
                  boxShadow: "0 2px 8px rgba(0,0,0,0.1)",
                }}
              >
                {item.imagePath ? (
                  <img
                    src={item.imagePath}
                    alt={item.name}
                    style={{
                      width: "100%",
                      height: "150px",
                      objectFit: "cover",
                      borderRadius: "8px",
                      border: "1px solid #ddd",
                    }}
                    onError={(e) => {
                      e.target.onerror = null;
                      e.target.src = "https://placehold.co/300x150?text=No+Image";
                    }}
                  />
                ) : (
                  <div
                    style={{
                      width: "100%",
                      height: "150px",
                      backgroundColor: "#e0e0e0",
                      color: "#555",
                      display: "flex",
                      alignItems: "center",
                      justifyContent: "center",
                      borderRadius: "8px",
                      border: "1px solid #ccc",
                      fontSize: "14px",
                    }}
                  >
                    No Image Available
                  </div>
                )}

                <h3 style={{ margin: "10px 0 5px 0" }}>{item.name}</h3>
                <p
                  style={{
                    fontSize: "14px",
                    color: "#666",
                    minHeight: "40px",
                  }}
                >
                  {item.description}
                </p>
                <button
                  className="view-details-button"
                  style={{
                    marginTop: "10px",
                    padding: "8px 12px",
                    backgroundColor: "#E26901",
                    color: "white",
                    border: "none",
                    borderRadius: "6px",
                    cursor: "pointer",
                    fontSize: "14px",
                  }}
                  onClick={() => navigate(`/itemdetails/${item.id || item.item_id}`)}
                >
                  View Details
                </button>
              </div>
            ))
          ) : (
            <p>No featured items available.</p>
          )}
        </div>
      </div>
    </div>
  );
};

export default Dashboard;
