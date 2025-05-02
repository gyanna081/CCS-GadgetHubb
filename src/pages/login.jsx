import { useState } from "react";
import { auth, provider } from "../firebaseconfig";
import { signInWithEmailAndPassword, signInWithPopup } from "firebase/auth";
import { useNavigate, Link } from "react-router-dom";
import axios from "axios";
import logo from "../assets/CCSGadgetHub.png"; // adjust path if needed

const Login = () => {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [error, setError] = useState("");
  const navigate = useNavigate();

  const redirectBasedOnRole = async (uid) => {
    try {
      const res = await axios.get(`http://localhost:8080/api/sync/get-by-uid?uid=${uid}`);
      const user = res.data;
      if (user.role === "admin") {
        navigate("/admindashboard");
      } else {
        navigate("/dashboard");
      }
    } catch (err) {
      console.error("Error in redirectBasedOnRole:", err);
      setError("Error fetching user role. Please try again.");
    }
  };

  const handleLogin = async (e) => {
    e.preventDefault();
    setError("");
    try {
      const userCredential = await signInWithEmailAndPassword(auth, email, password);
      const user = userCredential.user;

      const displayName = user.displayName || "Unnamed User";
      const nameParts = displayName.split(" ");
      const firstName = nameParts[0] || "Unnamed";
      const lastName = nameParts.slice(1).join(" ") || "";

      await axios.post("http://localhost:8080/api/sync/user", {
        uid: user.uid,
        email: user.email,
        firstName,
        lastName,
      });

      redirectBasedOnRole(user.uid);
    } catch (err) {
      console.error("Login error:", err);
      if (err.code === "auth/invalid-credential" || err.code === "auth/user-not-found") {
        setError("Invalid email or password");
      } else {
        setError(typeof err === "string" ? err : err.message || "Login failed");
      }
    }
  };

  const handleGoogleLogin = async () => {
    try {
      const result = await signInWithPopup(auth, provider);
      const user = result.user;

      const displayName = user.displayName || "Unnamed User";
      const nameParts = displayName.split(" ");
      const firstName = nameParts[0] || "Unnamed";
      const lastName = nameParts.slice(1).join(" ") || "";

      await axios.post("http://localhost:8080/api/sync/user", {
        uid: user.uid,
        email: user.email,
        firstName,
        lastName,
      });

      redirectBasedOnRole(user.uid);
    } catch (err) {
      console.error("Google login error:", err);
      setError(typeof err === "string" ? err : err.message || "Google Sign-In failed");
    }
  };

  return (
    <div className="login-page">
      <img src={logo} alt="CCS Gadget Hub Logo" className="login-logo" />
      <form className="login-form-container" onSubmit={handleLogin}>
        {error && <div className="login-error">{error}</div>}

        <input
          type="email"
          className="login-input"
          placeholder="Email"
          value={email}
          onChange={(e) => setEmail(e.target.value)}
          required
        />
        <input
          type="password"
          className="login-input"
          placeholder="Password"
          value={password}
          onChange={(e) => setPassword(e.target.value)}
          required
        />

        <button type="submit" className="login-button">Log In</button>

        <button type="button" className="google-button" onClick={handleGoogleLogin}>
          Sign in with Google
        </button>

        <div className="login-register">
          No account yet? <Link to="/signup">Register here</Link>
        </div>
      </form>
    </div>
  );
};

export default Login;
