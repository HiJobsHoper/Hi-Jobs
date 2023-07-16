package id.kharisma.studio.hijobs;

import com.google.firebase.firestore.DocumentId;

public class ItemRiwayat {

    private String DocumentId;

    private String Lowongan, Alamat, Kota;

    public String getLowongan() {
        return Lowongan;
    }

    public void setLowongan(String lowongan) {
        Lowongan = lowongan;
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

    public ItemRiwayat() {}

    public ItemRiwayat(String documentId,String lowongan,String alamat,String kota) {
        Lowongan = lowongan;
        Alamat = alamat;
        Kota = kota;
        DocumentId = documentId;
    }
}
