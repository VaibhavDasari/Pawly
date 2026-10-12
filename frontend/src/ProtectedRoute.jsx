import { useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { useAuth } from "./auth-context";
export default function ProtectedRoute({ children }) {
  const { user, ready } = useAuth(); const navigate = useNavigate();
  useEffect(() => { if (ready && !user) navigate("/login", { replace: true }); }, [ready, user, navigate]);
  if (!ready || !user) return <main className="app-state">{ready ? "Redirecting to login…" : "Loading your Pawly account…"}</main>;
  return children;
}
