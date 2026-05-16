# Get the root of the project
$repoRoot = (git rev-parse --show-toplevel).Trim()
$assetsPath = Join-Path $repoRoot "data/files/src/main/assets"

# Obtener todos los archivos .json
$files = Get-ChildItem -Path "$assetsPath/*.json"

# Preparar codificación UTF8 sin BOM
$Utf8NoBom = New-Object System.Text.UTF8Encoding($false)

# Mapeo de reparación manual mejorado
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

    ([char]0xC3 + [char]0x2030) = [char]0xC9 # É
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
    # Write-Host "Procesando: $($file.Name)"

    $categoryName = $file.BaseName -replace '_(es|en)$', ''
    $content = [System.IO.File]::ReadAllText($file.FullName, [System.Text.Encoding]::UTF8)

    # 1. Limpieza de comas
    $content = $content -replace ',\s*\]', ']'
    $content = $content -replace ',\s*\}', '}'

    # 2. Reparar codificación
    foreach ($key in $rep.Keys) {
        $content = $content.Replace($key, $rep[$key])
    }
    $content = $content.Replace('\u0027', "'").Replace('\u0026', "&")

    try {
        $json = $content | ConvertFrom-Json
        $countOriginal = $json.words.Count
        $totalOriginal += $countOriginal

        $processedWords = @()
        $index = 0

        foreach ($rawObj in $json.words) {
            # --- VALIDACIÓN ULTRA-ESTRICTA ---
            $props = $rawObj.PSObject.Properties.Name

            if ($props -notcontains "word") {
                Write-Warning "$($file.Name): Word at index $index missing 'word' field."
                $index++; continue
            }
            if ($props -notcontains "clues") {
                Write-Warning "$($file.Name): Word '$($rawObj.word)' at index $index missing 'clues' field."
                $index++; continue
            }

            if ($null -eq $rawObj.word -or $null -eq $rawObj.clues) {
                 Write-Warning "$($file.Name): Word at index $index has null values."
                 $index++; continue
            }

            $wordStr = $rawObj.word.ToString().Trim()
            if ($wordStr -eq "" -or ($wordStr -split '\s+').Length -gt 3) { $index++; continue }

            # Limpiar pistas
            $cleanClues = @()
            foreach ($c in $rawObj.clues) {
                if ($null -eq $c) { continue }
                $cs = $c.ToString().Replace("_", " ").Trim()
                if ($cs -ne "" -and ($cs -split '\s+').Length -le 3) {
                    $cleanClues += $cs
                }
            }

            if ($cleanClues.Count -eq 0) { $index++; continue }

            # RECONSTRUCCIÓN MANUAL
            $finalObj = [Ordered]@{
                word = $wordStr
                clues = $cleanClues
                category = $categoryName
            }
            $processedWords += New-Object PSObject -Property $finalObj
            $index++
        }

        # DEDUPLICACIÓN
        $uniqueWords = $processedWords | Group-Object { $_.word.ToLower() } | ForEach-Object { $_.Group[0] }

        $countFinal = $uniqueWords.Count
        $totalFinal += $countFinal

        $stats += [PSCustomObject]@{
            Archivo = $file.Name
            Original = $countOriginal
            Final = $countFinal
            Eliminados = $countOriginal - $countFinal
        }

        # 4. Reconstrucción final
        $wordLines = $uniqueWords | ForEach-Object {
            $compact = $_ | ConvertTo-Json -Compress
            "    $compact"
        }

        $finalJson = "{`n  `"words`": [`n" + ($wordLines -join ",`n") + "`n  ]`n}"
        $finalJson = $finalJson.Replace('\u0027', "'").Replace('\u0026', "&")

        [System.IO.File]::WriteAllText($file.FullName, $finalJson, $Utf8NoBom)

    } catch {
        Write-Warning "  !! Error fatal en $($file.Name): $($_.Exception.Message)"
    }
}

Write-Host "`n" + ("=" * 60)
Write-Host "RESUMEN DE INTEGRIDAD"
Write-Host ("=" * 60)
$stats | Format-Table -AutoSize
Write-Host ("-" * 60)
Write-Host "TOTAL PALABRAS ORIGINALES: $totalOriginal"
Write-Host "TOTAL PALABRAS FINALES:    $totalFinal"
Write-Host ("=" * 60)
