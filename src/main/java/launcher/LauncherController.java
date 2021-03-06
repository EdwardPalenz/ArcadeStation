package launcher;

import java.awt.Desktop;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;

import com.almasb.fxgl.app.GameApplication;

import games.ajedrez.AjedrezBeta;
import games.furiout.Furiout;
import games.pong.PongGame;
import games.snakeClassic.SnakeClassic;
import games.snakeevolution.SnakeEvolution;
import games.spaceinvaders.spaceinvaders.SpaceInvaders;
import javafx.animation.ParallelTransition;
import javafx.animation.TranslateTransition;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import javafx.util.Duration;
import launcher.puntuaciones.Puntuaciones;

public class LauncherController implements Initializable {

	private static final int CENTER_HEIGHT = 450;

	private static final int CENTER_WIDTH = 500;

	private static final String TEMAS_DIR = "styles/";

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
	private RadioMenuItem defaultTheme;

	@FXML
	private RadioMenuItem bywTheme;

	@FXML
	private RadioMenuItem autumTheme;

	@FXML
	private RadioMenuItem aquaTheme;

	@FXML
	private MenuItem acercaDeMenuItem;

	@FXML
	private Button minimizarButton;

	@FXML
	private Button cerrarButton;

	// Model
	private LauncherModel model = new LauncherModel();

	private ImageView imagenNueva;

	private double x, y;

	private boolean imageClicked = false;

	public LauncherController() throws IOException {
		moverReglas();

		System.setOut(
				new PrintStream(new FileOutputStream(new File(System.getProperty("user.home") + "\\report.txt"))));

		getJuegos();

		FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/LauncherView.fxml"));
		loader.setController(this);
		loader.load();

	}

	private void moverReglas() {
		File dirReglas = new File(LauncherApp.APP_SCORE_DIR + File.separator + "reglas");
		if (!dirReglas.exists()) {
			dirReglas.mkdir();
//			try {
//				String string = "/reglas/";
//				File reglas = new File(
//						LauncherApp.APP_SCORE_DIR + File.separator + "reglas" + File.separator + "AjedrezBeta.txt");
//
//				BufferedInputStream in = new BufferedInputStream(getClass().getResourceAsStream(string + "AjedrezBeta.txt"));
//				
//				BufferedOutputStream writer = new BufferedOutputStream(new FileOutputStream(reglas));
//
//				int read = in.read();
//				while (read != -1) {
//					writer.write(read);
//					read = in.read();
//				}
//
//				System.out.println(reglas.exists());
//				Files.copy(reglas.toPath(), new FileOutputStream(new File(
//						LauncherApp.APP_SCORE_DIR + File.separator + "reglas" + File.separator + "AjedrezBeta.txt")));
//
//				reglas = new File(getClass().getResource(string + "FuriOut.txt").toExternalForm());
//				Files.copy(reglas.toPath(), new FileOutputStream(new File(
//						LauncherApp.APP_SCORE_DIR + File.separator + "reglas" + File.separator + "FuriOut.txt")));
//
//				System.out.println(reglas.exists());
//				reglas = new File(getClass().getResource(string + "PongGame.txt").toExternalForm());
//				Files.copy(reglas.toPath(), new FileOutputStream(new File(
//						LauncherApp.APP_SCORE_DIR + File.separator + "reglas" + File.separator + "PongGame.txt")));
//
//				System.out.println(reglas.exists());
//				reglas = new File(getClass().getResource(string + "SnakeClassic.txt").toExternalForm());
//				Files.copy(reglas.toPath(), new FileOutputStream(new File(
//						LauncherApp.APP_SCORE_DIR + File.separator + "reglas" + File.separator + "SnakeClassic.txt")));
//
//				System.out.println(reglas.exists());
//				reglas = new File(getClass().getResource(string + "SnakeEvolution.txt").toExternalForm());
//				Files.copy(reglas.toPath(), new FileOutputStream(new File(LauncherApp.APP_SCORE_DIR + File.separator
//						+ "reglas" + File.separator + "SnakeEvolution.txt")));
//
//				System.out.println(reglas.exists());
//				reglas = new File(getClass().getResource(string + "SpaceInvaders.txt").toExternalForm());
//				Files.copy(reglas.toPath(), new FileOutputStream(new File(
//						LauncherApp.APP_SCORE_DIR + File.separator + "reglas" + File.separator + "SpaceInvaders.txt")));
//
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
		}
	}

	@SuppressWarnings("unchecked")
	private void getJuegos() {
		// Genera la lista de Juegos que utilizara el Launcher
		model.juegosProperty().addAll(SnakeClassic.class, SnakeEvolution.class, Furiout.class, AjedrezBeta.class,
				SpaceInvaders.class, PongGame.class);

		// Previews para los juegos
		model.previewsProperty().addAll(
				new Image("/assets/textures/snakeClassicPreview.png", CENTER_WIDTH, CENTER_HEIGHT, true, true),
				new Image("/assets/textures/snakePreview.png", CENTER_WIDTH, CENTER_HEIGHT, true, true),
				new Image("/assets/previews/furioutPreview.jpg", CENTER_WIDTH, CENTER_HEIGHT, true, true),
				new Image("/assets/textures/ajedrezPreview.png", CENTER_WIDTH, CENTER_HEIGHT, true, true),
				new Image("/assets/textures/spaceInvadersPreview.png", CENTER_WIDTH, CENTER_HEIGHT, true, true),
				new Image("/assets/textures/pongPreview.png", CENTER_WIDTH, CENTER_HEIGHT, true, true));
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		/*
		 * Imagen con el preview del juego seleccionado
		 */
		imagenNueva = new ImageView();

		imagenNueva = generateIV();

		center.getChildren().add(imagenNueva);

		/*
		 * Botones de siguiente y anterior
		 * 
		 */
		siguienteButton.setOnAction(e -> siguienteImagen());

		anteriorButton.setOnAction(e -> anteriorImagen());

		/* Men�s */
		minimizarAlAbrirMenuItem.selectedProperty().bindBidirectional(model.minimizarProperty());

		/*
		 * Permite mover la ventana arrastrando la barra del men� superior
		 */
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
		
		acercaDeMenuItem.setOnAction(e -> onAcercaDeAction(e));

		/*
		 * Temas
		 */
		ToggleGroup temasGroup = new ToggleGroup();

		defaultTheme.setUserData("default");
		defaultTheme.setToggleGroup(temasGroup);
		defaultTheme.setSelected(true);

		bywTheme.setUserData("silverStyle.css");
		bywTheme.setToggleGroup(temasGroup);

		aquaTheme.setUserData("aquaStyle.css");
		aquaTheme.setToggleGroup(temasGroup);

		autumTheme.setUserData("autumStyle.css");
		autumTheme.setToggleGroup(temasGroup);

		// Listener del ToggleGroup
		temasGroup.selectedToggleProperty().addListener((o, ov, nv) -> cambiarTema(nv));

	}

	private ImageView generateIV() {
		ImageView imagen = new ImageView(model.getPreviews().get(model.getJuegoSeleccionado()));
		imagen.setOnMousePressed(e -> esperarDoubleClick(e));
		return imagen;
	}

	private void anteriorImagen() {
		anteriorButton.toFront();
		siguienteButton.toFront();

		ImageView imagenVieja = imagenNueva;

		if (model.getJuegoSeleccionado() <= 0) {
			model.setJuegoSeleccionado(model.getJuegos().size() - 1);
		} else {
			model.setJuegoSeleccionado(model.getJuegoSeleccionado() - 1);
		}

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
			imagenNueva.setLayoutX(0);
		});
		pT.play();

	}

	private void siguienteImagen() {
		anteriorButton.toFront();
		siguienteButton.toFront();
		ImageView imagenVieja = imagenNueva;

		if (model.getJuegoSeleccionado() >= model.getJuegos().size() - 1) {
			model.setJuegoSeleccionado(0);
		} else {
			model.setJuegoSeleccionado(model.getJuegoSeleccionado() + 1);
		}

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
			imagenNueva.setLayoutX(0);
		});
		pT.play();

	}

	private void esperarDoubleClick(MouseEvent e) {
		// Espera a que ocurra un segundo click antes de 300 milissegundos para abrir el
		// juego
		if (!imageClicked) {
			imageClicked = true;

			Task<Void> esperarClick = new Task<Void>() {
				@Override
				protected Void call() throws Exception {
					Thread.sleep(300);
					imageClicked = false;
					return null;
				}
			};
			esperarClick.setOnSucceeded(e1 -> {
				imageClicked = false;
			});
			new Thread(esperarClick).start();
		} else {
			onJugarAction(null);
		}
	}

	private void abrirJuego() {
		String javaBin = System.getProperty("java.home") + File.separator + "bin" + File.separator + "java";
		String classPath = System.getProperty("java.class.path");
		Class<? extends GameApplication> game = model.juegosProperty().get(model.getJuegoSeleccionado());

		String className = game.getCanonicalName();

		Task<Void> abrirApp = new Task<Void>() {

			@Override
			protected Void call() throws Exception {
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
			Alert alert = new Alert(AlertType.ERROR);
			alert.setContentText(e1.getSource().getException().getMessage());
			alert.show();
		});

		abrirApp.run();
	}

	@FXML
	void onAcercaDeAction(ActionEvent event) {
		String url = "https://github.com/EdwardPalenz/ArcadeStation";
		try {
			Desktop.getDesktop().browse(new URI(url));
		} catch (IOException | URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@FXML
	void onJugarAction(ActionEvent event) {
		if (model.isMinimizar())
			minimizarStage();

		abrirJuego();

	}

	private void cambiarTema(Toggle nv) {
		// Lista de estilos de la escena
		ObservableList<String> stylesheets = view.getScene().getStylesheets();

		// Si hay m�s de uno, borra el �ltimo. (Dejando siempre el default)
		if (stylesheets.size() > 1)
			stylesheets.remove(stylesheets.size() - 1);

		// Si el seleccionado es el default no a�adira un tema nuevo
		String tema = nv.getUserData().toString();
		if (!"default".equals(tema)) {
			stylesheets.add(TEMAS_DIR + tema);
		}
	}

	@FXML
	void onPuntuacionesAction(ActionEvent event) {
		try {
			Puntuaciones puntuaciones = new Puntuaciones();

			Stage puntuacionesStage = new Stage();
			puntuacionesStage.setTitle("Puntuaciones");
			puntuacionesStage.initOwner(LauncherApp.getPrimaryStage());
			puntuacionesStage.setScene(new Scene(puntuaciones.getView()));
//			puntuacionesStage.getScene().getStylesheets()
//					.addAll(LauncherApp.getPrimaryStage().getScene().getStylesheets());

			puntuacionesStage.getIcons().addAll(LauncherApp.getPrimaryStage().getIcons());

			puntuacionesStage.show();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@FXML
	void onReglasAction(ActionEvent event) throws IOException {

		TextArea reglas = new TextArea();
		reglas.setEditable(false);
		reglas.setMinSize(CENTER_WIDTH, CENTER_HEIGHT);
		reglas.setWrapText(true);

//		String pathname = LauncherApp.APP_SCORE_DIR + File.separator + "reglas" + File.separator
//				+ model.getJuegos().get(model.getJuegoSeleccionado()).getSimpleName() + ".txt";
		
		String pathname = System.getProperty("user.dir") + File.separator + "reglas" + File.separator
				+ model.getJuegos().get(model.getJuegoSeleccionado()).getSimpleName() + ".txt";
		
		File file = new File(pathname);

		if (file.exists()) {
			List<String> reglasList = Files.readAllLines(file.toPath());
			for (int i = 0; i < reglasList.size(); i++) {
				reglas.setText(reglas.getText() + reglasList.get(i) + "\n");
			}
		}

		Pane root = new Pane();
		root.getChildren().add(reglas);

		Stage stage = new Stage();
		stage.getIcons().addAll(LauncherApp.getPrimaryStage().getIcons());
		stage.setScene(new Scene(root, CENTER_WIDTH, CENTER_HEIGHT));
		stage.setTitle("Reglas");
		stage.show();
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

	public LauncherModel getModel() {
		return model;
	}
}
