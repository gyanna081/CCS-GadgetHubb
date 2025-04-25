import { useState } from "react";
import { auth, provider } from "../firebaseconfig";
import { signInWithEmailAndPassword, signInWithPopup } from "firebase/auth";
import { useNavigate, Link } from "react-router-dom";
import logo from "../assets/CCSGadgetHub.png";
import "../index.css";


const Login = () => {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [error, setError] = useState("");
  const navigate = useNavigate();

  const handleLogin = async (e) => {
    e.preventDefault();
    setError("");
    try {
      await signInWithEmailAndPassword(auth, email, password);
      navigate("/userdashboard");
    } catch (err) {
      setError(err.message || "Invalid email or password");
    }
  };

  const handleGoogleLogin = async () => {
    try {
      await signInWithPopup(auth, provider);
      navigate("/userdashboard");
    } catch (err) {
      setError(err.message || "Google Sign-In failed");
    }
  };

  return (
    <div className="login-page">
      <img src={logo} alt="CCS Gadget Hub Logo" className="login-logo" />

      <div className="login-form-container">
        {error && <div className="login-error">{error}</div>}

        <input
          type="email"
          placeholder="Email"
          value={email}
          onChange={(e) => setEmail(e.target.value)}
          required
          className="login-input"
        />

        <input
          type="password"
          placeholder="Password"
          value={password}
          onChange={(e) => setPassword(e.target.value)}
          required
          className="login-input"
        />

        <button onClick={handleLogin} className="login-button">Log In</button>

        <button onClick={handleGoogleLogin} className="google-button">Sign in with Google</button>

        <div className="login-register">
          No account yet? <Link to="/register">Register here</Link>
        </div>
      </div>
    </div>
  );
};

export default Login;
