import React from "react";
import { Link, useLocation } from "react-router-dom";
import logo from "../../assets/CCSGadgetHub1.png";
import "../../admin.css";

const AdminDashboard = () => {
  const location = useLocation();

  const navLinks = [
    { label: "Dashboard", to: "/admin-dashboard" },
    { label: "Manage Items", to: "/admin-items" },
    { label: "Requests", to: "/admin-requests" },
    { label: "Manage Users", to: "/admin-users" },
  ];

  const recentActivities = [
    { id: 1, text: "Jane Doe borrowed \"Dell Laptop\"", date: "Apr 22" },
    { id: 2, text: "Mike returned \"Macbook Air\"", date: "Apr 21" },
    { id: 3, text: "Kyle canceled \"Tablet\"", date: "Apr 20" },
  ];

  const pendingRequests = [
    { id: 1, item: "Dell Laptop", user: "Jane Doe", date: "Apr 22" },
    { id: 2, item: "Macbook Air", user: "John Smith", date: "Apr 21" },
  ];

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
              className={
                location.pathname === link.to ? "navbar-link active-link" : "navbar-link"
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

      <div className="admin-dashboard-container">
        <h1 className="admin-welcome">Welcome back, Admin!</h1>
        <p className="admin-subtext">Here’s an overview of recent gadget hub activity.</p>

        <div className="admin-cards">
          <div className="admin-card">
            <h3>20</h3>
            <p>Total Items</p>
          </div>
          <div className="admin-card">
            <h3>10</h3>
            <p>Approved Requests</p>
          </div>
          <div className="admin-card">
            <h3>3</h3>
            <p>Pending Requests</p>
          </div>
          <div className="admin-card">
            <h3>2</h3>
            <p>Returned / Lost</p>
          </div>
        </div>

        <div className="admin-columns">
          {/* Recent Activity */}
          <div className="admin-activity-box">
            <h3>Recent Activity</h3>
            <ul>
              {recentActivities.map((log) => (
                <li key={log.id}>{log.text} - <span>{log.date}</span></li>
              ))}
            </ul>
          </div>

          {/* Pending Requests */}
          <div className="admin-pending-box">
            <h3>Pending Approvals</h3>
            {pendingRequests.map((req) => (
              <div key={req.id} className="pending-request">
                <p><strong>{req.item}</strong> - {req.user}</p>
                <span>{req.date}</span>
                <div className="action-btns">
                  <button className="approve-btn">Approve</button>
                  <button className="deny-btn">Deny</button>
                </div>
              </div>
            ))}
          </div>
        </div>
      </div>
    </div>
  );
};

export default AdminDashboard;
