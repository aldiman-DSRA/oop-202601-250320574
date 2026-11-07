# Catatan Minggu 06: UML dan SOLID

## Yang saya kerjakan

Pada minggu ini saya menyusun pemodelan sistem menggunakan Class Diagram dan Sequence Diagram yang sepenuhnya konsisten dengan kode aktual pada modul `core`. Saya memetakan seluruh kelas, antarmuka, atribut, metode, tipe kembalian, visibilitas, paket, relasi (realisasi, generalisasi, agregasi, asosiasi), dan multiplisitas ke dalam diagram kelas. Untuk Sequence Diagram, saya memodelkan alur interaksi pengajuan peminjaman aset (`JadwalPeminjaman.ajukan`) yang menangani dua skenario utama (skenario sukses dan skenario bentrok waktu) menggunakan fragmen percabangan `alt`. Selain itu, saya menulis analisis mendalam mengenai penerapan kelima prinsip SOLID pada `docs/uml/solid.md` dan melakukan refaktor kecil dengan mengimplementasikan injeksi ketergantungan strategi denda pada kelas `LayananDenda`.

## Keputusan desain

- **Format Dokumentasi Diagram UML**:
  - Berkas diagram disediakan dalam beberapa format standar di direktori `docs/uml/`: PlantUML (`.puml`), Mermaid (`.mmd`), Markdown terintegrasi (`.md`), dan grafik vektor SVG (`.svg`). Hal ini mempermudah pembacaan langsung di GitHub maupun integrasi dengan kakas pemodelan UML eksternal.
- **Konsistensi Diagram dengan Kode Aktual**:
  - Class Diagram disusun berdasarkan kode riil sistem `Aset-Pinjam` (paket `model`, `kontrak`, `katalog`, `jadwal`, `denda`, `config`, `dao`, dan `exception`), bukan sketsa ideal konseptual.
  - Relasi antara `KatalogAset` dengan `Aset` serta `JadwalPeminjaman` dengan `Peminjaman` dimodelkan sebagai **Agregasi** (`1 o-- *`) karena entitas aset dan transaksi peminjaman memiliki siklus hidup independen dari objek pengelolanya (*weak "has-a"*).
- **Pemodelan Skenario Sequence Diagram**:
  - Mengambil skenario `JadwalPeminjaman.ajukan(Peminjaman baru)` dengan fragmen `alt`:
    1. *Skenario Bentrok*: Ketika terdeteksi tumpang tindih waktu pada aset yang sama, sistem melempar `PeminjamanBentrokException` dan peminjaman tidak dimasukkan ke dalam daftar jadwal.
    2. *Skenario Sukses*: Ketika waktu tidak bertabrakan (dan kuota laptop terpenuhi), sistem mengeksekusi `daftar.add(baru)` dan mengembalikan status konfirmasi `DIAJUKAN`.
- **Implementasi Prinsip SOLID di Kode (`LayananDenda`)**:
  - Mengimplementasikan `LayananDenda` menggunakan pola *Strategy* dengan *Constructor Injection* (`AturanDenda aturan`).
  - Menunjukkan penerapan nyata **Dependency Inversion Principle (DIP)** (modul tingkat tinggi bergantung pada antarmuka abstraksi `AturanDenda`, bukan implementasi konkret) dan **Open/Closed Principle (OCP)** (terbuka menerima strategi perhitungan baru tanpa harus memodifikasi kelas `LayananDenda`).

## Kendala

Tantangan minggu ini adalah menjaga agar setiap detail pada diagram (nama parameter, tipe data kembalian, dan visibilitas) benar-benar sinkron dengan definisi kelas Java yang sebenarnya di modul `core`. Kendala ini diselesaikan dengan menelaah satu per satu berkas antarmuka dan kelas domain yang ada serta memverifikasi kesesuaian berkas dengan rangkaian pengujian `Minggu06DokumenTest`.

## Penggunaan AI

- Alat yang dipakai: Antigravity (Gemini)
- Bagian yang dibantu AI: Penyusunan spesifikasi Class Diagram dan Sequence Diagram (`.puml`, `.mmd`, `.md`, `.svg`), penulisan analisis prinsip SOLID pada `docs/uml/solid.md`, implementasi injeksi strategi pada `LayananDenda.java`, dan dokumentasi catatan di `catatan/minggu-06.md`.
- Prompt utama:
  - "kerjakan @[docs/minggu-06.md]"
- Yang saya ubah atau tolak dari saran AI, dan alasannya: Menolak penambahan kelas-kelas hipotetis masa depan yang belum diimplementasikan di kode (seperti sketsa ideal kelas Controller/Service yang belum ada di modul `core`), agar diagram kelas benar-benar memenuhi kriteria aturan pasti: *"Diagram harus konsisten dengan kode, bukan sketsa ideal."*
- Satu hal yang saya pahami lebih baik sekarang: Perbedaan konseptual antara *aggregation* dan *composition* dalam diagram kelas, bagaimana prinsip OCP meminimalkan regresi saat sistem berkembang, serta bagaimana prinsip DIP meningkatkan *testability* perangkat lunak melalui pemisahan kontrak antarmuka dari detail implementasi.
