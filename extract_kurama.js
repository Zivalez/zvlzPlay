const puppeteer = require('puppeteer-extra');
const StealthPlugin = require('puppeteer-extra-plugin-stealth');

// Gunakan Stealth Plugin untuk menghindari deteksi bot standar
puppeteer.use(StealthPlugin());

(async () => {
    // Setting browser agar terlihat
    const browser = await puppeteer.launch({
        headless: false, // Wajib false biar bisa liat & solve captcha manual
        defaultViewport: null,
        args: [
            '--start-maximized', // Biar enak liatnya
            '--disable-web-security',
            '--disable-features=IsolateOrigins,site-per-process' // Kadang bantu bypass iframe issue
        ]
    });

    const page = await browser.newPage();

    // Setup Interceptor/Listener untuk Network Request
    // Kita cari request XHR/Fetch yang sukses (200) dan isinya bukan cuma angka "1"
    page.on('response', async (response) => {
        const request = response.request();
        const resourceType = request.resourceType();
        const url = response.url();

        // Filter hanya tipe XHR (Ajax) atau Fetch
        if (['xhr', 'fetch'].includes(resourceType)) {
            try {
                // Pastikan request sukses
                if (response.status() === 200) {
                    const text = await response.text();

                    // Filter: Jangan print kalau responnya cuma "1" (biasanya check-episode)
                    if (text.trim() !== '1') {
                        console.log('================================================');
                        console.log(`[${resourceType.toUpperCase()}] URL: ${url}`);
                        console.log('--- Request Headers ---');
                        console.log(request.headers()); // Ini yang kita butuhkan untuk ditiru di Kotlin
                        console.log('--- Response Preview (First 100 chars) ---');
                        console.log(text.substring(0, 100));
                        console.log('================================================\n');
                    }
                }
            } catch (err) {
                // Ignore error (misal response berupa gambar/binary yg gagal di-text())
            }
        }
    });

    console.log('🚀 Membuka halaman target...');
    const targetUrl = 'https://v15.kuramanime.tel/anime/4507/majutsushi-kunon-wa-mieteiru/episode/9';

    // Pergi ke URL
    await page.goto(targetUrl, { waitUntil: 'networkidle2', timeout: 60000 });

    console.log('⏳ Menunggu elemen download render... (Silakan selesaikan Captcha jika muncul)');

    try {
        // TUNGGU sampai tag <a> di dalam #animeDownloadLink muncul
        // Timeout 60 detik buat jaga-jaga kalau kamu lama solve captcha
        await page.waitForSelector('#animeDownloadLink a[href]', { timeout: 60000 });

        console.log('✅ DOM Terdeteksi! Link sudah di-render oleh Browser/JS.');

        // Extract Link Structure
        const extractedData = await page.evaluate(() => {
            const container = document.getElementById('animeDownloadLink');
            if (!container) return null;

            const results = [];
            let currentQuality = 'Unknown';
            const children = Array.from(container.children);

            children.forEach(el => {
                // Header Kualitas biasanya di tag h6
                if (el.tagName === 'H6') {
                    currentQuality = el.innerText.trim();
                }
                // Link download ada di tag a
                else if (el.tagName === 'A') {
                    results.push({
                        quality: currentQuality,
                        text: el.innerText.trim(),
                        url: el.href,
                        isDisabled: el.classList.contains('link-disabled') // Info tambahan
                    });
                }
            });
            return results;
        });

        console.log('\n📦 HASIL EKSTRAKSI AKHIR:');
        console.log(JSON.stringify(extractedData, null, 2));

    } catch (e) {
        console.error('❌ Gagal mendapatkan elemen link. Timeout atau masih terdeteksi bot.');
        console.error(e);
    } finally {
        // Uncomment baris bawah kalau mau browser langsung nutup otomatis
        // await browser.close();
    }
})();