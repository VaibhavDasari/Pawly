import {
  ArrowRight,
  Heart,
  PawPrint,
  UserRoundPlus,
} from "lucide-react";
import "./HowItWorks.css";

const steps = [
  {
    number: "01",
    icon: UserRoundPlus,
    title: "Add your companion",
    description:
      "Create a profile for your pet with their name, breed, age, weight and important details.",
  },
  {
    number: "02",
    icon: PawPrint,
    title: "Organize their care",
    description:
      "Keep vaccinations, appointments, medications and everyday care information together.",
  },
  {
    number: "03",
    icon: Heart,
    title: "Keep them happy",
    description:
      "Stay on top of their health with reminders and quick access to everything that matters.",
  },
];

function HowItWorks() {
  return (
    <section className="how-section" id="how-it-works">
      <div className="how-container">

        <div className="how-heading">
          <div className="section-badge">
            <PawPrint size={15} />
            <span>Simple from day one</span>
          </div>

          <h2>
            Caring for your pet,
            <span> made easy.</span>
          </h2>

          <p>
            Pawly takes the little things off your mind so you can focus on
            what matters most — spending more happy moments with your
            companion.
          </p>
        </div>

        <div className="steps-wrapper">

          <div className="steps-line"></div>

          {steps.map((step, index) => {
            const Icon = step.icon;

            return (
              <div className="step" key={step.number}>

                <div className="step-number">
                  {step.number}
                </div>

                <div className="step-icon">
                  <Icon size={26} strokeWidth={2} />
                </div>

                <div className="step-content">
                  <h3>{step.title}</h3>

                  <p>{step.description}</p>
                </div>

                {index !== steps.length - 1 && (
                  <div className="step-arrow">
                    <ArrowRight size={20} />
                  </div>
                )}

              </div>
            );
          })}

        </div>

        <div className="how-bottom">
          <span>One place for every wag, purr and paw.</span>
          <span className="bottom-paw">
            <PawPrint size={18} />
          </span>
        </div>

      </div>
    </section>
  );
}

export default HowItWorks;