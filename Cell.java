import java.awt.event.ActionListener;
import java.io.Serializable;
public class Cell implements Serializable {
    private boolean wall;
    private boolean lightbulb;
    private boolean lit;
    private boolean marked;
    private boolean collision;

    public Cell(boolean wall){
        this.wall = wall;
        this.lightbulb = false;
        this.lit = false;
        this.marked = false;

    }
    public boolean isWall() {return wall;}
    public boolean hasLightBulb() {return lightbulb;}
    public boolean isLit() {return lit;}
    public boolean isMarked() {return marked;}
    public boolean hasCollision() {return collision;}

    public void placeLightbulb(){
        if(!wall){
            lightbulb = true;
            marked = false;
        }
    }
    public void deleteLightbulb(){
        if(!wall){
            lightbulb = false;
        }
    }
    public void lit(boolean value){
        lit = value;
    }
    public void mark(){
        if(!wall && !lightbulb){
         marked = !marked;
        }
    }
    public void collision(boolean collision){
        this.collision = collision;
    }

}
