# Get the root of the project
$repoRoot = (git rev-parse --show-toplevel).Trim()
$assetsPath = Join-Path $repoRoot "data/files/src/main/assets"

# Obtener todos los archivos .json
$files = Get-ChildItem -Path "$assetsPath/*.json"

# Preparar codificación UTF8 sin BOM
$Utf8NoBom = New-Object System.Text.UTF8Encoding($false)

# Mapeo de reparación manual mejorado (Tratando variaciones de Windows-1252/UTF-8)
$rep = @{
    ([char]0xC3 + [char]0xA1) = [char]0xE1 # á
    ([char]0xC3 + [char]0xA9) = [char]0xE9 # é
    ([char]0xC3 + [char]0xAD) = [char]0xED # í
    ([char]0xC3 + [char]0xB3) = [char]0xF3 # ó
    ([char]0xC3 + [char]0xBA) = [char]0xFA # ú
    ([char]0xC3 + [char]0xB1) = [char]0xF1 # ñ
    ([char]0xC3 + [char]0x81) = [char]0xC1 # Á
    ([char]0xC3 + [char]0x89) = [char]0xC9 # É
    ([char]0xC3 + [char]0x8D) = [char]0xCD # Í
    ([char]0xC3 + [char]0x93) = [char]0xD3 # Ó
    ([char]0xC3 + [char]0x9A) = [char]0xDA # Ú
    ([char]0xC3 + [char]0x91) = [char]0xD1 # Ñ
    ([char]0xC3 + [char]0xBC) = [char]0xFC # ü
    ([char]0xC2 + [char]0xBF) = [char]0xBF # ¿
    ([char]0xC2 + [char]0xA1) = [char]0xA1 # ¡

    # Variaciones específicas de representación visual (como Ã‰xtasis)
    ([char]0xC3 + [char]0x2030) = [char]0xC9 # É (Ã followed by ‰)
    ([char]0xC3 + [char]0x201C) = [char]0xD3 # Ó
    ([char]0xC3 + [char]0x201D) = [char]0xD3 # Ó
    ([char]0xC3 + [char]0x2014) = [char]0xDA # Ú
    ([char]0xC3 + [char]0x0161) = [char]0xDA # Ú
    ([char]0xC3 + [char]0x2020) = [char]0xCD # Í
    ([char]0xC3 + [char]0x017D) = [char]0xCD # Í
}

$totalOriginal = 0
$totalFinal = 0
$stats = @()

foreach ($file in $files) {
    Write-Host "Procesando: $($file.Name)"

    $content = [System.IO.File]::ReadAllText($file.FullName, [System.Text.Encoding]::UTF8)

    # Aplicar reparaciones de codificación
    foreach ($key in $rep.Keys) {
        $content = $content.Replace($key, $rep[$key])
    }

    # Reemplazar escapes unicode comunes por caracteres literales
    $content = $content.Replace('\u0027', "'").Replace('\u0026', "&")

    try {
        $json = $content | ConvertFrom-Json
        $countOriginal = $json.words.Count
        $totalOriginal += $countOriginal

        $processedWords = @()

        foreach ($wordObj in $json.words) {
            if ($null -eq $wordObj.word) { continue }
            $wordObj.word = $wordObj.word.ToString().Trim()

            $wordNameCount = ($wordObj.word -split '\s+').Length
            if ($wordNameCount -gt 3) { continue }

            $cleanClues = @()
            $hasTooLongClue = $false

            foreach ($clue in $wordObj.clues) {
                if ($null -eq $clue) { continue }
                $clueStr = $clue.ToString().Trim()
                if ($clueStr -eq "") { continue }

                $cleanClue = $clueStr.Replace("_", " ").Trim()

                if (($cleanClue -split '\s+').Length -gt 3) {
                    $hasTooLongClue = $true
                    break
                }
                $cleanClues += $cleanClue
            }

            if ($hasTooLongClue) { continue }
            if ($cleanClues.Count -eq 0) { continue }

            $wordObj.clues = $cleanClues
            $processedWords += $wordObj
        }

        $uniqueWords = $processedWords | Group-Object { $_.word.ToLower() } | ForEach-Object { $_.Group[0] }

        $countFinal = $uniqueWords.Count
        $totalFinal += $countFinal

        $stats += [PSCustomObject]@{
            Archivo = $file.Name
            Original = $countOriginal
            Final = $countFinal
            Eliminados = $countOriginal - $countFinal
        }

        $wordLines = $uniqueWords | ForEach-Object {
            $compact = $_ | ConvertTo-Json -Compress
            "    $compact"
        }

        $finalJson = "{`n  `"words`": [`n" + ($wordLines -join ",`n") + "`n  ]`n}"
        $finalJson = $finalJson.Replace('\u0027', "'").Replace('\u0026', "&")

        [System.IO.File]::WriteAllText($file.FullName, $finalJson, $Utf8NoBom)

    } catch {
        Write-Warning "  !! Error en $($file.Name): $($_.Exception.Message)"
    }
}

Write-Host "`n" + ("=" * 60)
Write-Host "RESUMEN DEL BANCO DE PALABRAS"
Write-Host ("=" * 60)
$stats | Format-Table -AutoSize
Write-Host ("-" * 60)
Write-Host "TOTAL PALABRAS ORIGINALES: $totalOriginal"
Write-Host "TOTAL PALABRAS FINALES:    $totalFinal"
Write-Host "TOTAL ELIMINADAS:          $($totalOriginal - $totalFinal)"
Write-Host ("=" * 60)
Write-Host "¡Listo! Limpieza y normalización completada."
