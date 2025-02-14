package pascal.ui;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

/**
 * A single message in a chat.
 * Contains a speech bubble and a profile image.
 */
public class DialogBox extends HBox {
    private Label text;
    private ImageView displayPicture;

    public DialogBox(String s, Image i) {
        super(10); // set a spacing between display picture and text.
        text = new Label(s);
        displayPicture = new ImageView(i);
        displayPicture.setFitWidth(42);
        displayPicture.setFitHeight(42);

        text.setWrapText(true);
        text.setFont(Config.BODY_FONT);
        HBox.setHgrow(text, Priority.ALWAYS);

        setLeft();
    }

    public void setLeft() {
        setAlignment(Pos.TOP_LEFT);
        getChildren().setAll(displayPicture, text);
    }

    public void setRight() {
        setAlignment(Pos.TOP_RIGHT);
        getChildren().setAll(text, displayPicture);
    }
}
