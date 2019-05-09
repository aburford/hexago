package hexago;

public class BoardLogic {
	MarbleHole[][] board;
	int axis;

	public BoardLogic(int n) {
		board = new MarbleHole[n][n];
		axis = n / 2;
		// future possibility: load game from save file
		for (int i = 0; i < board.length; i++)
			for (int j = 0; j < board[i].length; j++)
				board[i][j] = new MarbleHole(Marble.EMPTY);
	}

	public MarbleHole[][] getQuadrant(int num) {
		switch (num) {
		case 1:
			return (MarbleHole[][]) arrayCopy2D(board, 0, axis, axis, axis);
		case 2:
			return (MarbleHole[][]) arrayCopy2D(board, 0, axis, 0, axis);
		case 3:
			return (MarbleHole[][]) arrayCopy2D(board, axis, axis, 0, axis);
		case 4:
			return (MarbleHole[][]) arrayCopy2D(board, axis, axis, axis, axis);
		default:
			return null;
		}
	}

	public void rotate(int quadNum, boolean clockwise) {
		int rowOffset = (quadNum > 2) ? axis : 0;
		int colOffset = (quadNum == 1 || quadNum == 4) ? axis : 0;
		int centerX = colOffset + axis / 2;
		int centerY = rowOffset + axis / 2;
		int[] converted = new int[2];
		int[] converted2 = new int[2];
		// if you want to handle odd axis: + ((axis % 2 == 0)?0:1) and some
		// other stuff
		for (int r = rowOffset; r < rowOffset + axis / 2; r++) {
			for (int c = colOffset; c < colOffset + axis / 2; c++) {
				MarbleHole tmp = board[r][c];
				int[] rot = rotated(centerY - r, c - centerX, clockwise);
				convertPoint(converted, rot, centerX, centerY);
				board[r][c] = board[converted[0]][converted[1]];
				int[] nextRot = new int[2];
				int times = 2;
				while (times-- > 0) {
					nextRot[0] = (clockwise ? 1 : -1) * rot[1];
					nextRot[1] = (clockwise ? -1 : 1) * rot[0];
					convertPoint(converted, rot, centerX, centerY);
					convertPoint(converted2, nextRot, centerX, centerY);
					board[converted[0]][converted[1]] = board[converted2[0]][converted2[1]];
					rot[0] = nextRot[0];
					rot[1] = nextRot[1];
				}
				convertPoint(converted, rot, centerX, centerY);
				board[converted[0]][converted[1]] = tmp;
			}
		}
	}

	public void printBoard() {
		for (MarbleHole[] r : board) {
			for (MarbleHole mh : r) {
				switch (mh.value) {
				case WHITE:
					System.out.print('W');
					break;
				case BLACK:
					System.out.print('B');
					break;
				case EMPTY:
					System.out.print('X');
					break;
				}
			}
			System.out.println();
		}
	}

	// converts a rotated point back to usable indices
	private int[] convertPoint(int[] output, int[] p, int centerX, int centerY) {
		output[0] = centerY - p[0] - ((p[0] < 0) ? 1 : 0);
		output[1] = centerX + p[1] - ((p[1] > 0) ? 1 : 0);
		return output;
	}

	private int[] rotated(int y, int x, boolean clockwise) {
		if (clockwise)
			return new int[] { x, -y };
		else
			return new int[] { -x, y };
	}

	// n: the number of consecutive marbles one must have in a row in order to
	// win
	// will actually work for any convex shape
	// could probably be more efficient
	// TODO: deal with ties
	public Marble checkForWinner(int n) {
		printBoard();
		for (int r = 0; r < board.length; r++) {
			for (int c = 0; c < board[r].length; c++) {
				if (board[r][c].value == Marble.EMPTY)
					continue;
				if (r + n <= board.length && c - n >= -1 && traverse(Direction.SOUTHWEST, r, c, n)
						|| r + n <= board.length && traverse(Direction.SOUTH, r, c, n)
						|| r + n <= board.length && c + n <= board[r + n - 1].length
								&& traverse(Direction.SOUTHEAST, r, c, n)
						|| c + n <= board[r].length && traverse(Direction.EAST, r, c, n))
					return board[r][c].value;
			}
		}
		return Marble.EMPTY;
	}

	// checks if there is a neutral block and therefore player need not rotate a
	// block
	// reuse code instead of making any attempt at efficiency
	public boolean neutralBlock() {
		boolean neutral = true;
		for (int quad = 1; quad <= 4; quad++) {
			MarbleHole[][] orig = getQuadrant(quad);
			rotate(quad, true);
			MarbleHole[][] rotated = getQuadrant(quad);
			neutral = true;
			checkQuad: {
				for (int i = 0; i < orig.length; i++) {
					for (int j = 0; j < orig[i].length; j++) {
						if (!orig[i][j].equals(rotated[i][j])) {
							neutral = false;
							break checkQuad;
						}
					}
				}
			}
			// reset to original
			rotate(quad, false);
			if (neutral)
				return true;
		}
		return false;
	}

	// assumes board is big enough to traverse in given direction
	private boolean traverse(Direction d, int r, int c, int n) {
		Marble next = null;
		for (int i = 1; i < n; i++) {
			switch (d) {
			case SOUTHWEST:
				next = board[r + i][c - i].value;
				break;
			case SOUTH:
				next = board[r + i][c].value;
				break;
			case SOUTHEAST:
				next = board[r + i][c + i].value;
				break;
			case EAST:
				next = board[r][c + i].value;
				break;
			}
			if (next != board[r][c].value) {
				return false;
			}
		}
		return true;
	}

	private MarbleHole[][] arrayCopy2D(MarbleHole[][] src, int startR, int rLength, int startC, int cLength) {
		MarbleHole[][] copy = new MarbleHole[rLength][cLength];
		int stopR = startR + rLength;
		for (int r = startR; r < stopR; r++)
			System.arraycopy(src[r], startC, copy[r - startR], 0, cLength);
		return copy;
	}
}
