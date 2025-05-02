import React from "react";
import { Link, useParams, useLocation, useNavigate } from "react-router-dom";
import logo from "../../assets/CCSGadgetHub1.png";

const ViewUser = () => {
  const { id } = useParams();
  const location = useLocation();
  const navigate = useNavigate();

  const dummyUsers = [
    {
      id: "1",
      name: "Mica Ella Obeso",
      email: "micaella.obeso@cit.edu",
      role: "Student",
      yearLevel: "4th Year",
      course: "BSIT",
    },
    {
      id: "2",
      name: "John Smith",
      email: "john.smith@cit.edu",
      role: "Student",
      yearLevel: "3rd Year",
      course: "BSCS",
    },
    {
      id: "3",
      name: "Admin User",
      email: "admin@cit.edu",
      role: "Admin",
      yearLevel: "",
      course: "",
    },
  ];

  const user = dummyUsers.find((u) => u.id === id);

  if (!user) {
    return (
      <div className="admin-dashboard-container">
        <p>User not found.</p>
        <Link to="/admin-users" className="back-arrow">←</Link>
      </div>
    );
  }

  const handleEdit = () => {
    navigate(`/edit-user/${user.id}`);
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
        <div style={{ display: "flex", alignItems: "center", justifyContent: "space-between", maxWidth: "800px", margin: "0 auto" }}>
          <Link to="/admin-users" className="back-arrow">←</Link>
          <button className="edit-user-btn" onClick={handleEdit}>Edit User</button>
        </div>

        <div className="view-user-box">
          <h2 className="view-user-title">User Details</h2>

          <p><strong>Full Name:</strong> {user.name}</p>
          <p><strong>Email:</strong> {user.email}</p>
          <p><strong>Role:</strong> {user.role}</p>

          {user.role === "Student" && (
            <>
              <p><strong>Year Level:</strong> {user.yearLevel}</p>
              <p><strong>Course:</strong> {user.course}</p>
            </>
          )}
        </div>
      </div>
    </div>
  );
};

export default ViewUser;
