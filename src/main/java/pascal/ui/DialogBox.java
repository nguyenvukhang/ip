package pascal.ui;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

/**
 * A single message in a chat. Contains a speech bubble and a profile image.
 */
public class DialogBox extends HBox {
    private Label textLabel;
    private ImageView displayPicture;

    /** Create a new dialog box. */
    public DialogBox(String text, Image image) {
        super(10); // set a spacing between display picture and text.
        textLabel = new Label(text);
        displayPicture = new ImageView(image);
        displayPicture.setFitWidth(42);
        displayPicture.setFitHeight(42);

        textLabel.setWrapText(true);
        textLabel.setFont(Config.BODY_FONT);
        HBox.setHgrow(textLabel, Priority.ALWAYS);

        setLeft();
    }

    /** Set the dialog box to the left. */
    public void setLeft() {
        setAlignment(Pos.TOP_LEFT);
        getChildren().setAll(displayPicture, textLabel);
    }

    /** Set the dialog box to the right. */
    public void setRight() {
        setAlignment(Pos.TOP_RIGHT);
        getChildren().setAll(textLabel, displayPicture);
    }
}
