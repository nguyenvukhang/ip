package pascal.ui;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * The UI of the application.
 */
public class App extends Application {
    private Image pascalImage =
        new Image(this.getClass().getResourceAsStream("/images/tux.png"));

    /**
     * Scrolls over the dialog.
     * Has only one child: the `VBox` that contains the list of dialog messages.
     */
    private ScrollPane scrollPane;
    private VBox dialogContainer;
    private TextField userTextField;
    private Button sendButton;

    /** (Fixed) width of the OS window. */
    private final static double WIDTH = 400;

    /** (Fixed) height of the OS window. */
    private final static double HEIGHT = 600;

    /** (Fixed) width of the user input box. */
    private final static double USER_INPUT_WIDTH = 320;

    private VBox createDialogContainer() {
        VBox vb = new VBox();
        DialogBox dialogBox = new DialogBox("Hello!", pascalImage);
        vb.getChildren().addAll(dialogBox);
        vb.setPrefHeight(Region.USE_COMPUTED_SIZE);
        return vb;
    }

    private ScrollPane createScrollPane(VBox content) {
        ScrollPane sp = new ScrollPane(content);
        sp.setPrefSize(WIDTH, 535);
        sp.setVvalue(1.0);
        sp.setFitToWidth(true);
        return sp;
    }

    private void setStage(Stage stage) {
        stage.setTitle("Pascal");
        stage.setResizable(true);
        stage.setMinHeight(HEIGHT);
        stage.setMinWidth(WIDTH);
    }

    private void setBg(Node node, String color) {
        node.setStyle(String.format("-fx-background-color: %s;", color));
    }

    @Override
    public void start(Stage stage) {
        setStage(stage);

        // Construct all elements first.
        dialogContainer = createDialogContainer();
        scrollPane = new ScrollPane(dialogContainer);
        userTextField = new TextField();
        sendButton = new Button("Send");
        HBox userInputContainer = new HBox(8, userTextField, sendButton);
        VBox mainLayout = new VBox(8, dialogContainer, userInputContainer);

        // Only show the vertical scrollbar on the scroll pane.
        scrollPane.setHbarPolicy(ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollBarPolicy.ALWAYS);
        scrollPane.prefWidthProperty().bind(mainLayout.widthProperty());

        VBox.setVgrow(scrollPane, Priority.ALWAYS);

        userTextField.setOnAction(event -> {
            String input = userTextField.getText();
            System.out.println("Submitted: " + input);
            userTextField.clear(); // Clear the text field after submission
        });

        HBox.setHgrow(userTextField, Priority.ALWAYS);

        userInputContainer.prefWidthProperty().bind(mainLayout.widthProperty());
        // hbox.setStyle("-fx-background-color: #22c55e;");

        mainLayout.setPrefSize(WIDTH, HEIGHT);

        Scene scene = new Scene(mainLayout);
        scene.setOnKeyPressed(event -> {
            switch (event.getCode()) {
            case Q:
                Platform.exit();
            default:
                break;
            }
        });

        stage.setScene(scene);
        stage.show();
        stage.toFront();
        stage.requestFocus();
    }
}
