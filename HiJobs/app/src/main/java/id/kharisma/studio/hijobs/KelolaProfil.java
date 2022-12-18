package id.kharisma.studio.hijobs;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.RecyclerView;

import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.wifi.hotspot2.pps.Credential;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import id.kharisma.studio.hijobs.ui.main.ProfilFragment;

public class KelolaProfil extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private EditText etNama, etTanggal, etUmur, etAlamat, etKeahlian, etPengalaman, etKewarganegaraan, txtNama;
    private Spinner spnJenis, spnPendidikan;
    private Button btnSimpan;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore db;
    private String nama, email;
    private static final String TAG = "Kelola Profil";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.activity_kelola_profil);

        firebaseAuth = FirebaseAuth.getInstance(); //Menghubungkan dengan firebase authentifikasi
        db = FirebaseFirestore.getInstance(); //Menghubungkan dengar cloud firestore

        //Inisialisasi material desain
        etNama = findViewById(R.id.txtKelProfil_NamaLengkap);
        spnJenis = findViewById(R.id.spnKelProfil_JenisKelamin);
        etTanggal = findViewById(R.id.txtKelProfil_TglLahir);
        etUmur = findViewById(R.id.txtKelProfil_Umur);
        spnPendidikan = findViewById(R.id.spnKelProfil_Pendidikan);
        etAlamat = findViewById(R.id.txtKelProfil_Alamat);
        etKeahlian = findViewById(R.id.txtKelProfil_Keahlian);
        etPengalaman = findViewById(R.id.txtKelProfil_PengalamanKerja);
        etKewarganegaraan = findViewById(R.id.txtKelProfil_Kewarganegaraan);
        btnSimpan = findViewById(R.id.btnKelProfil_Simpan);
        txtNama = findViewById(R.id.txtKelProfil_Nama);

        SharedPreferences sharedPreferences = getBaseContext().getSharedPreferences("HiJobs",0);
        email = sharedPreferences.getString("Email",null);

        CollectionReference query = db.collection("Akun");
        query.document(email).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot snapshot) {
                nama = snapshot.getString("Nama");
                txtNama.setText(nama);
            }
        });

        //Membuat tombol kembali pada Navigasi Bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        //Membuat kolom jenis kelamin

        // Initializing a String Array
        String[] Jenis_Kelamin = new String[]{
                "Pilih jenis kelamin",
                "Laki-laki",
                "Perempuan"
        };
        String[] Pendidikan = new String[]{
                "Pilih pendidikan",
                "SMA/SMK",
                "S1",
                "S2"
        };

        //Membuat kolom jenis kelamin
        List<String> adapter1 = new ArrayList<>(Arrays.asList(Jenis_Kelamin));
        ArrayAdapter<String> spinnerArrayAdapter1 = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, adapter1) {
            @Override
            public boolean isEnabled(int position) {
                return position != 0;
            }
            @Override
            public View getDropDownView(int position, View convertView,@NonNull ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView textView = (TextView) view;
                if (position == 0) {
                    textView.setTextColor(Color.GRAY);
                } else {
                    textView.setTextColor(Color.BLACK);
                }
                return view;
            }
        };
        spinnerArrayAdapter1.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spnJenis.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        String selectedItemText = (String) parent.getItemAtPosition(position);
                        if (position > 0) {
                            spnJenis.setOnItemSelectedListener(this);
                        }
                    }
                    @Override
                    public void onNothingSelected(
                            AdapterView<?> parent) {
                    }
                });
        spnJenis.setAdapter(spinnerArrayAdapter1);

        //Membuat kolom pendidikan terakhir
        List<String> adapter2 = new ArrayList<>(Arrays.asList(Pendidikan));
        ArrayAdapter<String> spinnerArrayAdapter2 = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, adapter2) {
            @Override
            public boolean isEnabled(int position) {
                return position != 0;
            }
            @Override
            public View getDropDownView(int position, View convertView,@NonNull ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView textView = (TextView) view;
                if (position == 0) {
                    textView.setTextColor(Color.GRAY);
                } else {
                    textView.setTextColor(Color.BLACK);
                }
                return view;
            }
        };
        spinnerArrayAdapter2.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spnPendidikan.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        String selectedItemText = (String) parent.getItemAtPosition(position);
                        if (position > 0) {
                            spnPendidikan.setOnItemSelectedListener(this);
                        }
                    }
                    @Override
                    public void onNothingSelected(
                            AdapterView<?> parent) {
                    }
                });
        spnPendidikan.setAdapter(spinnerArrayAdapter2);

        //Tombol simpan
        btnSimpan.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //Inisialisasi data ke dalam variabel
                String Nama = etNama.getText().toString();
                String Jenis = spnJenis.getSelectedItem().toString();
                String Tanggal = etTanggal.getText().toString();
                String Umur = etUmur.getText().toString();
                String Pendidikan = spnPendidikan.getSelectedItem().toString();
                String Alamat = etAlamat.getText().toString();
                String Keahlian = etKeahlian.getText().toString();
                String Pengalaman = etPengalaman.getText().toString();
                String Kewarganegaraan = etKewarganegaraan.getText().toString();

                if (cek_KelPro(Nama,Jenis,Tanggal,Umur,Pendidikan,Alamat,Keahlian,Pengalaman,Kewarganegaraan) == true) {
                    setData(Nama,Jenis,Tanggal,Umur,Pendidikan,Alamat,Keahlian,Pengalaman,Kewarganegaraan);
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
    public boolean cek_KelPro(String Nama,String Jenis,String Tanggal,String Umur,String Pendidikan,
                              String Alamat,String Keahlian,String Pengalaman,String Kewarganegaraan) {

        //Memberikan tanda dan mengarahkan pada data yang belum di isi
        if (Kewarganegaraan.isEmpty()) {
            etKewarganegaraan.setError("Nationality required");
            etKewarganegaraan.requestFocus();
        }
//        if (Pengalaman.isEmpty()) {
//            etPengalaman.setError("Experience required");
//            etPengalaman.requestFocus();
//        }
//        if (Keahlian.isEmpty()) {
//            etKeahlian.setError("Skill required");
//            etKeahlian.requestFocus();
//        }
        if (Alamat.isEmpty()) {
            etAlamat.setError("Address required");
            etAlamat.requestFocus();
        }
        if (Pendidikan.isEmpty()) {
            spnPendidikan.requestFocus();
        }
        if (Umur.isEmpty()) {
            etNama.setError("Age required");
            etNama.requestFocus();
        }
        if (Tanggal.isEmpty()) {
            etTanggal.setError("Birth required");
            etTanggal.requestFocus();
        }
        if (Jenis.isEmpty()) {
            spnJenis.requestFocus();
        }
        if (Nama.isEmpty()) {
            etNama.setError("Name required");
            etNama.requestFocus();
        }

        //Mengecek apakah ada data yang belum di isi
        if (Jenis.equals("Pilih jenis kelamin")) {
            Toast.makeText(KelolaProfil.this,
                    "Anda belum memilih jenis kelamin", Toast.LENGTH_LONG).show();
            return false;
        } else if (Pendidikan.equals("Pilih pendidikan")) {
            Toast.makeText(KelolaProfil.this,
                    "Anda belum memilih pendidikan", Toast.LENGTH_LONG).show();
            return false;
        } else if(Nama.equals("") || Jenis.equals("Pilih jenis kelamin") || Tanggal.equals("") ||
                Umur.equals("") || Pendidikan.equals("Pilih pendidikan") || Alamat.equals("") ||
                /*Keahlian.equals("") || Pengalaman.equals("") ||*/ Kewarganegaraan.equals("")) {
            //Peringatan data tidak lengkap
            Toast.makeText(KelolaProfil.this,
                    "Data anda belum lengkap", Toast.LENGTH_LONG).show();
            return false; //Pengisian tidak sesuai ketentuan
        } else {
            return true;
        }
    }

    //Memasukkan data ke database
    public void setData(String Nama,String Jenis,String Tanggal,String Umur,String Pendidikan,
                        String Alamat,String Keahlian,String Pengalaman,String Kewarganegaraan) {

        final String email = firebaseAuth.getCurrentUser().getEmail();

        //Membuat kolom user
        Map<String, Object> user = new HashMap<>();
        user.put("Nama", Nama);
        user.put("Jenis Kelamin", Jenis);
        user.put("Tanggal Lahir", Tanggal);
        user.put("Umur", Umur);
        user.put("Pendidikan Terakhir", Pendidikan);
        user.put("Alamat", Alamat);
        user.put("Keahlian", Keahlian);
        user.put("Pengalaman Kerja", Pengalaman);
        user.put("Kewarganegaraan", Kewarganegaraan);

        //Menyimpan referensi data pada database berdasarkan user id
        db.collection("Profil").document(email)
                .set(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        etNama.setText("");
                        spnJenis.setSelection(0);
                        etTanggal.setText("");
                        etUmur.setText("");
                        spnPendidikan.setSelection(0);
                        etAlamat.setText("");
                        etKeahlian.setText("");
                        etPengalaman.setText("");
                        etKewarganegaraan.setText("");
                        Snackbar.make(findViewById(R.id.btnKelProfil_Simpan),
                                "Data berhasil ditambahkan", Snackbar.LENGTH_LONG).show();
                        //Log
                        Log.d(TAG, "DocumentSnapshot successfully written!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Snackbar.make(findViewById(R.id.btnKelProfil_Simpan),
                                "Data gagal ditambahkan", Snackbar.LENGTH_LONG).show();
                        //Log
                        Log.w(TAG, "Error writing document", e);
                    }
                });
    }
}