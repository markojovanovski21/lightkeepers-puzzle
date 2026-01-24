import javax.swing.*;
import java.awt.*;
import java.io.*;
public class Frame extends JFrame {
    private Logic logic;
    private Grid grid;
    private int size, ratio;
    private Timer timer;
    private int seconds;
    private JLabel timerLabel;

    public Frame(int size, int ratio) {
        setTitle("Lightkeeper's Puzzle");
        setLayout(new BorderLayout());
        timerLabel = new JLabel("Time: 0");
        timerLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        JPanel topPanel = new JPanel();
        topPanel.add(timerLabel);
        add(topPanel,BorderLayout.NORTH);
        setJMenuBar(createMenuBar());
        settingsMenu();
        pack();
        setVisible(true);
        //setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    private void startTimer(){
        seconds = 0;
        timer = new Timer(1000, e->{
            seconds++;
            timerLabel.setText("Time: "+seconds);
        });
        timer.start();
    }
    private JMenuBar createMenuBar(){
        JMenuBar menuBar = new JMenuBar();
        JMenu gameMenu = new JMenu("Lightkeeper's Puzzle Menu");
        JMenuItem startGame = new JMenuItem("Start New Game");
        JMenuItem saveGame = new JMenuItem("Save Game");
        JMenuItem loadGame = new JMenuItem("Load Game");
        JMenuItem gameSettings = new JMenuItem("Settings");
        JMenuItem exitGame = new JMenuItem("Exit");
        startGame.addActionListener(e->startGame());
        saveGame.addActionListener(e->saveGame());
        loadGame.addActionListener(e->loadGame());
        gameSettings.addActionListener(e->settingsMenu());
        exitGame.addActionListener(e->exitGame());
        menuBar.add(gameMenu);
        gameMenu.add(startGame);
        gameMenu.add(saveGame);
        gameMenu.add(loadGame);
        gameMenu.add(gameSettings);
        gameMenu.add(exitGame);
        return menuBar;
    }
    private void settingsMenu(){
        JTextField fieldSize = new JTextField(size == 0 ? "8":String.valueOf(size));
        JTextField fieldRatio = new JTextField(ratio == 0 ? "40" : String.valueOf(ratio));
        JPanel settingsMenu = new JPanel(new GridLayout(2,2,5,5));
        settingsMenu.add(new JLabel("Grid size:"));
        settingsMenu.add(fieldSize);
        settingsMenu.add(new JLabel("Grid ratio:"));
        settingsMenu.add(fieldRatio);
        int result = JOptionPane.showConfirmDialog(this,settingsMenu,"Lightkeeper's Puzzle Settings", JOptionPane.OK_CANCEL_OPTION);
        if(result == JOptionPane.OK_OPTION){
            applySettings(fieldSize.getText(),fieldRatio.getText());
        }
    }
    private void applySettings(String sizeText, String ratioText){
        int newSize = Integer.parseInt(sizeText);
        int newRatio = Integer.parseInt(ratioText);
        if(newSize<3 || newRatio<0 || newRatio>100){ //if size is lower than 3 grid is impossible to play on, if wall ratio is lower than 0 or higher than 100% also impossible to play on
            JOptionPane.showMessageDialog(this,"Invalid size/ratio!"
                    ,"Error",JOptionPane.ERROR_MESSAGE);
        }else{
        size = newSize;
        ratio = newRatio;
        }
        startGame();
    }
    private void startGame(){
        if(timer != null)
            timer.stop();
        if(grid != null)
            remove(grid);
        logic = new Logic(size,ratio);
        grid = new Grid(logic, this);
        add(grid,BorderLayout.CENTER);
        revalidate();
        repaint();
        startTimer();
    }
    private void exitGame(){
        System.exit(0);
    }
    public void whenWin(){
        timer.stop();
        int lightbulbsUsed = logic.countLightbulbs();
        JOptionPane.showMessageDialog(this,"You Won!\n" + "Lightbulbs used: " + lightbulbsUsed +
                    "\nTime elapsed: " + seconds + " seconds" + "\nPress OK to start new game."
                ,"Win", JOptionPane.INFORMATION_MESSAGE);
        startGame();
        }
    public void saveGame(){
        JFileChooser chooser = new JFileChooser();
        if(chooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION){
            try {
                ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(chooser.getSelectedFile()));
                Save save = new Save(logic,seconds);
                oos.writeObject(save);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this,"Failed to save","Error",JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    public void loadGame(){
        JFileChooser chooser = new JFileChooser();
        if(chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION){
            try {
                ObjectInputStream ois = new ObjectInputStream(new FileInputStream(chooser.getSelectedFile()));
                Save save = (Save) ois.readObject();
                if(timer!=null)
                    timer.stop();
                if(grid!=null)
                    remove(grid);
                logic = save.logic;
                seconds = save.seconds;
                timerLabel.setText("Time: "+seconds);
                grid = new Grid(logic, this);
                add(grid,BorderLayout.CENTER);
                logic.updateLighting();
                grid.refresh();
                revalidate();
                repaint();
                startTimer();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this,"Failed to load","Error",JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
