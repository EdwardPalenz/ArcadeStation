package games.furiout.component;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;

import com.almasb.fxgl.scene.GameScene;
import com.almasb.fxgl.ui.UIController;

import games.furiout.factory.ScoreFuryModel;

import javafx.beans.binding.Bindings;
import javafx.concurrent.Task;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import main.launcher.LauncherApp;

public class FuryUi implements UIController {
	private Label scoreLabel = new Label();

	private ScoreFuryModel scoreModel = new ScoreFuryModel();

	private GameScene gameScene;
	private double cordinateY = 720;
	@Override
	public void init() {

	}
	
	public FuryUi(GameScene gameScene) {
		this.gameScene = gameScene;
	}
	
	public void addScore() {
		scoreLabel.setLayoutX(510);
		scoreLabel.setLayoutY(cordinateY - 10);
		scoreLabel.setPrefSize(100, 100);
		scoreLabel.setTextFill(Color.WHITE);
		scoreLabel.textProperty().bind(Bindings.concat("Puntuación\n").concat(scoreModel.scoreProperty()));
		gameScene.addUINode(scoreLabel);
	}

	public void addPoints() {
		scoreModel.setScore(scoreModel.getScore() + 5);
	}
	
	public void save(String nombre) {
		Task<Void> taskGuardar=new Task<Void>() {

			@Override
			protected Void call() throws Exception {
				File file = new File(LauncherApp.APP_SCORE_DIR  + File.separator + "Fury Out");
				
				if(!file.exists())
					file.mkdir();
				
				file=new File(LauncherApp.APP_SCORE_DIR  + File.separator + "Fury Out"+File.separator+"puntuaciones.txt");
				if(!file.exists())
					file.createNewFile();
				
				Files.write(file.toPath(),("\n"+nombre+": "+scoreModel.getScore()+"\n").getBytes(), StandardOpenOption.APPEND);
				return null;
			}
			
		};
		new Thread(taskGuardar).start();
	}
	
	

}
