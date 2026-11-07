package id.ac.upb.asetpinjam.denda;

import id.ac.upb.asetpinjam.kontrak.AturanDenda;
import id.ac.upb.asetpinjam.model.Aset;

/**
 * Konteks pola Strategy dan demonstrasi prinsip SOLID (DIP dan OCP).
 * Aturan denda DISUNTIKKAN lewat konstruktor (bukan dibuat di dalam kelas ini),
 * sehingga kelas ini bergantung pada abstraksi (AturanDenda) bukan implementasi konkret.
 */
public class LayananDenda {

    private final AturanDenda aturan;

    /** Aturan tidak boleh null (IllegalArgumentException). */
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
