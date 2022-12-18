package id.kharisma.studio.hijobs;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Registrasi extends AppCompatActivity {

    private Button btnSimpan;
    private EditText etNama, etEmail, etTelvon, etPass, etPassKon;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore db;
    private static final String TAG = "Registrasi";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.activity_registrasi);

        firebaseAuth = FirebaseAuth.getInstance(); //Menghubungkan dengan firebase authentifikasi
        db = FirebaseFirestore.getInstance(); //Menghubungkan dengar cloud firestore

        //Inisialisasi material desain
        btnSimpan = findViewById(R.id.btnReg_Simpan);
        etNama = findViewById(R.id.txtReg_Nama);
        etEmail = findViewById(R.id.txtReg_Email);
        etTelvon = findViewById(R.id.txtReg_Telepon);
        etPass = findViewById(R.id.txtReg_KataSandi);
        etPassKon = findViewById(R.id.txtReg_KonfirmasiSandi);

        //Show hide password menggunakan icon mata
        ImageView imageViewShowHidePwd = findViewById(R.id.imgReg_Sandi);
        imageViewShowHidePwd.setImageResource(R.drawable.ic_baseline_visibility_off_24);
        imageViewShowHidePwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etPass.getTransformationMethod().equals(HideReturnsTransformationMethod.getInstance())){
                    //Membuat password tidak kelihatan
                    etPass.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    //Mengganti icon
                    imageViewShowHidePwd.setImageResource(R.drawable.ic_baseline_visibility_off_24);
                }else {
                    //Membuat password tidak kelihatan
                    etPass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    //Mengganti icon
                    imageViewShowHidePwd.setImageResource(R.drawable.ic_baseline_visibility_24);
                }
            }
        });

        //Show hide confirmation password menggunakan icon mata
        ImageView imageViewShowHideConPwd = findViewById(R.id.imgReg_KonfirSandi);
        imageViewShowHideConPwd.setImageResource(R.drawable.ic_baseline_visibility_off_24);
        imageViewShowHideConPwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etPassKon.getTransformationMethod().equals(HideReturnsTransformationMethod.getInstance())){
                    //Membuat password tidak kelihatan
                    etPassKon.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    //Mengganti icon
                    imageViewShowHideConPwd.setImageResource(R.drawable.ic_baseline_visibility_off_24);
                }else {
                    //Membuat password tidak kelihatan
                    etPassKon.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    //Mengganti icon
                    imageViewShowHideConPwd.setImageResource(R.drawable.ic_baseline_visibility_24);
                }
            }
        });

        //Button simpan
        btnSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Inisialisasi data ke dalam variabel
                String Nama = etNama.getText().toString();
                String Email = etEmail.getText().toString().trim();
                String Telvon = etTelvon.getText().toString();
                String Pass = etPass.getText().toString().trim();
                String PassKon = etPassKon.getText().toString().trim();

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
    public void createAccound(String nama,String email,String telvon,String pass) {

        FirebaseAuth firebaseauth = FirebaseAuth.getInstance();

        firebaseauth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener(
                Registrasi.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful() && task.getResult()!=null) {
                    //Akun berhasil di buat
                    firebaseauth.getCurrentUser().sendEmailVerification(); //Mengirim pemberitahuan ke email
                    firebaseauth.signOut(); //Keluar dari akun
                    //Kembali ke halaman login
                    startActivity(new Intent(Registrasi.this, Rekomendasi.class)); //Membuka halaman rekomendasi
                    setDataAkun(nama,email,telvon); //Menyimpan data akun ke database
                    //setDataProfil(email); //Menyimpan data profil ke database
                    finish(); //Menutup halaman registrasi
                } else {
                    //Akun gagal di buat
                    Toast.makeText(Registrasi.this,
                            task.getException().getLocalizedMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    //Memasukkan data ke database
    public void setDataAkun(String nama,String email,String televon) {

        //Membuat kolom user
        Map<String, Object> akun = new HashMap<>();
        akun.put("Nama", nama);
        akun.put("Email", email);
        akun.put("Nomor Televon", televon);

        //Menyimpan referensi data pada database berdasarkan user id
        db.collection("Akun").document(email)
                .set(akun)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        //Log
                        Log.d(TAG, "DocumentSnapshot successfully written!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //Log
                        Log.w(TAG, "Error writing document", e);
                    }
                });
    }

    //Memasukkan data ke database
    public void setDataProfil(String email) {

        //Membuat kolom user
        Map<String, Object> profil = new HashMap<>();
        profil.put("Nama", "");
        profil.put("Jenis Kelamin", "");
        profil.put("Tanggal Lahir", "");
        profil.put("Pendidikan Terakhir", "");
        profil.put("Alamat", "");
        profil.put("Keahlian", "");
        profil.put("Pengalaman Kerja", "");
        profil.put("Kewarganegaraan", "");

        //Menyimpan referensi data pada database berdasarkan user id
        db.collection("Profil").document(email)
                .set(profil)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        //Log
                        Log.d(TAG, "DocumentSnapshot successfully written!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //Log
                        Log.w(TAG, "Error writing document", e);
                    }
                });
    }
}