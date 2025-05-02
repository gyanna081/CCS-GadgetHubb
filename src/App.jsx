import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import Login from "./pages/login";
import Dashboard from "./pages/dashboard";
import Items from "./pages/items";
import AdminDashboard from "./pages/AdminDashboard";
import ItemDetails from "./pages/itemdetails";
import MyRequests from "./pages/my-requests";
import Profile from "./pages/profile";
import EditProfile from "./pages/edit-profile";
import RequestForm from "./pages/requestform";
import Signup from "./pages/signup";
import ViewRequests from "./pages/view-requests";
import "./index.css";

function App() {
  return (
    <Router>
      <Routes>
        <Route path="/" element={<Login />} />
        <Route path="/signup" element={<Signup />} />
        <Route path="/dashboard" element={<Dashboard />} />
        <Route path="/items" element={<Items />} />
        <Route path="/itemdetails/:id" element={<ItemDetails />} />
        <Route path="/borrow/:itemId" element={<RequestForm />} /> {/* Changed to match your link URL and include parameter */}
        <Route path="/admindashboard" element={<AdminDashboard />} />
        <Route path="/my-requests" element={<MyRequests />} />
        <Route path="/profile" element={<Profile />} />
        <Route path="/edit-profile" element={<EditProfile />} /> {/* Added route for edit profile page */}
        <Route path="/view-requests" element={<ViewRequests />} />
      </Routes>
    </Router>
  );
}

export default App;