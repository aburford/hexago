package hexago;

import javafx.animation.RotateTransition;
import javafx.geometry.Insets;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

public class Quadrant extends Pane {

	GameBoardStage delegate;
	int num;
	double startAngle;
	double startRotate;
	Rotation rotation = Rotation.NONE;
	boolean canRotate = true;
	
	public Quadrant(MarbleHole[][] holes, GameBoardStage delegate, int num) {
		this.delegate = delegate;
		this.num = num;
		final double SIDELENGTH = 300;
		this.setMaxWidth(SIDELENGTH);
		this.setMaxHeight(SIDELENGTH);
		Rectangle r = new Rectangle(SIDELENGTH, SIDELENGTH);
		double radius = ((SIDELENGTH - 10*(holes.length + 1))/holes.length - 1)/2;
		r.setArcHeight(radius * 2);
		r.setArcWidth(radius * 2);
		r.setFill(Color.RED);
		getChildren().add(r);

		GridPane grid = new GridPane();
		grid.setPadding(new Insets(10));
		grid.setHgap(10);
		grid.setVgap(10);
		for (int i = 0; i < holes.length; i++)
			for (int j = 0; j < holes[i].length; j++) {
				Circle c = new MarbleHoleView(holes[i][j], radius, delegate);
				c.setStroke(Color.BLACK);
				c.setFill(Color.TRANSPARENT);
				grid.add(c, j, i);
			}
		getChildren().add(grid);

		this.setOnMousePressed((e) -> {
			if (!canRotate)
				return;
			toFront();
			startAngle = angle(e.getX(), e.getY());
			startRotate = getRotate();
		});

		this.setOnMouseDragged((e) -> {
			if (canRotate)
				this.setRotate(startRotate + startAngle - angle(e.getX(), e.getY()));
		});

		this.setOnMouseReleased((e) -> {
			if (!canRotate)
				return;
			double theta;
			if ((rotation == Rotation.RIGHT || delegate.getRotatedQuadrant() == null) && getRotate() < startRotate - 22) {
				theta = startRotate - 90;
				rotation = (rotation == Rotation.RIGHT) ? Rotation.NONE : Rotation.LEFT;
			} else if ((rotation == Rotation.LEFT || delegate.getRotatedQuadrant() == null) && getRotate() > startRotate + 22) {
				theta = startRotate + 90;
				rotation = (rotation == Rotation.LEFT) ? Rotation.NONE : Rotation.RIGHT;
			} else {
				theta = startRotate;
			}
			if (rotation != Rotation.NONE)
				delegate.setRotatedQuadrange(this);
			else if (delegate.getRotatedQuadrant() == this)
				delegate.setRotatedQuadrange(null);
			theta -= getRotate();
			Duration duration = Duration.millis(Math.sqrt(Math.abs(theta) * 2000));
			RotateTransition rotateTransition = new RotateTransition(duration, this);
			rotateTransition.setByAngle(theta);
			rotateTransition.setOnFinished((ev) -> {
				canRotate = true;
			});
			canRotate = false;
			rotateTransition.play();
		});
	}

	private double angle(double x, double y) {
		return (Math.atan2(-y + getHeight() / 2, x - getWidth() / 2)) / (2 * Math.PI) * 360 - getRotate();
	}
}
