package hexago;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class MarbleHoleView extends Circle {
	MarbleHole hole;
	boolean unofficial = false;
	GameBoardStage delegate;
	public MarbleHoleView(MarbleHole hole, double radius, GameBoardStage delegate) {
		super(radius);
		this.hole = hole;
		this.delegate = delegate;
		this.setOnMouseClicked((e) -> {
			if (unofficial) {
				unofficial = false;
				this.hole.value = Marble.EMPTY;
				delegate.setPlacedMarble(null);
				repaint();
			} else if (hole.value == Marble.EMPTY) {
				MarbleHoleView placedMarble = delegate.getPlacedMarble();
				if (placedMarble != null){
					placedMarble.unofficial = false;
					placedMarble.hole.value = Marble.EMPTY;
					placedMarble.repaint();
				}
				unofficial = true;
				this.hole.value = delegate.getCurrentTurn();
				delegate.setPlacedMarble(this);
				repaint();
			}
		});
	}
	
	public void repaint() {
		double alpha = (unofficial) ? 0.5 : 1;
		switch (hole.value) {
		case BLACK:
			setFill(new Color(0,0,0,alpha));
			break;
		case WHITE:
			setFill(new Color(1,1,1,alpha));
			break;
		case EMPTY:
			setFill(Color.TRANSPARENT);
			break;
		}
	}
}
