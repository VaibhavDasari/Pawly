import {
  CalendarDays,
  ClipboardList,
  HeartPulse,
  Pill,
  Salad,
  Syringe,
} from "lucide-react";
import "./Features.css";

const features = [
  {
    icon: ClipboardList,
    title: "Pet Profiles",
    description:
      "Keep your pet's important information, records, and health history organized in one place.",
    className: "feature-green",
  },
  {
    icon: Syringe,
    title: "Vaccinations",
    description:
      "Never miss an important vaccination with reminders and an easy-to-follow vaccination history.",
    className: "feature-yellow",
  },
  {
    icon: CalendarDays,
    title: "Appointments",
    description:
      "Keep track of upcoming vet visits and important appointments without the paperwork.",
    className: "feature-blue",
  },
  {
    icon: Pill,
    title: "Medications",
    description:
      "Manage medications, dosage schedules, and treatment information for your pets.",
    className: "feature-purple",
  },
  {
    icon: Salad,
    title: "Diet & Nutrition",
    description:
      "Keep your pet's dietary requirements and feeding information easy to access.",
    className: "feature-orange",
  },
  {
    icon: HeartPulse,
    title: "Health & Wellness",
    description:
      "Keep an eye on your companion's overall wellness and access important health information.",
    className: "feature-pink",
  },
];

function Features() {
  return (
    <section className="features-section" id="features">
      <div className="features-container">

        <div className="section-heading">
          <div className="section-badge">
            <HeartPulse size={15} />
            <span>Everything in one place</span>
          </div>

          <h2>
            Care made simpler.
            <span> For both of you.</span>
          </h2>

          <p>
            Pawly brings your pet's everyday care and health information
            together, so you can spend less time organizing and more time
            caring.
          </p>
        </div>

        <div className="features-grid">
          {features.map((feature) => {
            const Icon = feature.icon;

            return (
              <div
                className={`feature-card ${feature.className}`}
                key={feature.title}
              >
                <div className="feature-icon">
                  <Icon size={25} strokeWidth={2} />
                </div>

                <h3>{feature.title}</h3>

                <p>{feature.description}</p>

                <div className="feature-paw">🐾</div>
              </div>
            );
          })}
        </div>

      </div>
    </section>
  );
}

export default Features;