package id.ac.upb.asetpinjam.model;

import id.ac.upb.asetpinjam.config.Varian;

/** Minggu 3: aset berupa ruang. Kapasitas harus lebih dari 0 (IllegalArgumentException). */
public class Ruang extends Aset {

    private final int kapasitas;

    public Ruang(String kode, String nama, int kapasitas) {
        super(kode, nama);
        if (kapasitas <= 0) {
            throw new IllegalArgumentException("Kapasitas ruang harus lebih dari 0");
        }
        this.kapasitas = kapasitas;
    }

    public int getKapasitas() {
        return kapasitas;
    }

    @Override
    public JenisAset getJenis() {
        return JenisAset.RUANG;
    }

    @Override
    public long hitungDenda(int hariTerlambat) {
        if (hariTerlambat < 0) {
            throw new IllegalArgumentException("Hari keterlambatan tidak boleh negatif: " + hariTerlambat);
        }
        return (long) hariTerlambat * 1L * Varian.aktif().dendaPerHari;
    }
}
