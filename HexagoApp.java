package hexago;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ResourceBundle;

import javax.annotation.Resources;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
public class HexagoApp extends Application{
	Stage primaryStage;
	public Button quitButton;
	public TextField whiteTF;
	public TextField blackTF;
	
	@Override
	public void start(Stage primaryStage) throws MalformedURLException, IOException{
        URL location = getClass().getResource("StartScreen.fxml");
        FXMLLoader fxmlLoader = new FXMLLoader(location);
        BorderPane rootPane = (BorderPane)fxmlLoader.load();
		Scene scene = new Scene(rootPane);
		primaryStage.setTitle("Hexago");
		primaryStage.setScene(scene);
		primaryStage.show();
		this.primaryStage = primaryStage;
	}
	
	public void quit(ActionEvent event) {
        System.exit(0);
    }
	
	public void start(ActionEvent event) {
		GameBoardStage board = new GameBoardStage(8, 6, whiteTF.getText(), blackTF.getText());
		board.show();
		((Stage)(((Button)event.getSource()).getScene().getWindow())).close();
	}
	
	public static void main(String[] args) {
		launch(args);
	}

}
