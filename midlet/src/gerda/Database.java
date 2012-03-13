package gerda;

import javax.microedition.rms.*;


/**
 *
 * @author Dogan & Anatoly
 * 20090705
 */
public class Database {

    RecordStore db = null;
    String dbName;
    CompareName comp = null;
    SearchFilter filter = null;

    public Database(String fileName, CompareName comp, SearchFilter filter) {
        this.dbName = fileName;
       this.comp = comp;
        this.filter = filter;

    }

    public void openDB() {
        try {
            db = RecordStore.openRecordStore(dbName, true);
        } catch (RecordStoreException e) {
            e.printStackTrace();
        }
    }

    public void closeDB() throws RecordStoreNotOpenException, RecordStoreException {
        if (db.getNumRecords() == 0) {
            dbName = db.getName();
            db.closeRecordStore();
            RecordStore.deleteRecordStore(dbName);
        } else {
            db.closeRecordStore(); // stäng rs
        }
    }

    public synchronized int insertRecord(byte[] bytes) {
        //byte[] bytes = record.getBytes(); // Få en generell insertRecord (görs i subklass)
        try {
            return db.addRecord(bytes, 0, bytes.length);
        } catch (RecordStoreException e) {
            e.printStackTrace();
        }
        return -1; // fel I insätningen
    }

    public synchronized void updateRecord(int id, byte[] bytes) {
        // Uppdaterar en record i record store
        try {
            db.setRecord(id, bytes, 0, bytes.length);
        } catch (RecordStoreException e) {
            e.printStackTrace();
        }
    }

    public synchronized void deleteRecord(int id) {
        // tabort en contact record från record store
        try {
            db.deleteRecord(id);
        } catch (RecordStoreException e) {
            e.printStackTrace();
        }
    }

    public synchronized byte[] getRecord(int id) {
        // Hämta en record från record store
        try {
            return db.getRecord(id);
        } catch (RecordStoreException e) {
            e.printStackTrace();
        }
        return null;
    }

    public synchronized void getRecord(int id, byte[] bytes) {
        // Hämta en record från record store
        try {
            db.getRecord(id, bytes, 0);
        } catch (RecordStoreException e) {
            e.printStackTrace();
        }
    }

    public void cleanUp() throws RecordStoreNotFoundException, RecordStoreException {
        this.closeDB();
        RecordStore.deleteRecordStore(dbName);
        this.openDB();
    }

    public void removeDB() {
        try {
            this.closeDB();
            RecordStore.deleteRecordStore(dbName);
        } catch (RecordStoreNotFoundException notFound) {
            System.err.println("RecordStoreNotFoundException...");
        } catch (RecordStoreException e) {
            System.err.println("RecordStoreException ..." + e);
        }
    }
    // Returnerar en RecordEnumeration

    public synchronized RecordEnumeration enumerate() throws RecordStoreNotOpenException {
        return db.enumerateRecords(filter, comp, false);
    }
}
