package id.ac.upb.asetpinjam.model;

import id.ac.upb.asetpinjam.config.Varian;
import id.ac.upb.asetpinjam.kontrak.DapatDipinjam;

/**
 * Aset yang dapat dipinjam.
 *
 * <p>Minggu 2: lengkapi field, konstruktor, getter/setter, dan validasi (encapsulation).
 * Minggu 3: tambahkan getJenis() di tiap subclass.
 * Minggu 4: hitungDenda() dioverride di tiap subclass.
 * Minggu 5: isi bisaDipinjam() dan ubah menjadi class abstrak
 * (TestSupport.aset(...) di folder test menyesuaikan sendiri, lihat docs/minggu-05.md).
 */
public class Aset implements DapatDipinjam {

    private final String kode;
    private String nama;
    private StatusAset status;
    private Kondisi kondisi;

    /**
     * Membuat aset baru dengan kode dan nama yang diberikan.
     * Status awal TERSEDIA, kondisi awal BAIK.
     * Kode dan nama tidak boleh null atau kosong (termasuk hanya spasi).
     */
    public Aset(String kode, String nama) {
        if (kode == null || kode.isBlank()) {
            throw new IllegalArgumentException("Kode aset tidak boleh null atau kosong");
        }
        if (nama == null || nama.isBlank()) {
            throw new IllegalArgumentException("Nama aset tidak boleh null atau kosong");
        }
        this.kode = kode;
        this.nama = nama;
        this.status = StatusAset.TERSEDIA;
        this.kondisi = Kondisi.BAIK;
    }

    public String getKode() {
        return kode;
    }

    public String getNama() {
        return nama;
    }

    public StatusAset getStatus() {
        return status;
    }

    public Kondisi getKondisi() {
        return kondisi;
    }

    /** Mengubah nama aset. Nama baru tidak boleh null atau kosong. */
    public void setNama(String nama) {
        if (nama == null || nama.isBlank()) {
            throw new IllegalArgumentException("Nama aset tidak boleh null atau kosong");
        }
        this.nama = nama;
    }

    /** Mengubah status aset. Status tidak boleh null. */
    public void setStatus(StatusAset status) {
        if (status == null) {
            throw new IllegalArgumentException("Status aset tidak boleh null");
        }
        this.status = status;
    }

    /** Mengubah kondisi aset. Kondisi tidak boleh null. */
    public void setKondisi(Kondisi kondisi) {
        if (kondisi == null) {
            throw new IllegalArgumentException("Kondisi aset tidak boleh null");
        }
        this.kondisi = kondisi;
    }

    /** TODO minggu 5: true hanya jika status TERSEDIA dan kondisi bukan RUSAK_BERAT. */
    @Override
    public boolean bisaDipinjam() {
        throw new UnsupportedOperationException("TODO minggu 5");
    }

    /** TODO minggu 3: dioverride tiap subclass agar mengembalikan jenis yang benar. */
    public JenisAset getJenis() {
        throw new UnsupportedOperationException("TODO minggu 3");
    }

    /** TODO minggu 4: denda keterlambatan. Dioverride tiap subclass (aturan di docs/aturan-bisnis.md). */
    public long hitungDenda(int hariTerlambat) {
        if (hariTerlambat < 0) {
            throw new IllegalArgumentException("Hari keterlambatan tidak boleh negatif: " + hariTerlambat);
        }
        return (long) hariTerlambat * Varian.aktif().dendaPerHari;
    }
}
