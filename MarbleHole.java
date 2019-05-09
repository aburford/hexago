package hexago;

public class MarbleHole {
	Marble value;

	public MarbleHole(Marble value) {
		this.value = value;
	}
	
	public boolean equals(MarbleHole o) {
		return value == o.value;
	}
	
}
