import React, { useEffect, useState } from "react";
import { Link, useLocation, useNavigate } from "react-router-dom";
import logo from "../assets/CCSGadgetHub1.png";
import "../admin.css";
import { db } from "../firebaseconfig";
import {
  collection,
  getDocs,
  query,
  where,
  orderBy,
  limit
} from "firebase/firestore";

const AdminDashboard = () => {
  const location = useLocation();
  const navigate = useNavigate();

  const [totalItems, setTotalItems] = useState(0);
  const [approvedCount, setApprovedCount] = useState(0);
  const [pendingRequests, setPendingRequests] = useState([]);
  const [returnedCount, setReturnedCount] = useState(0);
  const [recentActivities, setRecentActivities] = useState([]);

  useEffect(() => {
    const fetchDashboardData = async () => {
      try {
        const itemsSnap = await getDocs(collection(db, "items"));
        setTotalItems(itemsSnap.size);

        const reqRef = collection(db, "borrowRequests");

        const approvedSnap = await getDocs(
          query(reqRef, where("status", "==", "Approved"))
        );
        setApprovedCount(approvedSnap.size);

        const pendingSnap = await getDocs(
          query(reqRef, where("status", "==", "Pending"))
        );
        const pendingList = pendingSnap.docs.map((doc) => ({
          id: doc.id,
          ...doc.data()
        }));
        setPendingRequests(pendingList);

        const returnedSnap = await getDocs(
          query(reqRef, where("status", "==", "Returned"))
        );
        setReturnedCount(returnedSnap.size);

        const activitySnap = await getDocs(
          query(reqRef, orderBy("requestDate", "desc"), limit(5))
        );
        const recent = activitySnap.docs.map((doc) => {
          const d = doc.data();
          return {
            id: doc.id,
            text: `${d.borrowerName || "User"} ${d.status?.toLowerCase()} "${d.itemName || "item"}"`,
            date: d.requestDate || "N/A"
          };
        });
        setRecentActivities(recent);
      } catch (err) {
        console.error("Dashboard data fetch error:", err);
      }
    };

    fetchDashboardData();
  }, []);

  const navLinks = [
    { label: "Dashboard", to: "/admin-dashboard" },
    { label: "Manage Items", to: "/admin-items" },
    { label: "Requests", to: "/admin-requests" },
    { label: "Manage Users", to: "/admin-users" },
  ];

  const handleCardClick = (path) => {
    navigate(path);
  };

  const handleReview = (e, id) => {
    e.stopPropagation();
    navigate(`/review-request/${id}`);
  };

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

      {/* Main Content */}
      <div className="admin-dashboard-container">
        <h1 className="admin-welcome">Welcome back, Admin!</h1>
        <p className="admin-subtext">Here’s an overview of recent gadget hub activity.</p>

        {/* Cards */}
        <div className="admin-cards">
          <div className="admin-card clickable" onClick={() => handleCardClick("/admin-items")}>
            <h3>{totalItems}</h3>
            <p>Total Items</p>
          </div>
          <div className="admin-card clickable" onClick={() => handleCardClick("/admin-requests")}>
            <h3>{approvedCount}</h3>
            <p>Approved Requests</p>
          </div>
          <div className="admin-card clickable" onClick={() => handleCardClick("/admin-requests")}>
            <h3>{pendingRequests.length}</h3>
            <p>Pending Requests</p>
          </div>
          <div className="admin-card clickable" onClick={() => handleCardClick("/admin-requests")}>
            <h3>{returnedCount}</h3>
            <p>Returned</p>
          </div>
        </div>

        <div className="admin-columns">
          {/* Recent Activity */}
          <div className="admin-activity-box clickable" onClick={() => handleCardClick("/admin-requests")}>
            <h3>Recent Activity</h3>
            <ul>
              {recentActivities.map((log) => (
                <li key={log.id}>
                  {log.text} - <span>{log.date}</span>
                </li>
              ))}
            </ul>
          </div>

          {/* Pending Approval */}
          <div className="admin-pending-box clickable" onClick={() => handleCardClick("/admin-requests")}>
            <h3>Pending Approvals</h3>
            {pendingRequests.length > 0 ? (
              pendingRequests.map((req) => (
                <div key={req.id} className="pending-request">
                  <p><strong>{req.itemName}</strong> - {req.borrowerName}</p>
                  <span>{req.requestDate}</span>
                  <div className="review-btn-row">
                    <button className="review-request-btn" onClick={(e) => handleReview(e, req.id)}>
                      Review Request
                    </button>
                  </div>
                </div>
              ))
            ) : (
              <p>No pending requests.</p>
            )}
          </div>
        </div>
      </div>
    </div>
  );
};

export default AdminDashboard;
