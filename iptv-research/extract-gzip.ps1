$inputFile = "epg-id.xml.gz"
$outputFile = "epg-id.xml"

$inputStream = [System.IO.File]::OpenRead($inputFile)
$gzipStream = New-Object System.IO.Compression.GZipStream($inputStream, [System.IO.Compression.CompressionMode]::Decompress)
$outputStream = [System.IO.File]::Create($outputFile)

$buffer = New-Object byte[](1024)
while (($count = $gzipStream.Read($buffer, 0, $buffer.Length)) -gt 0) {
    $outputStream.Write($buffer, 0, $count)
}

$gzipStream.Close()
$outputStream.Close()
$inputStream.Close()

Write-Host "File extracted successfully to $outputFile"
