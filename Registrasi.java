package id.kharisma.studio.hijobs;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Registrasi extends AppCompatActivity {

    private Button btnSimpan;
    private EditText etNama, etEmail, etTelvon, etPass, etPassKon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrasi);

        btnSimpan = findViewById(R.id.btnReg_Simpan);
        etNama = findViewById(R.id.txtReg_Nama);
        etEmail = findViewById(R.id.txtReg_Email);
        etTelvon = findViewById(R.id.txtReg_Telepon);
        etPass = findViewById(R.id.txtReg_KataSandi);
        etPassKon = findViewById(R.id.txtReg_KonfirmasiSandi);

        //Tombol simpan
        btnSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cek_Reg() == true) {
                    //halaman selanjutnya ...
                    //finish();
                }
            }
        });

        //Membuat tombol back pada Navigasi Bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public boolean cek_Reg() {
        //Inisialisasi data ke dalam variabel
        String Nama = etNama.getText().toString();
        String Email = etEmail.getText().toString();
        String Telvon = etTelvon.getText().toString();
        String Pass = etPass.getText().toString();
        String PassKon = etPassKon.getText().toString();
        boolean nilai = false;

        //Memberikan tanda pada data yang belum di isi
        if (PassKon.isEmpty()) {
            etPassKon.setError("Confirm password required");
            etPassKon.requestFocus();
        }
        if (Pass.isEmpty()) {
            etPass.setError("Password required");
            etPass.requestFocus();
        }
        if (Telvon.isEmpty()) {
            etTelvon.setError("Phone number required");
            etTelvon.requestFocus();
        }
        if (Email.isEmpty()) {
            etEmail.setError("Email required");
            etEmail.requestFocus();
        }
        if (Nama.isEmpty()) {
            etNama.setError("Name required");
            etNama.requestFocus();
        }

        //Mengecek apakah ada data yang belum di isi
        if (Nama.equals("") || Email.equals("") || Telvon.equals("") || Pass.equals("") ||
                PassKon.equals("")) {
            Toast.makeText(Registrasi.this,
                    "Data anda belum lengkap", Toast.LENGTH_LONG).show();
        } else {
            //Memastikan kata sandi dan konfirmasi kata sandi sama
            if (Pass.equals(PassKon)) {
                nilai = true;
                //simpan data pada Database
            } else {
                Toast.makeText(Registrasi.this,
                        "Kata sandi anda tidak sama", Toast.LENGTH_LONG).show();
            }
        }

        return nilai;
    }
}