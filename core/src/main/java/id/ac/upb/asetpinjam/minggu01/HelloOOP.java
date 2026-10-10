package id.ac.upb.asetpinjam.minggu01;

import id.ac.upb.asetpinjam.config.Varian;

/** Minggu 1, gaya OOP: data dan perilaku dibungkus dalam sebuah class. */
public class HelloOOP {

    private final String nama;
    private final String nim;

    // TODO minggu 1: tambahkan field private untuk nama dan nim.

    public HelloOOP(String nama, String nim) {
        this.nama = nama;
        this.nim = nim;
    }

    /** TODO minggu 1: sapaan memuat "Aset-Pinjam", nama, dan NIM. */
    public String sapa() {
        return "selamat datang di Aset-Pinjam, " + this.nama + " (" + this.nim + ")";
    }

    public static void main(String[] args) {
        Varian v = Varian.aktif();
        System.out.println(new HelloOOP(v.nama, v.nim).sapa());
    }
}
