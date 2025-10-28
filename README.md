# Student Result Management System (SRMS)

A modern, responsive web application for managing student academic records, calculating marks and grades, and generating reports. Built with Java backend and modern web technologies.

## 🌟 Features

- **Modern UI/UX**: Beautiful, responsive design with smooth animations
- **Real-time Calculation**: Instant grade calculation and percentage computation
- **Cross-platform**: Works on desktop, tablet, and mobile devices
- **Fallback Support**: Local calculation if backend is unavailable
- **Professional Design**: Clean, modern interface with gradient themes

## 🏗️ Architecture

- **Frontend**: HTML5, CSS3, JavaScript (ES6+)
- **Backend**: Java with embedded HTTP server
- **Deployment**: GitHub Pages (Frontend) + Render (Backend)

## 🚀 Quick Start

### Prerequisites

- Java 11 or higher
- Maven 3.6+
- Git

### Local Development

1. **Clone the repository**
   ```bash
   git clone https://github.com/yourusername/student-result-management-system.git
   cd student-result-management-system
   ```

2. **Build the backend**
   ```bash
   mvn clean package
   ```

3. **Run the server**
   ```bash
   java -jar target/student-result-management-system-1.0.0.jar
   ```

4. **Open in browser**
   - Frontend: `http://localhost:8080`
   - API: `http://localhost:8080/api/calculate`

## 🌐 Deployment

### Option 1: Full Stack on Render (Recommended)

1. **Fork/Clone this repository to your GitHub account**

2. **Deploy on Render**
   - Go to [render.com](https://render.com) and sign up
   - Click "New +" → "Web Service"
   - Connect your GitHub repository
   - Configure:
     - **Name**: `smsys-backend`
     - **Environment**: `Java`
     - **Build Command**: `mvn clean package`
     - **Start Command**: `java -jar target/student-result-management-system-1.0.0.jar`
   - Click "Create Web Service"

3. **Update Frontend Configuration**
   - In `frontend/script.js`, update `BACKEND_URL` with your Render service URL
   - Example: `const BACKEND_URL = 'https://your-service.onrender.com';`

4. **Deploy Frontend to GitHub Pages**
   - Create a new repository for the frontend
   - Copy the `frontend/` folder contents
   - Enable GitHub Pages in repository settings
   - Set source to main branch

### Option 2: Separate Deployments

#### Backend on Render
- Follow steps 1-2 from Option 1
- Your backend will be available at: `https://your-service.onrender.com`

#### Frontend on GitHub Pages
- Create a new repository: `smsys-frontend`
- Copy the `frontend/` folder contents
- Enable GitHub Pages in repository settings
- Update `BACKEND_URL` in `script.js` with your Render URL

## 📁 Project Structure

```
student-result-management-system/
├── src/
│   └── main/java/com/smsys/server/
│       ├── ResultServer.java      # Main server with HTTP handlers
│       └── ResultCalculator.java  # Business logic for calculations
├── frontend/                      # Static files for GitHub Pages
│   ├── index.html                # Main HTML file
│   ├── styles.css                # CSS styles
│   └── script.js                 # Frontend JavaScript
├── pom.xml                       # Maven configuration
├── render.yaml                   # Render deployment config
└── README.md                     # This file
```

## 🔧 Configuration

### Environment Variables

- `PORT`: Server port (default: 8080)

### API Endpoints

- `POST /api/calculate`: Calculate student result
- `GET /api/calculate`: Calculate result via query parameters
- `GET /`: Serve static frontend files

### Request Format

```json
{
  "name": "Student Name",
  "roll": "Roll Number",
  "math": 90,
  "physics": 85,
  "chemistry": 88,
  "english": 78,
  "cs": 92
}
```

### Response Format

```json
{
  "name": "Student Name",
  "rollNumber": "Roll Number",
  "total": 433,
  "percentage": 86.6,
  "grade": "A",
  "subjectsCount": 5,
  "maxMarksPerSubject": 100,
  "subjects": {
    "Mathematics": 90,
    "Physics": 85,
    "Chemistry": 88,
    "English": 78,
    "Computer Science": 92
  }
}
```

## 🎨 Customization

### Styling
- Modify `frontend/styles.css` to change colors, fonts, and layout
- CSS variables are defined in `:root` for easy theming

### Functionality
- Add new subjects in `ResultCalculator.java`
- Modify grade calculation logic in `calculateGrade()` method
- Extend API endpoints in `ResultServer.java`

## 🚀 Performance Features

- **Lazy Loading**: Static content served efficiently
- **CORS Support**: Cross-origin requests enabled
- **Error Handling**: Graceful fallbacks and user-friendly messages
- **Responsive Design**: Optimized for all device sizes

## 🔒 Security Features

- Input validation for all form fields
- Mark range validation (0-100)
- CORS headers for web deployment
- Error message sanitization

## 📱 Browser Support

- Chrome 60+
- Firefox 55+
- Safari 12+
- Edge 79+

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch: `git checkout -b feature-name`
3. Commit changes: `git commit -am 'Add feature'`
4. Push to branch: `git push origin feature-name`
5. Submit a pull request

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 🆘 Support

If you encounter any issues:

1. Check the [Issues](https://github.com/yourusername/student-result-management-system/issues) page
2. Create a new issue with detailed description
3. Include your environment details and error logs

## 🙏 Acknowledgments

- Built with modern web technologies
- Deployed on Render and GitHub Pages
- Inspired by educational management needs

---

**Happy Coding! 🎓✨**


