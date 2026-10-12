import { ArrowRight, Heart, PawPrint } from "lucide-react";
import "./CTA.css";

function CTA() {
  return (
    <section className="cta-section">
      <div className="cta-container">

        <div className="cta-paws cta-paw-left">
          <PawPrint size={70} />
        </div>

        <div className="cta-paws cta-paw-right">
          <PawPrint size={45} />
        </div>

        <div className="cta-content">

          <div className="cta-icon">
            <Heart size={24} fill="currentColor" />
          </div>

          <h2>
            Because they deserve
            <span> the best care.</span>
          </h2>

          <p>
            Start your Pawly journey today and keep every important part of
            your pet's care in one happy little place.
          </p>

          <a href="/register" className="cta-button">
            Create your Pawly account
            <ArrowRight size={18} />
          </a>

          <div className="cta-note">
            <PawPrint size={14} />
            <span>Made for every wag, purr and paw.</span>
          </div>

        </div>

      </div>
    </section>
  );
}

export default CTA;