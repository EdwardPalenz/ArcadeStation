package games.spaceinvaders.scoreModel;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class ScoreModel {
		private IntegerProperty score;
		
		public ScoreModel() {
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
