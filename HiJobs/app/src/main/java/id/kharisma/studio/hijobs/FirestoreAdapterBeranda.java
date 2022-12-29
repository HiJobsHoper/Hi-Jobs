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

public class FirestoreAdapterBeranda extends FirestoreRecyclerAdapter<ItemBeranda, FirestoreAdapterBeranda.ItemBerandaViewHolder> {

    private OnListItemClick onListItemClick;

    public FirestoreAdapterBeranda(@NonNull FirestoreRecyclerOptions<ItemBeranda> options, OnListItemClick onListItemClick) {
        super(options);
        this.onListItemClick = onListItemClick;
    }

    @Override
    protected void onBindViewHolder(@NonNull ItemBerandaViewHolder holder, int position, @NonNull ItemBeranda model) {
        holder.nama.setText(model.getNama());
        holder.alamat.setText(model.getAlamat());
        holder.kota.setText(model.getKota());
        holder.nama.setTag(R.string.db_id,model.getDocumentId());
    }

    @NonNull
    @Override
    public ItemBerandaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_beranda,parent,false);
        return new ItemBerandaViewHolder(view);
    }

    public class ItemBerandaViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView nama, alamat, kota;

        public ItemBerandaViewHolder(@NonNull View itemView) {
            super(itemView);
            nama = itemView.findViewById(R.id.txtItemBeranda_NamaLow);
            alamat = itemView.findViewById(R.id.txtItemBeranda_Alamat);
            kota = itemView.findViewById(R.id.txtItemBeranda_Kota);
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
