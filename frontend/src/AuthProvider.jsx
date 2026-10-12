import { useEffect, useState } from "react";
import { authApi, TOKEN_KEY } from "./api";
import { AuthContext } from "./auth-context";
export default function AuthProvider({ children }) {
  const [user, setUser] = useState(null);
  const [ready, setReady] = useState(() => !localStorage.getItem(TOKEN_KEY));
  useEffect(() => {
    if (!localStorage.getItem(TOKEN_KEY)) return;
    let active = true;
    authApi.me().then(value => { if (active) setUser(value); }).catch(() => { localStorage.removeItem(TOKEN_KEY); }).finally(() => { if (active) setReady(true); });
    return () => { active = false; };
  }, []);
  useEffect(() => { const expire = () => setUser(null); window.addEventListener("pawly:unauthorized", expire); return () => window.removeEventListener("pawly:unauthorized", expire); }, []);
  const login = async credentials => { const result = await authApi.login(credentials); localStorage.setItem(TOKEN_KEY, result.token); setUser({ id: result.userId, name: result.name, email: result.email }); };
  const logout = () => { localStorage.removeItem(TOKEN_KEY); setUser(null); };
  return <AuthContext.Provider value={{ user, ready, login, logout }}>{children}</AuthContext.Provider>;
}
