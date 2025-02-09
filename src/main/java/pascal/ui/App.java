package pascal.ui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * The UI of the application.
 */
public class App extends Application {
    private Image pascalImage =
        new Image(this.getClass().getResourceAsStream("/images/tux.png"));

    private ScrollPane scrollPane;
    private VBox dialogContainer;
    private TextField userInput;
    private Button sendButton;
    private Scene scene;

    private final static double WIDTH = 400;
    private final static double HEIGHT = 600;

    private VBox createDialogContainer() {
        VBox vb = new VBox();
        DialogBox dialogBox = new DialogBox("Hello!", pascalImage);
        vb.getChildren().addAll(dialogBox);
        vb.setPrefHeight(Region.USE_COMPUTED_SIZE);
        return vb;
    }

    private ScrollPane createScrollPane(VBox content) {
        ScrollPane sp = new ScrollPane(content);
        sp.setPrefSize(385, 535);
        sp.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        sp.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        sp.setVvalue(1.0);
        sp.setFitToWidth(true);
        return sp;
    }

    private void setStage(Stage stage) {
        stage.setTitle("Pascal");
        stage.setResizable(false);
        stage.setMinHeight(HEIGHT);
        stage.setMinWidth(WIDTH);
    }

    @Override
    public void start(Stage stage) {
        setStage(stage);

        dialogContainer = createDialogContainer();

        scrollPane = createScrollPane(dialogContainer);

        userInput = new TextField();
        userInput.setPrefWidth(325.0);

        sendButton = new Button("Send");
        sendButton.setPrefWidth(55.0);

        AnchorPane mainLayout = new AnchorPane();

        mainLayout.setPrefSize(WIDTH, HEIGHT);
        mainLayout.getChildren().addAll(scrollPane, userInput, sendButton);

        AnchorPane.setTopAnchor(scrollPane, 1.0);
        AnchorPane.setBottomAnchor(sendButton, 1.0);
        AnchorPane.setRightAnchor(sendButton, 1.0);
        AnchorPane.setLeftAnchor(userInput, 1.0);
        AnchorPane.setBottomAnchor(userInput, 1.0);

        stage.setScene(new Scene(mainLayout));
        stage.show();
    }
}
