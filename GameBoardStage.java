package hexago;

import javafx.stage.Stage;

import java.util.HashMap;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Labeled;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.TextAlignment;

public class GameBoardStage extends Stage {
	Quadrant[] quads = new Quadrant[4];
	BoardLogic bl;
	private Marble currentTurn = Marble.WHITE;
	private MarbleHoleView placedMarble = null;
	private Quadrant rotatedQuadrant = null;
	private Button finishTurn;
	private Label info;
	private HashMap<Marble, String> names;
	// n must be even so we can divide into quadrants
	public GameBoardStage(int n, int consecutive, String white, String black) {
		names = new HashMap<Marble, String>();
		names.put(Marble.WHITE, white);
		names.put(Marble.BLACK, black);
		BorderPane bp = new BorderPane();
		Pane pane = new Pane();
		bp.setCenter(pane);
		bl = new BoardLogic(n, consecutive);
		for (int i = 1; i <= 4; i++) {
			Quadrant q = new Quadrant(bl.getQuadrant(i), this, i);
			if (i == 1 || i == 4)
				q.layoutXProperty().bind(pane.widthProperty().divide(2).add(5));
			else
				q.layoutXProperty().bind(pane.widthProperty().divide(2).subtract(q.widthProperty().add(5)));
			if (i > 2)
				q.layoutYProperty().bind(pane.heightProperty().divide(2).add(5));
			else
				q.layoutYProperty().bind(pane.heightProperty().divide(2).subtract(q.heightProperty().add(5)));
			pane.getChildren().add(q);
			quads[i - 1] = q;
		}

		finishTurn = new Button("Finish Turn");
		finishTurn.setOnAction((e) -> {
			if (rotatedQuadrant != null) {
				bl.rotate(rotatedQuadrant.num, rotatedQuadrant.rotation == Rotation.RIGHT);
				rotatedQuadrant.rotation = Rotation.NONE;
				rotatedQuadrant = null;
			}

			placedMarble.unofficial = false;
			placedMarble.repaint();
			placedMarble = null;
			currentTurn = (currentTurn == Marble.WHITE) ? Marble.BLACK : Marble.WHITE;
			// this won't do anything if no winner
			Marble winner = bl.checkForWinner();
			if (winner == Marble.EMPTY)
				updateFinishTurn();
			else {
				info.setText(winner.toString() + " has won!\n ");
				finishTurn.setDisable(true);
				for (Quadrant q : quads)
					q.canRotate = false;
				currentTurn = Marble.EMPTY;
			}
		});
		StackPane bottom = new StackPane(finishTurn);
		bp.setBottom(bottom);
		
		info = new Label();
		info.setTextAlignment(TextAlignment.CENTER);
		StackPane top = new StackPane(info);
		bp.setTop(top);
		
		Scene scene = new Scene(bp, 700, 700);
		setScene(scene);
		
		updateFinishTurn();
	}
	
	public MarbleHoleView getPlacedMarble() {
		return placedMarble;
	}
	
	public void setPlacedMarble(MarbleHoleView pm) {
		placedMarble = pm;
		updateFinishTurn();
	}
	
	public Quadrant getRotatedQuadrant() {
		return rotatedQuadrant;
	}
	
	public void setRotatedQuadrange(Quadrant q) {
		rotatedQuadrant = q;
		updateFinishTurn();
	}
	
	private void updateFinishTurn() {
		if (currentTurn == Marble.EMPTY) // game is over
			return;
		// set disable of finish turn button
		info.setText((names.get(currentTurn).isEmpty() ? currentTurn.toString() : names.get(currentTurn)) + "'s turn\n");
		if (placedMarble == null) {
			info.setText(info.getText() + "Please place a marble");
			finishTurn.setDisable(true);
		} else if (rotatedQuadrant == null) {
			if (bl.neutralBlock()) {
				info.setText(info.getText() + "You don't need to rotate a quadrant");
				finishTurn.setDisable(false);
			} else {
				info.setText(info.getText() + "You must rotate a quadrant");
				finishTurn.setDisable(true);
			}
		} else {
			info.setText(info.getText() + "Ready to finish turn");
			finishTurn.setDisable(false);
		}
	}

	public Marble getCurrentTurn() {
		return currentTurn;
	}

}
