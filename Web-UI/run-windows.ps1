Param(
    [switch]$SkipBuild,
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

if (-not $SkipBuild) {
  if (Get-Command mvn -ErrorAction SilentlyContinue) {
    mvn -q -e -DskipTests clean package
    if ($LASTEXITCODE -ne 0) { exit $LASTEXITCODE }
  } else {
    Write-Warning "'mvn' command not found. Skipping build step and using existing JAR."
  }
}

$jar = Get-ChildItem -Path target -Filter *.jar | Where-Object { $_.Name -notlike "*original*" } | Select-Object -First 1
if (-not $jar) { Write-Error "Jar not found in target. Please build via IntelliJ first."; exit 1 }

java -jar $jar.FullName
