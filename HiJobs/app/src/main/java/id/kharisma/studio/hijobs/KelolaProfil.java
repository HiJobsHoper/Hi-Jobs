package id.kharisma.studio.hijobs;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import id.kharisma.studio.hijobs.ui.main.ProfilFragment;

public class KelolaProfil extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private EditText etNama, etTanggal, etNomor, etEmail, etAlamat, etKeahlian, etPengalaman,
            etKewarganegaraan, etPass_L, etPass_B, etPassKon_B;
    private Spinner spnJenis, spnPendidikan;
    private Button btnSimpan;
    private FirebaseUser firebaseUser;
    private DatabaseReference databaseReference;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseAuth firebaseAuth;
    private String UserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kelola_profil);

        //Inisialisasi material desain
        etNama = findViewById(R.id.txtKelProfil_NamaPengguna);
        spnJenis = findViewById(R.id.spnKelProfil_JenisKelamin);
        etTanggal = findViewById(R.id.txtKelProfil_TglLahir);
        etNomor = findViewById(R.id.txtKelProfil_Telepon);
        etEmail = findViewById(R.id.txtKelProfil_Email);
        spnPendidikan = findViewById(R.id.spnKelProfil_Pendidikan);
        etAlamat = findViewById(R.id.txtKelProfil_Alamat);
        etKeahlian = findViewById(R.id.txtKelProfil_Keahlian);
        etPengalaman = findViewById(R.id.txtKelProfil_PengalamanKerja);
        etKewarganegaraan = findViewById(R.id.txtKelProfil_Kewarganegaraan);
        etPass_L = findViewById(R.id.txtKelProfil_SandiSekarang);
        etPass_B = findViewById(R.id.txtKelProfil_SandiBaru);
        etPassKon_B = findViewById(R.id.txtKelProfil_KonfirSandi);
        btnSimpan = findViewById(R.id.btnKelProfil_Simpan);

        //Membuat tombol kembali pada Navigasi Bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        //Membuat kolom jenis kelamin
        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(
                this, R.array.Jenis_Kelamin, android.R.layout.simple_spinner_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnJenis.setAdapter(adapter1);
        spnJenis.setOnItemSelectedListener(this);

        //Membuat kolom pendidikan terakhir
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(
                this, R.array.Pendidikan_Terakhir, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnPendidikan.setAdapter(adapter2);
        spnPendidikan.setOnItemSelectedListener(this);

        btnSimpan.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //Inisialisasi data ke dalam variabel
                String Nama = etNama.getText().toString();
                String Jenis = spnJenis.getSelectedItem().toString();
                String Tanggal = etTanggal.getText().toString();
                String Nomor = etNomor.getText().toString();
                String Email = etEmail.getText().toString();
                String Pendidikan = spnPendidikan.getSelectedItem().toString();
                String Alamat = etAlamat.getText().toString();
                String Keahlian = etKeahlian.getText().toString();
                String Pengalaman = etPengalaman.getText().toString();
                String Kewarganegaraan = etKewarganegaraan.getText().toString();
                String Pass_Lama = etPass_L.getText().toString();
                String Pass_Baru = etPass_B.getText().toString();
                String PassKon_Baru = etPassKon_B.getText().toString();

                if (!Pass_Lama.equals("") || !Pass_Baru.equals("") || !PassKon_Baru.equals("")) {
                    if (cek_UbahPass(Pass_Lama,Pass_Baru,PassKon_Baru) == true) {
                        changePassword(Pass_Baru);
                    }
                } else {
                    if (cek_KelPro(Nama,Tanggal,Nomor,Email,Alamat,Keahlian,Pengalaman,
                            Kewarganegaraan) == true) {
                        if (UserId == "") {
                            Toast.makeText(KelolaProfil.this, "Data belum ada", Toast.LENGTH_SHORT).show();
                        } else {
                            setData(Nama,Jenis,Tanggal,Nomor,Email,Pendidikan,Alamat,Keahlian,
                                    Pengalaman,Kewarganegaraan);
                        }
                    }
                }
            }
        });

        //Show hide kata sandi menggunakan icon mata pada sandi lama
        ImageView imageViewShowHidePwd = findViewById(R.id.img_KelProfilSandiSkrg);
        imageViewShowHidePwd.setImageResource(R.drawable.ic_baseline_visibility_off_24);
        imageViewShowHidePwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etPass_L.getTransformationMethod().equals(HideReturnsTransformationMethod.getInstance())){
                    //Membuat kata sandi kelihatan
                    etPass_L.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    //Mengganti icon
                    imageViewShowHidePwd.setImageResource(R.drawable.ic_baseline_visibility_off_24);
                }else {
                    //Membuat kata sandi tidak kelihatan
                    etPass_L.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    //Mengganti icon
                    imageViewShowHidePwd.setImageResource(R.drawable.ic_baseline_visibility_24);
                }
            }
        });

        //Show hide kata sandi menggunakan icon mata pada sandi baru
        ImageView imageViewShowHidePwdNew = findViewById(R.id.img_KelProfilSandiBaru);
        imageViewShowHidePwdNew.setImageResource(R.drawable.ic_baseline_visibility_off_24);
        imageViewShowHidePwdNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etPass_B.getTransformationMethod().equals(HideReturnsTransformationMethod.getInstance())){
                    //Membuat kata sandi kelihatan
                    etPass_B.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    //Mengganti icon
                    imageViewShowHidePwdNew.setImageResource(R.drawable.ic_baseline_visibility_off_24);
                }else {
                    //Membuat kata sandi tidak kelihatan
                    etPass_B.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    //Mengganti icon
                    imageViewShowHidePwdNew.setImageResource(R.drawable.ic_baseline_visibility_24);
                }
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String text = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
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
    public boolean cek_KelPro(String Nama,String Tanggal,String Nomor,String Email,String Alamat,
                              String Keahlian,String Pengalaman,String Kewarganegaraan) {

        //Memberikan tanda dan mengarahkan pada data yang belum di isi
        if (Kewarganegaraan.isEmpty()) {
            etKewarganegaraan.setError("Nationality required");
            etKewarganegaraan.requestFocus();
        }
        if (Pengalaman.isEmpty()) {
            etPengalaman.setError("Experience required");
            etPengalaman.requestFocus();
        }
        if (Keahlian.isEmpty()) {
            etKeahlian.setError("Skill required");
            etKeahlian.requestFocus();
        }
        if (Alamat.isEmpty()) {
            etAlamat.setError("Address required");
            etAlamat.requestFocus();
        }
        if (Email.isEmpty()) {
            etEmail.setError("Email required");
            etEmail.requestFocus();
        }
        if (Nomor.isEmpty()) {
            etNomor.setError("Phone number required");
            etNomor.requestFocus();
        }
        if (Tanggal.isEmpty()) {
            etTanggal.setError("Age required");
            etTanggal.requestFocus();
        }
        if (Nama.isEmpty()) {
            etNama.setError("Name required");
            etNama.requestFocus();
        }

        //Mengecek apakah ada data yang belum di isi
        if (Nama.equals("") || Tanggal.equals("") || Nomor.equals("") || Email.equals("") ||
                Alamat.equals("") || Keahlian.equals("") || Pengalaman.equals("") ||
                Kewarganegaraan.equals("")) {
            //Peringatan data tidak lengkap
            Toast.makeText(KelolaProfil.this,
                    "Data anda belum lengkap", Toast.LENGTH_LONG).show();
            return false; //Pengisian tidak sesuai ketentuan
        } else {
            //Memastikan email berformat email
            if (!Patterns.EMAIL_ADDRESS.matcher(Email).matches()) {
                etEmail.setError("Email is invalid");
                Toast.makeText(KelolaProfil.this,
                        "Email tidak sesuai", Toast.LENGTH_LONG).show();
            }
            return true;
        }
    }

    //Memastikan pengisian data sesuai ketentuan
    public boolean cek_UbahPass(String Pass_Lama,String Pass_Baru,String PassKon_Baru){

        //Memberikan tanda dan mengarahkan pada data yang belum di isi
        if (PassKon_Baru.isEmpty()) {
            etPassKon_B.setError("Confirm password required");
            etPassKon_B.requestFocus();
        }
        if (Pass_Baru.isEmpty()) {
            etPass_B.setError("New password required");
            etPass_B.requestFocus();
        }
        if (Pass_Lama.isEmpty()) {
            etPass_L.setError("Password required");
            etPass_L.requestFocus();
        }

        //Mengecek apakah ada data yang belum di isi
        if (Pass_Lama.equals("") || Pass_Baru.equals("") || PassKon_Baru.equals("")) {
            //Peringatan data tidak lengkap
            Toast.makeText(KelolaProfil.this,
                    "Data anda belum lengkap", Toast.LENGTH_LONG).show();
            return false; //Pengisian tidak sesuai ketentuan
        } else {
            //Memastikan kata sandi tidak kurang dari 6 karakter
            if (Pass_Baru.length()<6) {
                etPass_B.setError("Password is invalid");
                Toast.makeText(KelolaProfil.this,
                        "Kata sandi kurang dari 6 karakter", Toast.LENGTH_LONG).show();
            }
            //Memastikan kata sandi dan konfirmasi kata sandi sama
            if (Pass_Baru.equals(PassKon_Baru)) {
                return true; //Pengisian sesuai ketentuan
            } else {
                //Peringatan kata sandi dan konfirmasi kata sandi tidak sama
                Toast.makeText(KelolaProfil.this,
                        "Kata sandi anda tidak sama", Toast.LENGTH_LONG).show();
                etPassKon_B.requestFocus();
                return false; //Pengisian tidak sesuai ketentuan
            }
        }
    }

    //Mengubah kata sandi
    public void changePassword(String Pass_Baru) {

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        etPass_L.setTransformationMethod(PasswordTransformationMethod.getInstance());
        firebaseUser.updatePassword(Pass_Baru).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful() && task.getResult()!=null) {
                    //Kata sandi berhasil di ganti
                    Toast.makeText(KelolaProfil.this,
                            "Kata sandi berhasil di ganti", Toast.LENGTH_LONG).show();
                } else {
                    //Kata sandi gagal di ganti
                    Toast.makeText(KelolaProfil.this,
                            task.getException().getLocalizedMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    //Memasukkan data ke database
    public void setData(String Nama,String Jenis,String Tanggal,String Nomor,String Email,
                        String Pendidikan,String Alamat,String Keahlian,String Pengalaman,
                        String Kewarganegaraan) {

        firebaseDatabase = FirebaseDatabase.getInstance(); //Mendapatkan instance realtime database
        firebaseAuth = FirebaseAuth.getInstance(); //Menghubungkan dengan firebase authentifikasi
        UserId = firebaseAuth.getCurrentUser().getUid(); //Mengambil user id dari database
        databaseReference = firebaseDatabase.getReference(); //Mendapat data referensi dari database

        //Menyimpan referensi data pada database berdasarkan user id
        databaseReference.child(UserId).push().setValue(Nama);
        databaseReference.child(UserId).push().setValue(Jenis);
        databaseReference.child(UserId).push().setValue(Tanggal);
        databaseReference.child(UserId).push().setValue(Nomor);
        databaseReference.child(UserId).push().setValue(Email);
        databaseReference.child(UserId).push().setValue(Pendidikan);
        databaseReference.child(UserId).push().setValue(Alamat);
        databaseReference.child(UserId).push().setValue(Keahlian);
        databaseReference.child(UserId).push().setValue(Pengalaman);
        databaseReference.child(UserId).push().setValue(Kewarganegaraan).addOnSuccessListener(this,
                new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                etNama.setText("");
                spnJenis.setSelection(0);
                etTanggal.setText("");
                etNomor.setText("");
                etEmail.setText("");
                spnPendidikan.setSelection(0);
                etAlamat.setText("");
                etKeahlian.setText("");
                etPengalaman.setText("");
                etKewarganegaraan.setText("");
                Snackbar.make(findViewById(R.id.btnKelProfil_Simpan), "Data berhasil ditambahkan", Snackbar.LENGTH_LONG).show();
            }
        });
    }
}