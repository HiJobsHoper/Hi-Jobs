package id.kharisma.studio.hijobs;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Login extends AppCompatActivity {

    private TextView reg;
    private Button btnMasuk;
    private EditText etEmail,etPass;
    private CheckBox chkPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        reg = findViewById(R.id.textView9);
        btnMasuk = findViewById(R.id.btnLog_Masuk);
        etEmail = findViewById(R.id.txtLog_Email);
        etPass = findViewById(R.id.txtLog_KataSandi);
        chkPass = findViewById(R.id.chkLog_TampilSandi);

        //Membuka halaman registrasi
        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this, Registrasi.class));
                finish();
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
                if (cek_Log() == true) {
                    /*if (etEmail.getText().toString().equalsIgnoreCase() &&
                            etPass.getText().toString().equalsIgnoreCase()) {
                        startActivity(new Intent(Login.this,. class));
                        finish();
                    } else {
                        Toast.makeText(Login.this,
                            "Username atau Password Anda Salah", Toast.LENGTH_LONG).show();
                    }*/
                }
            }
        });
    }

    public boolean cek_Log() {
        //inisialisasi data ke dalam variabel
        String Email = etEmail.getText().toString();
        String Pass = etPass.getText().toString();
        boolean nilai = false;

        //Memberikan tanda pada data yang belum di isi
        if (!Pass.isEmpty() && !Email.isEmpty()) {
            nilai = true;
        } else {
            if (Pass.isEmpty()) {
                etPass.setError("Password required");
                etPass.requestFocus();
            }
            if (Email.isEmpty()) {
                etEmail.setError("Email required");
                etEmail.requestFocus();
            }
        }

        return nilai;
    }
}