package hexago;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ResourceBundle;

import javax.annotation.Resources;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
public class HexagoApp extends Application{
	public Button quitButton;
	public TextField whiteTF;
	public TextField blackTF;
	public ChoiceBox<Integer> dimensionCB;
	public ChoiceBox<Integer> consecutiveCB;
	private ObservableList<Integer> consecutive = FXCollections.observableArrayList(2,3,4,5,6,7,8);
	@Override
	public void start(Stage primaryStage) throws MalformedURLException, IOException{
        URL location = getClass().getResource("StartScreen.fxml");
        FXMLLoader fxmlLoader = new FXMLLoader(location);
        BorderPane rootPane = (BorderPane)fxmlLoader.load();
		Scene scene = new Scene(rootPane);
		primaryStage.setTitle("Hexago");
		primaryStage.setScene(scene);
		primaryStage.show();
		
		dimensionCB = ((HexagoApp)fxmlLoader.getController()).dimensionCB;
		consecutiveCB = ((HexagoApp)fxmlLoader.getController()).consecutiveCB;
		dimensionCB.setValue(8);
		consecutiveCB.setItems(consecutive);
		consecutiveCB.setValue(6);
		// update consecutive options when a different dimension is chosen
		dimensionCB.setOnAction((e) -> {
			// make things a bit more readable (why can't ArrayList have .pop() or .last()?)
			ObservableList<Integer> c = consecutive;
			while (c.get(c.size() - 1) != dimensionCB.getValue())
				if (c.get(c.size() - 1) < dimensionCB.getValue())
					c.add(c.get(c.size() - 1) + 1);
				else {
					if (consecutiveCB.getValue() == c.get(c.size() - 1))
						consecutiveCB.setValue(consecutiveCB.getValue() - 1);
					c.remove(c.size() - 1);
				}
		});
	}
	
	
	public void quit(ActionEvent event) {
        System.exit(0);
    }
	
	public void start(ActionEvent event) {
		GameBoardStage board = new GameBoardStage(dimensionCB.getValue(), consecutiveCB.getValue(), whiteTF.getText(), blackTF.getText());
		board.show();
		((Stage)(((Button)event.getSource()).getScene().getWindow())).close();
	}
	
	public static void main(String[] args) {
		launch(args);
	}

}
