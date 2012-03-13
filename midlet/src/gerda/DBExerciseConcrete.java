package gerda;

import java.io.*;
import java.util.Date;

/**
 *
 * @author mirontoli
 */
public class DBExerciseConcrete extends Database {

    private ByteArrayInputStream bais;
    private DataInputStream dis;
    private ByteArrayOutputStream baos;
    private DataOutputStream dos;
    private byte[] rec = new byte[50];

    public DBExerciseConcrete(String fileName, CompareName comp, SearchFilter filter) {
        super(fileName, comp, filter);
        this.initOutStream();
        this.initInputStream();
    }

    private void initOutStream() {
        baos = new ByteArrayOutputStream();
        dos = new DataOutputStream(baos);
    }

    private void initInputStream() {
        bais = new ByteArrayInputStream(rec);
        dis = new DataInputStream(bais);
    }

    public synchronized int insertRecord(ExerciseConcrete record) {
        byte[] bytes = null;
        try {
            baos.reset();
            dos.writeUTF(record.getName());
            dos.writeDouble(record.getWeight());
            dos.writeLong(record.getDate().getTime());


            dos.flush(); //buffer till stream
            bytes = baos.toByteArray(); //stream till bytes
            System.out.println("Sparades contact i databasen");
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        return super.insertRecord(bytes);
    }

    public synchronized void updateRecord(int id, ExerciseConcrete record) {
        try {
            baos.reset();
            dos.writeUTF(record.getName());
            dos.writeDouble(record.getWeight());
            dos.writeLong(record.getDate().getTime());
            dos.flush(); //buffer till stream
            byte[] bytes = baos.toByteArray();//stream to bytes
            super.updateRecord(id, bytes); // Uppdaterar en post i recordstore
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }

    public ExerciseConcrete getExerciseConcrete(int id) {
        // Hämta en contact record från record store
        try {
            bais.reset();
            this.getRecord(id, rec);//db till filter till stream
            String name = dis.readUTF(); //stream till data
            double weight = dis.readDouble();
            long time = dis.readLong();
            
            Date date = new Date(time);
            ExerciseConcrete ex = new ExerciseConcrete(name, weight, date);
            return ex;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public int getNumRecords() {
        return this.getNumRecords();
    }

    public void closeDB() {
        try {
            //stäng streams
            bais.close();
            baos.close();
            dos.close();
            super.closeDB();
        } catch (Exception e) {
            System.err.println("EXCEPTION:Kan inte stänga kontaktdb");
        }
    }
}
