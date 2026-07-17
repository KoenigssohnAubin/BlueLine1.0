import { Link } from 'react-router-dom';
import logo from '../assets/logo.png';
import './LandingPage.css';

const FEATURES = [
  {
    title: 'AI-Optimized Routing',
    desc: 'Every mission gets a computed route and live ETA, so control rooms can see exactly when an ambulance will arrive.',
    icon: (
      <path d="M3 12h4l3-8 4 16 3-8h4" />
    ),
  },
  {
    title: 'Live Ambulance Tracking',
    desc: 'Vehicle status and GPS position update in real time, from dispatch to hospital handoff.',
    icon: (
      <>
        <circle cx="12" cy="12" r="3" />
        <path d="M12 3v3M12 18v3M3 12h3M18 12h3" />
      </>
    ),
  },
  {
    title: 'Hospital Capacity Monitoring',
    desc: 'Department-level bed availability across the network, kept current so patients go where they can actually be treated.',
    icon: (
      <>
        <path d="M12 3v18M3 12h18" />
        <rect x="4" y="4" width="16" height="16" rx="2" />
      </>
    ),
  },
  {
    title: 'Incident Alerts',
    desc: 'Traffic, capacity and safety alerts are raised and dismissed as conditions change, visible to every role.',
    icon: (
      <>
        <path d="M12 2 2 20h20L12 2Z" />
        <path d="M12 10v4M12 17h.01" />
      </>
    ),
  },
  {
    title: 'Role-Based Access',
    desc: 'Paramedics, control room operators and administrators each see the tools relevant to their job — nothing more.',
    icon: (
      <>
        <circle cx="12" cy="8" r="4" />
        <path d="M4 21c0-4 3.5-7 8-7s8 3 8 7" />
      </>
    ),
  },
  {
    title: 'One System, Every Screen',
    desc: 'The mobile app for crews on the road and this console for control rooms share the exact same live data.',
    icon: (
      <>
        <rect x="3" y="4" width="12" height="16" rx="2" />
        <rect x="17" y="8" width="4" height="8" rx="1" />
      </>
    ),
  },
];

const STEPS = [
  {
    n: '01',
    title: 'Dispatch',
    desc: 'A control room operator logs the incident. The system suggests the nearest available ambulance and the best-matched hospital.',
  },
  {
    n: '02',
    title: 'Track',
    desc: 'The crew accepts the mission from the mobile app. Location, status and ETA update automatically for everyone watching.',
  },
  {
    n: '03',
    title: 'Deliver',
    desc: 'The patient is handed off at the hospital with live capacity data, and the mission is closed out with a full record.',
  },
];

function Icon({ children }) {
  return (
    <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="1.6" strokeLinecap="round" strokeLinejoin="round">
      {children}
    </svg>
  );
}

export default function LandingPage() {
  return (
    <div className="landing">
      <header className="landing-nav">
        <div className="landing-nav-inner">
          <span className="landing-brand">
            <img src={logo} alt="Blue Line IA" className="landing-logo" />
            Blue Line IA
          </span>
          <nav className="landing-nav-links">
            <a href="#features">Features</a>
            <a href="#how-it-works">How it works</a>
            <a href="#access">Access</a>
          </nav>
          <Link to="/login" className="landing-btn landing-btn-ghost">Sign in</Link>
        </div>
      </header>

      <section className="landing-hero">
        <div className="landing-hero-inner">
          <span className="landing-eyebrow">Ambulance dispatch, coordinated</span>
          <h1>AI-assisted ambulance dispatch for London</h1>
          <p className="landing-lede">
            Blue Line IA connects control rooms, paramedics and hospitals on one live system —
            so every dispatch decision is made with real-time information, not guesswork.
          </p>
          <div className="landing-cta-row">
            <Link to="/login" className="landing-btn landing-btn-primary">Open the console</Link>
            <a href="#how-it-works" className="landing-btn landing-btn-outline">See how it works</a>
          </div>
        </div>
        <div className="landing-hero-panel" aria-hidden="true">
          <div className="landing-mock-card">
            <div className="landing-mock-row landing-mock-row-header">
              <span>MIS-2026-1847</span>
              <span className="landing-mock-badge">EN COURS</span>
            </div>
            <div className="landing-mock-row"><span>Cardiac Arrest</span><span>ETA 6 min</span></div>
            <div className="landing-mock-row"><span>Royal London Hospital</span><span>3.7 km</span></div>
          </div>
          <div className="landing-mock-card landing-mock-card-secondary">
            <div className="landing-mock-row landing-mock-row-header">
              <span>King's College Hospital</span>
              <span className="landing-mock-badge landing-mock-badge-warn">At capacity</span>
            </div>
            <div className="landing-mock-row"><span>Alternative recommended</span></div>
          </div>
        </div>
      </section>

      <section className="landing-section" id="features">
        <div className="landing-section-inner">
          <h2>Everything a dispatch team needs, in one place</h2>
          <p className="landing-section-lede">
            Built around the missions, ambulances, hospitals and alerts that actually run a dispatch operation.
          </p>
          <div className="landing-features-grid">
            {FEATURES.map((f) => (
              <div className="landing-feature-card" key={f.title}>
                <div className="landing-feature-icon"><Icon>{f.icon}</Icon></div>
                <h3>{f.title}</h3>
                <p>{f.desc}</p>
              </div>
            ))}
          </div>
        </div>
      </section>

      <section className="landing-section landing-section-alt" id="how-it-works">
        <div className="landing-section-inner">
          <h2>How it works</h2>
          <div className="landing-steps">
            {STEPS.map((s) => (
              <div className="landing-step" key={s.n}>
                <span className="landing-step-n">{s.n}</span>
                <h3>{s.title}</h3>
                <p>{s.desc}</p>
              </div>
            ))}
          </div>
        </div>
      </section>

      <section className="landing-section" id="access">
        <div className="landing-section-inner">
          <h2>One console, three roles</h2>
          <p className="landing-section-lede">
            Everyone signs into the same console — what they see depends on what they do.
          </p>
          <div className="landing-roles">
            <div className="landing-role-card">
              <span className="landing-role-tag">Paramedic</span>
              <p>Accept and complete missions, update ambulance status and location from the field.</p>
            </div>
            <div className="landing-role-card">
              <span className="landing-role-tag">Control room</span>
              <p>Create missions, monitor the whole fleet, hospital capacity and live alerts.</p>
            </div>
            <div className="landing-role-card">
              <span className="landing-role-tag">Admin</span>
              <p>Everything above, plus user accounts and system-wide statistics.</p>
            </div>
          </div>
        </div>
      </section>

      <section className="landing-cta-band">
        <div className="landing-section-inner landing-cta-band-inner">
          <h2>Ready to take a look?</h2>
          <Link to="/login" className="landing-btn landing-btn-primary">Open the console</Link>
        </div>
      </section>

      <footer className="landing-footer">
        <div className="landing-section-inner landing-footer-inner">
          <span>Blue Line IA</span>
          <span className="landing-footer-credit">UWTSD Student Project : Emerging Trends — Birmingham</span>
        </div>
      </footer>
    </div>
  );
}
