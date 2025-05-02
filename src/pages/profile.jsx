import React, { useState, useEffect } from "react";
import { Link, useLocation, useNavigate } from "react-router-dom";
import { getAuth, onAuthStateChanged, signOut } from "firebase/auth";
import axios from "axios";
import logo from "../assets/CCSGadgetHub1.png";

const navLinks = [
  { label: "Dashboard", to: "/userdashboard" },
  { label: "Items", to: "/useritems" },
  { label: "My Requests", to: "/my-requests" },
  { label: "Profile", to: "/userprofile" },
];

const Profile = () => {
  const location = useLocation();
  const navigate = useNavigate();
  const [user, setUser] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");
  const auth = getAuth();

  useEffect(() => {
    const fetchUserProfile = async (currentUser) => {
      try {
        const idToken = await currentUser.getIdToken();
        const response = await axios.get(
          `http://localhost:8080/api/users/${currentUser.uid}/profile`,
          {
            headers: {
              Authorization: `Bearer ${idToken}`,
            },
          }
        );

        const userData = response.data;

        // Force-refresh profile image to avoid cache issues
        if (userData.profileImageUrl) {
          userData.profileImageUrl += `?t=${Date.now()}`;
        }

        setUser(userData);
        setLoading(false);
      } catch (error) {
        console.error("Error fetching profile:", error);
        setError("Failed to load profile data. Please try again later.");
        setLoading(false);
      }
    };

    const unsubscribe = onAuthStateChanged(auth, (currentUser) => {
      if (currentUser) {
        fetchUserProfile(currentUser);
      } else {
        navigate("/");
      }
    });

    return () => unsubscribe();
  }, [auth, navigate]);

  const handleLogout = async () => {
    try {
      await signOut(auth);
      navigate("/");
    } catch (error) {
      console.error("Logout error:", error);
      setError("Failed to log out. Please try again.");
    }
  };

  if (loading) {
    return (
      <div className="loading-container">
        <div className="loading-spinner"></div>
        <p>Loading profile...</p>
      </div>
    );
  }

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
              className={location.pathname === link.to ? "navbar-link active-link" : "navbar-link"}
            >
              {link.label}
            </Link>
          ))}
        </nav>
        <div style={{ marginLeft: "auto" }}>
          <button onClick={handleLogout} className="logout-link">Log Out</button>
        </div>
      </div>

      {/* Profile Content */}
      <div className="profile-page" style={{ padding: "40px", maxWidth: "900px", margin: "0 auto" }}>
        {error && <div className="error-message">{error}</div>}

        <div className="profile-container" style={{
          display: "flex",
          backgroundColor: "#fff",
          borderRadius: "12px",
          boxShadow: "0 4px 12px rgba(0,0,0,0.1)",
          overflow: "hidden",
          padding: "30px",
          gap: "30px",
        }}>
          {/* Profile Image */}
          <div className="profile-image" style={{ flex: "0 0 160px", height: "160px", borderRadius: "50%", overflow: "hidden" }}>
            {user?.profileImageUrl ? (
              <img
                src={user.profileImageUrl}
                alt="Profile"
                onError={(e) => {
                  e.target.onerror = null;
                  e.target.src = "https://placehold.co/160x160?text=No+Image";
                }}
                style={{
                  width: "100%",
                  height: "100%",
                  objectFit: "cover",
                }}
              />
            ) : (
              <div style={{
                width: "100%",
                height: "100%",
                backgroundColor: "#ddd",
                color: "#444",
                display: "flex",
                justifyContent: "center",
                alignItems: "center",
                fontSize: "48px",
                fontWeight: "bold",
              }}>
                {user?.firstName?.charAt(0).toUpperCase() || "U"}
              </div>
            )}
          </div>

          {/* Profile Info */}
          <div className="profile-info" style={{ flex: 1 }}>
            <div className="profile-edit" style={{ textAlign: "right", marginBottom: "10px" }}>
              <Link to="/edit-profile">
                <button className="edit-btn" style={{
                  backgroundColor: "#E26901",
                  color: "white",
                  border: "none",
                  padding: "8px 16px",
                  borderRadius: "4px",
                  cursor: "pointer",
                  fontSize: "14px"
                }}>
                  Edit Profile
                </button>
              </Link>
            </div>

            <h2 className="profile-name" style={{ margin: "0 0 10px 0", fontSize: "24px", fontWeight: "bold" }}>
              {`${user?.firstName || ""} ${user?.lastName || ""}`.trim()}
            </h2>
            <p style={{ marginBottom: "20px", fontSize: "16px", color: "#777" }}>
              {user?.role === "student" ? "Student" : (user?.role || "")}
            </p>

            <div className="profile-grid" style={{
              display: "grid",
              gridTemplateColumns: "1fr 1fr",
              gap: "20px"
            }}>
              <div>
                <p className="profile-label" style={{ fontSize: "14px", color: "#999", marginBottom: "4px" }}>Course</p>
                <p><strong>{user?.course || "Not set"}</strong></p>
              </div>
              <div>
                <p className="profile-label" style={{ fontSize: "14px", color: "#999", marginBottom: "4px" }}>Email</p>
                <p>{user?.email || "Not available"}</p>
              </div>
              <div>
                <p className="profile-label" style={{ fontSize: "14px", color: "#999", marginBottom: "4px" }}>Year</p>
                <p>{user?.year || "Not set"}</p>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default Profile;
