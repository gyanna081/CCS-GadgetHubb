import React from "react";
import { Link, useParams, useLocation, useNavigate } from "react-router-dom";
import logo from "../../assets/CCSGadgetHub1.png";

const AdminRequestReview = () => {
  const { id } = useParams();
  const location = useLocation();
  const navigate = useNavigate();

  const requestDetails = {
    id,
    borrower: "Jane Doe",
    item: "Dell Laptop",
    requestCreatedDate: "April 18, 2025", // Added request creation date
    borrowDate: "April 22, 2025",          // Adjusted field names
    borrowTime: "9:00 AM - 3:00 PM",
    reason: "Project Presentation for final grade submission.",
    status: "Pending",
  };

  const handleApprove = () => {
    alert("Request Approved!");
    navigate("/admin-requests");
  };

  const handleDeny = () => {
    alert("Request Denied!");
    navigate("/admin-requests");
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
        <Link to="/admin-requests" className="back-arrow">←</Link>

        <div className="request-details-box">
          <h2>Review Request Details</h2>

          {/* Request Timeline */}
          <h3 className="section-header">📅 Request Timeline</h3>
          <p><strong>Date Request Was Created:</strong> {requestDetails.requestCreatedDate}</p>

          {/* Borrowing Schedule */}
          <h3 className="section-header">📦 Borrowing Schedule</h3>
          <p><strong>Scheduled Borrow Date:</strong> {requestDetails.borrowDate}</p>
          <p><strong>Scheduled Time Slot:</strong> {requestDetails.borrowTime}</p>

          {/* Request Information */}
          <h3 className="section-header">📝 Request Information</h3>
          <p><strong>Borrower Name:</strong> {requestDetails.borrower}</p>
          <p><strong>Item Requested:</strong> {requestDetails.item}</p>
          <p><strong>Reason for Borrowing:</strong> {requestDetails.reason}</p>

          <p><strong>Current Status:</strong> {requestDetails.status}</p>

          {/* Approve and Deny Buttons */}
          <div className="request-action-btns">
            <button className="approve-btn" onClick={handleApprove}>Approve</button>
            <button className="deny-btn" onClick={handleDeny}>Deny</button>
          </div>
        </div>
      </div>
    </div>
  );
};

export default AdminRequestReview;
