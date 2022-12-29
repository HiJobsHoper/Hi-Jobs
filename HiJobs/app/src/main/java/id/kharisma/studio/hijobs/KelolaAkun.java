package id.kharisma.studio.hijobs;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.RecyclerView;

import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.wifi.hotspot2.pps.Credential;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import id.kharisma.studio.hijobs.ui.main.ProfilFragment;

public class KelolaAkun extends AppCompatActivity {

    //Inisialisasi material desain
    private EditText etNama, etNomor, etEmail, etPass_L, etPass_B, etPassKon_B;
    private Button btnSimpan;
    private FirebaseUser firebaseUser;
    private FirebaseAuth firebaseAuth;
    private AuthCredential authCredential;
    private FirebaseFirestore db;
    private String email;
    private static final String TAG = "Kelola Akun";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.activity_kelola_akun);

        firebaseAuth = FirebaseAuth.getInstance(); //Menghubungkan dengan firebase authentifikasi
        db = FirebaseFirestore.getInstance(); //Menghubungkan dengar cloud firestore

        //Inisialisasi material desain
        etNama = findViewById(R.id.txtKelAkun_NamaPengguna);
        etNomor = findViewById(R.id.txtKelAkun_Telepon);
        etEmail = findViewById(R.id.txtKelAkun_Email);
        etPass_L = findViewById(R.id.txtKelAkun_SandiSekarang);
        etPass_B = findViewById(R.id.txtKelAkun_SandiBaru);
        etPassKon_B = findViewById(R.id.txtKelAkun_KonfirSandi);
        btnSimpan = findViewById(R.id.btnKelAkun_Simpan);

        email = getIntent().getStringExtra("Email");

        CollectionReference query = db.collection("Akun");
        query.document(email).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot snapshot) {
                etNama.setText(snapshot.getString("Nama"));
                etEmail.setText(snapshot.getString("Email"));
                etNomor.setText(snapshot.getString("Nomor Televon"));
            }
        });

        //Membuat tombol kembali pada Navigasi Bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        btnSimpan.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //Inisialisasi data ke dalam variabel
                String Nama = etNama.getText().toString();
                String Nomor = etNomor.getText().toString();
                String Email = etEmail.getText().toString();
                String Pass_Lama = etPass_L.getText().toString();
                String Pass_Baru = etPass_B.getText().toString();
                String PassKon_Baru = etPassKon_B.getText().toString();

                if (!Pass_Lama.equals("") || !Pass_Baru.equals("") || !PassKon_Baru.equals("")) {
                    if (cek_UbahPass(Pass_Lama,Pass_Baru,PassKon_Baru) == true) {
                        changePassword(Pass_Lama,Pass_Baru);
                    }
                } else {
                    if (cek_KelPro(Nama,Nomor,Email) == true) {
                        setData(Nama,Nomor,Email);
                    }
                }
            }
        });

        //Show hide kata sandi menggunakan icon mata pada sandi lama
        ImageView imageViewShowHidePwd = findViewById(R.id.imgKelAkun_SandiSkrg);
        imageViewShowHidePwd.setImageResource(R.drawable.ic_baseline_visibility_off_24);
        imageViewShowHidePwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etPass_L.getTransformationMethod().equals(HideReturnsTransformationMethod.getInstance())){
                    //Membuat kata sandi tidak kelihatan
                    etPass_L.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    //Mengganti icon
                    imageViewShowHidePwd.setImageResource(R.drawable.ic_baseline_visibility_off_24);
                }else {
                    //Membuat kata sandi kelihatan
                    etPass_L.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    //Mengganti icon
                    imageViewShowHidePwd.setImageResource(R.drawable.ic_baseline_visibility_24);
                }
            }
        });

        //Show hide kata sandi menggunakan icon mata pada sandi baru
        ImageView imageViewShowHidePwdNew = findViewById(R.id.imgKelAkun_SandiBaru);
        imageViewShowHidePwdNew.setImageResource(R.drawable.ic_baseline_visibility_off_24);
        imageViewShowHidePwdNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etPass_B.getTransformationMethod().equals(HideReturnsTransformationMethod.getInstance())){
                    //Membuat kata sandi tidak kelihatan
                    etPass_B.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    //Mengganti icon
                    imageViewShowHidePwdNew.setImageResource(R.drawable.ic_baseline_visibility_off_24);
                }else {
                    //Membuat kata sandi kelihatan
                    etPass_B.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    //Mengganti icon
                    imageViewShowHidePwdNew.setImageResource(R.drawable.ic_baseline_visibility_24);
                }
            }
        });

        //Show hide kata sandi menggunakan icon mata pada konfirmasi sandi baru
        ImageView imageViewShowHideConPwd = findViewById(R.id.imgKelAkun_KonfirSandi);
        imageViewShowHideConPwd.setImageResource(R.drawable.ic_baseline_visibility_off_24);
        imageViewShowHideConPwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etPassKon_B.getTransformationMethod().equals(HideReturnsTransformationMethod.getInstance())){
                    //Membuat password tidak kelihatan
                    etPassKon_B.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    //Mengganti icon
                    imageViewShowHideConPwd.setImageResource(R.drawable.ic_baseline_visibility_off_24);
                }else {
                    //Membuat password tidak kelihatan
                    etPassKon_B.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    //Mengganti icon
                    imageViewShowHideConPwd.setImageResource(R.drawable.ic_baseline_visibility_24);
                }
            }
        });
    }

    //Kembali ke halaman profil
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
    public boolean cek_KelPro(String Nama,String Nomor,String Email) {

        //Memberikan tanda dan mengarahkan pada data yang belum di isi
        if (Nomor.isEmpty()) {
            etNomor.setError("Phone number required");
            etNomor.requestFocus();
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
        if (Nama.equals("") || Nomor.equals("") || Email.equals("")) {
            //Peringatan data tidak lengkap
            Toast.makeText(KelolaAkun.this,
                    "Data anda belum lengkap", Toast.LENGTH_LONG).show();
            return false; //Pengisian tidak sesuai ketentuan
        } else {
            //Memastikan email berformat email
            if (!Patterns.EMAIL_ADDRESS.matcher(Email).matches()) {
                etEmail.setError("Email is invalid");
                Toast.makeText(KelolaAkun.this,
                        "Email tidak sesuai", Toast.LENGTH_LONG).show();
            }
            return true;
        }
    }

    //Memasukkan data ke database
    public void setData(String Nama,String Nomor,String Email) {

        final String email = firebaseAuth.getCurrentUser().getEmail();

        //Membuat kolom user
        Map<String, Object> user = new HashMap<>();
        user.put("Nama", Nama);
        user.put("Nomor Televon", Nomor);
        user.put("Email", Email);

        //Menyimpan referensi data pada database berdasarkan user id
        db.collection("Akun").document(email)
                .update(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
//                        etNama.setText("");
//                        etNomor.setText("");
//                        etEmail.setText("");
                        Snackbar.make(findViewById(R.id.btnKelAkun_Simpan),
                                "Data berhasil ditambahkan", Snackbar.LENGTH_LONG).show();
                        //Log
                        Log.d(TAG, "DocumentSnapshot successfully written!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Snackbar.make(findViewById(R.id.btnKelAkun_Simpan),
                                "Data gagal ditambahkan", Snackbar.LENGTH_LONG).show();
                        //Log
                        Log.w(TAG, "Error writing document", e);
                    }
                });
    }

    //Memastikan pengisian data sesuai ketentuan
    public boolean cek_UbahPass(String Pass_Lama,String Pass_Baru,String PassKon_Baru){

        //Memberikan tanda dan mengarahkan pada data yang belum di isi
        if (PassKon_Baru.isEmpty()) {
            //etPassKon_B.setError("Confirm password required");
            etPassKon_B.requestFocus();
        }
        if (Pass_Baru.isEmpty()) {
            //etPass_B.setError("New password required");
            etPass_B.requestFocus();
        }
        if (Pass_Lama.isEmpty()) {
            //etPass_L.setError("Password required");
            etPass_L.requestFocus();
        }

        //Mengecek apakah ada data yang belum di isi
        if (Pass_Lama.equals("") || Pass_Baru.equals("") || PassKon_Baru.equals("")) {
            //Peringatan data tidak lengkap
            Toast.makeText(KelolaAkun.this,
                    "Data anda belum lengkap", Toast.LENGTH_LONG).show();
            return false; //Pengisian tidak sesuai ketentuan
        } else {
            //Memastikan kata sandi tidak kurang dari 6 karakter
            if (Pass_Baru.length()<6) {
                //etPass_B.setError("Password is invalid");
                Toast.makeText(KelolaAkun.this,
                        "Kata sandi kurang dari 6 karakter", Toast.LENGTH_LONG).show();
                etPass_B.requestFocus();
            }
            //Memastikan kata sandi dan konfirmasi kata sandi sama
            if (Pass_Baru.equals(PassKon_Baru)) {
                return true; //Pengisian sesuai ketentuan
            } else {
                //Peringatan kata sandi dan konfirmasi kata sandi tidak sama
                Toast.makeText(KelolaAkun.this,
                        "Kata sandi anda tidak sama", Toast.LENGTH_LONG).show();
                etPassKon_B.requestFocus();
                return false; //Pengisian tidak sesuai ketentuan
            }
        }
    }

    //Mengubah kata sandi
    public void changePassword(String Pass_Lama,String Pass_Baru) {
        final String email = firebaseAuth.getInstance().getCurrentUser().getEmail();
        authCredential = EmailAuthProvider.getCredential(email,Pass_Lama);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        etPass_L.setTransformationMethod(PasswordTransformationMethod.getInstance());
        firebaseUser.reauthenticate(authCredential).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    //Mengubah kata sandi di database
                    firebaseUser.updatePassword(Pass_Baru).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                //Kata sandi berhasil di ganti
                                etPass_L.setText("");
                                etPass_B.setText("");
                                etPassKon_B.setText("");
                                Snackbar.make(findViewById(R.id.btnKelAkun_Simpan),
                                        "Kata sandi berhasil di ganti", Snackbar.LENGTH_LONG).show();
                            } else {
                                //Kata sandi gagal di ganti
                                Toast.makeText(KelolaAkun.this,
                                        task.getException().getLocalizedMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                } else {
                    Toast.makeText(KelolaAkun.this, "Kata sandi lama tidak sesuai",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}