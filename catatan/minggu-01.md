# Catatan Minggu NN: (paradigma pemrograman dan setup)

Salin berkas ini menjadi `catatan/minggu-NN.md` (NN dua digit, misalnya `minggu-07.md`).

## Yang saya kerjakan
(saya telah menyiapkan lingkungan pengembangan dasar seperti jdk21,maven,git,dan struktur repositoriproyek.saya juga mengisi file identitas mahasiswa pada mahasiswa.properties dengan nim dan lengkap saya . selain itu daya menyelesaikan implementasi tiga gaya paradigma pada file helloprosedural.java,hellooop.java,dan hellofungsional.java.)

## Keputusan desain
(saya memutuskan unutk membuat format kalimat sapaan ynag konsisten di ketiga gaya pemrograman tersebut agar memenuhi syarat pengujian otomatis, yaitu wajib membuat teks "aset-pinjam", nama dan nim .pada gaya prosedural saya menggunakan dungsi statis, pada oop mengunakan field privat dengan konstruktor, dan pada fungsional menggunakan ekpresi lambda (function))
## Kendala
(sempat mengalami kendala saat menjalankan skrip pengujian lokal karena kebijakan eksekusi skrip (execution policy) di powershel windows yang memblokir file .ps1 kendala ini berhasil diatasi dengan menjalankan perintah bypass eksekusi atau menggunakan terminal git bash)

## Penggunaan AI
- Alat yang dipakai: (gemini)
- Bagian yang dibantu AI: (memahami struktur kode pada helloprosedural.java,hellooop.java,dan hellofungsional.java.)
- Prompt utama: (bagaimana cara melengkapi file HelloProsedural, HelloOOP, dan HelloFungsional untuk minggu 1? )
- Yang saya ubah atau tolak dari saran AI, dan alasannya: menyesuaikan kemabali teks sapaan agar formatnya persis memuat string "aset-pinjam" beserta variabel nama dan nim sesuai dengan tes unit
- Satu hal yang saya pahami lebih baik sekarang:Perbedaan penerapan fungsi statis (prosedural), enkapsulasi objek (OOP), dan fungsi nir-state/lambda (fungsional) di dalam bahasa Java.
