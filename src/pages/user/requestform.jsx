import React, { useState, useEffect } from "react";
import { Link, useLocation, useParams } from "react-router-dom";
import logo from "../../assets/CCSGadgetHub1.png";

const navLinks = [
  { label: "Dashboard", to: "/userdashboard" },
  { label: "Items", to: "/useritems" },
  { label: "My Requests", to: "/my-requests" },
  { label: "Activity Log", to: "/activity-log" },
  { label: "Profile", to: "/userprofile" },
];

const RequestForm = () => {
  const location = useLocation();
  const { itemId } = useParams();
  const [itemName] = useState(`Laptop ${itemId}`);
  const [reason, setReason] = useState("");
  const [fromTime, setFromTime] = useState("");
  const [toTime, setToTime] = useState("");
  const [estimatedReturn, setEstimatedReturn] = useState("");
  const [agree, setAgree] = useState(false);

  const [showConfirmModal, setShowConfirmModal] = useState(false);
  const [showSuccessModal, setShowSuccessModal] = useState(false);

  useEffect(() => {
    if (fromTime && toTime) {
      const today = new Date();
      const [toHours, toMinutes] = toTime.split(":").map(Number);
      const toDate = new Date(today);
      toDate.setHours(toHours, toMinutes, 0, 0);
      const returnDate = new Date(toDate.getTime() + 30 * 60 * 1000);

      const endOfDay = new Date(today);
      endOfDay.setHours(23, 59, 0, 0);
      if (returnDate > endOfDay) {
        returnDate.setHours(23, 59, 0, 0);
      }

      const options = {
        weekday: "long",
        year: "numeric",
        month: "long",
        day: "numeric",
        hour: "2-digit",
        minute: "2-digit",
      };
      setEstimatedReturn(returnDate.toLocaleString(undefined, options));
    } else {
      setEstimatedReturn("");
    }
  }, [fromTime, toTime]);

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
          <Link to="/" className="logout-link">Log Out</Link>
        </div>
      </div>

      {/* Form Section */}
      <div className="request-form-page">
        <div style={{ width: '100%', maxWidth: '800px', margin: '0 auto' }}>
          <Link to="/useritems" className="back-arrow">←</Link>
        </div>

        <h2 className="request-form-title">Request Form</h2>

        <form className="request-form-container" onSubmit={(e) => e.preventDefault()}>
          {/* Item Name */}
          <div className="input-row full-width">
            <label>Item Name:</label>
            <input type="text" value={itemName} disabled />
          </div>

          {/* Reason */}
          <div className="input-row full-width">
            <label>Reason for borrowing:</label>
            <textarea
              placeholder="State reason here..."
              value={reason}
              onChange={(e) => setReason(e.target.value)}
              rows="3"
            />
          </div>

          {/* Duration and Return Time */}
          <div className="input-row">
            <div className="full-width">
              <label>Duration:</label>
              <div style={{ display: "flex", gap: "10px" }}>
                <input type="time" value={fromTime} onChange={(e) => setFromTime(e.target.value)} />
                <span style={{ alignSelf: "center" }}>to</span>
                <input type="time" value={toTime} onChange={(e) => setToTime(e.target.value)} />
              </div>
            </div>
            <div className="full-width">
              <label>Return Time:</label>
              <div className="estimated-return">
                {estimatedReturn || "Estimated Return Date and Time Calculation here"}
              </div>
            </div>
          </div>

          {/* Terms and Agreement */}
          <div className="input-row full-width">
            <label>Terms and Agreement</label>
            <div style={{ fontSize: "14px", color: "#444" }}>
              <p>Terms and Agreement here..</p>
              <p>Terms and Agreement here..</p>
              <p>Terms and Agreement here..</p>
              <p>Terms and Agreement here..</p>
            </div>

            <div className="checkbox-row">
              <label htmlFor="agree">
                <input
                  type="checkbox"
                  id="agree"
                  checked={agree}
                  onChange={() => setAgree(!agree)}
                />
                I agree to the terms and conditions.
              </label>
            </div>
          </div>

          {/* Submit Button */}
          <div style={{ display: "flex", justifyContent: "flex-end", marginTop: "20px" }}>
            <button
              type="button"
              className="submit-btn"
              disabled={!agree}
              onClick={() => setShowConfirmModal(true)}
            >
              Submit Request
            </button>
          </div>
        </form>
      </div>

      {/* Confirmation Modal */}
      {showConfirmModal && (
        <div className="modal-overlay">
          <div className="modal">
            <h3><strong>{itemName}</strong></h3>
            <p>{reason || "No reason provided."}</p>
            <p><strong>Duration:</strong><br />{fromTime || "--:--"} - {toTime || "--:--"}</p>
            <p><strong>Return Time:</strong><br />{estimatedReturn || "--:--"}</p>

            <div className="checkbox-row" style={{ marginTop: "10px" }}>
              <label>
                <input type="checkbox" checked readOnly />
                I agree to return the item in good condition and follow the borrowing policies.
              </label>
            </div>

            <div className="modal-actions-centered">
              <button className="confirm-btn" onClick={() => {
                setShowConfirmModal(false);
                setShowSuccessModal(true);
              }}>
                Confirm Request
              </button>
              <button className="cancel-btn centered-cancel" onClick={() => setShowConfirmModal(false)}>
                Cancel
              </button>
            </div>
          </div>
        </div>
      )}


      {/* Success Modal */}
      {showSuccessModal && (
        <div className="modal-overlay">
          <div className="modal">
            <h2 style={{ color: "#d96528", textAlign: "center" }}>
              Your Request is<br />submitted successfully!
            </h2>
            <p style={{ textAlign: "center", marginTop: "10px" }}>
              Check the status in the <strong>"My Requests"</strong> section.
            </p>
            <div style={{ textAlign: "center", marginTop: "20px" }}>
              <Link to="/useritems" className="back-link">Back to Items Page</Link>
            </div>
          </div>
        </div>
      )}

    </div>
  );
};

export default RequestForm;
