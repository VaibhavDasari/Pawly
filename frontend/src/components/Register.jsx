import { useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import { ArrowLeft, Heart, PawPrint } from "lucide-react";
import { authApi } from "../api";
import { useAuth } from "../auth-context";
import "./Register.css";
function Register() {
  const [form, setForm] = useState({ name: "", email: "", password: "", confirm: "" });
  const [error, setError] = useState(""); const [busy, setBusy] = useState(false);
  const { login } = useAuth(); const navigate = useNavigate();
  function change(e) { setForm({ ...form, [e.target.name]: e.target.value }); }
  async function submit(e) { e.preventDefault(); setError(""); if (form.password !== form.confirm) { setError("The passwords do not match."); return; } setBusy(true); try { await authApi.register({ name: form.name, email: form.email, password: form.password }); await login({ email: form.email, password: form.password }); navigate("/dashboard", { replace: true }); } catch (err) { setError(err.message || "Unable to create your account."); } finally { setBusy(false); } }
  return <div className="auth-page"><div className="auth-decoration auth-decoration-one"><PawPrint size={70} /></div><div className="auth-decoration auth-decoration-two"><PawPrint size={45} /></div><div className="auth-card register-card">
    <Link to="/" className="auth-back"><ArrowLeft size={16} />Back to Pawly</Link><div className="auth-logo"><div className="auth-logo-icon"><PawPrint size={22} /></div><span>Pawly</span></div>
    <div className="auth-heading"><h1>Let's get started! 🐾</h1><p>Create your Pawly account and start caring smarter.</p></div>
    <form className="auth-form" onSubmit={submit}><div className="form-group"><label htmlFor="name">Full name</label><input id="name" name="name" placeholder="Your name" value={form.name} onChange={change} required autoComplete="name" /></div>
      <div className="form-group"><label htmlFor="email">Email address</label><input id="email" name="email" type="email" placeholder="you@example.com" value={form.email} onChange={change} required autoComplete="email" /></div>
      <div className="form-group"><label htmlFor="password">Password</label><input id="password" name="password" type="password" placeholder="Create a password" value={form.password} onChange={change} required minLength="12" autoComplete="new-password" /></div>
      <div className="form-group"><label htmlFor="confirm">Confirm password</label><input id="confirm" name="confirm" type="password" placeholder="Confirm your password" value={form.confirm} onChange={change} required minLength="12" autoComplete="new-password" /></div>
      {error && <p className="form-error" role="alert">{error}</p>}<button type="submit" className="auth-button" disabled={busy}>{busy ? "Creating account…" : "Create my account"}</button>
    </form><p className="auth-switch">Already have an account?<Link to="/login"> Log in</Link></p><div className="auth-footer"><Heart size={13} fill="currentColor" /><span>Made with love for pets</span></div>
  </div></div>;
}
export default Register;


