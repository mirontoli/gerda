package gerda;

/**
 *
 * @author Dogan Alkan & Anatoly Mironov
 * 20090629
 */
public class Exercise {



    private String name;

    private int sets;
    private int amount; // how many times one repeats per one set
    private String BODYBUILDERNAME;

    public Exercise(String name) {
        this.setName(name);
        setSets(3);
        setAmount(8);
    }

    public Exercise(String name, int sets, int amount) {
        this.name = name;
        this.sets = sets;
        this.amount = amount;

    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSets() {
        return sets;
    }

    public void setSets(int sets) {
        this.sets = sets;
    }

    public String getBODYBUILDERNAME() {
        return BODYBUILDERNAME;
    }

    public void setBODYBUILDERNAME(String BODYBUILDERNAME) {
        this.BODYBUILDERNAME = BODYBUILDERNAME;
    }



    
}
