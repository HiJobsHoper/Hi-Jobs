package id.kharisma.studio.hijobs;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import id.kharisma.studio.hijobs.ui.main.BerandaFragment;
import id.kharisma.studio.hijobs.ui.main.ProfilFragment;

public class TambahUsaha extends AppCompatActivity {

    private EditText etNama, etDeskripsi, etLokasi, etLinkMaps;
    private Button btnTambah;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_usaha);
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();

        //Inisialisasi material desain
        etNama = findViewById(R.id.txtTbhUsh_Usaha);
        etDeskripsi = findViewById(R.id.txtTbhUsh_DeskUsaha);
        etLokasi = findViewById(R.id.txtTbhUsh_AlamatUsaha);
        etLinkMaps = findViewById(R.id.txtTbhUsh_LinkMaps);
        btnTambah = findViewById(R.id.btnTbhUsh_Tambah);

        //Button Tambah
        btnTambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Inisialisasi data ke dalam variabel
                String Nama = etNama.getText().toString();
                String Desk = etDeskripsi.getText().toString();
                String Lokasi = etLokasi.getText().toString();
                String Link_Maps = etLinkMaps.getText().toString();

                if (cek_Thb_Ush(Nama,Desk,Lokasi,Link_Maps) == true) {
                    Toast.makeText(TambahUsaha.this, "Usaha berhasil ditambahkan", Toast.LENGTH_SHORT).show();
                    //createAccound(Nama,Email,Telvon,Pass); //Membuat akun pada database
                }
            }
        });

        //Membuat tombol back pada Navigasi Bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    //Kembali ke halaman usaha
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    //Mengaktifkan tombol kembali
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    //Memastikan pengisian data sesuai ketentuan
    public boolean cek_Thb_Ush(String Nama,String Desk,String Lokasi,String Link_Maps) {

        //Memberikan tanda dan mengarahkan pada data yang belum di isi
        if (Link_Maps.isEmpty()) {
            etLinkMaps.setError("Link google maps required");
            etLinkMaps.requestFocus();
        }
        if (Lokasi.isEmpty()) {
            etLokasi.setError("Address required");
            etLokasi.requestFocus();
        }
        if (Desk.isEmpty()) {
            etDeskripsi.setError("Deskripsi required");
            etDeskripsi.requestFocus();
        }
        if (Nama.isEmpty()) {
            etNama.setError("Name required");
            etNama.requestFocus();
        }

        //Mengecek apakah ada data yang belum di isi
        if (Nama.equals("") || Desk.equals("") || Lokasi.equals("") || Link_Maps.equals("")) {
            //Peringatan data tidak lengkap
            Toast.makeText(TambahUsaha.this,
                    "Data anda belum lengkap", Toast.LENGTH_LONG).show();
            return false; //Pengisian tidak sesuai ketentuan
        } else {
            return true;
        }
    }
}