import javafx.scene.paint.Color;
import javafx.scene.shape.StrokeType;

// This is one of the clickable boxes (the blue ones), so it extends Box class and implements Clickable interface
public class MirrorBox extends Box implements Clickable {

    // It's durability value is default and 1
    private int durability = 1;
    // All clickable boxes has a boolean data to check which box has clicked by the user
    private boolean isClicked = false;

    // Consturctor Method
    public MirrorBox(int x, int y) {
        super(x, y);
        // Coloring and shaping
        this.setFill(Color.rgb(153,217,234));
        this.setStroke(Color.rgb(0,159,231));
        this.setStrokeType(StrokeType.INSIDE);
        this.setStrokeWidth(3);

        // Whenever user clicks that box, if it has durability calls its click method and set its isClicked status to true
        this.setOnMouseClicked(e -> {
            if(this.durability > 0) {
                isClicked = true;
                this.click();
            }
        });
    }

    // Click method decreases its durability and turn it to Empty Box
    @Override
    public void click() {
        durability--;
        turnToEmptyBox();
    }

    // Our turn methods actually changes their colors and shapes
    public void turnToEmptyBox() {
        this.setFill(Color.rgb(255,255,255));
        this.setStroke(Color.rgb(195,195,195));
        this.setStrokeType(StrokeType.INSIDE);
        this.setStrokeWidth(3);
    }


    // Getter and setter methods

    public int getDurability() {
        return durability;
    }

    public boolean isClicked() {
        return isClicked;
    }

    public void setClicked(boolean value) {
        this.isClicked = value;
    }

}