import javax.swing.*;
import java.awt.*;
public class Button extends JButton {
    private int row;
    private int col;
    public Button(int row, int col){
        this.row = row;
        this.col = col;
        setFocusPainted(false);
        setFont(new Font("Arial", Font.BOLD, 20));
    }
    public int getRow() {return row;}
    public int getCol() {return col;}
}
