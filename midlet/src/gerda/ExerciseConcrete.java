package gerda;

import java.util.Date;

/**
 *
 * @author mirontoli
 */
public class ExerciseConcrete extends Exercise {
    private double weight;
    private Date date;

    public ExerciseConcrete(String name)  {
        super(name);
    }
    public ExerciseConcrete(String name, double weight, Date date) {
        super(name);
        this.weight = weight;
        this.date = date;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

}
