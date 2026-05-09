param(
    [Parameter(Mandatory = $true)]
    [ValidateRange(0, 999)]
    [int]$Patch,
    [switch]$Push,
    [switch]$DryRun
)
$ErrorActionPreference = "Stop"
function Exec-Git {
    param(
        [Parameter(Mandatory = $true)]
        [string[]]$Args
    )
    $output = & git @Args 2>&1
    if ($LASTEXITCODE -ne 0) {
        throw "git $($Args -join ' ') failed: $output"
    }
    return $output
}
function Get-GitHubRepoUrl {
    param(
        [Parameter(Mandatory = $true)]
        [string]$RemoteUrl
    )
    if ($RemoteUrl -match '^https://github\.com/(?<owner>[^/]+)/(?<repo>[^/.]+?)(?:\.git)?/?$') {
        return "https://github.com/$($Matches.owner)/$($Matches.repo)"
    }
    if ($RemoteUrl -match '^git@github\.com:(?<owner>[^/]+)/(?<repo>[^/.]+?)(?:\.git)?$') {
        return "https://github.com/$($Matches.owner)/$($Matches.repo)"
    }
    throw "Unsupported origin URL format for GitHub release link generation: $RemoteUrl"
}
$repoRoot = (Exec-Git -Args @('rev-parse', '--show-toplevel')).Trim()
$androidProjectRoot = Join-Path $repoRoot 'AndroidModApp'
$tagScriptPath = Join-Path $repoRoot 'release-tag.ps1'
if (-not (Test-Path $androidProjectRoot)) {
    throw "Could not find AndroidModApp at $androidProjectRoot"
}
if (-not (Test-Path $tagScriptPath)) {
    throw "Could not find release-tag.ps1 at $tagScriptPath"
}
$utcNow = [DateTime]::UtcNow
$tag = "v{0}.{1}.{2}" -f $utcNow.Year, $utcNow.ToString('MM'), $Patch
$originUrl = (Exec-Git -Args @('remote', 'get-url', 'origin')).Trim()
$repoUrl = Get-GitHubRepoUrl -RemoteUrl $originUrl
$releaseUrl = "$repoUrl/releases/tag/$tag"
Write-Host "Running Detekt before tagging..."
Push-Location $androidProjectRoot
try {
    & .\gradlew.bat detekt --no-daemon
    if ($LASTEXITCODE -ne 0) {
        throw "Detekt failed. Aborting release flow."
    }
}
finally {
    Pop-Location
}
$tagArgs = @{
    Patch = $Patch
}
if ($Push) {
    $tagArgs.Push = $true
}
if ($DryRun) {
    $tagArgs.DryRun = $true
}
& $tagScriptPath @tagArgs
if ($LASTEXITCODE -ne 0) {
    throw "release-tag.ps1 failed."
}
if ($DryRun) {
    Write-Host "[DRY-RUN] Expected release URL: $releaseUrl"
} elseif ($Push) {
    Write-Host "Release tag pushed. Expected GitHub release URL: $releaseUrl"
} else {
    Write-Host "Local tag created. Push the tag to trigger the release workflow."
    Write-Host "Expected GitHub release URL after push: $releaseUrl"
}
