package test;

import java.util.Calendar;
import java.util.Date;
import javax.microedition.lcdui.DateField;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Form;
import javax.microedition.midlet.*;

/**
 * @author mirontoli
 */
public class DateMIDlet extends MIDlet {
    private Calendar cal;
    private Form form;
    private Date today;
    private DateField df;

    public DateMIDlet() {
        cal = Calendar.getInstance();
        form = new Form("your time");
        today = new Date(System.currentTimeMillis());
        df = new DateField("Datum", DateField.DATE);
        df.setDate(today);


        String str = "" + Calendar.YEAR;
        String str2 = cal.getTime().toString();
        String str3 = cal.getTimeZone().toString();
        int i = 24 * 60 * 60;

        String todayStr = today.toString();

        String day = todayStr.substring(8,10);

        String month = todayStr.substring(4, 7);
        String[] months = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
        String monthNumber = "";
        int monthNumberInt = 0;
        boolean found = false;
        int index = 0;
        while (index < 12 && !found) {
            if (months[index].equals(month)) {
                monthNumberInt = index+1;
                found = true;
            }
            index++;
        }
        if (monthNumberInt < 10) {
            monthNumber = "0" + monthNumberInt;
        }
        else if (monthNumberInt < 1) {
            monthNumber = "xx";
        }
        else {
            monthNumber = "" + monthNumberInt;
        }

        String year = todayStr.substring(24, 28);
        String dateSWE = year + "-" + monthNumber + "-" + day;
        form.append(dateSWE);
        Display.getDisplay(this).setCurrent(form);
    }

    public void startApp() {
    }

    public void pauseApp() {
    }

    public void destroyApp(boolean unconditional) {
    }
}
