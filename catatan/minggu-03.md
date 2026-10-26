# Catatan Minggu 03: Inheritance

## Yang saya kerjakan

Minggu ini saya mengimplementasikan hierarki pewarisan (inheritance) untuk class aset dengan membuat tiga subclass dari `Aset`, yaitu `Ruang`, `Laptop`, dan `AlatLab` di package `id.ac.upb.asetpinjam.model`. Masing-masing subclass dilengkapi dengan field spesifik (`kapasitas` pada `Ruang`; `spesifikasi` dan `nomorInventaris` pada `Laptop`; `laboratorium` dan `tingkatRisiko` pada `AlatLab`), konstruktor yang memanggil `super(kode, nama)` untuk menggunakan ulang validasi kelas induk sekaligus melakukan validasi data khusus, getter untuk atribut terkait, serta override method `getJenis()` yang mengembalikan nilai enum `JenisAset` yang sesuai (`RUANG`, `LAPTOP`, `ALAT_LAB`).

## Keputusan desain

- **Pemisahan Field Induk dan Subclass**: Field yang bersifat umum dan relevan untuk semua aset (`kode`, `nama`, `status`, `kondisi`) tetap berada di superclass `Aset`. Field yang spesifik hanya untuk jenis tertentu ditempatkan di subclass masing-masing agar class induk tidak terbebani atribut yang tidak relevan.
- **Enkapsulasi dan Immutability**: Seluruh field pada subclass (`kapasitas`, `spesifikasi`, `nomorInventaris`, `laboratorium`, `tingkatRisiko`) dideklarasikan sebagai `private final` karena tidak disediakan setter dan nilainya ditetapkan saat instansiasi objek.
- **Pemanfaatan `super()`**: Pemanggilan `super(kode, nama)` diletakkan di baris pertama konstruktor setiap subclass sehingga validasi data dasar (kode dan nama tidak boleh null atau kosong) otomatis dijalankan oleh class induk.
- **Validasi Nilai Atribut**:
  - `Ruang`: `kapasitas` divalidasi harus lebih besar dari 0 (`kapasitas <= 0` melempar `IllegalArgumentException`).
  - `Laptop`: `spesifikasi` dan `nomorInventaris` divalidasi tidak boleh null atau kosong menggunakan `isBlank()` agar string yang hanya berisi whitespace juga ditolak.
  - `AlatLab`: `laboratorium` divalidasi menggunakan `isBlank()` dan `tingkatRisiko` dicek tidak boleh null.
- **Keterbatasan Inheritance dibanding Komposisi**:
  Salah satu contoh pada sistem ini di mana inheritance kurang tepat dibanding komposisi adalah pemodelan aturan perhitungan denda atau hak peminjaman berdasarkan peran peminjam dan karakteristik aset. Jika kita mencoba menggunakan inheritance untuk membedakan variasi aset (misalnya membuat subclass `LaptopMahasiswa`, `LaptopDosen`, `RuangKhusus`), akan terjadi ledakan kombinasi kelas (*class explosion*) dan hierarki menjadi kaku karena Java hanya mendukung *single inheritance*. Selain itu, inheritance tidak memungkinkan aset mengubah perilakunya secara dinamis saat runtime. Pendekatan komposisi (seperti pola *Strategy* untuk aturan denda) jauh lebih fleksibel karena objek aset cukup menyimpan referensi ke objek strategi denda (*has-a* alih-alih *is-a*).

## Kendala

Tantangan utama adalah memastikan urutan penanganan validasi dan pengecekan exception sesuai dengan perilaku pewarisan di Java. Karena `super(kode, nama)` wajib dipanggil di baris awal konstruktor, validasi `kode` dan `nama` di kelas induk akan selalu dieksekusi lebih dulu sebelum validasi atribut subclass. Selain itu, saya harus memastikan penggunaan `isBlank()` secara konsisten untuk semua atribut String agar data yang hanya berisi spasi ditolak dengan `IllegalArgumentException`.

## Penggunaan AI

- Alat yang dipakai: Antigravity (Gemini)
- Bagian yang dibantu AI: Implementasi subclass `Ruang.java`, `Laptop.java`, `AlatLab.java`, verifikasi pengujian unit test minggu 3, serta penyusunan laporan refleksi `catatan/minggu-03.md`.
- Prompt utama:
  - "kerjakan tugas @[docs/minggu-03.md]"
  - "sampai minggu 03 saja"
  - "keejakan catatan @[docs/minggu-03.md]"
- Yang saya ubah atau tolak dari saran AI, dan alasannya: Membatasi implementasi hanya pada scope minggu 3 (tidak mengimplementasikan method untuk minggu 4 seperti `hitungDenda` atau minggu 5 lebih awal), serta memastikan semua atribut baru di subclass berstatus `private final` demi menjaga prinsip enkapsulasi.
- Satu hal yang saya pahami lebih baik sekarang: Bagaimana subclass dapat mewarisi dan menggunakan kembali kode superclass lewat pemanggilan `super(...)`, serta memahami batasan inheritance (*fragile base class* dan kekakuan hierarki) dibandingkan dengan fleksibilitas yang ditawarkan oleh pendekatan komposisi.
