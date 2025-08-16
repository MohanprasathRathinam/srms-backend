Write-Host "Testing API endpoint..." -ForegroundColor Yellow

try {
    $body = "name=Test&roll=123&math=90&physics=85&chemistry=88&english=78&cs=92"
    
    Write-Host "Sending POST request to http://localhost:8080/api/calculate" -ForegroundColor Cyan
    Write-Host "Body: $body" -ForegroundColor Gray
    
    $response = Invoke-WebRequest -Uri "http://localhost:8080/api/calculate" -Method POST -Body $body -ContentType "application/x-www-form-urlencoded" -UseBasicParsing
    
    Write-Host "Success! Status: $($response.StatusCode)" -ForegroundColor Green
    Write-Host "Response: $($response.Content)" -ForegroundColor White
    
} catch {
    Write-Host "Error occurred:" -ForegroundColor Red
    Write-Host "Message: $($_.Exception.Message)" -ForegroundColor Red
    Write-Host "Status: $($_.Exception.Response.StatusCode)" -ForegroundColor Red
    if ($_.Exception.Response) {
        $reader = New-Object System.IO.StreamReader($_.Exception.Response.GetResponseStream())
        $responseBody = $reader.ReadToEnd()
        Write-Host "Response body: $responseBody" -ForegroundColor Red
    }
}

