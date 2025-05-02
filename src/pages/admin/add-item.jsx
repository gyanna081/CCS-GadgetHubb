import React, { useState } from "react";
import { Link, useLocation, useNavigate } from "react-router-dom";
import logo from "../../assets/CCSGadgetHub1.png";

const AddItem = () => {
  const location = useLocation();
  const navigate = useNavigate();

  const [itemName, setItemName] = useState("");
  const [itemDescription, setItemDescription] = useState("");
  const [itemCondition, setItemCondition] = useState("New");
  const [itemImage, setItemImage] = useState(null);
  const [showSuccessModal, setShowSuccessModal] = useState(false);

  const handleImageChange = (e) => {
    setItemImage(e.target.files[0]);
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    console.log({
      itemName,
      itemDescription,
      itemCondition,
      itemImage,
    });

    setShowSuccessModal(true);

    setTimeout(() => {
      setShowSuccessModal(false);
      navigate("/admin-items");
    }, 5000);
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

      <div style={{ width: "100%", maxWidth: "800px", margin: "0 auto" }}>
        <Link to="/admin-items" className="back-arrow">←</Link>
      </div>

      {/* Content */}
      <div className="add-item-container">
        <h2>Add New Item</h2>

        <form className="add-item-form" onSubmit={handleSubmit}>
          <label>
            Item Name:
            <input
              type="text"
              value={itemName}
              onChange={(e) => setItemName(e.target.value)}
              placeholder="Enter item name..."
              required
            />
          </label>

          <label>
            Description:
            <textarea
              value={itemDescription}
              onChange={(e) => setItemDescription(e.target.value)}
              placeholder="Enter item description..."
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
            Upload Image:
            <input
              type="file"
              accept="image/*"
              onChange={handleImageChange}
            />
          </label>

          <button type="submit" className="submit-btn">Add Item</button>
        </form>
      </div>

      {/* Success Modal */}
      {showSuccessModal && (
        <div className="modal-overlay">
          <div className="modal-content">
            Item added successfully!
            <div style={{ marginTop: "20px" }}>
              <Link to="/admin-items" className="modal-link">Back to Items Page</Link>
            </div>
          </div>
        </div>
      )}
    </div>
  );
};

export default AddItem;
