package id.kharisma.studio.hijobs;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Pattern;

public class Registrasi extends AppCompatActivity {

    private Button btnSimpan;
    private EditText etNama, etEmail, etTelvon, etPass, etPassKon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrasi);

        //Inisialisasi material desain
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
                //Inisialisasi data ke dalam variabel
                String Nama = etNama.getText().toString();
                String Email = etEmail.getText().toString();
                String Telvon = etTelvon.getText().toString();
                String Pass = etPass.getText().toString();
                String PassKon = etPassKon.getText().toString();

                if (cek_Reg(Nama,Email,Telvon,Pass,PassKon) == true) {
                    createAccound(Nama,Email,Telvon,Pass); //Membuat akun pada database
                }
            }
        });

        //Membuat tombol kembali pada Navigasi Bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    //Kembali ke halaman login
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        startActivity(new Intent(Registrasi.this, Login.class)); //Membuka halaman login
        finish(); //Menutup halaman registrasi
        return true;
    }

    //Mengaktifkan tombol kembali
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    //Memastikan pengisian data sesuai ketentuan
    public boolean cek_Reg(String Nama,String Email,String Telvon,String Pass,String PassKon) {

        //Memberikan tanda dan mengarahkan pada data yang belum di isi
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
            //Peringatan data tidak lengkap
            Toast.makeText(Registrasi.this,
                    "Data anda belum lengkap", Toast.LENGTH_LONG).show();
            return false; //Pengisian tidak sesuai ketentuan
        } else {
            //Memastikan email berformat email
            if (!Patterns.EMAIL_ADDRESS.matcher(Email).matches()) {
                etEmail.setError("Email is invalid");
                Toast.makeText(Registrasi.this,
                        "Email tidak sesuai", Toast.LENGTH_LONG).show();
            }
            //Memastikan kata sandi tidak kurang dari 6 karakter
            if (Pass.length()<6) {
                etPass.setError("Password is invalid");
                Toast.makeText(Registrasi.this,
                        "Kata sandi kurang dari 6 karakter", Toast.LENGTH_LONG).show();
            }
            //Memastikan kata sandi dan konfirmasi kata sandi sama
            if (Pass.equals(PassKon)) {
                return true; //Pengisian sesuai ketentuan
            } else {
                //Peringatan kata sandi dan konfirmasi kata sandi tidak sama
                Toast.makeText(Registrasi.this,
                        "Kata sandi anda tidak sama", Toast.LENGTH_LONG).show();
                etPassKon.requestFocus();
                return false; //Pengisian tidak sesuai ketentuan
            }
        }
    }

    //Menambahkan akun pada database
    public void createAccound(String Nama,String Email,String Telvon,String Pass) {

        FirebaseAuth firebaseauth = FirebaseAuth.getInstance();

        firebaseauth.createUserWithEmailAndPassword(Email,Pass).addOnCompleteListener(
                Registrasi.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    //Akun berhasil di buat
                    firebaseauth.getCurrentUser().sendEmailVerification(); //Mengirim pemberitahuan ke email
                    firebaseauth.signOut(); //Keluar dari akun
                    //Kembali ke halaman login
                    startActivity(new Intent(Registrasi.this, Login.class)); //Membuka halaman login
                    finish(); //Menutup halaman registrasi
                } else {
                    //Akun gagal di buat
                    Toast.makeText(Registrasi.this,
                            task.getException().getLocalizedMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}