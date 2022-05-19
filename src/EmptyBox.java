import javafx.scene.paint.Color;
import javafx.scene.shape.StrokeType;

// EmptyBox is a box without any future. WoodBox and MirrorBox turns to EmptyBox whenever their durability is 0
public class EmptyBox extends Box {

    // Constructor method of EmptyBox
    public EmptyBox (int x, int y) {
        super(x, y);
        // Coloring and shaping
        this.setFill(Color.rgb(255,255,255));
        this.setStroke(Color.rgb(195,195,195));
        this.setStrokeType(StrokeType.INSIDE);
        this.setStrokeWidth(3);
    }
}