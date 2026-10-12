import { Heart, PawPrint } from "lucide-react";
import "./Footer.css";

function Footer() {
  return (
    <footer className="footer" id="about">
      <div className="footer-container">

        <div className="footer-main">

          {/* Brand */}
          <div className="footer-brand">
            <div className="footer-logo">
              <div className="footer-logo-icon">
                <PawPrint size={21} />
              </div>

              <span>Pawly</span>
            </div>

            <p>
              Simple, thoughtful care for the companions
              who make life a little happier.
            </p>

            <div className="footer-love">
              <Heart size={14} fill="currentColor" />
              Made with love for pets
            </div>
          </div>

          {/* Product */}
          <div className="footer-column">
            <h4>Product</h4>

            <a href="#features">Features</a>
            <a href="#how-it-works">How It Works</a>
            <a href="/register">Get Started</a>
            <a href="/login">Log in</a>
          </div>

          {/* Care */}
          <div className="footer-column">
            <h4>Pet Care</h4>

            <a href="#features">Health</a>
            <a href="#features">Vaccinations</a>
            <a href="#features">Appointments</a>
            <a href="#features">Nutrition</a>
          </div>

          {/* Company */}
          <div className="footer-column">
            <h4>Pawly</h4>

            <a href="#about">About Us</a>
            <a href="#how-it-works">How It Works</a>
            <a href="#features">Features</a>
          </div>

        </div>

        <div className="footer-bottom">
          <span>© 2026 Pawly. All rights reserved.</span>

          <div className="footer-paws">
            <PawPrint size={15} />
            <PawPrint size={11} />
            <PawPrint size={8} />
          </div>

          <span>For every wag, purr & paw.</span>
        </div>

      </div>
    </footer>
  );
}

export default Footer;