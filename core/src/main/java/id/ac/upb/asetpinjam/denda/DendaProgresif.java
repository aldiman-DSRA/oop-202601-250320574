package id.ac.upb.asetpinjam.denda;

import id.ac.upb.asetpinjam.kontrak.AturanDenda;
import id.ac.upb.asetpinjam.model.Aset;

/**
 * Minggu 5: denda yang makin berat bila makin lama terlambat.
 *
 * <p>Rumus progresif:
 * - Hari 1-7 (keterlambatan ringan): denda flat normal.
 * - Hari 8-14 (keterlambatan sedang): denda flat + surcharge 50% dari tarif harian aset per hari untuk hari ke-8 s.d. 14.
 * - Hari 15 ke atas (keterlambatan berat): akumulasi surcharge tier sebelumnya + surcharge 100% dari tarif harian aset per hari untuk hari ke-15 ke atas.
 *
 * Memenuhi syarat:
 * - 0 hari = 0
 * - progresif >= flat untuk semua hari
 * - tidak pernah turun saat hari bertambah
 * - pada hari ke-30 lebih mahal dari flat
 */
public class DendaProgresif implements AturanDenda {

    @Override
    public long hitung(Aset aset, int hariTerlambat) {
        if (aset == null) {
            throw new IllegalArgumentException("Aset tidak boleh null");
        }
        if (hariTerlambat < 0) {
            throw new IllegalArgumentException("Hari keterlambatan tidak boleh negatif: " + hariTerlambat);
        }
        if (hariTerlambat == 0) {
            return 0L;
        }

        long flat = aset.hitungDenda(hariTerlambat);
        long basePerHari = aset.hitungDenda(1);
        long tambahan = 0L;

        if (hariTerlambat > 7) {
            int hariTier2 = Math.min(hariTerlambat, 14) - 7;
            tambahan += (long) hariTier2 * (basePerHari / 2);
        }
        if (hariTerlambat > 14) {
            int hariTier3 = hariTerlambat - 14;
            tambahan += (long) hariTier3 * basePerHari;
        }

        return flat + tambahan;
    }
}
