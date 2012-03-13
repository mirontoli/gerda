package gerda;

import java.io.IOException;
import java.util.*;
import javax.microedition.lcdui.*;
import javax.microedition.midlet.*;
import javax.microedition.rms.*;

/**
 * @author Dogan & Anatoly
 * 20090629
 */
public class GerdaMIDlet extends MIDlet implements CommandListener {

    private Display display;
    private boolean midletPaused,
            todayListSet;
    private List exerciseList,
            todayList;
    private Form exConcreteForm,
            exerciseForm,
            addExerciseForm,
            askForm;
    private TextField tfExercise,
            tfSets,
            tfAmount,
            tfWeight;
    private DateField tfDate;
    private Alert alertMSG;
    private Command exitCommand,
            backCommand,
            saveCommand,
            addCommand,
            deleteCommand,
            changeCommand,
            deleteAllExConcreteCommand,
            showConcreteExCommand,
            chooseTodayExCommand,
            yesCommand,
            noCommand;
    private DBExercise db = null;
    private DBExerciseConcrete dbConcrete = null;
    private Vector exerciseIDVector = new Vector();
    private PostConnection con;
    private String webRequest; //för att skicka data sedan till servlet

    public GerdaMIDlet() {
        midletPaused = false;
        todayListSet = false; //för att resuma midleten
        con = new PostConnection(getAppProperty("url"), this);
    }

    public void startApp() {
        if (midletPaused == true) {
            resumeMIDlet();
        } else {
            initMIDlet();
        }
    }

    public void pauseApp() {
        midletPaused = true;
    }

    public void destroyApp(boolean unconditional) {
    }

    public void resumeMIDlet() {
        midletPaused = false;
        if (!todayListSet) {
            switchDisplayable(null, exerciseList);
        } else {
            switchDisplayable(null, todayList);
        }
    }

    public void initMIDlet() {
        initDB();
        initDBConcrete();
        initCommands();
        initAlerts();

        initExerciseList();

        initAddExerciseForm();
        initTodayList();

        initExerciseForm();
        initAskForm();

        fillExerciseIDVector();
        initExConcreteForm();
        initDisplay();

    }

    public void initDB() {
        try {
            db = new DBExercise("Exercises", new CompareName(), new SearchFilter(""));
            db.openDB();
        } catch (Exception e) {
            System.out.println("Problem med db -initialiseringen");
        }
    }

    public void initDBConcrete() {
        try {
            dbConcrete = new DBExerciseConcrete("ConcreteExercises", new CompareName(), new SearchFilter(""));
            dbConcrete.openDB();

            System.out.println("Grattis, nu har vi lyckats skapa 2 databaser!!!");
            System.out.println("om du inte ser det, då inte grattis!");

        } catch (Exception e) {
            System.out.println("Problem med db -initialiseringen");
        }
    }

    public void initCommands() {
        exitCommand = new Command("Exit", Command.EXIT, 1);
        backCommand = new Command("Back", Command.BACK, 1);
        saveCommand = new Command("Save", Command.OK, 1);
        addCommand = new Command("add exercise", Command.OK, 1);
        deleteCommand = new Command("delete exercise", Command.OK, 1);
        changeCommand = new Command("change exercise", Command.OK, 1);
        deleteAllExConcreteCommand = new Command("tömma ex concrete", Command.OK, 1);
        showConcreteExCommand = new Command("kolla konkreta", Command.OK, 1);
        chooseTodayExCommand = new Command("Choose TodayEx", Command.OK, 0);
        yesCommand = new Command("Yes", Command.SCREEN, 1);
        noCommand = new Command("No", Command.BACK, 1);
    }

    public void initAlerts() {
        alertMSG = new Alert("Meddelande");
        //alertConfirm = new Alert("Confirm", null, null, AlertType.CONFIRMATION);
    }

    public void initExerciseList() {
        exerciseList = new List("Choose Exercises for Today", List.MULTIPLE);
        exerciseList.addCommand(exitCommand);
        exerciseList.addCommand(addCommand);
        exerciseList.addCommand(deleteCommand);
        exerciseList.addCommand(changeCommand);
        exerciseList.addCommand(deleteAllExConcreteCommand);
        exerciseList.addCommand(chooseTodayExCommand);
        exerciseList.setCommandListener(this);
    }

    public void initTodayList() {
        todayList = new List("Todays Exercises:", List.IMPLICIT);
        todayList.addCommand(backCommand);
        todayList.addCommand(showConcreteExCommand);
        todayList.setCommandListener(this);

    }

    public void initAddExerciseForm() {
        addExerciseForm = new Form("Add an exercise");
        tfExercise = new TextField("Exercise: ", "", 10, TextField.ANY);
        tfSets = new TextField("Sets:", "", 1, TextField.NUMERIC);
        tfAmount = new TextField("Amount:", "", 2, TextField.NUMERIC);

        addExerciseForm.append(tfExercise);
        addExerciseForm.append(tfSets);
        addExerciseForm.append(tfAmount);
        addExerciseForm.append("Observera: om du bara ändrar, klicka på \"save change\", inte bara \"save\"");
        addExerciseForm.addCommand(saveCommand);
        addExerciseForm.addCommand(backCommand);
        addExerciseForm.addCommand(changeCommand);
        addExerciseForm.setCommandListener(this);
    }

    public void initAskForm() {
        askForm = new Form("Really?");
        askForm.addCommand(yesCommand);
        askForm.addCommand(noCommand);
        askForm.setCommandListener(this);
    }

    public void initDisplay() {
        display = this.getDisplay();
        //display.setCurrent(exerciseList);
        switchDisplayable(null, new MySplashScreen(display, exerciseList));
    }

    public void fillExerciseIDVector() {
        RecordEnumeration records = null;
        try {
            records = db.enumerate();
            while (records.hasNextElement()) {
                int id = records.nextRecordId();
                exerciseIDVector.addElement(new Integer(id));
                Exercise ex = db.getExercise(id);
                System.out.println(ex.getName());
                exerciseList.append(ex.getName() + " \t\tSets: " + ex.getSets() + " \t\tAmount: " + ex.getAmount(), null);
            }
        } catch (Exception e) {
            System.out.println("Kan inte läsa db");
        }
    }

    public void initExerciseForm() {
        exerciseForm = new Form("Du tränar:");
        tfWeight = new TextField("Weight: ", "", 5, TextField.NUMERIC);

        Date today = new Date(System.currentTimeMillis());
        tfDate = new DateField("", DateField.DATE_TIME, TimeZone.getTimeZone("UTC+02:00"));
        tfDate.setDate(today);
        exerciseForm.append(tfWeight);
        exerciseForm.append(tfDate);
        exerciseForm.append(new StringItem("Set: ", "3"));
        exerciseForm.append(new StringItem("Amount: ", "8"));
        exerciseForm.addCommand(backCommand);
        exerciseForm.addCommand(saveCommand);
        exerciseForm.setCommandListener(this);
    }

    public void initExConcreteForm() {
        exConcreteForm = new Form("concrete");

        getExConcrete();
        exConcreteForm.addCommand(backCommand);
        exConcreteForm.setCommandListener(this);
    }

    public void getExConcrete() {
        exConcreteForm.deleteAll();

        RecordEnumeration records = null;
        try {
            records = dbConcrete.enumerate();


            while (records.hasNextElement()) {
                int id = records.nextRecordId();
                ExerciseConcrete ec = dbConcrete.getExerciseConcrete(id);
                exConcreteForm.append(ec.getName() + " " + ec.getWeight());
                DateField df = new DateField("", DateField.DATE);
                df.setDate(ec.getDate());
                df.setLayout(DateField.LAYOUT_2);


                exConcreteForm.append(df);
                exConcreteForm.append("\n");
            }
        } catch (Exception e) {
            System.out.println("Kan inte läsa dbConcrete");
        }
    }

    public void getExConcreteForToday() {
        exConcreteForm.deleteAll();
        exConcreteForm.setTitle("Du har tränat idag");
        long now = System.currentTimeMillis();
        String dateSWE = getDateSWE(new Date(now));
        //DateField df = new DateField("", DateField.DATE);
        //df.setDate(new Date(now));
        StringItem dateLabel = new StringItem(null, dateSWE + ":\n");
        exConcreteForm.append(dateLabel);


        RecordEnumeration records = null;
        try {
            records = dbConcrete.enumerate();


            while (records.hasNextElement()) {


                int nowNumber = Integer.parseInt((new Date(now).toString().substring(9, 10)));
                long dayBefore = now - (24 * 60 * 60 * 1000); // 24 hours
                int id = records.nextRecordId();
                ExerciseConcrete ec = dbConcrete.getExerciseConcrete(id);
                Date date = ec.getDate();
                long then = date.getTime();
                String day = date.toString().substring(9, 10);
                int dayNumber = Integer.parseInt(day);
                if (then > dayBefore) {

                    if (dayNumber == nowNumber) {
                        exConcreteForm.append(ec.getName() + " " + ec.getWeight() + " kg");
                        exConcreteForm.append("\n");
                    }
                }


            }
        } catch (Exception e) {
            System.out.println("Kan inte läsa dbConcrete");
        }
    }

    public void exitMIDlet() {
        destroyApp(true);
        notifyDestroyed();
    }

    public Display getDisplay() {
        return Display.getDisplay(this);
    }

    public void switchDisplayable(Alert alert, Displayable nextDisplayable) {
        display = getDisplay();
        if (alert == null) {
            display.setCurrent(nextDisplayable);
        } else {
            display.setCurrent(alert, nextDisplayable);
        }
    }

    public void informWithAlert(String message, Displayable nextDisplayable) {

        alertMSG.setString(message);
        alertMSG.setTimeout(Alert.FOREVER);
        switchDisplayable(alertMSG, nextDisplayable);

    }

    public void commandAction(Command c, Displayable d) {
        if (c == exitCommand) {
            exitMIDlet();
        } else if (c == List.SELECT_COMMAND) {
            switchDisplayable(null, exerciseForm);
            //System.out.println(exerciseList.getString(exerciseList.getSelectedIndex()));
        } else if (c == backCommand) {
            if (d == todayList || d == addExerciseForm) {
                switchDisplayable(null, exerciseList);
            } else {
                switchDisplayable(null, todayList);
            }
        } else if (c == saveCommand) {
            if (d == exerciseForm) {
                handleSaveExerciseConcrete();
            } else {
                handleSaveExercise();
            }

        } else if (c == addCommand) {
            handleAddExercise();
        } else if (c == deleteCommand) {
            handleDeleteExercise();
        } else if (c == changeCommand) {
            if (d == exerciseList) {
                handleChangeExercise();
            } else if (d == addExerciseForm) {
                handleChangeExerciseForm();
            }
        } else if (c == showConcreteExCommand) {
            getExConcreteForToday();
            switchDisplayable(null, exConcreteForm);
        } else if (c == chooseTodayExCommand) {
            handleChooseTodaysEx();
        } else if (c == deleteAllExConcreteCommand) {
            handleDeleteAllConcreteEx();
        } else if (c == yesCommand) {
            handleDeleteExerciseReally();
        } else if (c == noCommand) {
            switchDisplayable(null, exerciseList);
        }



    }

    public void handleAddExercise() {
        clearAddExerciseForm();
        switchDisplayable(null, addExerciseForm);
    }

    public void clearAddExerciseForm() {
        tfExercise.setString("");
        tfSets.setString("");
        tfAmount.setString("");

    }

    public void handleSaveExercise() {
        String name = tfExercise.getString();
        int sets = Integer.parseInt(tfSets.getString());
        int amount = Integer.parseInt(tfAmount.getString());
        Exercise ex = new Exercise(name, sets, amount);
        tfExercise.setString("");
        Integer id = new Integer(db.insertRecord(ex));
        exerciseIDVector.addElement(id);
        exerciseList.append(ex.getName() + " \tSets: " + ex.getSets() + " \tAmount: " + ex.getAmount(), null);
        switchDisplayable(null, exerciseList);
    }

    public void handleSaveExerciseConcrete() {
        if (tfWeight.getString().equals("")) {
            Alert alert = new Alert("Du glömde att skriva kilo");
            alert.setString("Gå tillbaka och skriv kilo!");
            alert.setTimeout(Alert.FOREVER);
            switchDisplayable(alert, exerciseForm);
        } else {
            double weight = Double.parseDouble(tfWeight.getString());
            int index = todayList.getSelectedIndex();

            //int id = ((Integer) exerciseIDVector.elementAt(index)).intValue();

            //Exercise exercise = db.getExercise(id);

            //String name = exercise.getName();

            String nameWhole = todayList.getString(index);
            int i = nameWhole.indexOf(" ");
            String name = nameWhole.substring(0, i);
            Date date = tfDate.getDate();
            ExerciseConcrete exConcrete = new ExerciseConcrete(name, weight, date);
            Integer idConcrete = new Integer(dbConcrete.insertRecord(exConcrete));

            sendToWeb(exConcrete);
            try {
                todayList.set(index, todayList.getString(index), Image.createImage("/gerda/action_check.gif"));
            } catch (IOException ex) {
                System.out.println("ingen bild");
            }

            informWithAlert("Övning: " + name + "\nDu har lyft " + tfWeight.getString() + " kg! " + getDateSWE(date), todayList);

        }

    }

    public void sendToWeb(ExerciseConcrete ec) {
        String name = ec.getName();
        String weight = String.valueOf(ec.getWeight());
        //String date = String.valueOf(ec.getDate().getTime());
        String date = getDateSWE(ec.getDate());

        webRequest = name + " : " + weight + " kg : " + date;
        con.send();
    }

    public String getWebRequest() {
        return webRequest;
    }

    public String getDateSWE(Date date) {
        String dateSWE = "";
        String dateStr = date.toString();
        String day = dateStr.substring(8, 10);
        String month = "";
        String year = dateStr.substring(24, 28);

        String monthStr = dateStr.substring(4, 7);
        int monthNr = 0;
        int i = 0;
        boolean found = false;
        String[] months = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
        while (i < 12 && !found) {
            if (months[i].equals(monthStr)) {
                monthNr = i + 1;
                found = true;
            }
            i++;
        }
        if (monthNr > 0 && monthNr < 10) {
            month = "0" + monthNr;
        } else if (monthNr > 9 && monthNr < 13) {
            month = "" + monthNr;
        } else {
            month = "xx";
        }
        dateSWE = year + "-" + month + "-" + day;
        return dateSWE;
    }

    public void handleDeleteExercise() {
        if (selectedOnlyOnceInExerciseList()) {
            int index = getSelectedIndexInExerciseList();
            int id = ((Integer) exerciseIDVector.elementAt(index)).intValue();

            Exercise ex = db.getExercise(id);
            askForm.deleteAll();
            askForm.append("Are you sure you want to delete " + ex.getName() + "?");
            switchDisplayable(null, askForm);
        } else {
            informWithAlert("Bocka i en övning! Men bara en åt gången!", exerciseList);
        }

    }

    public boolean selectedOnlyOnceInExerciseList() {
        boolean once = false;
        boolean[] selected = new boolean[exerciseList.size()];
        exerciseList.getSelectedFlags(selected);
        int times = 0;
        for (int i = 0; i < selected.length; i++) {
            if (selected[i]) {
                times++;
            }
        }
        if (times == 1) {
            once = true;
        }
        return once;
    }

    public int getSelectedIndexInExerciseList() {
        boolean[] selected = new boolean[exerciseList.size()];
        exerciseList.getSelectedFlags(selected);
        boolean found = false;
        int index = 0;
        int i = 0;
        while (i < selected.length && !found) {
            if (selected[i]) {
                index = i;
                found = true;

            }
            i++;
        }
        return index;
    }

    public void handleDeleteExerciseReally() {
        boolean[] selected = new boolean[exerciseList.size()];
        exerciseList.getSelectedFlags(selected);
        int times = 0;
        int index = 0;
        for (int i = 0; i < selected.length; i++) {
            if (selected[i]) {
                times++;
                index = i;
            }
        }
        int id = ((Integer) exerciseIDVector.elementAt(index)).intValue();
        // Ta bort en kontakt ur db
        db.deleteRecord(id);
        exerciseIDVector.removeElementAt(index);//Ta bort ur listan
        exerciseList.delete(index); // ta bort från listScreen
        switchDisplayable(null, exerciseList);
    }

    public void handleChangeExercise() {
        if (selectedOnlyOnceInExerciseList()) {
            int index = getSelectedIndexInExerciseList();
            int id = ((Integer) exerciseIDVector.elementAt(index)).intValue();

            Exercise exercise = db.getExercise(id);

            tfExercise.setString(exercise.getName());
            tfSets.setString("" + exercise.getSets());
            tfAmount.setString("" + exercise.getAmount());
            switchDisplayable(null, addExerciseForm);
        } else {
            informWithAlert("Bocka i en övning! Men bara en åt gången!", exerciseList);
        }
    }

    public void handleChangeExerciseForm() {
        int index = getSelectedIndexInExerciseList();

        int id = ((Integer) exerciseIDVector.elementAt(index)).intValue();
        String name = tfExercise.getString();
        int sets = Integer.parseInt(tfSets.getString());
        int amount = Integer.parseInt(tfAmount.getString());
        Exercise ex = new Exercise(name, sets, amount);
        db.updateRecord(id, ex);//spara
        exerciseList.set(index, ex.getName() + " \t\tSets: " + ex.getSets() + " \t\tAmount: " + ex.getAmount(), null); //Uppdatera fönstret
        switchDisplayable(null, exerciseList);
    }

    public void handleChooseTodaysEx() {
        todayList.deleteAll();
        boolean[] selected = new boolean[exerciseList.size()];
        exerciseList.getSelectedFlags(selected);

        for (int i = 0; i < selected.length; i++) {
            if (selected[i]) {
                todayList.append(exerciseList.getString(i), null);
            }
        }

        switchDisplayable(null, todayList);
        todayListSet = true;
    }

    public void handleDeleteAllConcreteEx() {
        try {
            dbConcrete.cleanUp();
            informWithAlert("Nu har tagit bort alla exConcrete", exerciseList);
        } catch (RecordStoreNotFoundException ex) {
            ex.printStackTrace();
        } catch (RecordStoreException ex) {
            ex.printStackTrace();
        }

    }
}
