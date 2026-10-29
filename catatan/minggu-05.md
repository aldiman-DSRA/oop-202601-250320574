# Catatan Minggu 05: Abstraction

## Yang saya kerjakan

Pada minggu ini saya menerapkan konsep abstraksi menggunakan *abstract class* dan *interface*. Saya mengubah kelas `Aset` menjadi kelas abstrak (*abstract class*) serta menjadikan method `hitungDenda(int)` dan `getJenis()` sebagai *abstract method* karena implementasi konkretnya ditentukan oleh masing-masing subclass. Selain itu, saya mengimplementasikan method `bisaDipinjam()` pada kelas `Aset` (sesuai kontrak interface `DapatDipinjam`) dengan aturan bahwa aset hanya bisa dipinjam jika berstatus `TERSEDIA` dan kondisi bukan `RUSAK_BERAT`. Terakhir, saya mengimplementasikan interface `AturanDenda` pada kelas `DendaFlat` (denda tetap per hari) dan `DendaProgresif` (denda bertingkat/tier progresif yang makin mahal seiring bertambahnya hari keterlambatan).

## Keputusan desain

- **Pemilihan Abstract Class vs Interface**:
  - `Aset` dijadikan `abstract class` karena merepresentasikan entitas dasar dalam domain sistem yang memiliki atribut bersama (*state*: `kode`, `nama`, `status`, `kondisi`) serta perilaku bersama yang konkret (seperti konstruktor, validasi, getter/setter, dan `bisaDipinjam()`), sementara implementasi spesifik seperti `hitungDenda` dan `getJenis` didelegasikan ke subclass melalui deklarasi *abstract*.
  - `DapatDipinjam` dan `AturanDenda` didefinisikan sebagai `interface` karena keduanya berfungsi sebagai kontrak perilaku (*behavioral contract*) murni tanpa state, mendukung fleksibilitas peminjaman dan strategi perhitungan denda yang dapat diganti sewaktu-waktu (*Strategy pattern*).
- **Implementasi `bisaDipinjam()` pada Superclass `Aset`**:
  - Logika peminjaman `this.status == StatusAset.TERSEDIA && this.kondisi != Kondisi.RUSAK_BERAT` ditempatkan langsung di kelas `Aset`. Subclass tidak perlu menduplikasi logika ini karena aturan kelaikan peminjaman berlaku seragam untuk semua tipe aset.
- **Perhitungan Denda Progresif (Tercatat di `docs/asumsi.md`)**:
  - `DendaFlat`: Menghitung denda secara konstan sesuai dengan `aset.hitungDenda(hariTerlambat)`.
  - `DendaProgresif`: Dihitung dengan skema *tiered surcharge* (`dendaFlat + tambahan`):
    - Hari 0: Rp 0.
    - Hari 1-7 (keterlambatan awal): tarif flat (100% tarif harian).
    - Hari 8-14 (keterlambatan sedang): denda flat ditambah surcharge 50% tarif harian dasar per hari untuk hari ke-8 s.d. 14.
    - Hari 15 ke atas (keterlambatan berat): akumulasi surcharge minggu sebelumnya ditambah surcharge 100% tarif harian dasar per hari untuk hari ke-15 dan seterusnya.
  - Formula ini menjamin pemenuhan seluruh kontrak pengujian: nilai selalu 0 pada 0 hari, tidak pernah lebih murah dari denda flat, monoton naik (tidak pernah turun seiring bertambahnya hari), dan pada hari ke-30 dipastikan jauh lebih mahal dari denda flat.
- **Validasi Argumen**:
  - Parameter `aset` divalidasi tidak boleh `null` (`IllegalArgumentException`).
  - Parameter `hariTerlambat` tidak boleh bernilai negatif (`IllegalArgumentException`).

## Kendala

Tantangan minggu ini adalah memastikan seluruh pengujian lama (seperti `Minggu02AsetTest`) tetap berjalan normal setelah kelas `Aset` menjadi abstrak. Pemahaman terhadap pemanfaatan refleksi pada pembantu tes `TestSupport.aset(...)` yang otomatis memakai subclass konkret `Ruang` saat `Aset` menjadi abstrak membuktikan pentingnya pengujian yang terisolasi dengan baik. Selain itu, formulasi denda progresif disusun agar menggunakan perhitungan aritmetika integer murni tanpa risiko kesalahan pembulatan desimal.

## Penggunaan AI

- Alat yang dipakai: Antigravity (Gemini)
- Bagian yang dibantu AI: Pengubahan kelas `Aset` menjadi abstrak, implementasi metode `bisaDipinjam()`, implementasi kelas `DendaFlat` dan `DendaProgresif`, pencatatan asumsi pada `docs/asumsi.md`, serta dokumentasi refleksi di `catatan/minggu-05.md`.
- Prompt utama:
  - "kerjakan @[docs/minggu-05.md]"
- Yang saya ubah atau tolak dari saran AI, dan alasannya: Memilih formula progresif berbasis penambahan bertingkat (*tiered additive surcharge*) `flat + tambahan` dibanding eksponensial murni, agar lebih realistis untuk aturan bisnis peminjaman aset institusi dan secara matematis terbukti selalu non-decreasing serta lebih besar dari flat.
- Satu hal yang saya pahami lebih baik sekarang: Perbedaan peruntukan antara *abstract class* (mewakili hubungan pewarisan *is-a* dengan state dan logika bersama) dan *interface* (mewakili kapabilitas atau kontrak *can-do* yang sepenuhnya memisahkan definisi antarmuka dari implementasinya).
