import React, { useState, useEffect } from "react";
import { Link, useLocation, useParams, useNavigate } from "react-router-dom";
import logo from "../../assets/CCSGadgetHub1.png";

const navLinks = [
  { label: "Dashboard", to: "/userdashboard" },
  { label: "Items", to: "/useritems" },
  { label: "My Requests", to: "/my-requests" },
  { label: "Profile", to: "/userprofile" },
];

const RequestForm = () => {
  const location = useLocation();
  const { itemId } = useParams();
  const navigate = useNavigate();

  const [itemName] = useState(`Laptop ${itemId}`);
  const [reason, setReason] = useState("");
  const [borrowDate, setBorrowDate] = useState("");
  const [startBlock, setStartBlock] = useState("");
  const [durationBlocks, setDurationBlocks] = useState("");
  const [returnTime, setReturnTime] = useState("");
  const [agree, setAgree] = useState(false);

  const [showConfirmModal, setShowConfirmModal] = useState(false);
  const [showSuccessModal, setShowSuccessModal] = useState(false);

  // Dummy unavailable periods
  const unavailablePeriods = [
    { start: 9.0, end: 10.0 },
    { start: 13.0, end: 15.0 },
    { start: 16.5, end: 17.5 },
  ];

  function generateBlocks() {
    const blocks = [];
    for (let time = 7.5; time <= 20.0; time += 0.5) {
      blocks.push({ value: time, label: formatBlockLabel(time) });
    }
    return blocks;
  }

  function formatBlockLabel(value) {
    const startHour = Math.floor(value);
    const startMin = value % 1 === 0.5 ? 30 : 0;
    const endValue = value + 0.5;
    const endHour = Math.floor(endValue);
    const endMin = endValue % 1 === 0.5 ? 30 : 0;
    return `${formatTime(startHour, startMin)} - ${formatTime(endHour, endMin)}`;
  }

  function formatTime(hour, minute) {
    const suffix = hour >= 12 ? "PM" : "AM";
    const formattedHour = ((hour + 11) % 12) + 1;
    const formattedMinute = minute === 0 ? "00" : "30";
    return `${formattedHour}:${formattedMinute} ${suffix}`;
  }

  function formatTimeRange(start, end) {
    const startHour = Math.floor(start);
    const startMin = start % 1 === 0.5 ? 30 : 0;
    const endHour = Math.floor(end);
    const endMin = end % 1 === 0.5 ? 30 : 0;
    return `${formatTime(startHour, startMin)} - ${formatTime(endHour, endMin)}`;
  }

  function isBlockUnavailable(time) {
    for (const period of unavailablePeriods) {
      if (time >= period.start && time < period.end) {
        return true;
      }
    }
    return false;
  }

  function getAvailableDurations(start) {
    const durations = [];
    let current = parseFloat(start);
    let count = 0;
    while (count < 6) {
      const nextBlock = current + 0.5;
      if (nextBlock > 21) break;
      if (isBlockUnavailable(current)) break;
      count++;
      durations.push(count * 0.5);
      current += 0.5;
    }
    return durations;
  }

  useEffect(() => {
    if (startBlock && durationBlocks) {
      const start = parseFloat(startBlock);
      const end = start + parseFloat(durationBlocks);
      if (end > 21) {
        setReturnTime("Invalid - exceeds 9:00 PM");
      } else {
        setReturnTime(formatTime(Math.floor(end), (end % 1 === 0.5 ? 30 : 0)));
      }
    } else {
      setReturnTime("");
    }
  }, [startBlock, durationBlocks]);

  const handleSubmit = (e) => {
    e.preventDefault();
    if (!borrowDate || !startBlock || !durationBlocks || returnTime.includes("Invalid")) {
      alert("Please complete all fields correctly.");
      return;
    }
    setShowConfirmModal(true);
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
          <Link to="/" className="logout-link">Log Out</Link>
        </div>
      </div>

      {/* Form */}
      <div className="request-form-page">
        <div style={{ width: '100%', maxWidth: '800px', margin: '0 auto' }}>
          <Link to="/useritems" className="back-arrow">←</Link>
        </div>

        <h2 className="request-form-title">Request Form</h2>

        <form className="request-form-container" onSubmit={handleSubmit}>
          {/* Item Name */}
          <div className="input-row full-width">
            <label>Item Name:</label>
            <input type="text" value={itemName} disabled />
          </div>

          {/* Borrow Date */}
          <div className="input-row full-width">
            <label>Date of Borrowing:</label>
            <input type="date" value={borrowDate} onChange={(e) => setBorrowDate(e.target.value)} required />
          </div>

          {/* Reason */}
          <div className="input-row full-width">
            <label>Reason for Borrowing:</label>
            <textarea
              placeholder="State reason here..."
              value={reason}
              onChange={(e) => setReason(e.target.value)}
              rows="3"
              required
            />
          </div>

          {/* Start Block and Duration */}
          <div className="input-row slot-select-row">
            <div className="full-width">
              <label>Select Time Slot (Start):</label>
              <select value={startBlock} onChange={(e) => { setStartBlock(e.target.value); setDurationBlocks(""); }} required>
                <option value="">-- Select Start Slot --</option>
                {generateBlocks().map(({ value, label }) => (
                  <option
                    key={value}
                    value={value}
                    disabled={isBlockUnavailable(value)}
                    style={{ color: isBlockUnavailable(value) ? "red" : "black" }}
                  >
                    {label} {isBlockUnavailable(value) ? "(Unavailable)" : ""}
                  </option>
                ))}
              </select>
            </div>

            <div className="full-width">
              <label>Select Duration:</label>
              <select value={durationBlocks} onChange={(e) => setDurationBlocks(e.target.value)} required disabled={!startBlock}>
                <option value="">-- Select Duration --</option>
                {startBlock &&
                  getAvailableDurations(parseFloat(startBlock)).map((duration, idx) => (
                    <option key={idx} value={duration}>
                      {duration === 0.5 ? "30 minutes" : `${Math.floor(duration)} hour${duration > 1 ? "s" : ""}${duration % 1 !== 0 ? " 30 minutes" : ""}`}
                    </option>
                  ))}
              </select>
            </div>
          </div>

          {/* Selected Time Slot */}
          {startBlock && durationBlocks && (
            <div className="selected-time-slot">
              Selected Time Slot: {formatTimeRange(parseFloat(startBlock), parseFloat(startBlock) + parseFloat(durationBlocks))}
            </div>
          )}

          {/* Return Time */}
          <div className="input-row full-width">
            <label>Expected Return Time:</label>
            <div className="estimated-return">
              {returnTime && !returnTime.includes("Invalid")
                ? `${returnTime} (+10 mins grace period)`
                : returnTime || "Return time will be shown here"}
            </div>
          </div>

          {/* Terms */}
          <div className="input-row full-width">
            <label>Terms and Agreement</label>
            <div style={{ fontSize: "14px", color: "#444" }}>
              <p>Borrowed items must be returned on or before the due time.</p>
              <p>Failure to return on time may result in penalties.</p>
              <p>Damaged items must be reported immediately.</p>
              <p>Borrowers must comply with all borrowing policies of the department.</p>
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

          {/* Submit */}
          <div style={{ display: "flex", justifyContent: "flex-end", marginTop: "20px" }}>
            <button
              type="submit"
              className="submit-btn"
              disabled={!agree}
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
            <p><strong>Borrow Date:</strong><br />{borrowDate}</p>
            <p><strong>Reason for Borrowing:</strong><br />{reason || "No reason provided."}</p>
            <p><strong>Selected Time Slot:</strong><br />
              {formatTimeRange(parseFloat(startBlock), parseFloat(startBlock) + parseFloat(durationBlocks))}
            </p>
            <p><strong>Return Time:</strong><br />
              {returnTime.includes("Invalid") ? "--" : returnTime} (+10 mins grace period)
            </p>

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
