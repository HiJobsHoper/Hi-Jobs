package id.kharisma.studio.hijobs;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {

    private TextView reg;
    private Button btnMasuk;
    private EditText etEmail,etPass;
    private CheckBox chkPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //reload(); //Langsung ke halaman utama

        //Inisialisasi material desain
        reg = findViewById(R.id.textView9);
        btnMasuk = findViewById(R.id.btnLog_Masuk);
        etEmail = findViewById(R.id.txtLog_Email);
        etPass = findViewById(R.id.txtLog_KataSandi);
        chkPass = findViewById(R.id.chkLog_TampilSandi);

        //Membuka halaman registrasi
        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this, Registrasi.class)); //Membuka halaman registrasi
                finish(); //Menutup halaman login
            }
        });

        //Menampilkan/menyembunyikan kata sandi
        chkPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (chkPass.isChecked()) {
                    etPass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    etPass.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });

        //Tombol masuk
        btnMasuk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //inisialisasi data ke dalam variabel
                String Email = etEmail.getText().toString();
                String Pass = etPass.getText().toString();

                if (cek_Log(Email,Pass) == true) {
                    loginAccound(Email,Pass); //Masuk menggunakan akun pada database
                }
            }
        });
    }

    //Memastikan pengisian data sesuai ketentuan
    public boolean cek_Log(String Email,String Pass) {

        //Memberikan tanda dan mengarahkan pada data yang belum di isi
        if (Pass.isEmpty()) {
            etPass.setError("Password required");
            etPass.requestFocus();
        }
        if (Email.isEmpty()) {
            etEmail.setError("Email required");
            etEmail.requestFocus();
        }

        //Mengecek apakah ada data yang belum di isi
        if (Email.isEmpty() || Pass.isEmpty()) {
            return false;
        } else {
            //Memastikan email berformat email
            if (!Patterns.EMAIL_ADDRESS.matcher(Email).matches()) {
                etEmail.setError("Email is invalid");
                Toast.makeText(Login.this,
                        "Email tidak sesuai", Toast.LENGTH_LONG).show();
                return false;
            }
            //Memastikan kata sandi tidak kurang dari 6 karakter
            if (Pass.length() < 6) {
                etPass.setError("Password is invalid");
                Toast.makeText(Login.this,
                        "Kata sandi kurang dari 6 karakter", Toast.LENGTH_LONG).show();
                return false;
            } else {
                return true;
            }
        }
    }

    //Masuk pada akun database
    public void loginAccound(String Email,String Pass) {

        FirebaseAuth firebaseauth = FirebaseAuth.getInstance();

        firebaseauth.signInWithEmailAndPassword(Email,Pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful() && task.getResult()!= null) {
                    //Berhasil login
                    startActivity(new Intent(Login.this, HalamanUtama.class)); //Membuka halaman utama
                    finish(); //Menutup halaman login
                } else {
                    //Gagal login
                    Toast.makeText(Login.this,
                            "Email atau kata sandi tidak sesuai", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    //Langsung masuk kalau sudah pernah login
    public void reload() {
        FirebaseAuth firebaseauth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = firebaseauth.getCurrentUser();

        if (currentUser != null) {
            startActivity(new Intent(Login.this, HalamanUtama.class)); //Membuka halaman utama
            finish(); //Menutup halaman login
        }
    }
}