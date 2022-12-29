package id.kharisma.studio.hijobs;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import id.kharisma.studio.hijobs.databinding.ActivityHalamanUtamaBinding;
import id.kharisma.studio.hijobs.ui.main.BerandaFragment;

public class HalamanUtama extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityHalamanUtamaBinding binding;
    private TextView txtnama, txtemail;
    private FirebaseFirestore db;
    private String nama, email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityHalamanUtamaBinding.inflate(getLayoutInflater());
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO); //Menonaktifkan night mode
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarHalamanBeranda.toolbar);

        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_beranda, R.id.nav_profil, R.id.nav_favorit)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_halaman_beranda);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        getSupportFragmentManager().beginTransaction().
                replace(R.id.beranda, new BerandaFragment(), "beranda").
                commit();
        // Now later we can lookup the fragment by tag
//        BerandaFragment berandaFragment = (BerandaFragment)
//                getSupportFragmentManager().findFragmentByTag("beranda");

        //Mengganti usernama dan email pada navigasi drawer
        View nav_view = navigationView.getHeaderView(0);
        txtnama = nav_view.findViewById(R.id.txtNav_Nama);
        txtemail = nav_view.findViewById(R.id.txtNav_Email);
        db = FirebaseFirestore.getInstance();

        nama = getIntent().getStringExtra("Nama");
        email = getIntent().getStringExtra("Email");
        SharedPreferences sharedPreferences = getBaseContext().getSharedPreferences("HiJobs",0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("Email",email);
        editor.commit();

        if (nama != null && email != null) {
            txtnama.setText(nama);
            txtemail.setText(email);
        }

        CollectionReference query = db.collection("Akun");
        query.document(email).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot snapshot) {
                nama = snapshot.getString("Nama");
                if (nama != null && email != null) {
                    txtnama.setText(nama);
                    txtemail.setText(email);
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.halaman_beranda, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_halaman_beranda);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_search:
                /*final SearchView searchView = (SearchView) item.getActionView();
                searchView.setQueryHint("Kolom pencarian");
                searchView.setIconified(false);
                searchView.clearFocus();*/
                break;
            case R.id.menu_chat:
                startActivity(new Intent(this, DaftarChat.class));
        }
        return super.onOptionsItemSelected(item);
    }
}