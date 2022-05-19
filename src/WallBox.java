import javafx.scene.paint.Color;
import javafx.scene.shape.StrokeType;

//WallBox is a box without any future. WallBox fills the gaps in the BoxList array
public class WallBox extends Box {

    // Constructor method of WallBox
    public WallBox (int x, int y) {
        super(x, y);
        // Coloring and shaping
        this.setFill(Color.rgb(127,127,127));
        this.setStroke(Color.rgb(47,47,47));
        this.setStrokeType(StrokeType.INSIDE);
        this.setStrokeWidth(2);
    }
}