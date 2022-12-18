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

public class FirestoreAdapterUsaha extends FirestoreRecyclerAdapter<ItemUsaha, FirestoreAdapterUsaha.ItemUsahaViewHolder> {

    private OnListItemClick onListItemClick;

    public FirestoreAdapterUsaha(@NonNull FirestoreRecyclerOptions<ItemUsaha> options, OnListItemClick onListItemClick) {
        super(options);
        this.onListItemClick = onListItemClick;
    }

    @Override
    protected void onBindViewHolder(@NonNull ItemUsahaViewHolder holder, int position, @NonNull ItemUsaha model) {
        holder.Nama.setText(model.getNama());
        holder.Nama.setTag(R.string.db_id,model.getDocumentId());
    }

    @NonNull
    @Override
    public ItemUsahaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_usaha,parent,false);
        return new ItemUsahaViewHolder(view);
    }

    public class ItemUsahaViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView Nama;

        public ItemUsahaViewHolder(@NonNull View itemView) {
            super(itemView);
            Nama = itemView.findViewById(R.id.txtItemUsaha_NamaUsaha);
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
