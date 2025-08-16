param()
$ErrorActionPreference = 'Stop'

Set-Location "$PSScriptRoot\.."

if (!(Test-Path -Path ".\out")) {
  Write-Host "Build output not found. Compiling..."
  & "$PSScriptRoot\compile.ps1"
}

Write-Host "Starting server at http://localhost:8080"
java -cp .\out server.ResultServer


