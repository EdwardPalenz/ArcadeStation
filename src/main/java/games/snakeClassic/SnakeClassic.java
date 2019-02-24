package games.snakeClassic;

import java.io.File;
import java.io.PrintStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.entity.Entities;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.RenderLayer;
import com.almasb.fxgl.input.UserAction;
import com.almasb.fxgl.settings.GameSettings;

import javafx.beans.binding.Bindings;
import javafx.concurrent.Task;
import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.util.Duration;
import launcher.LauncherApp;

public class SnakeClassic extends GameApplication {

	private static final String SCORE_PATHNAME = LauncherApp.APP_SCORE_DIR + File.separator
			+ SnakeClassic.class.getSimpleName();
	private static final String SCORE_FILENAME = "puntuaciones.txt";
	private static final int SPEED = 85;
	public static final int SNAKE_SIZE = 16;
	public static final int SCREEN_SIZE = SNAKE_SIZE * 36;
	public static final int BOTTOM_Y = SCREEN_SIZE - SNAKE_SIZE * 6;
	public static final int TOP_Y = SNAKE_SIZE;
	public static final int LEFT_X = SNAKE_SIZE;
	public static final int RIGHT_X = SCREEN_SIZE - SNAKE_SIZE;

	private static int highScore = 0;

	private ArrayList<Entity> serpiente = new ArrayList<Entity>();
	private Entity Manzana;

	private Text timerUI;

	@Override
	protected void initSettings(GameSettings settings) {
		settings.setWidth(SCREEN_SIZE);
		settings.setHeight(SCREEN_SIZE);
		settings.setTitle("SNAKE CLASSIC");
		settings.setVersion("1.0");
		settings.setFullScreenAllowed(true);
		settings.setAppIcon("snakeClassic.png");
	}

	@Override
	protected void initGame() {

		getGameScene().setBackgroundColor(Color.rgb(159, 192, 149));

		Entities.builder().at(new Point2D(LEFT_X, BOTTOM_Y))
				.viewFromNodeWithBBox(new Rectangle(RIGHT_X - LEFT_X, 1, Color.BLACK)).buildAndAttach();

		Entities.builder().at(new Point2D(LEFT_X, TOP_Y - 1))
				.viewFromNodeWithBBox(new Rectangle(RIGHT_X - LEFT_X, 1, Color.BLACK)).buildAndAttach();

		Entities.builder().at(new Point2D(LEFT_X - 1, TOP_Y))
				.viewFromNodeWithBBox(new Rectangle(1, BOTTOM_Y - TOP_Y, Color.BLACK)).buildAndAttach();

		Entities.builder().at(new Point2D(RIGHT_X, TOP_Y))
				.viewFromNodeWithBBox(new Rectangle(1, BOTTOM_Y - TOP_Y, Color.BLACK)).buildAndAttach();

		Direccion dir = Direccion.DERECHA;
		generarSnake(SNAKE_SIZE * 18, SNAKE_SIZE * 18, dir);

		serpiente.get(0).setRenderLayer(RenderLayer.TOP);
		for (int i = 0; i < 3; i++) {
			addCola();
		}

		getMasterTimer().runOnceAfter(new Runnable() {
			@Override
			public void run() {
				Mover();
			}
		}, Duration.seconds(3));

		getMasterTimer().runAtInterval(new Runnable() {

			@Override
			public void run() {
				getGameState().increment("timer", -1);
			}
		}, Duration.seconds(1), 3);

		Manzana = generarManzana();

	}

	private void Mover() {
		getMasterTimer().runAtInterval(new Runnable() {

			@Override
			public void run() {

				Direccion dirAnterior = (Direccion) serpiente.get(0).getObject("direccion");
				for (int i = 0; i < serpiente.size(); i++) {
					Entity cola = serpiente.get(i);

					Direccion dir = (Direccion) cola.getObject("direccion");

					cola.setPosition(cola.getPosition().add(dir.getX(), dir.getY()));

					if (cola.getX() < LEFT_X) {
						cola.setX(RIGHT_X - SNAKE_SIZE);
					} else if (cola.getX() >= RIGHT_X) {
						cola.setX(LEFT_X);
					}
					if (cola.getY() < TOP_Y) {
						cola.setY(BOTTOM_Y - SNAKE_SIZE);
					} else if (cola.getY() >= BOTTOM_Y) {
						cola.setY(TOP_Y);
					}
					cola.setProperty("direccion", dirAnterior);
					dirAnterior = dir;
				}
			}
		}, Duration.millis(SPEED));

	}

	private void addCola() {
		Entity ultima = serpiente.get(serpiente.size() - 1);
		Direccion dir = (Direccion) ultima.getObject("direccion");
		Point2D newPosition = ultima.getPosition().subtract(new Point2D(dir.getX(), dir.getY()));

		generarSnake((int) newPosition.getX(), (int) newPosition.getY(), ultima.getObject("direccion"));
	}

	@Override
	protected void onUpdate(double tpf) {

		if (getGameState().getInt("timer") < 1) {
			timerUI.textProperty().unbind();
			timerUI.setText("GO!");
			resetTimerUi();
		}

		Entity cabeza = serpiente.get(0);
		if (cabeza.getX() == Manzana.getX() && cabeza.getY() == Manzana.getY()) {
//			Color color = (Color) Manzana.getObject("color");
			getGameState().increment("score", 1);
			addCola();

			getGameWorld().removeEntities(Manzana);
			getAudioPlayer().playSound("snake_eat.wav");
			Manzana = generarManzana();
		}

		for (int i = 1; i < serpiente.size(); i++) {
			if (cabeza.getX() == serpiente.get(i).getX() && cabeza.getY() == serpiente.get(i).getY()) {
				if (getGameState().getInt("score") > highScore)
					highScore = getGameState().getInt("score");

				getDisplay().showConfirmationBox("GAME OVER\nPlay again?", resp -> {
					if (resp) {
						getGameWorld().getEntitiesCopy().forEach(Entity::removeFromWorld);
						serpiente.clear();
						startNewGame();

					} else {
						getDisplay().showInputBox("Indroduce tu nombre. (HighScore: " + highScore + ")", name -> {
							if (name != null && !name.isEmpty()) {
								GuardarPuntuacion(name);

							}

							exit();
						});
					}
				});
			}
		}

	}

	private void GuardarPuntuacion(String name) {
		Task<Void> taskGuardar = new Task<Void>() {

			@Override
			protected Void call() throws Exception {

				File ficheroPuntuacion = new File(SCORE_PATHNAME);

				if (!ficheroPuntuacion.exists())
					ficheroPuntuacion.mkdir();

				ficheroPuntuacion = new File(SCORE_PATHNAME + File.separator + SCORE_FILENAME);

				List<String> s = new ArrayList<String>();

				if (!ficheroPuntuacion.exists()) {
					ficheroPuntuacion.createNewFile();
				} else {
					s = Files.readAllLines(ficheroPuntuacion.toPath(), Charset.availableCharsets().get("UTF-8"));
				}

				s.add(name + ":" + highScore);

				PrintStream fileStream = new PrintStream(ficheroPuntuacion);

				for (String string : s) {
					System.out.println(string);
					fileStream.println(string);

				}

				fileStream.close();
				return null;
			}
		};

		new Thread(taskGuardar).start();

		taskGuardar.setOnFailed(e -> {
			e.getSource().getException().printStackTrace();
		});

	}

	private void resetTimerUi() {
		getMasterTimer().runOnceAfter(new Runnable() {

			@Override
			public void run() {
				timerUI.setManaged(false);
				;
			}
		}, Duration.seconds(2));
	}

	@Override
	protected void initUI() {
		Text textScore = getUIFactory().newText("", Color.BLACK, 22);
		timerUI = getUIFactory().newText("", Color.BLACK, 22);

		textScore.setX(SNAKE_SIZE * 3);
		textScore.setY(BOTTOM_Y + SNAKE_SIZE * 4);

		textScore.textProperty().bind(Bindings.concat("SCORE: 0").concat(getGameState().intProperty("score"))
				.concat("		HIGHSCORE: 0").concat(highScore));

		timerUI.setX(RIGHT_X - (SNAKE_SIZE * 5));
		timerUI.setY(BOTTOM_Y + SNAKE_SIZE * 4);

		timerUI.textProperty().bind(Bindings.concat("").concat(getGameState().intProperty("timer")));

		getGameScene().addUINodes(textScore, timerUI);
	}

	@Override
	protected void initGameVars(Map<String, Object> vars) {
		vars.put("score", 0);
		vars.put("timer", 3);
	}

	private Entity generarManzana() {
		Point2D randomPos = null;
		while (randomPos == null) {
			randomPos = getRandomPos();
		}
//		Color color = Color.rgb((int) FXGLMath.random(0, 255), (int) FXGLMath.random(0, 255),
//				(int) FXGLMath.random(0, 255));

//		Color color = Color.RED;
		return Entities.builder().at(randomPos)
//				.with("color", color)
//				.viewFromNodeWithBBox(new Rectangle(SNAKE_SIZE, SNAKE_SIZE, color))
				.viewFromNodeWithBBox(
						new ImageView(new Image("/games/snakeClassic/comida.png", SNAKE_SIZE, SNAKE_SIZE, true, true)))
				.renderLayer(RenderLayer.BOTTOM).buildAndAttach();
	}

	private Point2D getRandomPos() {
		int x = getIntegerInRangeMultipleOf(0, BOTTOM_Y - SNAKE_SIZE, SNAKE_SIZE);
		int y = getIntegerInRangeMultipleOf(0, BOTTOM_Y - SNAKE_SIZE, SNAKE_SIZE);

		Point2D pos = new Point2D(x, y);
		if (getGameWorld().getEntitiesAt(pos).size() < 1) {
			return pos;
		} else {
			getRandomPos();
		}
		return null;

	}

	public static int getIntegerInRangeMultipleOf(int minInclusive, int maxInclusive, int multiplier) {
		int minMultiplier = minInclusive <= multiplier ? 1 : (int) Math.ceil((double) minInclusive / multiplier);
		int maxMultiplier = maxInclusive / multiplier;
		return multiplier * ThreadLocalRandom.current().nextInt(minMultiplier, maxMultiplier + 1);
	}

	@Override
	protected void initInput() {
		getInput().addAction(new UserAction("up") {

			@Override
			protected void onAction() {
				Entity cabeza = serpiente.get(0);
				if (cabeza.getObject("direccion") != Direccion.ABAJO) {
					getMasterTimer().runOnceAfter(new Runnable() {

						@Override
						public void run() {
							Direccion dir = Direccion.ARRIBA;
							cabeza.setProperty("direccion", dir);
						}
					}, Duration.millis(SPEED));

				}
			}

		}, KeyCode.UP);

		getInput().addAction(new UserAction("down") {
			@Override
			protected void onAction() {
				Entity cabeza = serpiente.get(0);
				if (cabeza.getObject("direccion") != Direccion.ARRIBA) {
					getMasterTimer().runOnceAfter(new Runnable() {

						@Override
						public void run() {
							Direccion dir = Direccion.ABAJO;
							cabeza.setProperty("direccion", dir);
						}
					}, Duration.millis(SPEED));

				}
			}

		}, KeyCode.DOWN);

		getInput().addAction(new UserAction("right") {
			@Override
			protected void onAction() {
				Entity cabeza = serpiente.get(0);
				if (cabeza.getObject("direccion") != Direccion.IZQUIERDA) {
					getMasterTimer().runOnceAfter(new Runnable() {

						@Override
						public void run() {
							Direccion dir = Direccion.DERECHA;
							cabeza.setProperty("direccion", dir);
						}
					}, Duration.millis(SPEED));

				}
			}

		}, KeyCode.RIGHT);

		getInput().addAction(new UserAction("left") {
			@Override
			protected void onAction() {
				Entity cabeza = serpiente.get(0);
				if (cabeza.getObject("direccion") != Direccion.DERECHA) {
					getMasterTimer().runOnceAfter(new Runnable() {

						@Override
						public void run() {
							Direccion dir = Direccion.IZQUIERDA;
							cabeza.setProperty("direccion", dir);
						}
					}, Duration.millis(SPEED));

				}
			}

		}, KeyCode.LEFT);
	}

	private void generarSnake(int x, int y, Direccion dir) {

		System.out.println(x + " " + y);
		Entity snakePart = Entities.builder().at(new Point2D(x, y)).with("direccion", dir)
				.viewFromNodeWithBBox(new ImageView(
						new Image("/games/snakeClassic/snakeClassic.png", SNAKE_SIZE, SNAKE_SIZE, false, false)))
				.buildAndAttach();
		serpiente.add(snakePart);
	}

	public static void main(String[] args) {
		launch(args);
	}
}