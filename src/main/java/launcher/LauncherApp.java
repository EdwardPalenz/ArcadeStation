package launcher;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.List;

import com.almasb.fxgl.app.GameApplication;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import main.launcher.uso.ControlDeUSo;

public class LauncherApp extends Application {

	public static final String APP_SCORE_DIR = System.getProperty("user.home") + File.separator + "Documents"
			+ File.separator + "My Games" + File.separator + "ArcadeStation";
	private static Stage primaryStage;

	private LauncherController controller;

	//private static ControlDeUSo usoControl;

	@Override
	public void init() throws Exception {
		//setUsoControl(new ControlDeUSo());

		controller = new LauncherController();
//		if (getUsoControl().isRecienCreado()) {
//			guardarUsos();
//			getUsoControl().leerFicheroUsos();
//		}
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		LauncherApp.primaryStage = primaryStage;

		crearDirPuntuaciones();

		primaryStage.initStyle(StageStyle.UNDECORATED);
		primaryStage.setTitle("Arcade Station v0.1");
		Scene scene = new Scene(controller.getView());
		primaryStage.setScene(scene);
		scene.getStylesheets().add("/styles/LauncherBaseStyle.css");
		primaryStage.getIcons().add(new Image("/assets/textures/launcherIcon.png"));
		primaryStage.show();

	}

//	@Override
//	public void stop() throws Exception {
//		guardarUsos();
//	}

//	private void guardarUsos() throws FileNotFoundException {
//		List<Class<? extends GameApplication>> juegos = controller.getModel().getJuegos();
//
//		PrintWriter writer = new PrintWriter(getUsoControl().getFicheroUso());
//		for (int i = 0; i < juegos.size(); i++) {
//
//			String nombreJuego = juegos.get(i).getSimpleName();
//			writer.write(nombreJuego + ":" + getUsoControl().getUsoApps().getOrDefault(nombreJuego, 0) + "\n");
//
//		}
//		writer.close();
//	}

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

//	public static ControlDeUSo getUsoControl() {
//		return usoControl;
//	}
//
//	public static void setUsoControl(ControlDeUSo usoControl) {
//		LauncherApp.usoControl = usoControl;
//	}

}
