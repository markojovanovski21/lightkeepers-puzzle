import java.io.Serializable;
public class Save implements Serializable {
    private static final long serialVersionUID = 1L;
    public Logic logic;
    public int seconds;
    public Save(Logic logic, int seconds){
        this.logic = logic;
        this.seconds = seconds;
    }
}
