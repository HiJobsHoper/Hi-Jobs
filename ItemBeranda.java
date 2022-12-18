package id.kharisma.studio.hijobs;

import com.google.firebase.firestore.DocumentId;
import com.google.firebase.firestore.Exclude;

public class ItemBeranda{

    @DocumentId
    private String DocumentId;

    private String Nama, Nama_Usaha, Lokasi_Usaha;

    public String getNama_Usaha() {
        return Nama_Usaha;
    }

    public void setNama_Usaha(String nama_Usaha) {
        Nama_Usaha = nama_Usaha;
    }

    public String getLokasi_Usaha() {
        return Lokasi_Usaha;
    }

    public void setLokasi_Usaha(String lokasi_Usaha) {
        Lokasi_Usaha = lokasi_Usaha;
    }

    public String getDocumentId() {
        return DocumentId;
    }

    @DocumentId
    public void setDocumentId(String documentId) {
        DocumentId = documentId;
    }

    public ItemBeranda() {}

    public ItemBeranda(String documentId,String nama,String nama_Usaha,String lokasi_Usaha) {
        Nama = nama;
        DocumentId = documentId;
        Nama_Usaha = nama_Usaha;
        Lokasi_Usaha = lokasi_Usaha;
    }

    public String getNama() {
        return Nama;
    }

    public void setNama(String nama) {
        Nama = nama;
    }
}
