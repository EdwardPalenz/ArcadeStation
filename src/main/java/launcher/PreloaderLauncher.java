package launcher;

import javafx.application.Preloader;
import javafx.application.Preloader.StateChangeNotification.Type;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class PreloaderLauncher extends Preloader {
	private Stage preloaderStage;

	@Override
	public void start(Stage primaryStage) throws Exception {
		this.preloaderStage = primaryStage;

		VBox loading = new VBox(20);
		loading.setMaxWidth(Double.MAX_VALUE);
		loading.setMaxHeight(Double.MAX_VALUE);
		loading.setAlignment(Pos.CENTER);
		loading.getChildren().add(new ProgressBar());
		loading.getChildren().add(new Label("Cargando el launcher..."));

		BorderPane root = new BorderPane(loading);
		Scene scene = new Scene(root);
		scene.getStylesheets().add("styles/LauncherBaseStyle.css");

		loading.setStyle("-fx-border-width:1px;-fx-border-color:white;");

		primaryStage.setWidth(300);
		primaryStage.setHeight(300);
		primaryStage.setScene(scene);
		primaryStage.initStyle(StageStyle.TRANSPARENT);
		primaryStage.getIcons().add(new Image("assets/textures/launcherIcon.png"));
		primaryStage.show();
	}

	@Override
	public void handleStateChangeNotification(StateChangeNotification stateChangeNotification) {
		if (stateChangeNotification.getType() == Type.BEFORE_START) {
			preloaderStage.close();
		}
	}

}
