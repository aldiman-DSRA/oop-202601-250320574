package id.ac.upb.asetpinjam.mahasiswa;

import static id.ac.upb.asetpinjam.TestSupport.pinjam;
import static org.assertj.core.api.Assertions.assertThat;

import id.ac.upb.asetpinjam.TestSupport;
import id.ac.upb.asetpinjam.config.Varian;
import id.ac.upb.asetpinjam.jadwal.JadwalPeminjaman;
import id.ac.upb.asetpinjam.model.Peminjaman;
import org.junit.jupiter.api.Test;

/**
 * Pengujian mahasiswa untuk keputusan batas waktu bentrok peminjaman.
 * Sesuai keputusan pada docs/asumsi.md (A-01), peminjaman yang selesai tepat saat
 * peminjaman lain dimulai pada aset yang sama (back-to-back) adalah TIDAK BENTROK.
 */
class BatasBentrokTest {

    @Test
    void selesaiSamaDenganMulai() {
        JadwalPeminjaman jadwal = new JadwalPeminjaman(Varian.aktif().kuotaLaptop);
        var peminjam1 = TestSupport.peminjam("P-1");
        var peminjam2 = TestSupport.peminjam("P-2");
        var ruang = TestSupport.ruang("R-1");

        // Peminjaman 1: 09:00 - 11:00
        jadwal.tambah(pinjam("1", peminjam1, ruang, 9, 11));

        // Peminjaman 2: 11:00 - 13:00 (mulai tepat saat peminjaman 1 selesai)
        Peminjaman peminjaman2 = pinjam("2", peminjam2, ruang, 11, 13);

        // Keputusan A-01: Tidak bentrok (interval [09:00, 11:00) dan [11:00, 13:00) tidak bertumpang tindih)
        assertThat(jadwal.bentrok(peminjaman2)).isFalse();
    }

    @Test
    void mulaiSamaDenganSelesaiPeminjamanSetelahnya() {
        JadwalPeminjaman jadwal = new JadwalPeminjaman(Varian.aktif().kuotaLaptop);
        var peminjam1 = TestSupport.peminjam("P-1");
        var peminjam2 = TestSupport.peminjam("P-2");
        var ruang = TestSupport.ruang("R-1");

        // Peminjaman di jadwal: 13:00 - 15:00
        jadwal.tambah(pinjam("1", peminjam1, ruang, 13, 15));

        // Peminjaman baru: 11:00 - 13:00 (selesai tepat saat peminjaman di jadwal mulai)
        Peminjaman peminjamanSebelumnya = pinjam("2", peminjam2, ruang, 11, 13);

        // Keputusan A-01: Tidak bentrok
        assertThat(jadwal.bentrok(peminjamanSebelumnya)).isFalse();
    }
}
