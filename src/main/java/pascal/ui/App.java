package pascal.ui;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import pascal.Pascal;
import pascal.result.Error;
import pascal.result.Result;

/**
 * The UI of the application.
 */
public class App extends Application {
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

    /** (Fixed) width of the OS window. */
    private final static double WIDTH = 400;

    /** (Fixed) height of the OS window. */
    private final static double HEIGHT = 600;

    private VBox createDialogContainer() {
        VBox vb = new VBox();
        DialogBox dialogBox = new DialogBox("Hello!", pascalImage);
        vb.getChildren().addAll(dialogBox);
        vb.setPrefHeight(Region.USE_COMPUTED_SIZE);
        return vb;
    }

    private void respond(String text) {
        DialogBox db = new DialogBox(text, pascalImage);
        db.setLeft();
        dialogContainer.getChildren().add(db);
    }

    private void say(String text) {
        DialogBox db = new DialogBox(text, userImage);
        db.setRight();
        dialogContainer.getChildren().add(db);
    }

    // private void setBg(Node node, String color) {
    //     node.setStyle(String.format("-fx-background-color: %s;", color));
    // }

    @Override
    public void start(Stage stage) {
        // Construct all elements first.
        dialogContainer = createDialogContainer();
        scrollPane = new ScrollPane(dialogContainer);
        userTextField = new TextField();
        sendButton = new Button("Send");
        HBox userInputContainer = new HBox(8, userTextField, sendButton);
        VBox mainLayout = new VBox(8, dialogContainer, userInputContainer);

        mainLayout.setPadding(new Insets(10));

        // Only show the vertical scrollbar on the scroll pane.
        scrollPane.setHbarPolicy(ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollBarPolicy.ALWAYS);
        scrollPane.prefWidthProperty().bind(mainLayout.widthProperty());

        VBox.setVgrow(dialogContainer, Priority.ALWAYS);
        VBox.setVgrow(scrollPane, Priority.ALWAYS);

        Pascal pascal = new Pascal();
        userTextField.setOnAction(event -> {
            String input = userTextField.getText();
            say(input);
            userTextField.clear();
            Result<String, Error> res = pascal.handleUserInput(input);

            if (res.isErr()) {
                respond("Error: " + res.getErr());
                return;
            } else {
                respond(res.get());
            }
        });

        HBox.setHgrow(userTextField, Priority.ALWAYS);

        userInputContainer.prefWidthProperty().bind(mainLayout.widthProperty());
        // hbox.setStyle("-fx-background-color: #22c55e;");

        Scene scene = new Scene(mainLayout);
        scene.setOnKeyPressed(event -> {
            switch (event.getCode()) {
            case Q:
                Platform.exit();
            default:
                break;
            }
        });

        stage.setTitle("Pascal");
        stage.setResizable(true);
        stage.setMinHeight(HEIGHT);
        stage.setMinWidth(WIDTH);
        stage.setScene(scene);
        stage.show();
        stage.toFront();
        stage.requestFocus();
    }
}
