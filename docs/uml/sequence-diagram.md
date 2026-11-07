# Sequence Diagram: Pengajuan Peminjaman (`JadwalPeminjaman.ajukan`)

Dokumen ini memodelkan interaksi objek dinamis saat pengajuan peminjaman aset dilakukan ke dalam jadwal (`JadwalPeminjaman`), mencakup **skenario sukses** dan **skenario bentrok** dengan menggunakan fragmen kondisional `alt`.

## Visualisasi Sequence Diagram

```mermaid
sequenceDiagram
    autonumber
    actor P as Peminjam
    participant J as jadwal: JadwalPeminjaman
    participant B as baru: Peminjaman
    participant A as aset: Aset
    participant E as existing: Peminjaman
    participant D as daftar: List~Peminjaman~

    P->>J: ajukan(baru)
    activate J

    J->>B: getAset()
    activate B
    B-->>J: aset
    deactivate B

    J->>A: bisaDipinjam()
    activate A
    A-->>J: true (TERSEDIA & bukan RUSAK_BERAT)
    deactivate A

    J->>J: bentrok(baru)
    activate J
    J->>B: getMulai()
    activate B
    B-->>J: mulaiBaru
    deactivate B
    J->>B: getSelesai()
    activate B
    B-->>J: selesaiBaru
    deactivate B

    loop Setiap peminjaman pada daftar
        J->>D: get(i)
        activate D
        D-->>J: existing
        deactivate D
        J->>E: getAset()
        activate E
        E-->>J: asetExisting
        deactivate E
        opt asetExisting.getKode() == aset.getKode()
            J->>E: getMulai()
            activate E
            E-->>J: mulaiExisting
            deactivate E
            J->>E: getSelesai()
            activate E
            E-->>J: selesaiExisting
            deactivate E
        end
    end
    deactivate J

    alt Skenario Bentrok: Waktu tumpang tindih pada aset yang sama
        Note over J: Terdeteksi bentrok:<br/>mulaiBaru < selesaiExisting AND selesaiBaru > mulaiExisting
        J-->>P: throw PeminjamanBentrokException
        Note over P: Pengajuan ditolak.<br/>Data tidak ditambahkan ke daftar.
    else Skenario Sukses: Tidak ada bentrok jadwal
        Note over J: Validasi bentrok lolos.<br/>Pemeriksaan kuota laptop lolos.
        J->>D: add(baru)
        activate D
        D-->>J: true
        deactivate D
        J-->>P: return void (sukses, status DIAJUKAN)
        Note over P: Peminjaman berhasil diajukan<br/>dan tersimpan di jadwal.
    end
    deactivate J
```

---

## Penjelasan Alur Interaksi

1. **Pemanggilan Awal**:
   Aktor peminjam memanggil metode `ajukan(baru)` pada objek `JadwalPeminjaman`.
2. **Pemeriksaan Kelaikan Aset**:
   `JadwalPeminjaman` memanggil `baru.getAset()` lalu memvalidasi ketersediaan aset melalui `aset.bisaDipinjam()`. Berdasarkan aturan bisnis, aset hanya dapat dipinjam bila statusnya `TERSEDIA` dan kondisinya bukan `RUSAK_BERAT`.
3. **Pemeriksaan Bentrok Jadwal (`bentrok(baru)`)**:
   - `JadwalPeminjaman` mengambil rentang waktu peminjaman baru (`getMulai()` dan `getSelesai()`).
   - Sistem melakukan perulangan (*loop*) terhadap seluruh peminjaman yang ada di dalam koleksi `daftar`.
   - Untuk setiap peminjaman yang ada (`existing`), sistem mencocokkan kode aset. Bila kode aset sama, sistem membandingkan interval waktu:
     `mulaiBaru < selesaiExisting && selesaiBaru > mulaiExisting`.
4. **Fragmen Percabangan (`alt`)**:
   - **Skenario Bentrok (`[Terjadi Tumpang Tindih Waktu]`)**:
     - Sistem mendeteksi adanya peminjaman aktif lain pada aset yang sama di rentang waktu tersebut.
     - `JadwalPeminjaman` melempar `PeminjamanBentrokException` (yang menyebutkan kode aset bersangkutan).
     - Pengajuan dibatalkan secara atomik, dan objek `baru` **tidak dimasukkan** ke dalam koleksi jadwal `daftar`.
   - **Skenario Sukses (`[Tidak Ada Bentrok]`)**:
     - Seluruh verifikasi bentrok lolos (dan kuota terpenuhi jika aset berjenis `Laptop`).
     - Objek `baru` ditambahkan ke dalam koleksi `daftar` melalui `daftar.add(baru)`.
     - Peminjaman berstatus awal `DIAJUKAN`.
     - `JadwalPeminjaman` mengembalikan konfirmasi sukses kepada peminjam.
