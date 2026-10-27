package id.ac.upb.asetpinjam.model;

import id.ac.upb.asetpinjam.config.Varian;

/** Minggu 3: aset berupa laptop. Spesifikasi dan nomor inventaris tidak boleh kosong. */
public class Laptop extends Aset {

    private final String spesifikasi;
    private final String nomorInventaris;

    public Laptop(String kode, String nama, String spesifikasi, String nomorInventaris) {
        super(kode, nama);
        if (spesifikasi == null || spesifikasi.isBlank()) {
            throw new IllegalArgumentException("Spesifikasi laptop tidak boleh null atau kosong");
        }
        if (nomorInventaris == null || nomorInventaris.isBlank()) {
            throw new IllegalArgumentException("Nomor inventaris laptop tidak boleh null atau kosong");
        }
        this.spesifikasi = spesifikasi;
        this.nomorInventaris = nomorInventaris;
    }

    public String getSpesifikasi() {
        return spesifikasi;
    }

    public String getNomorInventaris() {
        return nomorInventaris;
    }

    @Override
    public JenisAset getJenis() {
        return JenisAset.LAPTOP;
    }

    @Override
    public long hitungDenda(int hariTerlambat) {
        if (hariTerlambat < 0) {
            throw new IllegalArgumentException("Hari keterlambatan tidak boleh negatif: " + hariTerlambat);
        }
        return (long) hariTerlambat * 2L * Varian.aktif().dendaPerHari;
    }
}
