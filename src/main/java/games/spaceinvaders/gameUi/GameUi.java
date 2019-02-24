
package games.spaceinvaders.gameUi;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

import com.almasb.fxgl.scene.GameScene;
import com.almasb.fxgl.texture.Texture;
import com.almasb.fxgl.ui.UIController;

import games.spaceinvaders.scoreModel.ScoreModel;
import games.spaceinvaders.spaceinvaders.SpaceInvaders;
import javafx.beans.binding.Bindings;
import javafx.concurrent.Task;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import launcher.LauncherApp;

public class GameUi implements UIController {

	private double cordinateY = 10;

	private List<Texture> lives = new ArrayList<>();

	private Label scoreLabel = new Label();

	private ScoreModel scoreModel = new ScoreModel();

	private GameScene gameScene;

	@Override
	public void init() {

	}

	public GameUi(GameScene gameScene) {
		this.gameScene = gameScene;
	}

	public void addLife() {
		int numLives = lives.size();
		Image image = new Image("/games/spaceinvaders/main/resources/like.png");
		Texture texture = new Texture(image);

		texture.setTranslateX(20 * numLives + 540);
		texture.setTranslateY(cordinateY);

		lives.add(texture);

		gameScene.addUINode(texture);

	}

	public void addScore() {
		scoreLabel.setLayoutX(510);
		scoreLabel.setLayoutY(cordinateY - 10);
		scoreLabel.setPrefSize(100, 100);
		scoreLabel.setTextFill(Color.WHITE);
		scoreLabel.textProperty().bind(Bindings.concat("Puntuaci�n\n").concat(scoreModel.scoreProperty()));
		gameScene.addUINode(scoreLabel);
	}

	public void addPoints() {
		scoreModel.setScore(scoreModel.getScore() + 50);
	}

	public void removePoints() {
		scoreModel.setScore(scoreModel.getScore() - 10);
	}

	public void removeLife() {
		Texture vidaFuera = lives.get(lives.size() - 1);

		lives.remove(vidaFuera);

		scoreModel.setScore(scoreModel.getScore() - 300);

		gameScene.removeUINode(vidaFuera);

	}

	public void savePoints(String string) throws IOException {
		Task<Void> taskGuardar = new Task<Void>() {

			@Override
			protected Void call() throws Exception {
				File file = new File(LauncherApp.APP_SCORE_DIR + File.separator + SpaceInvaders.class.getSimpleName());

				if (!file.exists())
					file.mkdir();

				file = new File(file.toPath() + File.separator + "puntuaciones.txt");
				if (!file.exists())
					file.createNewFile();

				Files.write(file.toPath(), (string + ":" + scoreModel.getScore() + "\n").getBytes(),
						StandardOpenOption.APPEND);
				return null;
			}

		};
		new Thread(taskGuardar).start();

	}

	public List<Texture> getLives() {
		return lives;
	}

}
