# 🚀 Deployment Guide: Student Result Management System

This guide will walk you through deploying your SRMS project as a live website using **GitHub Pages** for the frontend and **Render** for the backend.

## 📋 Prerequisites

- GitHub account
- Render account (free tier available)
- Java 11+ and Maven (for local testing)

## 🎯 Deployment Strategy

```
Frontend (GitHub Pages) ←→ Backend (Render)
     ↓                           ↓
https://username.github.io/  https://your-service.onrender.com/
```

## 🔧 Step 1: Prepare Your Project

### 1.1 Clean up the project
```bash
# Remove old Git repository
Remove-Item -Recurse -Force .git

# Initialize new Git repository
git init
git add .
git commit -m "Initial commit: SRMS project ready for deployment"
```

### 1.2 Test locally
```bash
# Build the project
mvn clean package

# Run locally
java -jar target/student-result-management-system-1.0.0.jar
```

Visit `http://localhost:8080` to ensure everything works.

## 🌐 Step 2: Deploy Backend to Render

### 2.1 Create Render Account
1. Go to [render.com](https://render.com)
2. Sign up with your GitHub account
3. Verify your email

### 2.2 Deploy Web Service
1. Click **"New +"** → **"Web Service"**
2. Connect your GitHub repository
3. Configure the service:

   **Basic Settings:**
   - **Name**: `smsys-backend`
   - **Environment**: `Java`
   - **Region**: Choose closest to your users
   - **Branch**: `main`

   **Build & Deploy:**
   - **Build Command**: `mvn clean package`
   - **Start Command**: `java -jar target/student-result-management-system-1.0.0.jar`
   - **Auto-Deploy**: ✅ Enabled

4. Click **"Create Web Service"**

### 2.3 Wait for Deployment
- Build time: ~2-3 minutes
- You'll see logs showing the build process
- Once complete, you'll get a URL like: `https://smsys-backend.onrender.com`

### 2.4 Test Your Backend
Test the API endpoint:
```bash
curl "https://your-service.onrender.com/api/calculate?name=Test&roll=123&math=90&physics=85&chemistry=88&english=78&cs=92"
```

## 🎨 Step 3: Deploy Frontend to GitHub Pages

### 3.1 Create Frontend Repository
1. Go to [GitHub.com](https://github.com)
2. Click **"New repository"**
3. Repository name: `smsys-frontend`
4. Make it **Public** (required for GitHub Pages)
5. Don't initialize with README
6. Click **"Create repository"**

### 3.2 Update Backend URL
1. In your local project, edit `frontend/script.js`
2. Update the `BACKEND_URL` constant:
   ```javascript
   const BACKEND_URL = 'https://your-actual-service.onrender.com';
   ```
3. Replace `your-actual-service.onrender.com` with your actual Render service URL

### 3.3 Upload Frontend Files
1. In your new `smsys-frontend` repository
2. Click **"uploading an existing file"**
3. Drag and drop the contents of your `frontend/` folder:
   - `index.html`
   - `styles.css`
   - `script.js`
4. Add commit message: `"Add frontend files"`
5. Click **"Commit changes"**

### 3.4 Enable GitHub Pages
1. Go to **Settings** tab
2. Scroll down to **"Pages"** section
3. Under **"Source"**, select **"Deploy from a branch"**
4. Choose **"main"** branch
5. Click **"Save"**

Your frontend will be available at: `https://username.github.io/smsys-frontend`

## 🔗 Step 4: Connect Frontend and Backend

### 4.1 Verify Backend URL
Ensure your `frontend/script.js` has the correct Render URL:
```javascript
const BACKEND_URL = 'https://smsys-backend.onrender.com'; // Your actual URL
```

### 4.2 Test the Complete System
1. Visit your GitHub Pages URL
2. Fill out the form with test data
3. Submit and verify the API call works
4. Check browser console for any errors

## 🧪 Step 5: Testing and Validation

### 5.1 Test Scenarios
- ✅ Form submission with valid data
- ✅ API communication with Render backend
- ✅ Fallback to local calculation if backend fails
- ✅ Responsive design on mobile/tablet
- ✅ Cross-browser compatibility

### 5.2 Performance Check
- Frontend loads quickly on GitHub Pages
- Backend responds within reasonable time
- API calls complete successfully

## 🚨 Troubleshooting

### Common Issues

**Backend not responding:**
- Check Render service logs
- Verify the service is running
- Check if the service went to sleep (free tier)

**CORS errors:**
- Backend includes CORS headers
- Frontend makes requests to correct URL

**Build failures:**
- Check Maven dependencies
- Verify Java version compatibility
- Review Render build logs

### Debug Steps
1. Check browser console for errors
2. Verify backend URL in `script.js`
3. Test backend API directly
4. Check Render service status

## 🔄 Step 6: Continuous Deployment

### 6.1 Automatic Updates
- **Render**: Automatically redeploys on Git push
- **GitHub Pages**: Updates when you push to main branch

### 6.2 Making Changes
1. Update your local code
2. Test locally
3. Push to GitHub
4. Both services will auto-deploy

## 📊 Monitoring

### 6.3 Health Checks
- Render provides service health monitoring
- GitHub Pages shows deployment status
- Monitor API response times

## 🎉 Success!

Your Student Result Management System is now live at:
- **Frontend**: `https://username.github.io/smsys-frontend`
- **Backend API**: `https://your-service.onrender.com/api/calculate`

## 🔗 Useful Links

- [Render Dashboard](https://dashboard.render.com)
- [GitHub Pages Settings](https://github.com/username/smsys-frontend/settings/pages)
- [Your Backend Service](https://your-service.onrender.com)

## 📞 Support

If you encounter issues:
1. Check this deployment guide
2. Review Render and GitHub documentation
3. Check service logs and browser console
4. Create an issue in your repository

---

**Congratulations! 🎓 Your SRMS is now live on the internet! 🌐**


