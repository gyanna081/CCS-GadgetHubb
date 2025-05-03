import React, { useEffect, useState } from "react";
import { Link, useLocation, useNavigate } from "react-router-dom";
import { getAuth, onAuthStateChanged, signOut } from "firebase/auth";
import { doc, getDoc } from "firebase/firestore";
import { db } from "../../firebaseconfig";
import logo from "../../assets/CCSGadgetHub1.png";

const navLinks = [
  { label: "Dashboard", to: "/userdashboard" },
  { label: "Items", to: "/useritems" },
  { label: "My Requests", to: "/my-requests" },
  { label: "Profile", to: "/userprofile" },
];

const Profile = () => {
  const location = useLocation();
  const navigate = useNavigate();
  const auth = getAuth();

  const [userData, setUserData] = useState({
    firstName: "",
    lastName: "",
    email: "",
    course: "",
    year: "",
    profileImageUrl: ""
  });

  useEffect(() => {
    const unsubscribe = onAuthStateChanged(auth, async (user) => {
      if (user) {
        try {
          const docRef = doc(db, "users", user.uid);
          const docSnap = await getDoc(docRef);
          if (docSnap.exists()) {
            const data = docSnap.data();
            setUserData({
              firstName: data.firstName || "",
              lastName: data.lastName || "",
              email: user.email || "",
              course: data.course || "",
              year: data.year || "",
              profileImageUrl: data.profileImageUrl || ""
            });
          }
        } catch (error) {
          console.error("Error fetching user data:", error);
        }
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
      console.error("Error signing out:", error);
    }
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
      <div className="profile-page">
        <div className="profile-container">
          {/* Left: Image */}
          <div className="profile-image">
            {userData.profileImageUrl ? (
              <img
                src={`${userData.profileImageUrl}?t=${Date.now()}`}
                alt="Profile"
                style={{ width: "150px", height: "150px", borderRadius: "50%", objectFit: "cover" }}
              />
            ) : (
              <div style={{ width: "150px", height: "150px", borderRadius: "50%", backgroundColor: "#eee", display: "flex", justifyContent: "center", alignItems: "center" }}>
                No Image
              </div>
            )}
          </div>

          {/* Right: Info */}
          <div className="profile-info">
            <div className="profile-edit">
              <Link to="/usereditprofile">
                <button className="edit-btn">Edit Profile</button>
              </Link>
            </div>

            <h2 className="profile-name">{userData.firstName} {userData.lastName}</h2>
            <p>Student</p>

            <div className="profile-grid">
              <div>
                <p className="profile-label">Course</p>
                <p><strong>{userData.course}</strong></p>
              </div>
              <div>
                <p className="profile-label">Email</p>
                <p>{userData.email}</p>
              </div>
              <div>
                <p className="profile-label">Year</p>
                <p>{userData.year}</p>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default Profile;
