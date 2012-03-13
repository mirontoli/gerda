package gerda;

import javax.microedition.rms.RecordComparator;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;

public class CompareName implements RecordComparator {

    private ByteArrayInputStream bais;
    private DataInputStream dis;

    public int compare(byte[] rec1, byte[] rec2) {
        try {
            initInputStream(rec1);//Skapa stream
            String name1 = dis.readUTF();//läs buffer
            bais.close();//stäng
            initInputStream(rec2);//Skapa stream
            String name2 = dis.readUTF();
            bais.close();
            int result = name1.compareTo(name2);
            if (result == 0) {
                return RecordComparator.EQUIVALENT;
            } else if (result < 0) {
                return RecordComparator.PRECEDES;
            } else {
                return RecordComparator.FOLLOWS;
            }
        } catch (Exception e) {
            return RecordComparator.EQUIVALENT;
        }
    }

    private void initInputStream(byte[] rec) {
        bais = new ByteArrayInputStream(rec);
        dis = new DataInputStream(bais);
    }
}