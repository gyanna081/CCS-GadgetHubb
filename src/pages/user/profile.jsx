import React from "react";
import { Link, useLocation } from "react-router-dom";
import logo from "../../assets/CCSGadgetHub1.png";

const navLinks = [
  { label: "Dashboard", to: "/userdashboard" },
  { label: "Items", to: "/useritems" },
  { label: "My Requests", to: "/my-requests" },
  { label: "Activity Log", to: "/activity-log" },
  { label: "Profile", to: "/userprofile" },
];

const Profile = () => {
  const location = useLocation();

  return (
    <div className="items-page">
      {/* Navbar */}
      <div className="navbar">
        <img src={logo} alt="CCS Gadget Hub Logo" />
        <nav>
          {navLinks.map((link) => (
            <Link
              key={link.to}
              to={link.to}
              className={
                location.pathname === link.to
                  ? "navbar-link active-link"
                  : "navbar-link"
              }
            >
              {link.label}
            </Link>
          ))}
        </nav>
        <div style={{ marginLeft: "auto" }}>
          <Link to="/" className="logout-link">
            Log Out
          </Link>
        </div>
      </div>

      {/* Profile Content */}
      <div className="profile-page">
        <div className="profile-container">
          {/* Left: Image */}
          <div className="profile-image">Profile Image</div>

          {/* Right: Info */}
          <div className="profile-info">
            <div className="profile-edit">
              <Link to="/usereditprofile">
                <button className="edit-btn">Edit Profile</button>
              </Link>
            </div>

            <h2 className="profile-name">Mica Ella Obeso</h2>
            <p>Student</p>

            <div className="profile-grid">
              <div>
                <p className="profile-label">Student No.</p>
                <p><strong>xx-xxxxx-xxx</strong></p>
              </div>
              <div>
                <p className="profile-label">Course</p>
                <p><strong>BSIT</strong></p>
              </div>
              <div>
                <p className="profile-label">Email</p>
                <p>micaella.obeso@cit.edu</p>
              </div>
              <div>
                <p className="profile-label">Year</p>
                <p>4th Year</p>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default Profile;
