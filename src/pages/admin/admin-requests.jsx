import React, { useState, useEffect } from "react";
import { Link, useNavigate, useLocation } from "react-router-dom";
import logo from "../../assets/CCSGadgetHub1.png";

const AdminRequests = () => {
  const location = useLocation();
  const navigate = useNavigate();

  const [requests, setRequests] = useState([]);
  const [statusFilter, setStatusFilter] = useState("All");
  const [searchTerm, setSearchTerm] = useState("");
  const [dateFilter, setDateFilter] = useState("");

  useEffect(() => {
    const dummyRequests = [
      {
        id: 1,
        borrower: "Jane Doe",
        item: "Dell Laptop",
        requestDate: "2025-04-22",
        requestTime: "9:00 AM - 3:00 PM",
        status: "Pending",
        decisionDate: null
      },
      {
        id: 2,
        borrower: "John Smith",
        item: "Macbook Air",
        requestDate: "2025-04-21",
        requestTime: "10:00 AM - 2:00 PM",
        status: "Approved",
        decisionDate: "2025-04-22"
      },
      {
        id: 3,
        borrower: "Emily Brown",
        item: "Huawei Matebook D15",
        requestDate: "2025-04-20",
        requestTime: "11:00 AM - 4:00 PM",
        status: "Denied",
        decisionDate: "2025-04-21"
      }
    ];
    setRequests(dummyRequests);
  }, []);

  const filteredRequests = requests.filter((req) => {
    const matchesStatus = statusFilter === "All" || req.status === statusFilter;
    const matchesSearch =
      req.borrower.toLowerCase().includes(searchTerm.toLowerCase()) ||
      req.item.toLowerCase().includes(searchTerm.toLowerCase());
    const matchesDate = dateFilter === "" || req.requestDate === dateFilter;
    return matchesStatus && matchesSearch && matchesDate;
  });

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
        <h1 className="admin-welcome">Manage Requests</h1>

        {/* Filters Row */}
        <div className="admin-filters-row">
          <input
            type="text"
            placeholder="Search by borrower or item..."
            value={searchTerm}
            onChange={(e) => setSearchTerm(e.target.value)}
            className="admin-search-bar"
          />

          <select
            value={statusFilter}
            onChange={(e) => setStatusFilter(e.target.value)}
            className="admin-filter-dropdown"
          >
            <option value="All">All</option>
            <option value="Pending">Pending</option>
            <option value="Approved">Approved</option>
            <option value="Denied">Denied</option>
          </select>

          <input
            type="date"
            value={dateFilter}
            onChange={(e) => setDateFilter(e.target.value)}
            className="admin-date-filter"
          />
        </div>

        {/* Requests List */}
        <div className="admin-requests-grid">
          {filteredRequests.length > 0 ? (
            filteredRequests.map((req) => (
              <div
                key={req.id}
                className="admin-request-card fixed-card-width"
                onClick={() => {
                  if (req.status === "Pending") return; // Pending = no card click
                  navigate(`/admin-view-request/${req.id}`); // ✅ Correct redirection for Approved/Denied
                }}
                style={{ cursor: req.status !== "Pending" ? "pointer" : "default" }}
              >
                <h3>{req.borrower}</h3>
                <p><strong>Item:</strong> {req.item}</p>
                <p><strong>Time Slot:</strong> ({req.requestTime})</p>
                <p><strong>Date:</strong> {req.requestDate}</p>
                <p className={`status-badge ${req.status.toLowerCase()}`}>{req.status}</p>

                {/* Show Review Button only if Pending */}
                {req.status === "Pending" && (
                  <button
                    className="review-btn"
                    onClick={(e) => {
                      e.stopPropagation(); // prevent card click
                      navigate(`/review-request/${req.id}`);
                    }}
                  >
                    Review Request
                  </button>
                )}
              </div>
            ))
          ) : (
            <p>No requests found for current filters.</p>
          )}
        </div>
      </div>
    </div>
  );
};

export default AdminRequests;
