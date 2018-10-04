/**
 * Created by Andr on 28-Nov-17.
 */
public class Block {

    //no getters and setters is a deliberate design choice
    public char name;
    public int x, y;

    public Block(char name, int x, int y) {
        this.name = name;
        this.x = x;
        this.y = y;
    }

    public int hashCode(){
        Character c = name;
        int sum = c.hashCode();
        sum = sum*17+ x;
        sum = sum*17+ y;
        return sum;
    }
}
