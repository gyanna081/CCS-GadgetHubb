import React, { useState, useEffect } from "react";
import { Link, useLocation } from "react-router-dom";
import logo from "../../assets/CCSGadgetHub1.png";

const ActivityLog = () => {
  const location = useLocation();
  const [searchTerm, setSearchTerm] = useState("");
  const [typeFilter, setTypeFilter] = useState("all");
  const [dateFilter, setDateFilter] = useState("");
  const [logs, setLogs] = useState([]);

  useEffect(() => {
    const dummyLogs = [
      {
        id: 1,
        type: "Borrow",
        item: "Dell Laptop",
        date: "2025-04-21",
        time: "10:00 AM - 3:00 PM",
        description: "Project Presentation"
      },
      {
        id: 2,
        type: "Return",
        item: "Huawei Matebook",
        date: "2025-04-21",
        time: "3:45 PM",
        description: "Item returned in good condition."
      },
      {
        id: 3,
        type: "Cancel",
        item: "Macbook Air",
        date: "2025-04-20",
        time: "11:00 AM",
        description: "Request canceled by user."
      }
    ];
    setLogs(dummyLogs);
  }, []);

  const filteredLogs = logs.filter((log) => {
    const matchesSearch =
      log.item.toLowerCase().includes(searchTerm.toLowerCase()) ||
      log.description.toLowerCase().includes(searchTerm.toLowerCase()) ||
      log.type.toLowerCase().includes(searchTerm.toLowerCase());

    const matchesType = typeFilter === "all" || log.type.toLowerCase() === typeFilter;
    const matchesDate = dateFilter === "" || log.date === dateFilter;

    return matchesSearch && matchesType && matchesDate;
  });

  return (
    <div className="items-page">
      {/* Navbar */}
      <div className="navbar">
        <img src={logo} alt="CCS Gadget Hub Logo" />
        <nav>
          {[
            { label: "Dashboard", to: "/userdashboard" },
            { label: "Items", to: "/useritems" },
            { label: "My Requests", to: "/my-requests" },
            { label: "Activity Log", to: "/activity-log" },
            { label: "Profile", to: "/userprofile" },
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

      {/* Activity Log */}
      <div className="dashboard-container activity-log-page">
        <h2 className="featured-title">Activity Log</h2>

        <div className="activity-filters">
          <input
            className="activity-search"
            type="text"
            placeholder="Search activity..."
            value={searchTerm}
            onChange={(e) => setSearchTerm(e.target.value)}
          />
          <select
            className="activity-dropdown"
            value={typeFilter}
            onChange={(e) => setTypeFilter(e.target.value)}
          >
            <option value="all">All</option>
            <option value="borrow">Borrow</option>
            <option value="return">Return</option>
            <option value="cancel">Cancel</option>
          </select>
          <input
            className="activity-date-filter"
            type="date"
            value={dateFilter}
            onChange={(e) => setDateFilter(e.target.value)}
          />
        </div>

        <div className="log-cards-container">
          {filteredLogs.map((log) => (
            <div className={`log-card ${log.type.toLowerCase()}`} key={log.id}>
              <div className="log-header">
                <span className="log-type">{log.type}</span>
                <span>{log.date}</span>
              </div>
              <div className="log-details">
                <p><strong>{log.item}</strong></p>
                <p>{log.time}</p>
                <p>{log.description}</p>
              </div>
            </div>
          ))}
        </div>
      </div>
    </div>
  );
};

export default ActivityLog;
