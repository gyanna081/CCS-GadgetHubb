import React, { useState, useEffect } from "react";
import { Link, useLocation } from "react-router-dom";
import logo from "../../assets/CCSGadgetHub1.png";

const Items = () => {
  const [items, setItems] = useState([]);
  const [availabilityFilter, setAvailabilityFilter] = useState("available");
  const [ratingFilter, setRatingFilter] = useState("all");
  const [searchQuery, setSearchQuery] = useState("");

  const location = useLocation();

  useEffect(() => {
    const fetchedItems = [
      { id: 1, name: "Laptop 1", available: true, rating: 5, image: "https://via.placeholder.com/120?text=Laptop+1" },
      { id: 2, name: "Laptop 2", available: true, rating: 4, image: "https://via.placeholder.com/120?text=Laptop+2" },
      { id: 3, name: "Laptop 3", available: false, rating: 3, image: "https://via.placeholder.com/120?text=Laptop+3" },
      { id: 4, name: "Laptop 4", available: true, rating: 5, image: "https://via.placeholder.com/120?text=Laptop+4" },
      { id: 5, name: "Laptop 5", available: false, rating: 3, image: "https://via.placeholder.com/120?text=Laptop+5" },
      { id: 6, name: "Laptop 6", available: true, rating: 4, image: "https://via.placeholder.com/120?text=Laptop+6" },
    ];
    setItems(fetchedItems);
  }, []);

  const navLinks = [
    { label: "Dashboard", to: "/userdashboard" },
    { label: "Items", to: "/useritems" },
    { label: "My Requests", to: "/my-requests" },
    { label: "Profile", to: "/userprofile" },
  ];

  const filteredItems = items.filter((item) => {
    const matchesAvailability =
      availabilityFilter === "all" ||
      (availabilityFilter === "available" && item.available) ||
      (availabilityFilter === "not-available" && !item.available);

    const matchesRating =
      ratingFilter === "all" || item.rating === parseInt(ratingFilter);

    const matchesSearch = item.name
      .toLowerCase()
      .includes(searchQuery.toLowerCase());

    return matchesAvailability && matchesRating && matchesSearch;
  });

  return (
    <div className="items-page">
      {/* Navigation Bar */}
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
      <div className="dashboard-container">
        <h2 className="featured-title">Items</h2>

        {/* 🔍 Search Bar */}
        <div className="search-bar" style={{ marginBottom: "15px" }}>
          <input
            type="text"
            placeholder="Search by item name..."
            value={searchQuery}
            onChange={(e) => setSearchQuery(e.target.value)}
            style={{
              padding: "8px 12px",
              borderRadius: "8px",
              border: "1px solid #ccc",
              width: "370px",
            }}
          />
        </div>

        {/* Filters */}
        <div className="filter-container" style={{ marginBottom: "20px" }}>
          <label>
            Status:
            <select
              value={availabilityFilter}
              onChange={(e) => setAvailabilityFilter(e.target.value)}
              style={{ marginLeft: "8px", marginRight: "20px" }}
            >
              <option value="all">All</option>
              <option value="available">Available</option>
              <option value="not-available">Not Available</option>
            </select>
          </label>

          <label>
            Rating:
            <select
              value={ratingFilter}
              onChange={(e) => setRatingFilter(e.target.value)}
              style={{ marginLeft: "8px" }}
            >
              <option value="all">All</option>
              <option value="3">3 stars</option>
              <option value="4">4 stars</option>
              <option value="5">5 stars</option>
            </select>
          </label>
        </div>

        {/* Laptop Grid */}
        <div className="items-grid">
          {filteredItems.map((item) => (
            <div key={item.id} className="item-box">
              <img src={item.image} alt={item.name} className="item-image" />
              <h3>{item.name}</h3>
              <p className="item-status">
                {item.available ? "Available" : "Not Available"}
              </p>
              <p className="item-rating">⭐ {item.rating}</p>
              <Link to={`/useritem-details/${item.id}`} className="item-details-btn">
                View Details
              </Link>
            </div>
          ))}
        </div>
      </div>
    </div>
  );
};

export default Items;