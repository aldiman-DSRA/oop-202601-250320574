# Analisis dan Penerapan Prinsip SOLID pada Sistem Aset-Pinjam

Dokumen ini menjelaskan penerapan prinsip-prinsip desain berorientasi objek **SOLID** (*Single Responsibility, Open/Closed, Liskov Substitution, Interface Segregation, Dependency Inversion*) pada basis kode sistem `Aset-Pinjam`.

---

## 1. Single Responsibility Principle (SRP)

> *"A class should have one, and only one, reason to change."*

### Kelas Terkait:
- `id.ac.upb.asetpinjam.model.Aset`
- `id.ac.upb.asetpinjam.katalog.KatalogAset`
- `id.ac.upb.asetpinjam.model.Peminjaman`
- `id.ac.upb.asetpinjam.jadwal.JadwalPeminjaman`

### Alasan dan Bukti Desain:
1. **Pemisahan Entitas dan Koleksi**:
   - Kelas `Aset` hanya bertanggung jawab merepresentasikan *state* dan validasi suatu aset individual (kode, nama, status ketersediaan, kondisi fisik, kelaikan pinjam, serta perhitungan tarif denda dasar jenis tersebut). `Aset` tidak bertanggung jawab atas penyimpanan banyak aset atau algoritma pencarian.
   - Tanggung jawab pengelolaan kumpulan aset, penambahan ke memori, dan penyaringan (*search/filter*) berdasarkan kata kunci nama atau jenis aset didelegasikan sepenuhnya ke kelas `KatalogAset`.
2. **Pemisahan Transaksi dan Penjadwalan**:
   - Kelas `Peminjaman` memodelkan satu transaksi peminjaman (siapa peminjamnya, aset apa yang dipinjam, kapan rentang waktunya, dan status siklus hidupnya).
   - Pengelolaan relasi antar banyak peminjaman, pemeriksaan bentrok waktu (*conflict detection*), dan penegakan kuota peminjam ditangani secara terpisah oleh kelas `JadwalPeminjaman`.

Dengan pemisahan ini, perubahan pada aturan pencarian aset tidak akan memengaruhi kelas `Aset`, dan perubahan mekanisme deteksi bentrok tidak akan mengubah struktur kelas `Peminjaman`.

---

## 2. Open/Closed Principle (OCP)

> *"Software entities should be open for extension, but closed for modification."*

### Kelas Terkait:
- `id.ac.upb.asetpinjam.model.Aset` beserta subclass `Ruang`, `Laptop`, dan `AlatLab`
- `id.ac.upb.asetpinjam.kontrak.AturanDenda` beserta kelas `DendaFlat`, `DendaProgresif`, dan `LayananDenda`

### Alasan dan Bukti Desain:
1. **Hierarki Aset**:
   - Kelas `Aset` didefinisikan sebagai kelas abstrak (*abstract class*) dengan metode abstrak `getJenis()` dan `hitungDenda(int)`.
   - Ketika sistem memerlukan tipe aset baru (misalnya `Kendaraan` atau `Proyektor`), pengembang cukup memperluas (*extends*) kelas `Aset` dan mengimplementasikan metode yang dibutuhkan. Kode kelas `Aset`, `KatalogAset`, maupun kode pemanggil lainnya tidak perlu diubah atau dimodifikasi sedikit pun.
2. **Strategi Perhitungan Denda**:
   - Interface `AturanDenda` menjadi titik ekstensi (*extension point*). Strategi denda flat diimplementasikan pada `DendaFlat`, dan strategi denda bertingkat diimplementasikan pada `DendaProgresif`.
   - Kelas `LayananDenda` memanggil `aturan.hitung(aset, hariTerlambat)`. Jika di masa mendatang dibutuhkan skema denda baru (seperti denda diskon hari libur atau denda musiman), kita cukup membuat kelas baru yang mengimplementasikan `AturanDenda` tanpa mengubah satu baris pun kode pada `LayananDenda`.

---

## 3. Liskov Substitution Principle (LSP)

> *"Subtypes must be substitutable for their base types without altering the correctness of the program."*

### Kelas Terkait:
- Superclass `id.ac.upb.asetpinjam.model.Aset`
- Subclass `Ruang`, `Laptop`, dan `AlatLab`

### Alasan dan Bukti Desain:
- Seluruh subclass dari `Aset` dapat disubstitusikan di mana pun referensi tipe `Aset` diharapkan (misalnya pada parameter metode `KatalogAset.tambah(Aset)`, `Peminjaman(..., Aset, ...)`, atau `AturanDenda.hitung(Aset, ...)`).
- Subclass tidak memperketat prakondisi (*preconditions*) kontrak superclass: metode `hitungDenda(int hariTerlambat)` pada `Ruang`, `Laptop`, maupun `AlatLab` seragam menerima nilai `hariTerlambat >= 0` dan melempar `IllegalArgumentException` hanya bila hari bernilai negatif, persis seperti kontrak pada superclass.
- Subclass tidak melemahkan pascakondisi (*postconditions*): seluruh implementasi `hitungDenda` selalu menghasilkan nilai denda bertipe `long` yang proporsional dan non-negatif.
- Tidak ada subclass yang melempar perkecualian (*exception*) tak terduga yang tidak diantisipasi oleh superclass. Pemanggil yang berinteraksi dengan `Aset` tidak perlu mengetahui tipe riil objek di balik layar untuk mendapatkan perilaku yang benar dan konsisten.

---

## 4. Interface Segregation Principle (ISP)

> *"Clients should not be forced to depend upon interfaces that they do not use."*

### Kelas Terkait:
- `id.ac.upb.asetpinjam.kontrak.DapatDipinjam`
- `id.ac.upb.asetpinjam.kontrak.AturanDenda`

### Alasan dan Bukti Desain:
- Daripada membuat satu antarmuka gemuk (*fat interface*) serbaguna yang menggabungkan perilaku peminjaman dan denda (misalnya antarmuka `OperasiAset` yang berisi `bisaDipinjam()`, `hitungDenda()`, dan `simpanKeDatabase()`), antarmuka dipecah menjadi kontrak-kontrak kecil yang sangat kohesif:
  - `DapatDipinjam`: hanya mendefinisikan metode tunggal `boolean bisaDipinjam()`. Digunakan oleh pemanggil yang hanya berkepentingan memeriksa status dan kondisi kelaikan peminjaman suatu aset.
  - `AturanDenda`: hanya mendefinisikan metode tunggal `long hitung(Aset aset, int hariTerlambat)`. Digunakan oleh komponen yang menangani perhitungan sanksi finansial keterlambatan.
- Klien yang hanya membutuhkan pengecekan ketersediaan tidak dipaksa untuk mengetahui atau mengimplementasikan logika denda.

---

## 5. Dependency Inversion Principle (DIP)

> *"High-level modules should not depend on low-level modules. Both should depend on abstractions. Abstractions should not depend on details. Details should depend on abstractions."*

### Kelas Terkait:
- `id.ac.upb.asetpinjam.denda.LayananDenda`
- `id.ac.upb.asetpinjam.kontrak.AturanDenda`
- `id.ac.upb.asetpinjam.dao.AsetDAO` dan `id.ac.upb.asetpinjam.dao.PeminjamanDAO`

### Alasan dan Bukti Desain:
1. **Injeksi Ketergantungan pada Layanan Denda**:
   - Modul tingkat tinggi `LayananDenda` tidak membuat atau bergantung secara langsung pada kelas konkret tingkat rendah seperti `new DendaFlat()` atau `new DendaProgresif()`.
   - Sebaliknya, `LayananDenda` bergantung pada abstraksi antarmuka `AturanDenda`. Objek aturan konkret disuntikkan dari luar melalui konstruktor (*Constructor Dependency Injection*):
     ```java
     public class LayananDenda {
         private final AturanDenda aturan;

         public LayananDenda(AturanDenda aturan) {
             if (aturan == null) {
                 throw new IllegalArgumentException("Aturan denda tidak boleh null");
             }
             this.aturan = aturan;
         }

         public long hitung(Aset aset, int hariTerlambat) {
             return aturan.hitung(aset, hariTerlambat);
         }
     }
     ```
2. **Abstraksi Akses Data (DAO)**:
   - Komponen logika bisnis dan pengendali API bergantung pada antarmuka `AsetDAO` dan `PeminjamanDAO`, bukan langsung pada implementasi JDBC (`JdbcAsetDAO`, `JdbcPeminjamanDAO`). Hal ini memisahkan aturan bisnis inti dari mekanisme persistensi database fisik.

---

## Rangkuman Konsep Tambahan (Persiapan Kuis)

1. **Perbedaan Aggregation dan Composition**:
   - **Agregasi (*Aggregation*, `o--`)**: Hubungan kepemilikan lemah (*weak "has-a"*). Objek bagian (*part*) memiliki siklus hidup independen dari objek pemilik (*whole*). Jika pemilik dihancurkan, objek bagian tetap ada. Contoh: `KatalogAset` dan `Aset`. Menghapus katalog tidak menghancurkan data aset di dunia nyata.
   - **Komposisi (*Composition*, `*--`)**: Hubungan kepemilikan kuat (*strong "has-a"*). Objek bagian sepenuhnya dimiliki oleh pemilik dan siklus hidupnya terikat erat (*coincident lifetime*). Jika pemilik dihancurkan, objek bagian ikut musnah. Contoh: sebuah Faktur Peminjaman dengan Baris Item Faktur.
2. **Mengapa OCP Memudahkan Pengembangan**:
   - OCP mengurangi risiko regresi (*regression bugs*). Saat menambahkan fitur baru, kode lama yang sudah teruji dan stabil tidak disentuh atau diubah, sehingga meminimalkan potensi rusaknya fungsi yang sudah berjalan. Pengembang cukup menulis modul baru yang mengimplementasikan antarmuka atau memperluas kelas dasar yang sudah ada.
3. **Mengapa DIP Meningkatkan *Testability***:
   - DIP membebaskan kelas dari keterikatan pada dependensi konkret/berat (seperti database, jaringan, atau kalkulator eksternal). Dalam pengujian unit (*unit testing*), dependensi abstraksi dapat dengan mudah diganti (*mocking/stubbing*) dengan implementasi tiruan yang cepat, terisolasi, dan dapat diprediksi (seperti `(aset, hari) -> 42L`), tanpa memerlukan infrastruktur eksternal aktif.
