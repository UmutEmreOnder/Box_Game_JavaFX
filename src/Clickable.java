// Clickable interface has methods that have the main mechanics of the game
public interface Clickable {
    // Click method is the most principal method. Whenever a user clicks a box this method executes (if the box is Clickable and has a durability)
    void click();

    // Getter and setter methods of isClicked status to find which box is clicked
    boolean isClicked();
    void setClicked(boolean value);
}