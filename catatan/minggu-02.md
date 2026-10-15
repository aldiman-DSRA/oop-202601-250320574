# Catatan Minggu 02: Struktur OOP — Class, Object, Encapsulation

## Yang saya kerjakan

Minggu ini saya mengimplementasikan class `Aset` di `core/src/main/java/id/ac/upb/asetpinjam/model/Aset.java`. Saya menambahkan empat field private (`kode`, `nama`, `status`, `kondisi`), konstruktor dengan validasi null/kosong, getter untuk semua field, serta setter untuk `nama`, `status`, dan `kondisi` dengan validasi masing-masing. Field `kode` sengaja tidak diberi setter karena kode aset tidak boleh berubah setelah dibuat. Nilai awal status diset `TERSEDIA` dan kondisi diset `BAIK` sesuai aturan yang ditetapkan.

## Keputusan desain

Saya memutuskan field `kode` dideklarasikan sebagai `final` agar secara sintaks pun tidak bisa diubah, bukan sekadar tidak menyediakan setter. Ini lebih aman daripada hanya mengandalkan ketiadaan setter karena compiler langsung melarang assignment ulang. Untuk validasi string kosong, saya menggunakan `isBlank()` (bukan hanya `isEmpty()`) agar string yang hanya berisi spasi juga ditolak. Saya tidak menerapkan normalisasi (trim/toUpperCase) pada kode maupun nama karena tidak ada aturan eksplisit untuk itu; keputusan ini dicatat di `docs/asumsi.md`.

## Kendala

Awalnya saya ragu apakah perlu menggunakan `isEmpty()` atau `isBlank()` untuk validasi string. Setelah membaca tes `kodeKosongDitolak()` yang menguji string `"   "` (hanya spasi), saya sadar harus pakai `isBlank()`. Selain itu, tes `semuaFieldPrivatDanKodeTidakBisaDiubah()` memverifikasi secara refleksi bahwa tidak ada method `setKode`, sehingga saya hanya perlu memastikan method tersebut memang tidak dibuat.

## Penggunaan AI

- Alat yang dipakai: Kiro (Claude)
- Bagian yang dibantu AI: pembuatan file `catatan/minggu-02.md` (laporan ini)
- Prompt utama: "buatkan laporan dari hasil tugas minggu-02 dan buatkan file catatan/minggu-02.md"
- Yang saya ubah atau tolak dari saran AI, dan alasannya: isi laporan disesuaikan dengan keputusan desain yang benar-benar saya ambil saat menulis kode (misalnya alasan pakai `final` dan `isBlank()`), bukan sekadar penjelasan generik tentang encapsulation.
- Satu hal yang saya pahami lebih baik sekarang: Perbedaan antara `isEmpty()` dan `isBlank()` di Java — `isBlank()` mendeteksi string yang hanya berisi whitespace, sedangkan `isEmpty()` hanya mengecek panjang nol. Untuk validasi input pengguna, `isBlank()` jauh lebih tepat.
