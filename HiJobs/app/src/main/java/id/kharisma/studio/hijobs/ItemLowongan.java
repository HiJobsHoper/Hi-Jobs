package id.kharisma.studio.hijobs;

import com.google.firebase.firestore.DocumentId;

public class ItemLowongan{

    private String Nama;

    public String getDocumentId() {
        return DocumentId;
    }

    @DocumentId
    public void setDocumentId(String documentId) {
        DocumentId = documentId;
    }

    @DocumentId
    private String DocumentId;

    public ItemLowongan() {}

    public ItemLowongan(String documentId, String nama) {
        Nama = nama;
        DocumentId = documentId;
    }

    public String getNama() {
        return Nama;
    }

    public void setNama(String nama) {
        Nama = nama;
    }
}
