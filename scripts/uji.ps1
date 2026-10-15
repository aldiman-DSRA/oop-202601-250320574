# Menjalankan tes sampai minggu aktif (dibaca dari minggu-aktif.txt).
# Pemakaian: .\scripts\uji.ps1 [-Minggu 5]
param([int]$Minggu = 0)

$ErrorActionPreference = "Stop"
Set-Location (Join-Path $PSScriptRoot "..")

if ($Minggu -lt 1) {
    $Minggu = [int](Get-Content "minggu-aktif.txt" -Raw).Trim()
}
if ($Minggu -lt 1) {
    Write-Error "minggu-aktif.txt harus berisi angka >= 1"
    exit 2
}

$groups = (1..$Minggu | ForEach-Object { "week$_" }) -join " | "
Write-Host "Menjalankan tes minggu 1 sampai $Minggu  (groups: $groups)"
mvn -B -ntp test "-Dgroups=$groups"
exit $LASTEXITCODE