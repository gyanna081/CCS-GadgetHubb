import React, { useState, useEffect } from "react";
import { Link, useLocation, useNavigate } from "react-router-dom";
import logo from "../../assets/CCSGadgetHub1.png";

const AdminManageItems = () => {
  const location = useLocation();
  const navigate = useNavigate();

  const navLinks = [
    { label: "Dashboard", to: "/admin-dashboard" },
    { label: "Manage Items", to: "/admin-items" },
    { label: "Requests", to: "/admin-requests" },
    { label: "Manage Users", to: "/admin-users" },
  ];

  const [searchTerm, setSearchTerm] = useState("");
  const [statusFilter, setStatusFilter] = useState("all");
  const [items, setItems] = useState([]);

  const [showDeleteModal, setShowDeleteModal] = useState(false);
  const [itemToDelete, setItemToDelete] = useState(null);

  useEffect(() => {
    const dummyItems = [
      { id: 1, name: "Laptop 1", status: "Available", rating: 5, image: "https://via.placeholder.com/100" },
      { id: 2, name: "Macbook Air", status: "Borrowed", rating: 4, image: "https://via.placeholder.com/100" }
    ];
    setItems(dummyItems);
  }, []);

  const filteredItems = items.filter(item => {
    const matchesSearch = item.name.toLowerCase().includes(searchTerm.toLowerCase());
    const matchesStatus = statusFilter === "all" || item.status.toLowerCase() === statusFilter;
    return matchesSearch && matchesStatus;
  });

  const handleCardClick = (e, id) => {
    if (e.target.tagName.toLowerCase() === "button") return;
    navigate(`/view-item/${id}`);
  };

  const handleEdit = (id) => {
    navigate(`/edit-item/${id}`);
  };

  const handleDelete = (id) => {
    setItemToDelete(id);
    setShowDeleteModal(true);
  };

  const confirmDelete = () => {
    setItems(prev => prev.filter(item => item.id !== itemToDelete));
    setShowDeleteModal(false);
    setItemToDelete(null);
  };

  return (
    <div className="admin-dashboard">
      {/* Navbar */}
      <div className="navbar">
        <img src={logo} alt="CCS Gadget Hub Logo" />
        <nav>
          {navLinks.map((link) => (
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

      {/* Main Content */}
      <div className="admin-dashboard-container">
        <h1 className="admin-welcome">Manage Items</h1>

        {/* Filters */}
        <div className="admin-filters-row">
          <Link to="/add-item" className="add-item-btn">
            Add New Item
          </Link>
          <input
            type="text"
            placeholder="Search by item name..."
            value={searchTerm}
            onChange={(e) => setSearchTerm(e.target.value)}
            className="admin-search-bar"
          />
          <select
            value={statusFilter}
            onChange={(e) => setStatusFilter(e.target.value)}
            className="admin-filter-dropdown"
          >
            <option value="all">All</option>
            <option value="available">Available</option>
            <option value="borrowed">Borrowed</option>
          </select>
        </div>

        {/* Items Grid */}
        <div className="admin-items-grid">
          {filteredItems.map(item => (
            <div
              key={item.id}
              className="admin-item-card"
              onClick={(e) => handleCardClick(e, item.id)}
            >
              <img src={item.image} alt={item.name} className="admin-item-img" />
              <h3>{item.name}</h3>
              <p className={`item-status ${item.status.toLowerCase()}`}>
                {item.status === "Borrowed" ? "Not Available" : item.status}
              </p>

              <div className="item-rating">
                <span className="star-icon">⭐</span>
                <span className="rating-number">{item.rating}</span>
              </div>

              <div className="admin-item-buttons">
                <button className="edit-btn" onClick={() => handleEdit(item.id)}>Edit</button>
                <button className="delete-btn" onClick={() => handleDelete(item.id)}>Delete</button>
              </div>
            </div>
          ))}
        </div>
      </div>

      {/* Delete Confirmation Modal */}
      {showDeleteModal && (
  <div className="admin-item-modal-overlay">
    <div className="admin-item-modal">
      <p className="admin-item-modal-message">Are you sure you want to delete this item?</p>
      <div className="admin-item-modal-buttons">
        <button className="admin-item-yes-btn" onClick={confirmDelete}>Yes</button>
        <button className="admin-item-no-btn" onClick={() => setShowDeleteModal(false)}>No</button>
      </div>
    </div>
  </div>
)}

    </div>
  );
};

export default AdminManageItems;
