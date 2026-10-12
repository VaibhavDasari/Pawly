import { ArrowRight, Heart, PawPrint } from "lucide-react";
import "./Hero.css";

function Hero() {
  return (
    <section className="hero" id="home">
      <div className="hero-container">

        {/* Left Content */}
        <div className="hero-content">

          <div className="hero-badge">
            <PawPrint size={16} />
            <span>Everything your pet needs</span>
          </div>

          <h1>
            Better care for
            <span> happier pets.</span>
          </h1>

          <p>
            Keep your pet's health, appointments, vaccinations, medications
            and daily care organized — all in one simple place.
          </p>

          <div className="hero-buttons">
            <a href="/register" className="hero-primary-btn">
              Get Started
              <ArrowRight size={18} />
            </a>

            <a href="#features" className="hero-secondary-btn">
              Explore Pawly
            </a>
          </div>

          <div className="hero-trust">
            <div className="trust-icon">
              <Heart size={16} fill="currentColor" />
            </div>

            <div>
              <strong>Made with love for pets</strong>
              <span>Simple care. Happier companions.</span>
            </div>
          </div>

        </div>

        {/* Right Visual */}
        <div className="hero-visual">

          <div className="hero-circle"></div>

          <div className="paw-decoration paw-one">
            <PawPrint size={30} />
          </div>

          <div className="paw-decoration paw-two">
            <PawPrint size={20} />
          </div>

          <div className="pet-card">
            <div className="pet-placeholder">
              🐶
            </div>

            <div className="pet-info">
              <span>Meet your Pawly companion</span>
              <strong>Happy • Healthy • Loved</strong>
            </div>
          </div>

          <div className="floating-card vaccination-card">
            <div className="floating-icon">
              💉
            </div>

            <div>
              <span>Vaccination</span>
              <strong>Up to date ✓</strong>
            </div>
          </div>

          <div className="floating-card health-card">
            <div className="floating-icon">
              ❤️
            </div>

            <div>
              <span>Pet wellness</span>
              <strong>Looking good!</strong>
            </div>
          </div>

        </div>

      </div>
    </section>
  );
}

export default Hero;