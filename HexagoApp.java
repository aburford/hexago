package hexago;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
public class HexagoApp extends Application {
	Stage primaryStage;
	@FXML
	public Button quitButton;
	@Override
	public void start(Stage primaryStage) throws MalformedURLException, IOException{
		FXMLLoader loader = new FXMLLoader();
		InputStream inputStream = getClass().getResourceAsStream("StartScreen.fxml");
        BorderPane rootPane = loader.<BorderPane>load(inputStream);
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
		GameBoardStage board = new GameBoardStage(8);
		board.show();
		((Stage)(((Button)event.getSource()).getScene().getWindow())).close();
	}
	
	public static void main(String[] args) {
		launch(args);
	}

}
