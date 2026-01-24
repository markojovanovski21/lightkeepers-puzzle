import java.io.Serializable;
import java.util.Random;
public class Logic implements Serializable {
    private Cell[][] grid;
    private int size;

    private void gridGeneration(int ratio) {
        Random rand = new Random();
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                boolean wall = rand.nextInt(100) < ratio;
                grid[i][j] = new Cell(wall);
            }
        }
    }
    public Logic(int n, int wallRatio) {
        this.size = n;
        grid = new Cell[n][n];
        gridGeneration(wallRatio);
    }
    public int getSize() {
        return size;
    }
    public Cell getCell(int i, int j) {
        return grid[i][j];
    }
    public boolean isWall(int i, int j) {
        return grid[i][j].isWall();
    }
    public void placeLightbulb(int i, int j) {
        grid[i][j].placeLightbulb();
    }
    public void deleteLightbulb(int i, int j) {
        grid[i][j].deleteLightbulb();
    }
    public void toggleMark(int i, int j) {
        grid[i][j].mark();
    }
    public void updateLighting(){
        clearLighting();
        clearCollisions();
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if(grid[i][j].hasLightBulb()){ //vector spaces
                    light(i, j, 1, 0); //down
                    light(i,j,-1,0); //up
                    light(i,j,0,1); //right
                    light(i,j,0,-1); //left
                    grid[i][j].lit(true);
                }
            }
        }
        updateCollisions();
    }
    public void clearLighting(){
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                grid[i][j].lit(false);
            }
        }
    }
    private void light(int row, int col, int directionRow, int directionCol){
        row += directionRow;
        col += directionCol;

        while(row >= 0 && row < size && col >= 0 && col < size) {
            if(grid[row][col].isWall()){
                break;
            }
            grid[row][col].lit(true);
            row+=directionRow;
            col+=directionCol;
        }
    }
    public boolean everyCellLit(){
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if(grid[i][j].hasCollision()){
                    return false;
                }
            }
        }
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if(!grid[i][j].isWall() && !grid[i][j].isLit())
                    return false;
            }
        }return true;
    }
    public int countLightbulbs(){
        int count=0;
        for(int i = 0; i < size; i++){
            for(int j = 0; j < size; j++){
                if(grid[i][j].hasLightBulb())
                    count++;
            }
        }return count;
    }
    public void updateCollisions(){
        clearCollisions();
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if(grid[i][j].hasLightBulb()){
                    if(collisionCheck(i,j))
                        grid[i][j].collision(true);
                }
            }
        }
    }
    public boolean directionCheck(int row, int col, int directionRow, int directionCol){
        row += directionRow;
        col += directionCol;
        while(row >= 0 && row < size && col >= 0 && col < size){
            if(grid[row][col].isWall()){
                return false; //wall blocks the light
            }
            if(grid[row][col].hasLightBulb()){
                grid[row][col].collision(true);
                return true; //collision
            }
            row += directionRow;
            col += directionCol;
        }return false;
    }
    public boolean collisionCheck(int row, int col){
        if(directionCheck(row,col,-1,0)){return true;} //up
        if(directionCheck(row,col,1,0)){return true;} //down
        if(directionCheck(row,col,0,-1)){return true;} //left
        if(directionCheck(row,col,0,1)){return true;} //right
        return false;
    }
    public void clearCollisions(){
        for(int i = 0; i < size; i++){
            for(int j = 0; j < size; j++){
                grid[i][j].collision(false);
            }
        }
    }

}
