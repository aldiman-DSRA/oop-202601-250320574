# Catatan Minggu 01: Paradigma Pemrograman dan Setup

## Yang saya kerjakan

Pada minggu pertama ini, saya menyiapkan lingkungan pengembangan dasar yang mencakup JDK 21, Maven, Git, dan struktur repositori proyek `Aset-Pinjam`. Saya mengisi berkas identitas mahasiswa pada `core/src/main/resources/mahasiswa.properties` dengan NIM (`250320574`) dan nama lengkap saya (`ALDIMAN`). Selain itu, saya mengimplementasikan tiga gaya paradigma pemrograman (prosedural, OOP, dan fungsional) pada berkas `HelloProsedural.java`, `HelloOOP.java`, dan `HelloFungsional.java` agar menghasilkan format sapaan yang konsisten dan memenuhi seluruh pengujian pada `Minggu01Test`.

## Keputusan desain

- **Format Teks Sapaan yang Konsisten**:
  Membuat format kalimat sapaan `"selamat datang di Aset-Pinjam, " + nama + " (" + nim + ")"` yang seragam pada ketiga gaya pemrograman untuk memenuhi aturan pasti pengujian otomatis (memuat teks `"Aset-Pinjam"`, nama, dan NIM).
- **Penerapan Ketiga Paradigma**:
  - **Prosedural**: Menggunakan fungsi statis murni `HelloProsedural.sapa(nama, nim)` tanpa pembentukan objek *instance*.
  - **OOP**: Menggunakan kelas `HelloOOP` dengan enkapsulasi field *private* (`nama`, `nim`), konstruktor inisialisasi, dan metode instans `sapa()`.
  - **Fungsional**: Menggunakan ekspresi lambda melalui antarmuka fungsional `Function<String, String>` pada metode `HelloFungsional.pembuatSapaan(nim)`.
- **Analisis Kelebihan dan Keterbatasan Tiap Gaya untuk Sistem Peminjaman Aset**:
  1. **Gaya Prosedural**:
     - *Kelebihan*: Alur eksekusi langsung dan sederhana untuk komputasi atau prosedur utilitas kalkulasi linear tanpa perlu mengalokasikan memori untuk objek baru.
     - *Keterbatasan*: Sulit memodelkan entitas bisnis yang memiliki banyak atribut dan perubahan *state* (seperti siklus status aset dan riwayat peminjaman), serta rawan modifikasi data global yang tidak terkontrol.
  2. **Gaya OOP (Object-Oriented Programming)**:
     - *Kelebihan*: Sangat ideal untuk domain sistem peminjaman karena mampu membungkus data (*state*) dan perilaku (*behavior*) secara utuh (enkapsulasi), serta memudahkan ekstensi berbagai tipe aset (ruang, laptop, alat lab) melalui pewarisan dan polimorfisme.
     - *Keterbatasan*: Membutuhkan struktur arsitektur kelas yang lebih kompleks dan *boilerplate code* yang lebih banyak untuk operasi yang sebenarnya sederhana.
  3. **Gaya Fungsional**:
     - *Kelebihan*: Fungsi bersifat *pure* dan data bersifat *immutable* (nir-mutasi), sangat andal dan aman dari *side-effects* terutama saat melakukan filter, pemetaan (*mapping*), dan pencarian daftar katalog aset.
     - *Keterbatasan*: Kurang intuitif dalam memodelkan transisi status mesin (*state machine*) transaksi peminjaman yang terus berubah dari waktu ke waktu (`DIAJUKAN` $\rightarrow$ `DISETUJUI` $\rightarrow$ `DIPINJAM` $\rightarrow$ `DIKEMBALIKAN`).

## Kendala

Sempat mengalami kendala saat menjalankan skrip pengujian lokal `scripts/uji.ps1` di terminal PowerShell Windows akibat pembatasan kebijakan eksekusi skrip (*Execution Policy*). Kendala ini berhasil diatasi dengan menjalankan perintah menggunakan flag `-ExecutionPolicy Bypass` pada PowerShell atau menggunakan Git Bash untuk mengeksekusi `bash scripts/uji.sh`.

## Penggunaan AI

- Alat yang dipakai: Antigravity (Gemini)
- Bagian yang dibantu AI: Memahami struktur perbandingan kode pada `HelloProsedural.java`, `HelloOOP.java`, dan `HelloFungsional.java`, serta penyusunan analisis perbandingan paradigma.
- Prompt utama:
  - "bagaimana cara melengkapi file HelloProsedural, HelloOOP, dan HelloFungsional untuk minggu 1?"
- Yang saya ubah atau tolak dari saran AI, dan alasannya: Menyesuaikan kembali teks sapaan agar persis memuat token `"Aset-Pinjam"`, nama, dan NIM sesuai kontrak pengujian `Minggu01Test`.
- Satu hal yang saya pahami lebih baik sekarang: Perbedaan mendasar penerapan fungsi statis (prosedural), enkapsulasi objek (OOP), dan ekspresi fungsi nir-state/lambda (fungsional) di dalam ekosistem Java modern.
