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

public class FirestoreAdapterLowongan extends FirestoreRecyclerAdapter<ItemLowongan, FirestoreAdapterLowongan.ItemLowonganViewHolder> {

    private OnListItemClick onListItemClick;

    public FirestoreAdapterLowongan(@NonNull FirestoreRecyclerOptions<ItemLowongan> options, OnListItemClick onListItemClick) {
        super(options);
        this.onListItemClick = onListItemClick;
    }

    @Override
    protected void onBindViewHolder(@NonNull ItemLowonganViewHolder holder, int position, @NonNull ItemLowongan model) {
        holder.Nama.setText(model.getNama());
        holder.Nama.setTag(R.string.db_id,model.getDocumentId());
    }

    @NonNull
    @Override
    public ItemLowonganViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_lowongan,parent,false);
        return new ItemLowonganViewHolder(view);
    }

    public class ItemLowonganViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView Nama;

        public ItemLowonganViewHolder(@NonNull View itemView) {
            super(itemView);
            Nama = itemView.findViewById(R.id.txtItemLowongan_NamaLowongan);
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
