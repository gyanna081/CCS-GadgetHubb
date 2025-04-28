import React, { useState } from "react";
import { Link, useLocation, useNavigate, useParams } from "react-router-dom";
import logo from "../../assets/CCSGadgetHub1.png";

const AdminEditItem = () => {
  const location = useLocation();
  const navigate = useNavigate();
  const { id } = useParams();

  // Dummy data fetching simulation (you would fetch this in a real app)
  const [itemName, setItemName] = useState("Dell Laptop");
  const [itemDescription, setItemDescription] = useState("A high-performance laptop suitable for work and study.");
  const [itemCondition, setItemCondition] = useState("Good");
  const [itemStatus, setItemStatus] = useState("Available");
  const [itemImage, setItemImage] = useState(null);

  const handleImageChange = (e) => {
    setItemImage(e.target.files[0]);
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    console.log({
      id,
      itemName,
      itemDescription,
      itemCondition,
      itemStatus,
      itemImage,
    });
    alert("Item updated successfully!");
    navigate("/admin-items");
  };

  return (
    <div className="add-item-page">
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
      <div style={{ width: '100%', maxWidth: '800px', margin: '0 auto' }}>
        <Link to="/admin-items" className="back-arrow">←</Link>
      </div>

      <div className="add-item-container">
        <h2>Edit Item</h2>

        <form className="add-item-form" onSubmit={handleSubmit}>
          <label>
            Item Name:
            <input
              type="text"
              value={itemName}
              onChange={(e) => setItemName(e.target.value)}
              required
            />
          </label>

          <label>
            Description:
            <textarea
              value={itemDescription}
              onChange={(e) => setItemDescription(e.target.value)}
              required
            />
          </label>

          <label>
            Condition:
            <select
              value={itemCondition}
              onChange={(e) => setItemCondition(e.target.value)}
            >
              <option value="Good">Good</option>
              <option value="Fair">Fair</option>
              <option value="Poor">Poor</option>
            </select>
          </label>

          <label>
            Status:
            <select
              value={itemStatus}
              onChange={(e) => setItemStatus(e.target.value)}
            >
              <option value="Available">Available</option>
              <option value="Borrowed">Borrowed</option>
            </select>
          </label>

          <label>
            Upload New Image:
            <input
              type="file"
              accept="image/*"
              onChange={handleImageChange}
            />
          </label>

          <button type="submit" className="submit-btn">Update Item</button>
        </form>
      </div>
    </div>
  );
};

export default AdminEditItem;
