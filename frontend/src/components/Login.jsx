import { useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import { PawPrint, ArrowLeft, Heart } from "lucide-react";
import { useAuth } from "../auth-context";
import "./Login.css";
function Login() {
  const { login } = useAuth(); const navigate = useNavigate();
  const [email, setEmail] = useState(""); const [password, setPassword] = useState("");
  const [error, setError] = useState(""); const [busy, setBusy] = useState(false);
  async function submit(event) { event.preventDefault(); setError(""); setBusy(true); try { await login({ email, password }); navigate("/dashboard", { replace: true }); } catch (err) { setError(err.isNetworkError ? "Unable to connect to the server. Please try again." : err.status === 401 ? "Invalid email or password. Please try again." : err.status >= 500 || !err.status ? "Something went wrong. Please try again later." : err.message || "Unable to log in. Please try again."); } finally { setBusy(false); } }
  return <div className="auth-page"><div className="auth-decoration auth-decoration-one"><PawPrint size={70} /></div><div className="auth-decoration auth-decoration-two"><PawPrint size={45} /></div><div className="auth-card">
    <Link to="/" className="auth-back"><ArrowLeft size={16} />Back to Pawly</Link><div className="auth-logo"><div className="auth-logo-icon"><PawPrint size={22} /></div><span>Pawly</span></div>
    <div className="auth-heading"><h1>Welcome back! 🐾</h1><p>Log in to keep taking care of your furry companion.</p></div>
    <form className="auth-form" onSubmit={submit}><div className="form-group"><label htmlFor="email">Email address</label><input id="email" type="email" placeholder="you@example.com" value={email} onChange={e => setEmail(e.target.value)} required autoComplete="email" /></div>
      <div className="form-group"><label htmlFor="password">Password</label><input id="password" type="password" placeholder="Enter your password" value={password} onChange={e => setPassword(e.target.value)} required autoComplete="current-password" /></div>
      {error && <p className="form-error" role="alert">{error}</p>}<button type="submit" className="auth-button" disabled={busy}>{busy ? "Logging in…" : "Log in"}</button>
    </form><p className="auth-switch">Don't have an account?<Link to="/register"> Create one</Link></p><div className="auth-footer"><Heart size={13} fill="currentColor" /><span>Made with love for pets</span></div>
  </div></div>;
}
export default Login;

