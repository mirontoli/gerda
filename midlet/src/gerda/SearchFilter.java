package gerda;

import javax.microedition.rms.RecordFilter;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;

public class SearchFilter implements RecordFilter {

    private String searchString = null;
    private ByteArrayInputStream bais;
    private DataInputStream dis;
    String str = null;

    public SearchFilter(String searchString) {
        //Skiljer inte på versaler och gemener
        this.searchString = searchString.toLowerCase();
    }

    public boolean matches(byte[] values) {
        try {
            initInputStream(values);
            str = dis.readUTF().toLowerCase();//läs buffer
            bais.close();
        } catch (Exception e) {
            return false;
        }
        if (str != null && str.indexOf(searchString) != -1) {
            return true;
        } else {
            return false;
        }
    }

    private void initInputStream(byte[] rec) {
        bais = new ByteArrayInputStream(rec);
        dis = new DataInputStream(bais);
    }
}