import { PawPrint } from "lucide-react";
import "./Navbar.css";

function Navbar() {
  return (
    <nav className="navbar">
      <div className="navbar-container">

        {/* Logo */}
        <a href="/" className="logo">
          <div className="logo-icon">
            <PawPrint size={22} strokeWidth={2.5} />
          </div>

          <span>Pawly</span>
        </a>

        {/* Navigation */}
        <div className="nav-links">
          <a href="#home">Home</a>
          <a href="#features">Features</a>
          <a href="#how-it-works">How It Works</a>
          <a href="#about">About</a>
        </div>

        {/* Actions */}
        <div className="nav-actions">
          <a href="/login" className="login-link">
            Log in
          </a>

          <a href="/register" className="get-started-btn">
            Get Started
            <PawPrint size={17} strokeWidth={2.5} />
          </a>
        </div>

      </div>
    </nav>
  );
}

export default Navbar;