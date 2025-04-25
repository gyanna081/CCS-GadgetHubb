import React, { useState, useEffect } from "react";
import { useParams, Link, useLocation } from "react-router-dom";
import { Carousel } from "react-responsive-carousel";
import "react-responsive-carousel/lib/styles/carousel.min.css";
import logo from "../../assets/CCSGadgetHub1.png";

const navLinks = [
  { label: "Dashboard", to: "/userdashboard" },
  { label: "Items", to: "/useritems" },
  { label: "My Requests", to: "/my-requests" },
  { label: "Activity Log", to: "/activity-log" },
  { label: "Profile", to: "/userprofile" },
];

const ItemDetails = () => {
  const { itemId } = useParams();
  const [item, setItem] = useState(null);
  const location = useLocation();

  useEffect(() => {
    const fetchedItems = [
      {
        id: 1,
        name: "Laptop 1",
        available: true,
        rating: 5,
        images: ["image1.jpg", "image2.jpg"],
        description: "A powerful laptop for all your needs.",
        condition: "Good",
        conditionDescription: "Well-maintained, minor scratches on the body.",
      },
      {
        id: 2,
        name: "Laptop 2",
        available: true,
        rating: 4,
        images: ["image3.jpg", "image4.jpg"],
        description: "A lightweight laptop for everyday use.",
        condition: "Excellent",
        conditionDescription: "Barely used, no visible damage.",
      },
    ];

    const itemData = fetchedItems.find((item) => item.id === parseInt(itemId));
    setItem(itemData);
  }, [itemId]);

  if (!item) {
    return <div>Loading...</div>;
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
              className={
                location.pathname.replace(/\/$/, "") === link.to.replace(/\/$/, "")
                  ? "navbar-link active-link"
                  : "navbar-link"
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

      {/* Main Content */}
      <div className="item-details-section">
        {/* Back Arrow */}
        <Link to="/useritems" className="back-arrow">&#8592;</Link>

        {/* Image Carousel */}
        <div className="carousel-wrapper">
          <Carousel showThumbs={false}>
            {item.images.map((image, index) => (
              <div key={index}>
                <img src={`../assets/${image}`} alt={`Item ${index + 1}`} />
              </div>
            ))}
          </Carousel>
        </div>

        {/* Info Section */}
        <div className="details-wrapper">
          <div className="details-left">
            <h2>{item.name}</h2>
            <p className="item-description">{item.description}</p>

            <h3>Condition</h3>
            <p>{item.condition}: {item.conditionDescription}</p>
          </div>

          <div className="details-right">
            <h3>Status:</h3>
            <p className={`status-text ${item.available ? 'available' : 'not-available'}`}>
              {item.available ? "Available" : "Not Available"}
            </p>

            {item.available ? (
              <Link to={`/borrow/${item.id}`} className="borrow-btn">Borrow Item</Link>
            ) : (
              <button disabled className="borrow-btn not-available-btn">Not Available</button>
            )}
          </div>
        </div>
      </div>
    </div>
  );
};

export default ItemDetails;
