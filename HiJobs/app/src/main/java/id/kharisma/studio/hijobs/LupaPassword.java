package id.kharisma.studio.hijobs;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class LupaPassword extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private EditText etEmail;
    private Button btnKirim;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.activity_lupa_password);

        //Inisialisasi material desain
        firebaseAuth = FirebaseAuth.getInstance();
        etEmail = findViewById(R.id.txtLupaPass_Email);
        btnKirim = findViewById(R.id.btnLupaPass_Kirim);
        progressBar = findViewById(R.id.prograssBar);

        //Tombol Kirim
        btnKirim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Inisialisasi data ke dalam variabel
                String email = etEmail.getText().toString().trim();


                if (cek_Email(email) == true) {
                    lupaPassword(email);
                }
            }
        });

        //Membuat tombol kembali pada Navigasi Bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    //Memastikan pengisian data sesuai ketentuan
    public boolean cek_Email(String email) {
        //Memberikan tanda dan mengarahkan pada data yang belum di isi
        if (email.isEmpty()) {
            etEmail.setError("Email required");
            etEmail.requestFocus();
        }
        //Memastikan email berformat email
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError("Email is invalid");
            Toast.makeText(LupaPassword.this,
                    "Email tidak sesuai", Toast.LENGTH_LONG).show();
            return false;
        } else {
            return true;
        }
    }

    public void lupaPassword(String email) {
        progressBar.setVisibility(View.VISIBLE);
        firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    //Pesan email berhasil di kirim
                    Toast.makeText(LupaPassword.this,
                            "Silahkan cek email untuk link reset kata sandi", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(LupaPassword.this, Login.class)); //Membuka halaman login
                    finish(); //Menutup halaman lupa password
                } else {
                    //Pesan email gagal di kirim
                    Toast.makeText(LupaPassword.this, "Link gagal dikirim", Toast.LENGTH_LONG).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(LupaPassword.this, "Error Failed", Toast.LENGTH_LONG).show();
            }
        });
    }

    //Kembali ke halaman utama
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        startActivity(new Intent(LupaPassword.this, Login.class)); //Membuka halaman login
        finish(); //Menutup halaman lupa password
        return true;
    }

    //Mengaktifkan tombol chat
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}