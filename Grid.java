import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Grid extends JPanel {
    private Logic logic;
    private Button[][] buttons;
    private Frame frame;
    private boolean gameOver = false;
    public Grid(Logic logic, Frame frame) {
        this.logic = logic;
        this.frame = frame;
        int size = logic.getSize();
        setLayout(new GridLayout(size,size));
        buttons = new Button[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                Button button = new Button(i,j);
                button.setPreferredSize(new Dimension(60,60));
                button.addActionListener(e -> buttonClick(button));
                button.addMouseListener(new MouseAdapter() {
                    public void mousePressed(MouseEvent e){
                        if(SwingUtilities.isRightMouseButton(e)){
                            buttonRightClick(button);
                        }
                    }
                });
                buttons[i][j] = button;
                add(button);
            }
        }
        refresh();
    }
    public void refresh(){
        int size = logic.getSize();
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                Cell cell = logic.getCell(i, j);
                Button button = buttons[i][j];
                if(cell.isWall()){
                    button.setBackground(Color.BLACK);
                    button.setText(" ");
                } else if (cell.hasLightBulb()) {
                    if(cell.hasCollision()){
                        button.setBackground(Color.RED);
                    } else {
                        button.setBackground(Color.YELLOW);
                    }
                    button.setFont(new Font("Noto Sans Symbols 2", Font.PLAIN, 20));
                    button.setText("𖡊");
                } else if (cell.isLit()) {
                    button.setBackground(Color.YELLOW);
                    button.setText(" ");
                } else if (cell.isMarked()) {
                    button.setBackground(Color.WHITE);
                    button.setText("X");
                } else  {
                    button.setBackground(Color.WHITE);
                    button.setText(" ");
                }
            }
        }
    }
    public void buttonClick(Button button) {
        int row = button.getRow();
        int col = button.getCol();
        if(logic.isWall(row,col)){return;}
        else if (logic.getCell(row,col).hasLightBulb()){
            logic.deleteLightbulb(row,col);
        } else if (gameOver){
            return;
        } else
            logic.placeLightbulb(row,col);

        logic.updateLighting();
        refresh();
        checkForWin();
    }
    public void buttonRightClick(Button button){
        int row = button.getRow();
        int col = button.getCol();
        logic.toggleMark(row,col);
        refresh();
    }
    public void checkForWin(){
        if(!gameOver && logic.everyCellLit()){
            gameOver = true;
            this.frame.whenWin();
        }
    }
}
