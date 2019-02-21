package main.launcher;

import java.io.File;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class LauncherApp extends Application {

	public static final String APP_SCORE_DIR = System.getProperty("user.home") + File.separator + "Documents"
			+ File.separator + "My Games" + File.separator + "ArcadeStation";
	private static Stage primaryStage;

	private LauncherController controller;

	@Override
	public void init() throws Exception {
		controller = new LauncherController();
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		LauncherApp.primaryStage = primaryStage;

		crearDirPuntuaciones();

		primaryStage.initStyle(StageStyle.UNDECORATED);
		primaryStage.setTitle("Arcade Station v0.1");
		Scene scene = new Scene(controller.getView());
		primaryStage.setScene(scene);
		scene.getStylesheets().add("main/launcher/styles/LauncherBaseStyle.css");
		primaryStage.getIcons().add(new Image("assets/textures/launcherIcon.png"));
		primaryStage.show();

	}

	private void crearDirPuntuaciones() {
		File dirMyGames = new File(
				System.getProperty("user.home") + File.separator + "Documents" + File.separator + "My Games");
		if (!dirMyGames.exists())
			dirMyGames.mkdir();

		File dir = new File(APP_SCORE_DIR);
		try {
			if (!dir.exists())
				dir.mkdir();
		} catch (SecurityException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		launch(args);
	}

	public static Stage getPrimaryStage() {
		return primaryStage;
	}

	public static void setPrimaryStage(Stage primaryStage) {
		LauncherApp.primaryStage = primaryStage;
	}

}
