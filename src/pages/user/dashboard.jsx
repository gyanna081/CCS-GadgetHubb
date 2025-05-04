import { useEffect, useState } from "react";
import { useNavigate, Link, useLocation } from "react-router-dom";
import { collection, getDocs, query, orderBy, limit, doc, getDoc } from "firebase/firestore";
import { auth, db } from "../../firebaseconfig";
import { signOut } from "firebase/auth";
import logo from "../../assets/CCSGadgetHub1.png";

const Dashboard = () => {
  const navigate = useNavigate();
  const location = useLocation();

  const [items, setItems] = useState([]);
  const [userData, setUserData] = useState(null);

  const handleLogout = async () => {
    try {
      await signOut(auth);
      navigate("/");
    } catch (err) {
      console.error("Logout failed:", err);
    }
  };

  const navLinks = [
    { label: "Dashboard", to: "/dashboard" },
    { label: "Items", to: "/useritems" },
    { label: "My Requests", to: "/my-requests" },
    { label: "Profile", to: "/userprofile" },
  ];

  useEffect(() => {
    const fetchUser = async () => {
      const currentUser = auth.currentUser;
      if (currentUser) {
        try {
          const userDocRef = doc(db, "users", currentUser.uid);
          const userSnap = await getDoc(userDocRef);
          if (userSnap.exists()) {
            setUserData(userSnap.data());
          }
        } catch (err) {
          console.error("Error fetching user data:", err);
        }
      }
    };

    const fetchItems = async () => {
      try {
        const itemsRef = collection(db, "items");
        const q = query(itemsRef, orderBy("createdAt", "desc"), limit(3));
        const querySnapshot = await getDocs(q);
        const fetchedItems = querySnapshot.docs.map(doc => ({ id: doc.id, ...doc.data() }));
        setItems(fetchedItems);
      } catch (error) {
        console.error("Error fetching items from Firestore:", error);
      }
    };

    fetchUser();
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

      <div className="dashboard-container">
        <h1 className="dashboard-greeting">
          Welcome back{userData?.firstName ? `, ${userData.firstName}!` : "!"}
        </h1>
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
          {items.length > 0 ? (
            items.map((item) => (
              <Link to={`/useritem-details/${item.id}`} key={item.id} className="item-box">
                <img
                  src={item.imagePath || "https://via.placeholder.com/150"}
                  alt={item.name}
                  className="item-image"
                />
                <h4>{item.name}</h4>
                <p className="item-status">{item.status}</p>
              </Link>
            ))
          ) : (
            <p>No items found.</p>
          )}
        </div>
      </div>
    </div>
  );
};

export default Dashboard;
