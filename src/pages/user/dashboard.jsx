import { useEffect, useState } from "react";
import { useNavigate, Link, useLocation } from "react-router-dom";
import { collection, getDocs, query, orderBy, limit } from "firebase/firestore";
import { auth } from "../../firebaseconfig";
import { signOut } from "firebase/auth";
import { db } from "../../firebaseconfig";
import logo from "../../assets/CCSGadgetHub1.png";

const Dashboard = () => {
  const navigate = useNavigate();
  const location = useLocation();
  const [items, setItems] = useState([]);

  const handleLogout = async () => {
    try {
      await signOut(auth);
      navigate("/");
    } catch (err) {
      console.error("Logout failed:", err);
    }
  };

  const navLinks = [
    { label: "Dashboard", to: "/userdashboard" },
    { label: "Items", to: "/useritems" },
    { label: "My Requests", to: "/my-requests" },
    { label: "Profile", to: "/userprofile" },
  ];

  useEffect(() => {
    const fetchItems = async () => {
      try {
        const itemsRef = collection(db, "items");
        const q = query(itemsRef, orderBy("createdAt", "desc"), limit(3));
        const querySnapshot = await getDocs(q);
        const fetchedItems = querySnapshot.docs.map(doc => doc.data());
        setItems(fetchedItems);
      } catch (error) {
        console.error("Error fetching items from Firestore:", error);
      }
    };

    fetchItems();
  }, []);

  return (
    <div className="dashboard-page">
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

      <div className="dashboard-container">
        <h1 className="dashboard-greeting">Welcome back, Mica!</h1>
        <p className="dashboard-subtext">Here’s a quick overview of your gadget hub activity.</p>

        <div className="summary-cards">
          <div className="summary-card"><h3>3</h3><p>Items Borrowed</p></div>
          <div className="summary-card"><h3>1</h3><p>Pending Requests</p></div>
          <div className="summary-card"><h3>0</h3><p>Overdue</p></div>
        </div>

        <div className="featured-header">
          <h2 className="featured-title">Featured Items</h2>
          <div className="featured-buttons">
            <button className="custom-button" onClick={() => navigate("/useritems")}>Borrow Item</button>
            <button className="custom-button" onClick={() => navigate("/my-requests")}>View Requests</button>
          </div>
        </div>

        <div className="items-grid">
          {items.length > 0 ? items.map((item, index) => (
            <div key={index} className="item-box">
              <img src={item.imagePath} alt={item.name} className="item-image" />
              <h4>{item.name}</h4>
              <p className="item-status">{item.status}</p>
              <p className="item-rating">⭐ 5</p>
            </div>
          )) : <p>No items found.</p>}
        </div>
      </div>
    </div>
  );
};

export default Dashboard;
