package games.furiout.factory;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class ScoreFuryModel {
	private IntegerProperty score;
	public ScoreFuryModel() {
		score=new SimpleIntegerProperty(this,"score");
	}

	public final IntegerProperty scoreProperty() {
		return this.score;
	}
	

	public final int getScore() {
		return this.scoreProperty().get();
	}
	

	public final void setScore(final int score) {
		this.scoreProperty().set(score);
	}

}
