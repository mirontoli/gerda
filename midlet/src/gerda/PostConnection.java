package gerda;

import java.io.*;
import javax.microedition.io.*;

/**
 *
 * @author mirontoli
 */
public class PostConnection implements Runnable {

    private String url;
    private GerdaMIDlet gerda;

    public PostConnection(String url, GerdaMIDlet gerda) {
        this.url = url;
        this.gerda = gerda;
    }

    public void send() {
        Thread t = new Thread(this);
        t.start();
    }

    public void run() {
        connect();
    }

    private void connect() {
        HttpConnection hc = null;
        InputStream is = null;
        OutputStream os = null;

        String requestString = "name=" + gerda.getWebRequest();
        String str = "";



        try {
            hc = (HttpConnection) Connector.open(url);
            hc.setRequestMethod(HttpConnection.POST);

            hc.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            hc.setRequestProperty("Connection", "close");


            //Det här är väldigt viktigt!!!! Utan längden vill inte servleten processa det.
            hc.setRequestProperty("Content-length", "" + requestString.getBytes().length);



            //skicka request
            os = hc.openOutputStream();
            os.write(requestString.getBytes());
            System.out.println("BU BU om du ser dettta så ska vi kunna skicka data över nätet");
            os.flush();




            is = hc.openInputStream();
            if (hc.getResponseCode() == HttpConnection.HTTP_OK) {
                int contentLength = (int) hc.getLength();
                if (contentLength != -1) {
                    byte[] buffer = new byte[contentLength];
                    is.read(buffer);
                    str = new String(buffer);
                } else { //om längd inte är tillgänglig
                    ByteArrayOutputStream bs = new ByteArrayOutputStream();
                    int ch;
                    while ((ch = is.read()) != -1) {
                        bs.write(ch);
                    }
                    str = new String(bs.toByteArray());
                    bs.close();
                }
            } else {
                str = "error";
            }
            is.close();
            os.close();
            hc.close();

        } catch (IOException ioe) {
        }
        //messageItem.setText(str);


    }
}
