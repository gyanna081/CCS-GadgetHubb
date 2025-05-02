  import { useState } from "react";
  import { auth, provider } from "../firebaseconfig";
  import { signInWithEmailAndPassword, signInWithPopup } from "firebase/auth";
  import { useNavigate, Link } from "react-router-dom"; // <-- import Link here
  import axios from "axios";
  import logo from "../assets/CCSGadgetHub.png";

  const Login = () => {
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const [error, setError] = useState("");
    const navigate = useNavigate();

    const redirectBasedOnRole = async (uid) => {
      try {
        const res = await axios.get(`http://localhost:8080/api/sync/get-by-uid?uid=${uid}`);
        const user = res.data;
        console.log("User data from backend:", user);
        console.log("User role:", user.role);
        
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
        const nameParts = displayName.split(' ');
        const firstName = nameParts[0] || "Unnamed";
        const lastName = nameParts.slice(1).join(' ') || "";

        await axios.post("http://localhost:8080/api/sync/user", {
          uid: user.uid,
          email: user.email,
          firstName,
          lastName
        }).catch(error => {
          console.error("Sync user error:", error.response?.data);
          throw error;
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
        const nameParts = displayName.split(' ');
        const firstName = nameParts[0] || "Unnamed";
        const lastName = nameParts.slice(1).join(' ') || "";

        await axios.post("http://localhost:8080/api/sync/user", {
          uid: user.uid,
          email: user.email,
          firstName,
          lastName
        }).catch(error => {
          console.error("Sync user error:", error.response?.data);
          throw error;
        });

        redirectBasedOnRole(user.uid);
      } catch (err) {
        console.error("Google login error:", err);
        setError(typeof err === "string" ? err : err.message || "Google Sign-In failed");
      }
    };

    return (
      <div style={{
        display: "flex", justifyContent: "center", alignItems: "center",
        height: "100vh", width: "100vw", background: "#545454"
      }}>
        <div style={{
          background: "#ffffff", padding: "3rem", borderRadius: "12px",
          boxShadow: "0px 8px 24px rgba(0, 0, 0, 0.2)", textAlign: "center",
          width: "400px", maxWidth: "90%"
        }}>
          <div style={{ display: "flex", justifyContent: "center", marginBottom: "24px" }}>
            <img src={logo} alt="CCS GadgetHub Logo" style={{ width: "200px", height: "auto" }} />
          </div>

          <h2 style={{ color: "#000", fontSize: "26px", fontWeight: "600", marginBottom: "10px" }}>
            Welcome Back
          </h2>
          <p style={{ color: "#545454", fontSize: "18px", marginBottom: "28px" }}>
            Sign in to your account
          </p>

          {error && (
            <div style={{
              backgroundColor: "rgba(226, 105, 1, 0.1)", color: "#E26901", fontSize: "14px",
              padding: "12px", borderRadius: "6px", marginBottom: "16px"
            }}>
              {typeof error === "string" ? error : (error.message || "An error occurred")}
            </div>
          )}

          <form onSubmit={handleLogin}>
            <div style={{ marginBottom: "16px", textAlign: "left" }}>
              <label style={{ display: "block", marginBottom: "6px", color: "#545454" }}>Email</label>
              <input
                type="email"
                value={email}
                onChange={(e) => setEmail(e.target.value)}
                placeholder="Enter your email"
                required
                style={{ width: "100%", padding: "14px", borderRadius: "8px",
                        border: "1px solid #ccc", fontSize: "16px" }}
              />
            </div>

            <div style={{ marginBottom: "16px", textAlign: "left" }}>
              <label style={{ display: "block", marginBottom: "6px", color: "#545454" }}>Password</label>
              <input
                type="password"
                value={password}
                onChange={(e) => setPassword(e.target.value)}
                placeholder="Enter your password"
                required
                style={{ width: "100%", padding: "14px", borderRadius: "8px",
                        border: "1px solid #ccc", fontSize: "16px" }}
              />
            </div>

            <button type="submit" style={{
              width: "100%", padding: "14px", background: "#E26901", color: "white",
              fontSize: "16px", fontWeight: "600", borderRadius: "8px", border: "none",
              cursor: "pointer"
            }}>
              Sign In
            </button>
          </form>

          <div style={{ display: "flex", alignItems: "center", margin: "24px 0" }}>
            <span style={{ flex: 1, borderBottom: "1px solid #ccc" }}></span>
            <span style={{ margin: "0 10px", color: "#909090", fontSize: "14px" }}>or</span>
            <span style={{ flex: 1, borderBottom: "1px solid #ccc" }}></span>
          </div>

          <button onClick={handleGoogleLogin} style={{
            width: "100%", padding: "14px", background: "#fff", color: "#545454",
            border: "1px solid #ccc", borderRadius: "8px", fontSize: "16px", fontWeight: "600",
            cursor: "pointer"
          }}>
            Sign in with Google
          </button>

          <div style={{ marginTop: "20px", fontSize: "16px", color: "#545454" }}>
            Don't have an account?{" "}
            <Link to="/signup" style={{ color: "#E26901", fontWeight: "600", cursor: "pointer", textDecoration: "underline" }}>
              Sign Up
            </Link>
          </div>
        </div>
      </div>
    );
  };

  export default Login;
