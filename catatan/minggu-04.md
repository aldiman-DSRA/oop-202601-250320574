# Catatan Minggu 04: Polymorphism

## Yang saya kerjakan

Minggu ini saya mengimplementasikan konsep polimorfisme, baik dinamis (*method overriding* dan *dynamic binding*) maupun statis (*method overloading*). Pada *method overriding*, saya mengimplementasikan method `hitungDenda(int hariTerlambat)` pada superclass `Aset` dan meng-override perhitungannya pada subclass `Ruang` (multiplier 1x), `Laptop` (multiplier 2x), dan `AlatLab` (multiplier 4x) dengan membaca tarif dasar varian dari `Varian.aktif().dendaPerHari` serta memvalidasi hari negatif agar melempar `IllegalArgumentException`. Pada *method overloading*, saya mengimplementasikan dua variasi method `cari` pada `KatalogAset`: `cari(String kata)` untuk mencari aset berdasarkan nama yang memuat kata kunci pencarian tanpa membedakan huruf besar/kecil (*case-insensitive*), dan `cari(String kata, JenisAset jenis)` untuk menyaring hasil pencarian hanya untuk jenis aset tertentu.

## Keputusan desain

- **Dynamic Method Binding (Overriding)**: Method `hitungDenda(int hariTerlambat)` dideklarasikan pada kelas `Aset` dan di-override secara spesifik di setiap subclass (`Ruang`, `Laptop`, `AlatLab`). Hal ini memungkinkan pemanggil memanggil `aset.hitungDenda(hari)` pada tipe referensi `Aset` dan Java secara dinamis saat runtime (*dynamic dispatch*) akan mengeksekusi logika denda yang sesuai dengan jenis objek riilnya.
- **Validasi Nilai Hari Keterlambatan**: Pada setiap implementasi `hitungDenda`, dilakukan pengecekan kondisi jika `hariTerlambat < 0`, maka sistem melempar `IllegalArgumentException` sesuai spesifikasi aturan bisnis. Jika `hariTerlambat == 0`, denda yang dihasilkan adalah 0 rupiah.
- **Method Overloading pada `KatalogAset`**:
  - `cari(String kata)`: Mengembalikan daftar aset yang namanya mengandung substring `kata` secara *case-insensitive* dengan memanfaatkan perbandingan huruf kecil (`toLowerCase()`).
  - `cari(String kata, JenisAset jenis)`: Menggunakan ulang method `cari(kata)` (*delegation*) dan kemudian menyaring hasilnya berdasarkan enum `jenis` yang cocok. Hal ini meminimalkan duplikasi logika pencarian nama (*DRY - Don't Repeat Yourself*).
- **Perbedaan Overloading dan Overriding**:
  - Overloading (`cari`): Terjadi pada kelas yang sama (`KatalogAset`) dengan nama method sama tetapi parameter/signature berbeda. Resolusi pemanggilan ditentukan pada saat kompilasi (*compile-time polymorphism* / *static binding*).
  - Overriding (`hitungDenda`): Terjadi antar superclass dan subclass dengan signature method yang persis sama. Resolusi pemanggilan ditentukan pada saat program berjalan (*runtime polymorphism* / *dynamic binding*) berdasarkan tipe objek sebenarnya.

## Kendala

Tantangan pada materi ini adalah memahami dengan jelas kapan dynamic binding terjadi dan memastikan perhitungan denda tetap akurat sesuai varian NIM mahasiswa yang dibaca dari `Varian.aktif().dendaPerHari`. Selain itu, pencarian string perlu dipastikan case-insensitive dan menangani kemungkinan input null secara aman.

## Penggunaan AI

- Alat yang dipakai: Antigravity (Gemini)
- Bagian yang dibantu AI: Implementasi overriding `hitungDenda(int)` pada `Aset.java`, `Ruang.java`, `Laptop.java`, `AlatLab.java`, implementasi overloading `cari(...)` pada `KatalogAset.java`, serta penyusunan catatan refleksi `catatan/minggu-04.md`.
- Prompt utama:
  - "kerjakan @[docs/minggu-04.md]"
- Yang saya ubah atau tolak dari saran AI, dan alasannya: Memastikan implementasi `cari(String kata, JenisAset jenis)` memanfaatkan pendelegasian ke `cari(kata)` agar menghindari duplikasi kode dan menjaga struktur kode tetap bersih.
- Satu hal yang saya pahami lebih baik sekarang: Perbedaan mendasar antara *static binding* (overloading) yang ditentukan saat fase kompilasi berdasarkan signature argumen, dan *dynamic binding* (overriding) yang memanfaatkan tabel metode virtual (*vtable*) saat runtime untuk mengeksekusi metode milik tipe instansi konkret.
