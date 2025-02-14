package pascal.ui;

// clang-format off
import javafx.application.Application;
import javafx.event.Event;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import pascal.Pascal;
import pascal.color.Colors;
import pascal.result.Error;
import pascal.result.Result;
// clang-format on

/**
 * The UI of the application.
 */
public class App extends Application {
    /** (Fixed) width of the OS window. */
    private static final double WIDTH = 400;

    /** (Fixed) height of the OS window. */
    private static final double HEIGHT = 600;

    private Image pascalImage =
        new Image(this.getClass().getResourceAsStream("/images/uwuntu.png"));
    private Image userImage =
        new Image(this.getClass().getResourceAsStream("/images/tux.png"));

    /**
     * Scrolls over the dialog.
     * Has only one child: the `VBox` that contains the list of dialog messages.
     */
    private ScrollPane scrollPane;

    /**
     * Parent of all the individual messages.
     * Each child is a message.
     */
    private VBox dialogContainer;
    private TextField userTextField;
    private Button sendButton;

    private Pascal pascal;

    private void respond(String text) {
        DialogBox db = new DialogBox(text, pascalImage);
        db.setLeft();
        dialogContainer.getChildren().add(db);
        db.prefWidthProperty().bind(dialogContainer.widthProperty());
    }

    private void say(String text) {
        DialogBox db = new DialogBox(text, userImage);
        db.setRight();
        dialogContainer.getChildren().add(db);
    }

    private void setBg(Node node, String color) {
        // node.setStyle(String.format("-fx-background-color: %s;", color));
    }

    private void handleUserInput(String input) {
        if (input.isEmpty()) {
            return;
        }
        say(input);
        Result<String, Error> result = pascal.handleUserInput(input);
        if (result.isErr()) {
            respond("Unknown input. Try again.");
            return;
        }
        respond(result.get());
    }

    @Override
    public void start(Stage stage) {
        // Construct all elements first.
        dialogContainer = new VBox(8);
        scrollPane = new ScrollPane(dialogContainer);
        userTextField = new TextField();
        sendButton = new Button("Send");
        HBox userInputContainer = new HBox(8, userTextField, sendButton);
        VBox mainLayout = new VBox(8, scrollPane, userInputContainer);

        // Debug backgrounds.
        setBg(mainLayout, Colors.Emerald.z200());
        setBg(scrollPane, Colors.Blue.z200());
        setBg(dialogContainer, Colors.Rose.z200());

        scrollPane.addEventFilter(MouseEvent.MOUSE_PRESSED, Event::consume);

        mainLayout.setPadding(new Insets(10));

        // Only show the vertical scrollbar on the scroll pane.
        scrollPane.setHbarPolicy(ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollBarPolicy.AS_NEEDED);
        scrollPane.prefWidthProperty().bind(mainLayout.widthProperty());
        scrollPane.setFitToWidth(true);

        // VBox.setVgrow(dialogContainer, Priority.ALWAYS);
        VBox.setVgrow(scrollPane, Priority.ALWAYS);

        // Fill the userInputContainer with the userTextField.
        HBox.setHgrow(userTextField, Priority.ALWAYS);

        // Start talking backend.
        pascal = new Pascal();
        userTextField.setOnAction(event -> {
            String input = userTextField.getText();
            userTextField.clear();
            handleUserInput(input);
        });
        respond("Hello!");

        stage.setTitle("Pascal");
        stage.setResizable(true);
        stage.setMinHeight(HEIGHT);
        stage.setMinWidth(WIDTH);
        stage.setScene(new Scene(mainLayout));
        stage.show();
        stage.toFront();
        stage.requestFocus();
    }
}
