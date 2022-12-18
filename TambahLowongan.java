package id.kharisma.studio.hijobs;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TambahLowongan extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private EditText etNama, etDesk, etSyarat, etGaji;
    private Spinner spnWaktu, spnKategori;
    private Button btnTambah;
    private FirebaseFirestore db;
    private FirebaseAuth firebaseAuth;
    private final String TAG = "Tambah Lowongan";
    private String email, deskUsaha, lokasiUsaha, idUsaha, namaUsaha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.activity_tambah_lowongan);

        db = FirebaseFirestore.getInstance(); //Menghubungkan dengar cloud firestore

        //Inisialisasi material desain
        etNama = findViewById(R.id.txtTbhLow_Lowongan);
        etDesk = findViewById(R.id.txtTbhLow_DeskUsaha);
        etSyarat = findViewById(R.id.txtTbhLow_Syarat);
        spnWaktu = findViewById(R.id.spnTbhLow_WaktuKerja);
        spnKategori = findViewById(R.id.spnTbhLow_Kategori);
        etGaji = findViewById(R.id.txtTbhLow_Gaji);
        btnTambah = findViewById(R.id.btnTbhLow_Tambah);

        SharedPreferences sharedPreferences = getBaseContext().getSharedPreferences("HiJobs",0);
        deskUsaha = sharedPreferences.getString("Desk_Usaha",null);
        namaUsaha = sharedPreferences.getString("Nama_Usaha",null);
        lokasiUsaha = sharedPreferences.getString("Lokasi_Usaha",null);
        idUsaha = sharedPreferences.getString("Id_Usaha", null);
        email = sharedPreferences.getString("Email",null);

        // Initializing a String Array
        String[] Kategori_Pekerjaan = new String[]{
                "Pilih kategori pekerjaan",
                "Barang",
                "Jasa"
        };
        String[] Waktu_Kerja = new String[]{
                "Pilih waktu kerja",
                "Full Time",
                "Part Time"
        };

        //Membuat kolom waktu kerja
        List<String> adapter3 = new ArrayList<>(Arrays.asList(Kategori_Pekerjaan));
        ArrayAdapter<String> spinnerArrayAdapter3 = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, adapter3) {
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
        spinnerArrayAdapter3.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spnKategori.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        String selectedItemText = (String) parent.getItemAtPosition(position);
                        if (position > 0) {
                            spnKategori.setOnItemSelectedListener(this);
                        }
                    }
                    @Override
                    public void onNothingSelected(
                            AdapterView<?> parent) {
                    }
                });
        spnKategori.setAdapter(spinnerArrayAdapter3);

        //Membuat kolom kategori pekerjaan
        List<String> adapter4 = new ArrayList<>(Arrays.asList(Waktu_Kerja));
        ArrayAdapter<String> spinnerArrayAdapter4 = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, adapter4) {
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
        spinnerArrayAdapter4.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spnWaktu.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        String selectedItemText = (String) parent.getItemAtPosition(position);
                        if (position > 0) {
                            spnWaktu.setOnItemSelectedListener(this);
                        }
                    }
                    @Override
                    public void onNothingSelected(
                            AdapterView<?> parent) {
                    }
                });
        spnWaktu.setAdapter(spinnerArrayAdapter4);

        //Tombol tambah
        btnTambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Inisialisasi data ke dalam variabel
                String Nama = etNama.getText().toString();
                String Desk = etDesk.getText().toString();
                String Syarat = etSyarat.getText().toString();
                String Waktu = spnWaktu.getSelectedItem().toString();
                String Kategori = spnKategori.getSelectedItem().toString();
                String Gaji = etGaji.getText().toString();

                if (cek_TbhLow(Nama,Desk,Syarat,Waktu,Kategori,Gaji) == true) {
                    setData(Nama,Desk,Syarat,Waktu,Kategori,Gaji,namaUsaha,deskUsaha,lokasiUsaha,idUsaha);
                    finish();
                }
            }
        });

        //Membuat tombol back pada Navigasi Bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    //Kembali ke halaman login
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        finish(); //Menutup halaman tambah lowongan
        return true;
    }

    //Mengaktifkan tombol kembali
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    //Memastikan pengisian data sesuai ketentuan
    public boolean cek_TbhLow(String Nama,String Desk,String Syarat,String Waktu,String Kategori,String Gaji) {

        //Memberikan tanda dan mengarahkan pada data yang belum di isi
        if (Gaji.isEmpty()) {
            etGaji.setError("Gaji required");
            etGaji.requestFocus();
        }
        if (Kategori.isEmpty()) {
            spnKategori.requestFocus();
        }
        if (Waktu.isEmpty()) {
            spnWaktu.requestFocus();
        }
        if (Syarat.isEmpty()) {
            etSyarat.setError("Syarat required");
            etSyarat.requestFocus();
        }
        if (Desk.isEmpty()) {
            etDesk.setError("Deskripsi required");
            etDesk.requestFocus();
        }
        if (Nama.isEmpty()) {
            etNama.setError("Name Pekerjaan required");
            etNama.requestFocus();
        }

        //Mengecek apakah ada data yang belum di isi
        if (Kategori.equals("Pilih kategori pekerjaan")) {
            Toast.makeText(TambahLowongan.this,
                    "Anda belum memilih kategori pekerjaan", Toast.LENGTH_LONG).show();
            return false;
        } else if (Waktu.equals("Pilih waktu kerja")) {
            Toast.makeText(TambahLowongan.this,
                    "Anda belum memilih waktu kerja", Toast.LENGTH_LONG).show();
            return false;
        } else if (Nama.equals("") || Desk.equals("") || Syarat.equals("") || Waktu.equals("") ||
                Gaji.equals("")) {
            //Peringatan data tidak lengkap
            Toast.makeText(TambahLowongan.this,
                    "Data anda belum lengkap", Toast.LENGTH_LONG).show();
            return false;
        } else {
            return true;
        }
    }

    //Memasukkan data ke database
    public void setData(String Nama,String Desk,String Syarat,String Waktu,String Kategori,
                        String Gaji,String Nama_Usaha,String Desk_Usaha,String Lokasi_Usaha,
                        String Id_Usaha) {

        //Membuat kolom user
        Map<String, Object> low = new HashMap<>();
        low.put("Nama", Nama);
        low.put("Deskripsi", Desk);
        low.put("Syarat", Syarat);
        low.put("Waktu", Waktu);
        low.put("Kategori", Kategori);
        low.put("Gaji", Gaji);
        low.put("Nama_Usaha", Nama_Usaha);
        low.put("Deskripsi_Usaha", Desk_Usaha);
        low.put("Lokasi_Usaha", Lokasi_Usaha);
        low.put("Id_Usaha", Id_Usaha);

        //Menyimpan referensi data pada database berdasarkan user id
        db.collection("Lowongan").document(email+"_"+Nama)
                .set(low)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        etNama.setText("");
                        etDesk.setText("");
                        etSyarat.setText("");
                        spnWaktu.setSelection(0);
                        spnKategori.setSelection(0);
                        etGaji.setText("");
                        Snackbar.make(findViewById(R.id.btnTbhLow_Tambah),
                                "Data berhasil ditambahkan", Snackbar.LENGTH_LONG).show();
                        //Log
                        Log.d(TAG, "DocumentSnapshot successfully written!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Snackbar.make(findViewById(R.id.btnTbhLow_Tambah),
                                "Data gagal ditambahkan", Snackbar.LENGTH_LONG).show();
                        //Log
                        Log.w(TAG, "Error writing document", e);
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
}