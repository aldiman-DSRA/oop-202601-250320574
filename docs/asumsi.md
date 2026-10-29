# Asumsi dan Keputusan Desain

Isi bagian ini sendiri. Satu entri per keputusan. Dinilai dari kejelasan alasan dan konsistensi dengan kode dan tes Anda,
bukan dari jawaban "benar".

Format tiap entri:

```
## A-NN: judul singkat
- Pertanyaan: ...
- Keputusan: ...
- Alasan: ...
- Dampak ke kode: ... (kelas/metode)
- Tes yang membuktikan: ... (nama tes di folder mahasiswa/)
```

## Aturan ambigu dari docs/aturan-bisnis.md

Salin setiap pertanyaan ambigu (nomor 1 sampai 7) ke sini dan jawab. Contoh:

## A-01: batas waktu bentrok
- Pertanyaan: 09:00-11:00 dan 11:00-13:00 pada aset yang sama bentrok atau tidak?
- Keputusan: (isi)
- Alasan: (isi)
- Dampak ke kode: (isi)
- Tes yang membuktikan: (isi)

## Asumsi lain

Tambahkan di sini keputusan lain yang Anda ambil: rumus denda progresif, perubahan skema database, format error API, dan seterusnya.

## A-08: Rumus denda progresif keterlambatan
- Pertanyaan: Bagaimana formula perhitungan denda progresif yang adil dan memberikan disinsentif bagi keterlambatan yang makin lama?
- Keputusan: Denda progresif dihitung bertingkat (tiered penalty) berdasarkan denda flat dasar ditambah surcharge keterlambatan:
  - Hari 0: Rp 0 (tidak ada denda).
  - Hari 1 s.d. 7 (keterlambatan ringan / masa toleransi awal): denda sama dengan denda flat normal (100% dari tarif harian dasar aset).
  - Hari 8 s.d. 14 (keterlambatan sedang): denda flat ditambah surcharge 50% dari tarif harian dasar aset per hari untuk hari ke-8 s.d. 14.
  - Hari 15 ke atas (keterlambatan berat): akumulasi surcharge minggu sebelumnya ditambah surcharge 100% dari tarif harian dasar aset per hari untuk hari ke-15 dan seterusnya.
- Alasan: Struktur tier memberikan disinsentif finansial yang bertahap makin berat seiring lamanya peminjam menahan aset universitas. Aturan ini memenuhi seluruh kriteria wajib:
  1. Hari 0 menghasilkan denda 0.
  2. Nilai denda tidak pernah lebih murah dari DendaFlat (`progresif >= flat`).
  3. Nilai denda monoton naik (tidak pernah turun saat hari bertambah).
  4. Pada hari ke-30 nilainya dipastikan lebih mahal daripada DendaFlat.
- Dampak ke kode: Kelas `id.ac.upb.asetpinjam.denda.DendaProgresif` pada method `hitung(Aset aset, int hariTerlambat)`.
- Tes yang membuktikan: `id.ac.upb.asetpinjam.Minggu05AbstraksiTest.dendaProgresifMemenuhiSyaratMinimal`
