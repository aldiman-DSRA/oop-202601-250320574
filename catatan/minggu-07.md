# Catatan Minggu 07: Koleksi

## Yang saya kerjakan

Pada minggu ini saya mengimplementasikan manipulasi struktur data koleksi (List dan Map) serta pengurutan data dengan `Comparator` pada kelas `JadwalPeminjaman.java`. Empat metode utama yang saya selesaikan adalah:
1. `bentrok(Peminjaman baru)`: Mendeteksi tumpang tindih rentang waktu pada aset yang sama.
2. `semuaUrutMulai()`: Mengembalikan daftar seluruh peminjaman yang terurut menaik (*ascending*) berdasarkan waktu mulai peminjaman (`getMulai()`).
3. `perAset()`: Mengelompokkan seluruh transaksi peminjaman ke dalam `Map<String, List<Peminjaman>>` berdasarkan kode aset menggunakan Java Stream `Collectors.groupingBy`.
4. `hapus(String id)`: Menghapus peminjaman dari jadwal berdasarkan ID-nya dan mengembalikan nilai boolean penanda keberhasilan.

Selain itu, saya merumuskan keputusan batas waktu bentrok pada `docs/asumsi.md` (entri A-01) dan membuktikannya lewat pengujian unit buatan sendiri di `core/src/test/java/id/ac/upb/asetpinjam/mahasiswa/BatasBentrokTest.java`.

## Keputusan desain

- **Perilaku di Batas Waktu Bentrok (A-01 di `docs/asumsi.md`)**:
  - Pertanyaan: Apakah peminjaman 09:00-11:00 dan 11:00-13:00 pada aset yang sama bentrok?
  - Keputusan: **TIDAK BENTROK** (peminjaman berurutan / *back-to-back* diizinkan).
  - Alasan: Rentang peminjaman dimodelkan sebagai interval waktu setengah terbuka `[mulai, selesai)`. Titik waktu selesai (11:00:00) merupakan momen serah terima atau pengembalian aset, sehingga aset langsung siap digunakan oleh peminjam berikutnya tepat pada jam tersebut. Hal ini memaksimalkan utilisasi aset tanpa waktu menganggur (*idle time*). Logika yang digunakan:
    `baru.getMulai().isBefore(existing.getSelesai()) && baru.getSelesai().isAfter(existing.getMulai())`.
    Kondisi ini memastikan bahwa saat `baru.mulai.equals(existing.selesai)` nilainya adalah `false`.
- **Struktur Data Internal (`List` saja vs `Map`/`Set`)**:
  - Keputusan: Tetap menggunakan `List<Peminjaman>` (`ArrayList`) sebagai penyimpan utama di memori.
  - Alasan:
    1. Kelas menyediakan metode `protected List<Peminjaman> daftarMutable()` yang diharapkan mengembalikan `List`. Mengubah struktur internal menjadi `Map` atau `Set` murni akan merusak kontrak metode tersebut.
    2. Menghindari overhead sinkronisasi dua struktur data ganda (misal List bersamaan dengan Map/Set). Menjaga dua struktur data secara paralel rentan memicu ketidaksinkronan (*state inconsistency*) ketika ada operasi penambahan atau penghapusan.
    3. Untuk volume data peminjaman di memori, `ArrayList` memiliki *cache locality* yang sangat baik dan operasi `removeIf`, stream sorting, serta grouping sangat cepat.
    4. Pengelompokan `perAset()` dapat dibentuk secara dinamis dan fungsional melalui `Collectors.groupingBy` tanpa membebani penyimpanan tetap.
- **Immutability pada Pengurutan**:
  - Metode `semuaUrutMulai()` tidak memutasi koleksi `daftar` asli di tempat (*in-place mutation*), melainkan mengalirkan stream terurut dan mengembalikan list baru (`.toList()`), sehingga data asal tetap aman dari efek samping (*side effects*).

## Kendala

Tantangan minggu ini adalah memastikan logika bentrok di batas waktu berlaku simetris dan konsisten baik ketika peminjaman baru berada tepat sebelum peminjaman yang ada, maupun ketika berada tepat setelahnya. Hal ini diatasi dengan merancang dua pengujian terarah pada `BatasBentrokTest` untuk memverifikasi kedua skenario batas tersebut.

## Penggunaan AI

- Alat yang dipakai: Antigravity (Gemini)
- Bagian yang dibantu AI: Implementasi metode `bentrok`, `semuaUrutMulai`, `perAset`, dan `hapus` pada `JadwalPeminjaman.java`, penulisan unit test `BatasBentrokTest.java`, perumusan asumsi A-01 pada `docs/asumsi.md`, serta dokumentasi refleksi di `catatan/minggu-07.md`.
- Prompt utama:
  - "kerjakan @[docs/minggu-07.md]"
- Yang saya ubah atau tolak dari saran AI, dan alasannya: Memastikan pengurutan pada `semuaUrutMulai()` tidak menggunakan `Collections.sort(daftar)` secara langsung pada list internal agar tidak merusak urutan asli penambahan data (*insertion order*), melainkan menggunakan Stream API dengan `Comparator.comparing(Peminjaman::getMulai)`.
- Satu hal yang saya pahami lebih baik sekarang: Pemanfaatan Stream Collector (`Collectors.groupingBy`) untuk mengelompokkan data koleksi secara deklaratif, serta perlakuan interval waktu setengah terbuka `[start, end)` dalam sistem penjadwalan riil.
