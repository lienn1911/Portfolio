const projectCards = {
  "project-advanced-programming.html": {
    no: "01",
    type: "Java App",
    title: "SparkFit Fitness App",
    desc: "JavaFX fitness app with login, profile, workout logging, nutrition, goals and progress charts.",
    tags: ["Java", "JavaFX", "Maven"],
    highlight: true
  },
  "project-wireless-security.html": {
    no: "02",
    type: "Security Report",
    title: "Wireless Networks and Security",
    desc: "Secure wireless and enterprise network planning with architecture and security recommendations.",
    tags: ["Network", "Wireless", "Security"],
    highlight: true
  },
  "project-database.html": {
    no: "03",
    type: "Database Report",
    title: "Database System",
    desc: "ERD/EER, physical modelling, database creation, table implementation and SQL analysis.",
    tags: ["SQL", "ERD", "Backend"]
  },
  "project-dsa.html": {
    no: "04",
    type: "Algorithm Report",
    title: "Data Structures and Algorithms",
    desc: "Algorithm complexity analysis, dominant term reasoning, loop analysis and Big-O explanation.",
    tags: ["Big-O", "Algorithm", "C++"]
  },
  "project-computer-network-security.html": {
    no: "05",
    type: "Risk Study",
    title: "Computer and Network Security",
    desc: "Ransomware risk analysis for a university environment with attack paths, impact and controls.",
    tags: ["Ransomware", "NIST", "Risk"],
    highlight: true
  },
  "project-digital-evidence.html": {
    no: "06",
    type: "Forensics Report",
    title: "Computer Crime and Digital Evidence",
    desc: "Digital evidence report covering legal framework, evidence procedure and chain of custody.",
    tags: ["Forensics", "Legal", "Evidence"]
  },
  "project-operating-systems-networks.html": {
    no: "07",
    type: "Network Design",
    title: "Operating Systems and Computer Networks",
    desc: "Packet Tracer LAN design for staff, visitors, IoT devices, shared resources and external access.",
    tags: ["LAN", "Packet Tracer", "IoT"]
  },
  "project-professional-practices-security.html": {
    no: "08",
    type: "Security Practice",
    title: "Professional Practices and Information Security",
    desc: "Information security case study covering breach analysis, ethics, legal impact and responsibility.",
    tags: ["Ethics", "Legal", "Governance"]
  },
  "project-systems-fundamentals.html": {
    no: "09",
    type: "Systems Report",
    title: "Systems Fundamentals",
    desc: "Virtualization and hypervisor report explaining full, para and OS-level virtualization concepts.",
    tags: ["Systems", "Virtualization", "Cloud"]
  },
  "project-theory-computation.html": {
    no: "10",
    type: "Theory Report",
    title: "Theory of Computation",
    desc: "Formal reasoning project connecting RSA calculation, Euclidean algorithm and computational limits.",
    tags: ["RSA", "Theory", "Security"]
  },
  "project-game-studies.html": {
    no: "11",
    type: "Game Studies",
    title: "CGPA 3.0",
    desc: "Tabletop card game project about university life, GPA collection, course sets, playtesting and final pitch.",
    tags: ["Card Game", "Playtest", "Pitch"]
  },
  "project-3d-modeling.html": {
    no: "12",
    type: "3D Modeling",
    title: "3D Modeling Portfolio",
    desc: "Autodesk Maya coursework showing a washroom environment, chessboard model, materials, renders and playblast views.",
    tags: ["Maya", "Environment", "Rendering"]
  },
  "project-sonic-design.html": {
    no: "13",
    type: "Sonic Design",
    title: "Sonic Design",
    desc: "Audio design coursework prepared for ambience, game sound effects, scene audio and layered sound work.",
    tags: ["Adobe Audition", "SFX", "Audio"]
  },
  "project-experimental-design.html": {
    no: "14",
    type: "AR Prototype",
    title: "Experimental Design",
    desc: "Unity and Vuforia AR design practice exploring marker-based interaction and physical-to-digital play.",
    tags: ["Unity", "Vuforia", "AR"]
  },
  "project-game-development.html": {
    no: "15",
    type: "3D Game",
    title: "Game Development",
    desc: "Full game development project space for gameplay flow, mechanics, environment setup and playable delivery.",
    tags: ["3D Game", "Gameplay", "Build"]
  }
};

document.querySelectorAll(".rail-head span").forEach(title => {
  title.textContent = "More Projects";
});

document.querySelectorAll(".project-link").forEach(link => {
  const file = link.getAttribute("href")?.split("/").pop();
  const project = projectCards[file];
  if (!project || link.querySelector(".project-card-top")) return;

  link.dataset.no = project.no;
  link.classList.toggle("highlight-project", Boolean(project.highlight));
  link.innerHTML = `
    <div class="project-card-top"><span>${project.no}</span><span>${project.type}</span></div>
    <div class="project-preview" aria-hidden="true"><span class="browser-dot"></span><i></i><i></i><i></i></div>
    <h3>${project.title}</h3>
    <p>${project.desc}</p>
    <div class="project-mini-tags">${project.tags.map(tag => `<span>${tag}</span>`).join("")}</div>
    <div class="project-card-cta"><span>View Project</span><span>-></span></div>
  `;
});

const sparkfitDemo = {
  login: {
    image: "sparkfit-screens/image14.jpg",
    alt: "SparkFit login screen",
    kicker: "01 / Login Flow",
    title: "Login and sign-up entry.",
    desc: "A simple first screen before users enter the SparkFit dashboard.",
    points: ["Login", "Sign-up switch", "Exit action"]
  },
  dashboard: {
    image: "sparkfit-screens/image8.jpg",
    alt: "SparkFit dashboard screen",
    kicker: "02 / Dashboard",
    title: "Dashboard overview.",
    desc: "A central view for exercise, meals, dates, and achievement charts.",
    points: ["Daily logs", "Date filter", "Progress charts"]
  },
  profile: {
    image: "sparkfit-screens/image2.png",
    alt: "SparkFit profile screen",
    kicker: "03 / User Profile",
    title: "Profile management.",
    desc: "Users can update personal fitness details and view a profile summary.",
    points: ["Profile form", "Summary card", "Goal count"]
  },
  "set-goals": {
    image: "sparkfit-screens/image18.png",
    alt: "SparkFit set goals screen",
    kicker: "04 / Goal Setting",
    title: "Fitness goal setting.",
    desc: "Users can set targets for calories, steps, body weight, and sleep.",
    points: ["Goal values", "Deadline", "Set goals"]
  },
  "view-goals": {
    image: "sparkfit-screens/image11.png",
    alt: "SparkFit view goals screen",
    kicker: "05 / Goal Progress",
    title: "Goal progress view.",
    desc: "Users can review current targets, countdown, and achievement status.",
    points: ["Countdown", "Goal summary", "Reset option"]
  },
  workout: {
    image: "sparkfit-screens/image20.png",
    alt: "SparkFit workout logger screen",
    kicker: "06 / Workout Logger",
    title: "Workout logger.",
    desc: "Users can record exercise type, duration, and calories burned.",
    points: ["Exercise form", "Save log", "Retrieve logs"]
  },
  nutrition: {
    image: "sparkfit-screens/image6.png",
    alt: "SparkFit nutrition screen",
    kicker: "07 / Nutrition Tracking",
    title: "Nutrition tracking.",
    desc: "Users can log meals and view calorie or macro summaries.",
    points: ["Meal log", "Macro inputs", "History filter"]
  },
  progress: {
    image: "sparkfit-screens/image13.png",
    alt: "SparkFit progress screen",
    kicker: "08 / Progress Visualisation",
    title: "Progress visualisation.",
    desc: "Charts summarise calories, steps, weight, and sleep progress.",
    points: ["Daily progress", "Date range", "Charts"]
  }
};

const demoImage = document.getElementById("sparkfit-demo-image");
const demoFallback = document.getElementById("sparkfit-demo-fallback");
const demoKicker = document.getElementById("sparkfit-demo-kicker");
const demoTitle = document.getElementById("sparkfit-demo-title");
const demoDesc = document.getElementById("sparkfit-demo-desc");
const demoPoints = document.getElementById("sparkfit-demo-points");

if (demoImage) {
  const showDemo = key => {
    const item = sparkfitDemo[key];
    if (!item) return;
    demoImage.src = item.image;
    demoImage.alt = item.alt;
    demoKicker.textContent = item.kicker;
    demoTitle.textContent = item.title;
    demoDesc.textContent = item.desc;
    demoPoints.innerHTML = item.points.map(point => `<span>${point}</span>`).join("");
    document.querySelectorAll(".demo-tab").forEach(tab => tab.classList.toggle("active", tab.dataset.demo === key));
  };

  demoImage.addEventListener("load", () => demoFallback.style.display = "none");
  demoImage.addEventListener("error", () => demoFallback.style.display = "grid");
  document.querySelectorAll(".demo-tab").forEach(tab => {
    tab.addEventListener("click", () => showDemo(tab.dataset.demo));
  });
}

const observer = new IntersectionObserver(entries => {
  entries.forEach(entry => {
    if (entry.isIntersecting) entry.target.classList.add("in");
  });
}, { threshold: 0.12 });

document.querySelectorAll(".reveal").forEach((el, index) => {
  el.style.transitionDelay = `${Math.min(index % 5, 4) * 55}ms`;
  observer.observe(el);
});
