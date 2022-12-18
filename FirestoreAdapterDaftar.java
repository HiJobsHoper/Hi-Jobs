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

public class FirestoreAdapterDaftar extends FirestoreRecyclerAdapter<ItemDaftar, FirestoreAdapterDaftar.ItemDaftarViewHolder> {

    private OnListItemClick onListItemClick;

    public FirestoreAdapterDaftar(@NonNull FirestoreRecyclerOptions<ItemDaftar> options, OnListItemClick onListItemClick) {
        super(options);
        this.onListItemClick = onListItemClick;
    }

    @Override
    protected void onBindViewHolder(@NonNull ItemDaftarViewHolder holder, int position, @NonNull ItemDaftar model) {
        holder.Nama.setText(model.getNama());
        holder.Nama.setTag(R.string.db_id,model.getDocumentId());
    }

    @NonNull
    @Override
    public ItemDaftarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_daftarlamaran,parent,false);
        return new ItemDaftarViewHolder(view);
    }

    public class ItemDaftarViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView Nama;

        public ItemDaftarViewHolder(@NonNull View itemView) {
            super(itemView);
            Nama = itemView.findViewById(R.id.txtItemDaftarLamaran_NamaDaftarLamaran);
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
