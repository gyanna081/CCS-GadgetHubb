import React, { useState } from "react";
import { Link, useNavigate, useLocation } from "react-router-dom";
import logo from "../../assets/CCSGadgetHub1.png";

const navLinks = [
  { label: "Dashboard", to: "/dashboard" },
  { label: "Items", to: "/items" },
  { label: "My Requests", to: "/my-requests" },
  { label: "Activity Log", to: "/activity-log" },
  { label: "Profile", to: "/profile" },
];

const EditProfile = () => {
  const navigate = useNavigate();
  const location = useLocation();

  const [firstName, setFirstName] = useState("Mica Ella");
  const [lastName, setLastName] = useState("Obeso");
  const [program, setProgram] = useState("BSIT");
  const [yearLevel, setYearLevel] = useState("4th Year");

  const handleCancel = () => {
    navigate("/userprofile");
  };

  const handleSave = (e) => {
    e.preventDefault();
    // logic to update profile here
    navigate("/userprofile");
  };

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

      {/* Content */}
      <div className="edit-profile-page">
        <Link to="/userprofile" className="back-arrow">←</Link>

        <div className="edit-profile-container">
          {/* Left: Form */}
          <form onSubmit={handleSave} className="edit-profile-form">
            <div className="input-row">
              <label>First Name:</label>
              <input type="text" value={firstName} onChange={(e) => setFirstName(e.target.value)} />
            </div>

            <div className="input-row">
              <label>Last Name:</label>
              <input type="text" value={lastName} onChange={(e) => setLastName(e.target.value)} />
            </div>

            <p style={{ margin: "10px 0 0", fontWeight: "bold" }}>xx-xxxxx-xxx</p>
            <p style={{ marginBottom: "20px" }}>micaella.obeso@cit.edu</p>

            <div className="input-row">
              <label>Course/Program:</label>
              <select value={program} onChange={(e) => setProgram(e.target.value)}>
                <option value="BSIT">BSIT</option>
                <option value="BSCS">BSCS</option>
              </select>
            </div>

            <div className="input-row">
              <label>Year Level:</label>
              <select value={yearLevel} onChange={(e) => setYearLevel(e.target.value)}>
                <option value="1st Year">1st Year</option>
                <option value="2nd Year">2nd Year</option>
                <option value="3rd Year">3rd Year</option>
                <option value="4th Year">4th Year</option>
              </select>
            </div>

            <div className="edit-btn-row">
              <button type="button" className="cancel-btn" onClick={handleCancel}>Cancel</button>
              <button type="submit" className="edit-save-btn">Save</button>
            </div>
          </form>

          {/* Right: Profile Picture */}
          <div className="profile-pic-placeholder">Change Profile Picture</div>
        </div>
      </div>
    </div>
  );
};

export default EditProfile;
