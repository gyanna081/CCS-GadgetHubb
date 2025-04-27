import React from "react";
import { Link, useLocation, useParams, useNavigate } from "react-router-dom";
import logo from "../../assets/CCSGadgetHub1.png";

const AdminViewItem = () => {
  const location = useLocation();
  const navigate = useNavigate();
  const { id } = useParams();

  // Dummy Data for now
  const dummyItems = [
    { id: "1", name: "Dell Laptop", description: "High performance laptop", condition: "Good", status: "Available", image: "https://via.placeholder.com/150" },
    { id: "2", name: "Macbook Air", description: "Lightweight and portable", condition: "New", status: "Borrowed", image: "https://via.placeholder.com/150" }
  ];

  const item = dummyItems.find((i) => i.id === id);

  if (!item) {
    return (
      <div className="dashboard-container">
        <p>Item not found.</p>
        <Link to="/admin-items" className="back-arrow">← Back to Items</Link>
      </div>
    );
  }

  const handleEdit = () => {
    navigate(`/edit-item/${item.id}`);
  };

  return (
    <div className="admin-dashboard">
      {/* Navbar */}
      <div className="navbar">
        <img src={logo} alt="CCS Gadget Hub Logo" />
        <nav>
          {[
            { label: "Dashboard", to: "/admin-dashboard" },
            { label: "Manage Items", to: "/admin-items" },
            { label: "Requests", to: "/admin-requests" },
            { label: "Manage Users", to: "/admin-users" },
          ].map((link) => (
            <Link
              key={link.to}
              to={link.to}
              className={location.pathname === link.to ? "navbar-link active-link" : "navbar-link"}
            >
              {link.label}
            </Link>
          ))}
        </nav>
        <div style={{ marginLeft: "auto" }}>
          <Link to="/" className="logout-link">Log Out</Link>
        </div>
      </div>

      {/* Content */}
      <div className="admin-dashboard-container">
        <div style={{ display: "flex", alignItems: "center", justifyContent: "space-between", marginBottom: "20px" }}>
          <Link to="/admin-items" className="back-arrow">←</Link>
          <button className="edit-btn" onClick={handleEdit}>Edit Item</button>
        </div>

        <div className="view-item-box">
          <img src={item.image} alt={item.name} className="view-item-image" />
          <h2>{item.name}</h2>
          <p><strong>Description:</strong> {item.description}</p>
          <p><strong>Condition:</strong> {item.condition}</p>
          <p><strong>Status:</strong>
            <span className={`status ${item.status.toLowerCase()}`}>
              {item.status === "Borrowed" ? "Not Available" : item.status}
            </span>
          </p>
        </div>
      </div>
    </div>
  );
};

export default AdminViewItem;
