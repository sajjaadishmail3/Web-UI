param(
  [switch]$SkipBuild
)

if (-not $SkipBuild) {
  if (Get-Command mvn -ErrorAction SilentlyContinue) {
    Write-Host "[INFO] Building all modules..."
    mvn -q -DskipTests clean package
    if ($LASTEXITCODE -ne 0) {
      Write-Error "Build failed."
      exit $LASTEXITCODE
    }
  } else {
    Write-Warning "'mvn' command not found. Skipping build step. Ensure you have built the project via IntelliJ."
  }
}

# 2. Define Paths
$beJar = "Management-BE/target/management-be-0.0.1-SNAPSHOT.jar"
$uiJar = "Web-UI/target/book-catalogue-ui.jar"

# 3. Validate JARs exist
if (-not (Test-Path $beJar)) {
  Write-Error "Backend JAR not found at $beJar. Please build the project first."
  exit 1
}
if (-not (Test-Path $uiJar)) {
  Write-Error "Web-UI JAR not found at $uiJar. Please build the project first."
  exit 1
}

# 4. Launch Backend in a new window
Write-Host "[INFO] Launching Management-BE (Port 8081)..."
Start-Process powershell -ArgumentList "-NoExit", "-Command", "& java -jar $beJar"

# 5. Wait a moment for BE to initialize (optional but helpful)
Write-Host "[INFO] Waiting 10 seconds for Backend to start..."
Start-Sleep -Seconds 10

# 6. Launch Web-UI in a new window
Write-Host "[INFO] Launching Web-UI (Port 8080)..."
$env:MANAGEMENT_API_BASE_URL = "http://localhost:8081/api"
# Note: We pass the env var inside the new process scope
Start-Process powershell -ArgumentList "-NoExit", "-Command", "`$env:MANAGEMENT_API_BASE_URL='http://localhost:8081/api'; & java -jar $uiJar"

Write-Host "[SUCCESS] Both services launched in separate windows."
