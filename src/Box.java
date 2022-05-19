import javafx.scene.shape.Rectangle;

// Its an abstract root class of all boxes
public abstract class Box extends Rectangle {
    //Every box has an x and y coordinate to write the coordinates on the clickInfoLabel whenever user clicks a box
    private int x, y;
    //Every box has also durability which default is 0 to check is the level end or not
    private int durability = 0;

    // It is the constructor of Box. (super(int width, int length) : Rectangle), even we cannot execute its constructor because it is abstract class, we need it for constructor chaining
    public Box (int x, int y) {
        super(33, 33);
        this.x = x;
        this.y = y;
    }

    // Setter and getter methods
    public void setX(int x) {
        this.x = x;
    }

    public int getCurrentX() {
        return this.x;
    }

    public int getCurrentY() {
        return this.y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getDurability() {
        return this.durability;
    }
}