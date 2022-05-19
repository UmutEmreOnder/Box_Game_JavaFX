import javafx.scene.paint.Color;
import javafx.scene.shape.StrokeType;

// WoodBox is one of the clickable boxes (the purple ones), so it extends Box class and implements Clickable interface
public class WoodBox extends Box implements Clickable {
    // It's durability value is default and 2
    private int durability = 2;
    // All clickable boxes has a boolean data to check which box has clicked by the user
    private boolean isClicked = false;

    // Consturctor Method
    public WoodBox (int x, int y) {
        super(x, y);
        // Coloring and shaping
        this.setFill(Color.rgb(200,191,231));
        this.setStroke(Color.rgb(161,67,160));
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

    // Click method decreases its durability and turn it to either EmptyBox or MirrorBox according its current durability
    @Override
    public void click() {
        durability--;
        if (durability == 1) {
            turnToMirrorBox();
        }
        else {
            turnToEmptyBox();
        }
    }

    // Our turn methods actually changes their colors and shapes
    public void turnToMirrorBox() {
        this.setFill(Color.rgb(153,217,234));
        this.setStroke(Color.rgb(0,159,231));
    }

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