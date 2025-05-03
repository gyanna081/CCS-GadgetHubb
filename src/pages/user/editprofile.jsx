import React, { useState, useEffect } from "react";
import { Link, useNavigate } from "react-router-dom";
import { getAuth, onAuthStateChanged, signOut } from "firebase/auth";
import axios from "axios";
import { db } from "../../firebaseconfig";
import { doc, getDoc, setDoc } from "firebase/firestore";
import logo from "../../assets/CCSGadgetHub1.png";

const EditProfile = () => {
  const navigate = useNavigate();
  const auth = getAuth();
  const [user, setUser] = useState(null);

  const [form, setForm] = useState({
    firstName: "",
    lastName: "",
    email: "",
    course: "BSIT",
    year: "4th Year",
  });

  const [profileImageUrl, setProfileImageUrl] = useState("");
  const [profileImage, setProfileImage] = useState(null);
  const [loading, setLoading] = useState(true);
  const [saving, setSaving] = useState(false);
  const [error, setError] = useState("");

  useEffect(() => {
    const unsubscribe = onAuthStateChanged(auth, async (user) => {
      if (user) {
        setUser(user);
        const docRef = doc(db, "users", user.uid);
        const docSnap = await getDoc(docRef);

        if (docSnap.exists()) {
          const data = docSnap.data();
          setForm({
            firstName: data.firstName || "",
            lastName: data.lastName || "",
            email: user.email || "",
            course: data.course || "BSIT",
            year: data.year || "4th Year",
          });

          if (data.profileImageUrl) {
            setProfileImageUrl(`${data.profileImageUrl}?t=${Date.now()}`);
          }
        } else {
          setForm((prev) => ({ ...prev, email: user.email }));
        }

        setLoading(false);
      } else {
        navigate("/");
      }
    });

    return () => unsubscribe();
  }, [auth, navigate]);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setForm((prev) => ({ ...prev, [name]: value }));
  };

  const handleImageChange = (e) => {
    const file = e.target.files[0];
    if (!file) return;

    if (!file.type.match("image.*")) {
      setError("Please upload a valid image file.");
      return;
    }

    if (file.size > 5 * 1024 * 1024) {
      setError("Image must be less than 5MB.");
      return;
    }

    setProfileImage(file);

    const reader = new FileReader();
    reader.onloadend = () => {
      setProfileImageUrl(reader.result); // For preview only
    };
    reader.readAsDataURL(file);
  };

  const handleSave = async (e) => {
    e.preventDefault();
    setSaving(true);
    setError("");

    try {
      const idToken = await auth.currentUser.getIdToken();

      // Update user profile info in Firestore
      await setDoc(doc(db, "users", user.uid), form, { merge: true });

      // Upload image if selected
      if (profileImage) {
        const formData = new FormData();
        formData.append("image", profileImage);

        const uploadRes = await axios.post(
          `http://localhost:8080/api/users/${user.uid}/profile-image`,
          formData,
          {
            headers: {
              "Content-Type": "multipart/form-data",
              Authorization: `Bearer ${idToken}`,
            },
          }
        );

        if (uploadRes.data.imageUrl) {
          setProfileImageUrl(`${uploadRes.data.imageUrl}?t=${Date.now()}`);

          // Save URL in Firestore
          await setDoc(
            doc(db, "users", user.uid),
            { profileImageUrl: uploadRes.data.imageUrl },
            { merge: true }
          );
        }
      }

      navigate("/userprofile");
    } catch (err) {
      console.error("Failed to update profile:", err);
      setError("Failed to update profile. Please try again.");
    } finally {
      setSaving(false);
    }
  };

  const handleLogout = async () => {
    try {
      await signOut(auth);
      navigate("/");
    } catch (err) {
      console.error("Logout failed:", err);
    }
  };

  if (loading) return <p style={{ textAlign: "center" }}>Loading...</p>;

  return (
    <div style={{ fontFamily: "Arial, sans-serif", backgroundColor: "#fff6e5", minHeight: "100vh" }}>
      {/* Navbar */}
      <header style={{
        backgroundColor: "#e67e22",
        padding: "15px 20px",
        display: "flex",
        alignItems: "center",
        justifyContent: "space-between",
        boxShadow: "0 2px 4px rgba(0,0,0,0.1)"
      }}>
        <div style={{ display: "flex", alignItems: "center" }}>
          <img src={logo} alt="Logo" style={{ height: "50px", marginRight: "20px" }} />
          <nav style={{ display: "flex" }}>
            <Link to="/userdashboard" style={navLink}>Dashboard</Link>
            <Link to="/useritems" style={navLink}>Items</Link>
            <Link to="/my-requests" style={navLink}>My Requests</Link>
            <Link to="/userprofile" style={{ ...navLink, fontWeight: "bold" }}>Profile</Link>
          </nav>
        </div>
        <button
          onClick={handleLogout}
          style={{
            background: "none",
            border: "none",
            color: "white",
            fontSize: "16px",
            cursor: "pointer"
          }}
        >
          Log Out
        </button>
      </header>

      {/* Form */}
      <div style={{ maxWidth: "700px", margin: "40px auto", background: "#fff", padding: "30px", borderRadius: "10px", boxShadow: "0 2px 8px rgba(0,0,0,0.1)" }}>
        <h2 style={{ textAlign: "center", marginBottom: "20px" }}>Edit Profile</h2>
        {error && <p style={{ color: "red", textAlign: "center" }}>{error}</p>}

        <form onSubmit={handleSave} style={{ display: "grid", gap: "15px" }}>
          <div style={{ display: "flex", gap: "15px" }}>
            <div style={{ flex: 1 }}>
              <label>First Name:</label>
              <input name="firstName" value={form.firstName} onChange={handleChange} required style={inputStyle} />
            </div>
            <div style={{ flex: 1 }}>
              <label>Last Name:</label>
              <input name="lastName" value={form.lastName} onChange={handleChange} required style={inputStyle} />
            </div>
          </div>

          <div>
            <label>Email:</label>
            <input name="email" value={form.email} disabled readOnly style={{ ...inputStyle, backgroundColor: "#f0f0f0" }} />
          </div>

          <div style={{ display: "flex", gap: "15px" }}>
            <div style={{ flex: 1 }}>
              <label>Course:</label>
              <select name="course" value={form.course} onChange={handleChange} style={inputStyle}>
                <option value="BSIT">BSIT</option>
                <option value="BSCS">BSCS</option>
                <option value="BSIS">BSIS</option>
              </select>
            </div>
            <div style={{ flex: 1 }}>
              <label>Year:</label>
              <select name="year" value={form.year} onChange={handleChange} style={inputStyle}>
                <option value="1st Year">1st Year</option>
                <option value="2nd Year">2nd Year</option>
                <option value="3rd Year">3rd Year</option>
                <option value="4th Year">4th Year</option>
                <option value="5th Year">5th Year</option>
              </select>
            </div>
          </div>

          <div style={{ display: "flex", flexDirection: "column", alignItems: "center" }}>
            <div style={{
              width: "150px", height: "150px", borderRadius: "50%", overflow: "hidden",
              border: "1px solid #ddd", backgroundColor: "#f5f5f5", marginBottom: "15px"
            }}>
              {profileImageUrl ? (
                <img src={profileImageUrl} alt="Preview" style={{ width: "100%", height: "100%", objectFit: "cover" }} />
              ) : (
                <div style={{ textAlign: "center", lineHeight: "150px", color: "#aaa" }}>No Image</div>
              )}
            </div>
            <input type="file" id="profile-image" accept="image/*" onChange={handleImageChange} style={{ display: "none" }} />
            <label htmlFor="profile-image" style={{
              backgroundColor: "#e67e22", color: "white", padding: "8px 16px", borderRadius: "4px",
              cursor: "pointer", marginBottom: "10px", fontSize: "14px"
            }}>
              Change Profile Picture
            </label>
          </div>

          <div style={{ textAlign: "right" }}>
            <button type="submit" disabled={saving} style={buttonStyle}>
              {saving ? "Saving..." : "Save Changes"}
            </button>
          </div>
        </form>
      </div>
    </div>
  );
};

const navLink = {
  color: "white",
  textDecoration: "none",
  margin: "0 15px"
};

const inputStyle = {
  width: "100%",
  padding: "10px",
  fontSize: "14px",
  borderRadius: "6px",
  border: "1px solid #ccc"
};

const buttonStyle = {
  backgroundColor: "#e67e22",
  color: "white",
  padding: "10px 18px",
  fontSize: "15px",
  borderRadius: "6px",
  border: "none",
  cursor: "pointer"
};

export default EditProfile;
