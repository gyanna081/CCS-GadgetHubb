
import React, { useState, useEffect } from "react";
import { Link, useLocation, useNavigate } from "react-router-dom";
import logo from "../../assets/CCSGadgetHub1.png";

const MyRequests = () => {
  const location = useLocation();
  const navigate = useNavigate();
  const [searchTerm, setSearchTerm] = useState("");
  const [statusFilter, setStatusFilter] = useState("all");
  const [requests, setRequests] = useState([]);

  useEffect(() => {
    const dummyData = [
      { id: 1, item: "Dell Laptop", date: "2025-04-12T09:30", status: "Approved", returnDate: "" },
      { id: 2, item: "Macbook Air", date: "2025-04-10T14:00", status: "Pending", returnDate: "" },
      { id: 3, item: "Huawei Matebook", date: "2025-04-05T11:15", status: "Returned", returnDate: "2025-04-06T10:00" },
    ];
    setRequests(dummyData);
  }, []);

  const filteredRequests = requests.filter((req) => {
    const matchesSearch = req.item.toLowerCase().includes(searchTerm.toLowerCase());
    const matchesStatus = statusFilter === "all" || req.status.toLowerCase() === statusFilter;
    return matchesSearch && matchesStatus;
  });

  const formatDate = (dateStr) => {
    if (!dateStr) return "-";
    const options = { year: "numeric", month: "short", day: "numeric" };
    return new Date(dateStr).toLocaleDateString(undefined, options);
  };

  const formatDateTime = (dateTimeStr) => {
    if (!dateTimeStr) return "-";
    const options = {
      year: "numeric", month: "short", day: "numeric",
      hour: "2-digit", minute: "2-digit"
    };
    return new Date(dateTimeStr).toLocaleString(undefined, options);
  };

  return (
    <div className="items-page">
      {/* Navbar */}
      <div className="navbar">
        <img src={logo} alt="CCS Gadget Hub Logo" />
        <nav>
          {[
            { label: "Dashboard", to: "/userdashboard" },
            { label: "Items", to: "/useritems" },
            { label: "My Requests", to: "/my-requests" },
            { label: "Profile", to: "/userprofile" },
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
      <div className="dashboard-container">
        <h2 className="featured-title">My Requests</h2>

        <div className="filter-bar">
          <input
            type="text"
            placeholder="Search by item name..."
            value={searchTerm}
            onChange={(e) => setSearchTerm(e.target.value)}
          />
          <select value={statusFilter} onChange={(e) => setStatusFilter(e.target.value)}>
            <option value="all">All</option>
            <option value="pending">Pending</option>
            <option value="approved">Approved</option>
            <option value="returned">Returned</option>
            <option value="denied">Denied</option>
          </select>
        </div>

        <table className="request-table">
          <thead>
            <tr>
              <th>Item</th>
              <th>Request Date</th>
              <th>Status</th>
              <th>Returned Date & Time</th>
              <th>Action</th>
            </tr>
          </thead>
          <tbody>
            {filteredRequests.map((req) => (
              <tr key={req.id}>
                <td>{req.item}</td>
                <td>{formatDate(req.date)}</td>
                <td><span className={`status-badge ${req.status.toLowerCase()}`}>{req.status}</span></td>
                <td>{req.status.toLowerCase() === "returned" ? formatDateTime(req.returnDate) : "-"}</td>
                <td>
                  <button className="view-btn" onClick={() => navigate(`/view-request/${req.id}`)}>View</button>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
};

export default MyRequests;
