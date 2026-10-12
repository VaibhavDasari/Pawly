import { LogOut } from "lucide-react";
import { useNavigate } from "react-router-dom";
import { useAuth } from "./auth-context";
export default function SessionActions() {
  const { user, logout } = useAuth(); const navigate = useNavigate();
  return <div className="session-actions"><span>{user?.name}</span><button type="button" onClick={() => { logout(); navigate("/", { replace: true }); }}><LogOut size={16}/>Log out</button></div>;
}
