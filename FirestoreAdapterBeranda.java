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
        holder.Nama.setText(model.getNama());
        holder.NamaUsaha.setText(model.getNama_Usaha());
        holder.Alamat.setText(model.getLokasi_Usaha());
        holder.Nama.setTag(R.string.db_id,model.getDocumentId());
    }

    @NonNull
    @Override
    public ItemBerandaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_beranda,parent,false);
        return new ItemBerandaViewHolder(view);
    }

    public class ItemBerandaViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView Nama, NamaUsaha, Alamat;

        public ItemBerandaViewHolder(@NonNull View itemView) {
            super(itemView);
            Nama = itemView.findViewById(R.id.txtItemBeranda_NamaBeranda);
            NamaUsaha = itemView.findViewById(R.id.txtItemBeranda_NamaUsaha);
            Alamat = itemView.findViewById(R.id.txtItemBeranda_Alamat);
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
