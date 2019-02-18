package main.launcher;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import com.almasb.fxgl.app.GameApplication;

import games.ajedrez.Ajedrez;
import games.snakeClassic.SnakeClassic;
import games.snakeevolution.Snake;
import games.spaceinvaders.spaceinvaders.SpaceInvaders;
import javafx.animation.ParallelTransition;
import javafx.animation.TranslateTransition;
import javafx.beans.binding.Bindings;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

public class LauncherController implements Initializable {

	private static final int CENTER_HEIGHT = 450;

	private static final int CENTER_WIDTH = 500;

	@FXML
	private BorderPane view;

	@FXML
	private Button jugarButton;

	@FXML
	private Button reglasButton;

	@FXML
	private Button anteriorButton;

	@FXML
	private Button siguienteButton;

	@FXML
	private HBox header;

	@FXML
	private MenuBar menuBar;

	@FXML
	private StackPane center;

	@FXML
	private MenuItem salirMenuItem;

	@FXML
	private CheckMenuItem minimizarAlAbrirMenuItem;

	@FXML
	private MenuItem puntuacionesMenuItem;

	@FXML
	private MenuItem acercaDeMenuItem;
	@FXML
	private Button minimizarButton;

	@FXML
	private Button cerrarButton;

	// Model
	private LauncherModel model = new LauncherModel();

	private ImageView imagenNueva;

	private Button imageButton;

	double x, y;

	public LauncherController() throws IOException {
		getJuegos();

		FXMLLoader loader = new FXMLLoader(getClass().getResource("LauncherView.fxml"));
		loader.setController(this);
		loader.load();

	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		imagenNueva = new ImageView();
		imageButton = new Button();
		imageButton.setBackground(Background.EMPTY);

		imageButton.setOnAction(e -> onJugarAction(e));

		imagenNueva = generateIV();
		imageButton.setGraphic(imagenNueva);
		center.getChildren().add(imageButton);

		siguienteButton.disableProperty()
				.bind(Bindings.equal(model.juegosProperty().size() - 1, model.juegoSeleccionadoProperty()));

		anteriorButton.disableProperty().bind(Bindings.equal(0, model.juegoSeleccionadoProperty()));

		siguienteButton.setOnAction(e -> siguienteImagen());

		anteriorButton.setOnAction(e -> anteriorImagen());

		/* Menús */
		minimizarAlAbrirMenuItem.selectedProperty().bindBidirectional(model.minimizarProperty());

		menuBar.setOnMousePressed(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				x = LauncherApp.getPrimaryStage().getX() - event.getScreenX();
				y = LauncherApp.getPrimaryStage().getY() - event.getScreenY();
			}
		});

		menuBar.setOnMouseDragged(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				LauncherApp.getPrimaryStage().setX(event.getScreenX() + x);
				LauncherApp.getPrimaryStage().setY(event.getScreenY() + y);
			}
		});

	}

	@SuppressWarnings("unchecked")
	private void getJuegos() {

		model.juegosProperty().addAll(SnakeClassic.class, Snake.class, Ajedrez.class, SpaceInvaders.class);

		model.previewsProperty().addAll(
				new Image("assets/textures/snakeClassicPreview.png", CENTER_WIDTH, CENTER_HEIGHT, false, true),
				new Image("assets/textures/snakePreview.png", CENTER_WIDTH, CENTER_HEIGHT, false, true),
				new Image("assets/textures/ajedrezPreview.png", CENTER_WIDTH, CENTER_HEIGHT, false, true),
				new Image("assets/textures/spaceInvadersPreview.png", CENTER_WIDTH, CENTER_HEIGHT, false, true));
	}

	private void getTooltip() {
		Tooltip t = new Tooltip(model.getJuegos().get(model.getJuegoSeleccionado()).getSimpleName());
		t.centerOnScreen();
		t.setShowDelay(Duration.millis(200));
		imageButton.setTooltip(t);
	}

	private ImageView generateIV() {
		ImageView imagen = new ImageView(model.getPreviews().get(model.getJuegoSeleccionado()));

		getTooltip();

		return imagen;
	}

	private void anteriorImagen() {
		anteriorButton.toFront();
		siguienteButton.toFront();

		ImageView imagenVieja = imagenNueva;

		model.setJuegoSeleccionado(model.getJuegoSeleccionado() - 1);

		imagenNueva = generateIV();
		center.getChildren().add(imagenNueva);
		imagenNueva.setX(-CENTER_WIDTH);

		TranslateTransition hoja1 = new TranslateTransition();
		hoja1.setNode(view.getCenter());
		hoja1.setFromX(0);
		hoja1.setToX(1000);
		hoja1.setDuration(Duration.seconds(0.5));
		hoja1.setNode(imagenVieja);
		hoja1.setDelay(Duration.seconds(0.15));

		TranslateTransition hoja2 = new TranslateTransition();
		hoja2.setNode(view.centerProperty().get());
		hoja2.setFromX(-1000);
		hoja2.setToX(0);
		hoja2.setDuration(Duration.seconds(0.5));
		hoja2.setNode(imagenNueva);

		ParallelTransition pT = new ParallelTransition(center, hoja1, hoja2);
		pT.setOnFinished(e -> {
			imageButton.setGraphic(imagenNueva);
			imageButton.setLayoutX(0);
		});
		pT.play();

	}

	private void siguienteImagen() {
		anteriorButton.toFront();
		siguienteButton.toFront();
		ImageView imagenVieja = imagenNueva;

		model.setJuegoSeleccionado(model.getJuegoSeleccionado() + 1);

		imagenNueva = generateIV();
		center.getChildren().add(imagenNueva);

		imagenNueva.setX(CENTER_WIDTH);

		TranslateTransition hoja1 = new TranslateTransition();
		hoja1.setNode(view.getCenter());
		hoja1.setFromX(0);
		hoja1.setToX(-1000);
		hoja1.setDuration(Duration.seconds(0.5));
		hoja1.setNode(imagenVieja);
		hoja1.setDelay(Duration.seconds(0.15));

		TranslateTransition hoja2 = new TranslateTransition();
		hoja2.setNode(view.centerProperty().get());
		hoja2.setFromX(1000);
		hoja2.setToX(0);
		hoja2.setDuration(Duration.seconds(0.5));
		hoja2.setNode(imagenNueva);

		ParallelTransition pT = new ParallelTransition(center, hoja1, hoja2);
		pT.setOnFinished(e -> {
			imageButton.setGraphic(imagenNueva);
			imageButton.setLayoutX(0);
		});
		pT.play();

	}

	private void abrirJuego() {
		String javaBin = System.getProperty("java.home") + File.separator + "bin" + File.separator + "java";
		String classPath = System.getProperty("java.class.path");
		Class<? extends GameApplication> game = model.juegosProperty().get(model.getJuegoSeleccionado());

		String className = game.getCanonicalName();

		Alert cargando = new Alert(AlertType.INFORMATION, "El juego se está cargando...");
		cargando.setHeaderText("");
		cargando.initOwner(LauncherApp.getPrimaryStage());
		cargando.setGraphic(new ImageView(new Image("assets/textures/lancherLoading.gif")));

		Task<Void> abrirApp = new Task<Void>() {

			@Override
			protected Void call() throws Exception {
				cargando.show();
				ProcessBuilder builder = new ProcessBuilder(javaBin, "-cp", classPath, className);

				try {
					builder.start();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				return null;
			}

		};

		abrirApp.setOnFailed(e1 -> {
			cargando.close();
			Alert alert = new Alert(AlertType.ERROR);
			alert.setContentText("Fallo");
		});

		abrirApp.setOnSucceeded(e -> {
			cargando.close();
		});

		abrirApp.run();
	}

	@FXML
	void onAcercaDeAction(ActionEvent event) {

	}

	@FXML
	void onJugarAction(ActionEvent event) {
		if (model.isMinimizar())
			minimizarStage();

		abrirJuego();

	}

	@FXML
	void onPuntuacionesAction(ActionEvent event) {

	}

	@FXML
	void onReglasAction(ActionEvent event) {

	}

	@FXML
	void onSalirAction(ActionEvent event) {
		cerrarStage();
	}

	@FXML
	void onCerrarAction(ActionEvent event) {
		cerrarStage();
	}

	@FXML
	void onMinimizarAction(ActionEvent event) {
		minimizarStage();
	}

	private void cerrarStage() {
		LauncherApp.getPrimaryStage().close();
	}

	private void minimizarStage() {
		LauncherApp.getPrimaryStage().setIconified(true);
	}

	public BorderPane getView() {
		return view;
	}
}
