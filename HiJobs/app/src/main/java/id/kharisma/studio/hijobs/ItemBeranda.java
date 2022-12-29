package id.kharisma.studio.hijobs;

import com.google.firebase.firestore.DocumentId;
import com.google.firebase.firestore.Exclude;

public class ItemBeranda{

    @DocumentId
    private String DocumentId;

    private String Nama, Alamat, Kota;

    public String getNama() {
        return Nama;
    }

    public void setNama(String nama) {
        Nama = nama;
    }

    public String getAlamat() {
        return Alamat;
    }

    public void setAlamat(String alamat) {
        Alamat = alamat;
    }

    public String getKota() {
        return Kota;
    }

    public void setKota(String kota) {
        Kota = kota;
    }

    public String getDocumentId() {
        return DocumentId;
    }

    @DocumentId
    public void setDocumentId(String documentId) {
        DocumentId = documentId;
    }

    public ItemBeranda() {}

    public ItemBeranda(String documentId,String nama,String alamat,String kota) {
        Nama = nama;
        Alamat = alamat;
        Kota = kota;
        DocumentId = documentId;
    }
}
