Student Result Management System

Overview
- A responsive web app with a lively, modern UI (gradients, subtle animations).
- Front-end: HTML, CSS, and vanilla JavaScript.
- Back-end: Java (built-in HttpServer) serving static files and an API for result calculation.

Run (Windows / PowerShell)
1) Compile
   - Run: `./scripts/compile.ps1`
2) Start server
   - Run: `./scripts/run.ps1`
3) Open: `http://localhost:8080`

Structure
- `public/` static assets: `index.html`, `styles.css`, `script.js`
- `src/server/` Java sources: `ResultServer.java`, `ResultCalculator.java`
- `scripts/` helper PowerShell scripts

API
- POST `/api/calculate` with content-type `application/x-www-form-urlencoded`
  - Fields: `name`, `roll`, `math`, `physics`, `chemistry`, `english`, `cs`
  - Response JSON: `{ name, rollNumber, total, percentage, grade, subjectsCount, maxMarksPerSubject }`

Notes
- No external dependencies required (uses JDK's HttpServer).
- If you change subjects, update both `public/index.html` and `src/server/ResultServer.java` accordingly.


