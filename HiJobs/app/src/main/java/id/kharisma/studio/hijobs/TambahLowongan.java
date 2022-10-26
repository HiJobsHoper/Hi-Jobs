package id.kharisma.studio.hijobs;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class TambahLowongan extends AppCompatActivity {

    private EditText etNama, etDesk, etSyarat, etWaktu, etGaji;
    private Button btnTambah;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_lowongan);

        //Inisialisasi material desain
        etNama = findViewById(R.id.txtTbhLow_Lowongan);
        etDesk = findViewById(R.id.txtTbhLow_DeskUsaha);
        etSyarat = findViewById(R.id.txtTbhLow_Syarat);
        etWaktu = findViewById(R.id.txtTbhLow_WaktuKerja);
        etGaji = findViewById(R.id.txtTbhLow_Gaji);
        btnTambah = findViewById(R.id.btnTbhLow_Tambah);

        //Tombol tambah
        btnTambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cek_Reg() == true) {
                    //halaman selanjutnya ...
                    startActivity(new Intent(TambahLowongan.this, Login.class));
                    finish(); //Menutup halaman registrasi
                }
            }
        });

        //Membuat tombol back pada Navigasi Bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    //Kembali ke halaman login
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        startActivity(new Intent(TambahLowongan.this, Login.class)); //Membuka halaman login
        finish(); //Menutup halaman tambah lowongan
        return true;
    }

    //Mengaktifkan tombol kembali
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    //Memastikan pengisian data sesuai ketentuan
    public boolean cek_Reg() {
        //Inisialisasi data ke dalam variabel
        String Nama = etNama.getText().toString();
        String Desk = etDesk.getText().toString();
        String Syarat = etSyarat.getText().toString();
        String Waktu = etWaktu.getText().toString();
        String Gaji = etGaji.getText().toString();
        boolean nilai = false;

        //Memberikan tanda dan mengarahkan pada data yang belum di isi
        if (Gaji.isEmpty()) {
            etGaji.setError("Gaji required");
            etGaji.requestFocus();
        }
        if (Waktu.isEmpty()) {
            etWaktu.setError("Waktu required");
            etWaktu.requestFocus();
        }
        if (Syarat.isEmpty()) {
            etSyarat.setError("Syarat required");
            etSyarat.requestFocus();
        }
        if (Desk.isEmpty()) {
            etDesk.setError("Deskripsi required");
            etDesk.requestFocus();
        }
        if (Nama.isEmpty()) {
            etNama.setError("Name Pekerjaan required");
            etNama.requestFocus();
        }

        //Mengecek apakah ada data yang belum di isi
        if (Nama.equals("") || Desk.equals("") || Syarat.equals("") || Waktu.equals("") ||
                Gaji.equals("")) {
            //Peringatan data tidak lengkap
            Toast.makeText(TambahLowongan.this,
                    "Data anda belum lengkap", Toast.LENGTH_LONG).show();
        }
        return nilai;
    }
}