package id.kharisma.studio.hijobs.ui.main;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import id.kharisma.studio.hijobs.HubungiKami;
import id.kharisma.studio.hijobs.KelolaAkun;
import id.kharisma.studio.hijobs.KelolaProfil;
import id.kharisma.studio.hijobs.Login;
import id.kharisma.studio.hijobs.Pengaturan;
import id.kharisma.studio.hijobs.R;
import id.kharisma.studio.hijobs.Registrasi;
import id.kharisma.studio.hijobs.RiwayatLamaran;
import id.kharisma.studio.hijobs.Usaha;

public class ProfilFragment extends Fragment {

    private EditText etNama, etProfil, etAkun, etUsaha, etRiwayat, etHK, etPengaturan, etKeluar, txtNama;
    private String nama, email;
    private FirebaseFirestore db;

    public static ProfilFragment newInstance() {return new ProfilFragment();}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_main2, container, false);
        final TextView textView = root.findViewById(R.id.txtMain2_Nama);
        View fragmentView = inflater.inflate(R.layout.fragment_main2, container, false);

        setHasOptionsMenu(true); //Menghilangkan icon search dan chat

        etNama = fragmentView.findViewById(R.id.txtMain2_Nama);
        etProfil = fragmentView.findViewById(R.id.txtMain2_KelolaProfil);
        etAkun = fragmentView.findViewById(R.id.txtMain2_KelolaAkun);
        etUsaha = fragmentView.findViewById(R.id.txtMain2_UsahaSaya);
        etRiwayat = fragmentView.findViewById(R.id.txtMain2_RiwayatLamaran);
        etHK = fragmentView.findViewById(R.id.txtMain2_HubKami);
        etPengaturan = fragmentView.findViewById(R.id.txtMain2_Pengaturan);
        etKeluar = fragmentView.findViewById(R.id.txtMain2_Keluar);
        txtNama = fragmentView.findViewById(R.id.txtMain2_Nama);

        db = FirebaseFirestore.getInstance(); //Menghubungkan dengar cloud firestore
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("HiJobs",0);
        email = sharedPreferences.getString("Email",null);

        CollectionReference query = db.collection("Akun");
        query.document(email).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot snapshot) {
                nama = snapshot.getString("Nama");
                txtNama.setText(nama);
            }
        });

        //Edit text kelola profil
        etProfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), KelolaProfil.class)); //Membuka halaman kelola profil
            }
        });

        //Edit text kelola akun
        etAkun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), KelolaAkun.class)); //Membuka halaman kelola akun
            }
        });

        //Edit text usaha saya
        etUsaha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), Usaha.class)); //Membuka halaman usaha
            }
        });

        //Edit text riwayat lamaran
        etRiwayat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), RiwayatLamaran.class)); //Membuka halaman riwayat lamaran
            }
        });

        //Edit text hubungi kami
        etHK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), HubungiKami.class)); //Membuka halaman hubungi kami
            }
        });

        //Edit text pengaturan
        etPengaturan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), Pengaturan.class)); //Membuka halaman pengaturan
            }
        });

        //Edit text keluar
        etKeluar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });

        return fragmentView;
    }

    //Menghilangkan option menu
    @Override
    public void onPrepareOptionsMenu(@NonNull Menu menu) {
        menu.clear();
    }

    //Peringatan sebelum logout
    public void showDialog() {
        FirebaseAuth firebaseauth = FirebaseAuth.getInstance();
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());

        //Isi alert dialog
        alertDialogBuilder.setTitle("");
        alertDialogBuilder.setMessage("Yakin ingin keluar ?")
                .setCancelable(false)
                //Jika memilih ya
                .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        firebaseauth.signOut(); //Keluar dari akun
                        startActivity(new Intent(getActivity(), Login.class)); //Membuka halaman login
                    }
                //Jika memilih tidak
                }).setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel(); //Membatalkan alert dialog
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create(); //Membuat alert dialog dari builder
        alertDialog.show(); //Menampilkan alert dialog
    }
}