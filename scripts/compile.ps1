param()
$ErrorActionPreference = 'Stop'

if (!(Test-Path -Path "$PSScriptRoot\..\out")) {
  New-Item -ItemType Directory -Path "$PSScriptRoot\..\out" | Out-Null
}

Set-Location "$PSScriptRoot\.."

javac -version | Out-Null

javac -d .\out .\src\server\*.java

Write-Host "Compiled to out/"


