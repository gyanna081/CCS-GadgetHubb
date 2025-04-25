import { BrowserRouter as Router, Routes, Route } from "react-router-dom";

import Login from "./pages/login";

// User pages
import Dashboard from "./pages/user/dashboard";
import Items from "./pages/user/items";
import ItemDetails from "./pages/user/itemdetails";
import RequestForm from "./pages/user/requestform";
import Register from './pages/user/register';
import Profile from "./pages/user/profile";
import EditProfile from "./pages/user/editprofile";
import MyRequests from "./pages/user/my-requests";
import ViewRequest from "./pages/user/view-request";
import ActivityLog from "./pages/user/activity-log";

// Admin pages

import AdminDashboard from "./pages/admin/admin-dashboard";




function App() {
  return (
    <Router>
      <Routes>
        {/* User Routes */}
        <Route path="/" element={<Login />} />
        <Route path="/userdashboard" element={<Dashboard />} />
        <Route path="/useritems" element={<Items />} />
        <Route path="/useritem-details/:itemId" element={<ItemDetails />} /> {/* Dynamic route for item details */}
        <Route path="/borrow/:itemId" element={<RequestForm />} />
        <Route path="/register" element={<Register />} />
        <Route path="/userprofile" element={<Profile />} />
        <Route path="/usereditprofile" element={<EditProfile />} />
        <Route path="/my-requests" element={<MyRequests />} />
        <Route path="/view-request/:id" element={<ViewRequest />} />
        <Route path="/activity-log" element={<ActivityLog />} />

        {/* Admin Routes */}
        <Route path="/admin-dashboard" element={<AdminDashboard />} />

      </Routes>
    </Router>
  );
}

export default App;
