Param(
    [string]$ApiUrl
)

if (-not $ApiUrl) {
  if ($env:MANAGEMENT_API_BASE_URL) {
    $ApiUrl = $env:MANAGEMENT_API_BASE_URL
  } else {
    $ApiUrl = "http://localhost:8081/api"
  }
}

Write-Host "[INFO] Using MANAGEMENT_API_BASE_URL=$ApiUrl"
$env:MANAGEMENT_API_BASE_URL = $ApiUrl

if (-not (Test-Path "target/book-ui.jar")) {
  Write-Host "[INFO] Building project..."
  mvn -q -DskipTests clean package
}

Write-Host "[INFO] Starting Web UI on http://localhost:8080"
& java -jar "target/book-ui.jar"
