# SRMS Deployment Helper Script
# This script helps you prepare and deploy your Student Result Management System

Write-Host "🚀 Student Result Management System - Deployment Helper" -ForegroundColor Cyan
Write-Host "=====================================================" -ForegroundColor Cyan
Write-Host ""

# Check if Git is available
try {
    $gitVersion = git --version
    Write-Host "✅ Git found: $gitVersion" -ForegroundColor Green
} catch {
    Write-Host "❌ Git not found. Please install Git first." -ForegroundColor Red
    exit 1
}

# Check if Maven is available
try {
    $mvnVersion = mvn --version | Select-Object -First 1
    Write-Host "✅ Maven found: $mvnVersion" -ForegroundColor Green
} catch {
    Write-Host "❌ Maven not found. Please install Maven first." -ForegroundColor Red
    Write-Host "   Download from: https://maven.apache.org/download.cgi" -ForegroundColor Yellow
    exit 1
}

# Check if Java is available
try {
    $javaVersion = java -version 2>&1 | Select-Object -First 1
    Write-Host "✅ Java found: $javaVersion" -ForegroundColor Green
} catch {
    Write-Host "❌ Java not found. Please install Java 11+ first." -ForegroundColor Red
    exit 1
}

Write-Host ""
Write-Host "🔧 Project Status Check:" -ForegroundColor Yellow

# Check if project builds
Write-Host "Building project..." -ForegroundColor Yellow
try {
    mvn clean package -q
    Write-Host "✅ Project builds successfully" -ForegroundColor Green
} catch {
    Write-Host "❌ Project build failed. Please fix build issues first." -ForegroundColor Red
    exit 1
}

Write-Host ""
Write-Host "📋 Next Steps:" -ForegroundColor Cyan
Write-Host "=============" -ForegroundColor Cyan
Write-Host ""
Write-Host "1. 🌐 Deploy Backend to Render:" -ForegroundColor Yellow
Write-Host "   - Go to https://render.com and sign up" -ForegroundColor White
Write-Host "   - Create new Web Service" -ForegroundColor White
Write-Host "   - Connect your GitHub repository" -ForegroundColor White
Write-Host "   - Use build command: mvn clean package" -ForegroundColor White
Write-Host "   - Use start command: java -jar target/student-result-management-system-1.0.0.jar" -ForegroundColor White
Write-Host ""
Write-Host "2. 🎨 Deploy Frontend to GitHub Pages:" -ForegroundColor Yellow
Write-Host "   - Create new repository: smsys-frontend" -ForegroundColor White
Write-Host "   - Upload contents of frontend/ folder" -ForegroundColor White
Write-Host "   - Enable GitHub Pages in repository settings" -ForegroundColor White
Write-Host ""
Write-Host "3. 🔗 Connect Frontend and Backend:" -ForegroundColor Yellow
Write-Host "   - Update BACKEND_URL in frontend/script.js" -ForegroundColor White
Write-Host "   - Test the complete system" -ForegroundColor White
Write-Host ""
Write-Host "📖 For detailed instructions, see DEPLOYMENT.md" -ForegroundColor Cyan
Write-Host ""
Write-Host "🎯 Quick Test:" -ForegroundColor Yellow
Write-Host "Run: java -jar target/student-result-management-system-1.0.0.jar" -ForegroundColor White
Write-Host "Visit: http://localhost:8080" -ForegroundColor White
Write-Host ""
Write-Host "Good luck with your deployment! 🚀" -ForegroundColor Green


