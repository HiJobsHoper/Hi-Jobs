package id.kharisma.studio.hijobs;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;

public class FirestoreAdapterRiwayat extends FirestoreRecyclerAdapter<ItemRiwayat, FirestoreAdapterRiwayat.ItemRiwayatViewHolder> {

    private OnListItemClick onListItemClick;

    public FirestoreAdapterRiwayat(@NonNull FirestoreRecyclerOptions<ItemRiwayat> options, OnListItemClick onListItemClick) {
        super(options);
        this.onListItemClick = onListItemClick;
    }

    @Override
    protected void onBindViewHolder(@NonNull ItemRiwayatViewHolder holder, int position, @NonNull ItemRiwayat model) {
        holder.lowongan.setText(model.getLowongan());
        holder.alamat.setText(model.getAlamat());
        holder.kota.setText(model.getKota());
        holder.lowongan.setTag(R.string.db_id,model.getDocumentId());
    }

    @NonNull
    @Override
    public ItemRiwayatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_riwayat,parent,false);
        return new ItemRiwayatViewHolder(view);
    }

    public class ItemRiwayatViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView lowongan, alamat, kota;

        public ItemRiwayatViewHolder(@NonNull View itemView) {
            super(itemView);
            lowongan = itemView.findViewById(R.id.txtItemRiwayat_NamaLow);
            alamat = itemView.findViewById(R.id.txtItemRiwayat_Alamat);
            kota = itemView.findViewById(R.id.txtItemRiwayat_Kota);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onListItemClick.onItemClick(v);
        }
    }

    public interface OnListItemClick {
        void onItemClick(View view);
    }
}
