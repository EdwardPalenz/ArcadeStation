package games.snakeevolution;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;

import com.almasb.fxgl.app.DSLKt;
import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.audio.Music;
import com.almasb.fxgl.core.math.FXGLMath;
import com.almasb.fxgl.entity.Entities;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.RenderLayer;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.components.CollidableComponent;
import com.almasb.fxgl.entity.view.EntityView;
import com.almasb.fxgl.input.UserAction;
import com.almasb.fxgl.settings.GameSettings;
import com.almasb.fxgl.time.TimerAction;
import com.almasb.fxgl.ui.FontType;

import javafx.beans.binding.Bindings;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.concurrent.Task;
import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;
import main.launcher.LauncherApp;

public class Snake extends GameApplication {

	private static final int SPEED = 100;
	public static final int SNAKE_SIZE = 25;
	public static final int SCREEN_SIZE = SNAKE_SIZE * 25;
	private static final String SCORE_PATHNAME = LauncherApp.APP_SCORE_DIR + File.separator + "SnakeEvolution";
	private static final String SCORE_FILENAME = "Puntuaciones.txt";
	
	Entity apple;
	Entity cabeza;
	Entity obstaculo;
	Entity blueApple;
	Entity rain;
	TimerAction movimientoSerpiente;
	boolean puedeBlueApple = false;
	Music music;
	Image iv;
	Image ground1, ground2, ground3, ground4, ground5;
	Text levelText;
	ArrayList<String> titulosList = new ArrayList<>();
	EntityView ev;
	int level = 0;
	int varNiveles = 5;
	private ArrayList<Entity> obstaculoList = new ArrayList<Entity>();
	private ArrayList<Entity> fireList = new ArrayList<Entity>();
	int cantComido = 0;
	private ArrayList<Entity> serpiente = new ArrayList<Entity>();
	
	private IntegerProperty puntuacion;
	Modelo modelo = new Modelo();
	
	File ficheroPuntuacion;
	
	@Override
	protected void initSettings(GameSettings settings) {
		settings.setWidth(SCREEN_SIZE);
		settings.setHeight(SCREEN_SIZE);
		settings.setFontGame("jungle_roar_bold.ttf");
		settings.setAppIcon("snake_head.png");
		settings.setTitle("Snake Evolution");
		settings.setVersion("2.0");
		settings.setSoundMenuPress("explosion.wav");
		settings.setSoundMenuSelect("eat.wav");
		settings.setSoundMenuBack("eat_blue_apple.wav");
		// getGameScene().setCursor("cursor.png", new Point2D(getWidth(),getHeight()));
//		settings.setProfilingEnabled(true);
	}

	@Override
	protected void initUI() {
		tituloNiveles();
		Font fontUI = getUIFactory().newFont(FontType.GAME, 30.0);
		Text textPunt = getUIFactory().newText("",Color.RED, 22);
		textPunt.setX(15);
		textPunt.setY(35);
		textPunt.setFont(fontUI);
		textPunt.textProperty().bind(Bindings.concat("Puntuacion: ").concat(puntuacion));
		getGameScene().addUINode(textPunt);
	}

	private void tituloNiveles() {
		Font fontUI = getUIFactory().newFont(FontType.GAME, 40.0);
		levelText = new Text("Nivel " + level + "\n" + titulosList.get(level - 1));
		levelText.setTranslateX(20);
		levelText.setTranslateY(getHeight() - 70);
		levelText.setFill(Color.RED);
		levelText.setFont(fontUI);
		getGameScene().addUINode(levelText);
		DSLKt.fadeInOut(levelText, Duration.seconds(3)).startInPlayState();
	}

	@Override
	protected void initGame() {
		
		bindeos();
		
		getAssetLoader().cache();
		generarTitulos();
		music = getAudioPlayer().loopBGM("world1_music.mp3");
		getAudioPlayer().setGlobalMusicVolume(0.2);
		getAudioPlayer().setGlobalSoundVolume(0.1);

		cambiarNivel(0);

		Direccion dir = Direccion.DERECHA;
		generarCabeza(getWidth() / 2, getHeight() / 2, dir, getClass().getResource("/assets/textures/snake_head.png"));
//		velocityCabeza = new Point2D(Direccion.DERECHA.getX(), Direccion.DERECHA.getY());

		getGameWorld().addEntityFactory(new BlueApple());
		getGameWorld().addEntityFactory(new ParticleExplosion());
		getGameWorld().addEntityFactory(new ParticleRain());
		getGameWorld().addEntityFactory(new Apple());
		getGameWorld().addEntityFactory(new Obstaculo());

		spawnearApple();

		movimientoSerpiente = getMasterTimer().runAtInterval(new Runnable() {

			@Override
			public void run() {

				Direccion dirAnterior = (Direccion) serpiente.get(0).getObject("direccion");
				for (int i = 0; i < serpiente.size(); i++) {
					Entity cola = serpiente.get(i);

					Direccion dir = (Direccion) cola.getObject("direccion");

					cola.setPosition(cola.getPosition().add(dir.getX(), dir.getY()));

					if (cola.getX() >= getWidth()) {
						cola.setX(0);
					} else if (cola.getX() < 0) {
						cola.setX(getWidth() - SNAKE_SIZE);
					}

					if (cola.getY() >= getHeight()) {
						cola.setY(0);
					} else if (cola.getY() < 0) {
						cola.setY(getHeight() - SNAKE_SIZE);
					}

					cola.setProperty("direccion", dirAnterior);
					dirAnterior = dir;
				}
			}
		}, Duration.millis(SPEED));

	}

	private void bindeos() {
		puntuacion = new SimpleIntegerProperty(this, "puntuacion");

		modelo.puntuacionProperty().bind(puntuacion);
		
	}

	private void generarTitulos() {
		titulosList.add("tierra de las Manzanas");
		titulosList.add("mas alla de la luz");
		titulosList.add("reino del hielo");
		titulosList.add("desierto sin fin");
		titulosList.add("crater del apocalipsis");
	}

	private void spawnearRain() {
		DSLKt.spawn("ParticleRain");
		rain = ParticleRain.rain;
		rain.setRenderLayer(RenderLayer.TOP);
	}

	private void spawnearApple() {
		DSLKt.spawn("Apple");
		apple = Apple.getFruta();
		apple.setRenderLayer(RenderLayer.TOP);
	}

	private void spawnearBlueApple() {
		DSLKt.spawn("BlueApple");
		blueApple = BlueApple.getBlueApple();
		blueApple.setRenderLayer(RenderLayer.TOP);
	}

	@Override
	protected void initInput() {
		getInput().addAction(new UserAction("up") {
			@Override
			protected void onAction() {
				cabeza = serpiente.get(0);
				if (cabeza.getObject("direccion") != Direccion.ABAJO) {
					getMasterTimer().runOnceAfter(new Runnable() {

						@Override
						public void run() {
							serpiente.get(0).setRotation(180);
							Direccion dir = Direccion.ARRIBA;
							cabeza.setProperty("direccion", dir);
						}
					}, Duration.millis(SPEED));

//					velocityCabeza = new Point2D(dir.getX(), dir.getY());

				}
			}

		}, KeyCode.UP);

		getInput().addAction(new UserAction("down") {
			@Override
			protected void onAction() {
				cabeza = serpiente.get(0);
				if (cabeza.getObject("direccion") != Direccion.ARRIBA) {
					getMasterTimer().runOnceAfter(new Runnable() {
						@Override
						public void run() {
							serpiente.get(0).setRotation(0);
							Direccion dir = Direccion.ABAJO;
							cabeza.setProperty("direccion", dir);
//							velocityCabeza = new Point2D(dir.getX(), dir.getY());
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
							serpiente.get(0).setRotation(-90);
							Direccion dir = Direccion.DERECHA;
							cabeza.setProperty("direccion", dir);
//							velocityCabeza = new Point2D(dir.getX(), dir.getY());
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
							serpiente.get(0).setRotation(90);
							Direccion dir = Direccion.IZQUIERDA;
							cabeza.setProperty("direccion", dir);
//							velocityCabeza = new Point2D(dir.getX(), dir.getY());
						}
					}, Duration.millis(SPEED));
				}
			}

		}, KeyCode.LEFT);
	}

	@Override
	protected void onUpdate(double tpf) {
		if (!movimientoSerpiente.isExpired()) {
			if (cantComido == varNiveles * 2) {
				varNiveles = cantComido;
				getAudioPlayer().stopMusic(music);
				cambiarNivel(2);
			}

			if (serpiente.get(0).isColliding(apple)) {
				puntuacion.set(puntuacion.get()+5);
				cantComido++;
				apple.removeFromWorld();
				getAudioPlayer().playSound("eat.wav");
				generarCola();
				spawnearApple();
			}

			if (puedeBlueApple) {
				if (serpiente.get(0).isColliding(blueApple)) {
					blueApple.removeFromWorld();
					getAudioPlayer().playSound("eat_blue_apple.wav");
					puedeBlueApple = false;
					blueApple = null;
					destruirCola();
				}
			}

			for (int i = 1; i < serpiente.size(); i++) {
				if (cabeza.getX() == serpiente.get(i).getX() && cabeza.getY() == serpiente.get(i).getY()) {
					getAudioPlayer().stopMusic(music);
					finalizarJuego();
					break;
				}
			}

			// GENERAR BOMBA
			if (level >= 2) {
				if (cantComido >= 20 && FXGLMath.randomBoolean(0.05) && obstaculoList.size() < 2) {

					int x = FXGLMath.random(30, getWidth() - 30);
					int y = FXGLMath.random(30, getHeight() - 30);
					boolean puede = true;

					for (int i = 0; i < serpiente.size(); i++) {
						Point2D subtract = serpiente.get(i).getPosition().subtract(x, y);
						if (Math.sqrt(Math.pow(subtract.getX(), 2) + Math.pow(subtract.getY(), 2)) < SNAKE_SIZE * 8) {
							puede = false;
						}
					}

					if (puede) {
						spawnearObstaculo(x, y);
						getMasterTimer().runOnceAfter(new Runnable() {
							@Override
							public void run() {
								if (!obstaculoList.isEmpty()) {
									spawnearExplosion();
									getGameWorld().removeEntity(obstaculoList.get(0));
									obstaculoList.remove(0);
								}
							}
						}, Duration.seconds(7));
					}
				}
			}
			
			//CHOQUE CONTRA EL OBSTACULO
			if (obstaculo != null && !obstaculoList.isEmpty()) {
				for (int i = 0; i < obstaculoList.size(); i++) {
					if (cabeza.isColliding(obstaculoList.get(i))) {
						finalizarJuego();
						break;
					}
				}
			}
			
			//GENERAR ALEATORIAMENTE MANZANAS AZULES
			if (level != 5) {
				if (blueApple == null && cantComido >= 10 && FXGLMath.randomBoolean(0.001)) {
					if (serpiente.size() - 1 >= 10) {
						spawnearBlueApple();
						puedeBlueApple = true;
						getMasterTimer().runOnceAfter(new Runnable() {
							@Override
							public void run() {
								if (blueApple != null) {
									blueApple.removeFromWorld();
									blueApple = null;
								}
								puedeBlueApple = false;
							}
						}, Duration.seconds(4));
					}
				}
			}
			
		}
	}

	//CAMBIO DE NIVEL
	private void cambiarNivel(int duracion) {
		level++;
		if (level == 2) {
			spawnearRain();
		}
		music = getAudioPlayer().loopBGM("world" + level + "_music.mp3");
		iv = new Image("/assets/textures/ground" + level + ".jpg");
		ev = new EntityView(new ImageView(iv));
		getGameScene().addGameView(ev);
//		DSLKt.fadeIn(nivelText, Duration.seconds(duracion));
		DSLKt.fadeIn(ev, Duration.seconds(duracion)).startInPlayState();
		tituloNiveles();
	}

	//DESTRUIR COLA
	private void destruirCola() {
		int j = serpiente.size() - 1;
		for (int i = 5; i >= 0; i--) {
			serpiente.get(j).removeFromWorld();
			serpiente.remove(j);
			j--;
		}
	}

	//FINALIZAR JUEGO
	private void finalizarJuego() {
		movimientoSerpiente.expire();
		getAudioPlayer().stopMusic(music);
		getAudioPlayer().playSound("snake_death.wav");
		getMasterTimer().runAtInterval(new Runnable() {
			@Override
			public void run() {
				if (!serpiente.isEmpty()) {
					DSLKt.spawn("ParticleExplosion", serpiente.get(0).getPosition());
					getGameWorld().removeEntity(serpiente.get(0));
					serpiente.remove(0);
				}
			}
		}, Duration.millis(70), serpiente.size()+1);

		getMasterTimer().runOnceAfter(new Runnable() {
			@Override
			public void run() {
				getDisplay().showConfirmationBox("Fin del juego\n¿Volver a empezar?", resp -> {
					if (resp) {
						getGameWorld().getEntitiesCopy().forEach(Entity::removeFromWorld);
						resetear();
						startNewGame();
					} else {
						getDisplay().showInputBox(
								"Indroduce tu nombre", nombre -> {
									if (nombre != null) {
										try {
										ficheroPuntuacion = new File(SCORE_PATHNAME);

										if (!ficheroPuntuacion.exists())
											ficheroPuntuacion.mkdir();

										ficheroPuntuacion = new File(SCORE_PATHNAME + File.separator + SCORE_FILENAME);

										if (!ficheroPuntuacion.exists())
											ficheroPuntuacion.createNewFile();
										
										Task<Void> taskGuardar = new Task<Void>() {
											@Override
											protected Void call() throws Exception {
												Files.write(ficheroPuntuacion.toPath(), (nombre + " " + modelo.getPuntuacion() + "\n").getBytes(),
														StandardOpenOption.APPEND);
												return null;
											}
										};
										
										new Thread(taskGuardar).start();

										taskGuardar.setOnFailed(e -> {
											e.getSource().getException().printStackTrace();
										});
										} catch (IOException e) {
											e.printStackTrace();
										}
									}
									exit();
								});
					}
				});
			}
		}, Duration.millis(70 * (serpiente.size() * 2)));
	}

	//SPAWNEAR EXPLOSION
	private void spawnearExplosion() {
		DSLKt.spawn("ParticleExplosion", obstaculoList.get(0).getPosition());
	}

	//RESETEAR
	private void resetear() {
		puedeBlueApple = false;
		serpiente.clear();
		if (blueApple != null) {
			blueApple.removeFromWorld();
			blueApple = null;
		}
		if (obstaculo != null) {
			obstaculo.removeFromWorld();
		}
		fireList.clear();
		obstaculoList.clear();
		cantComido = 0;
		varNiveles = 5;
		level = 0;
	}

	//GENERAR EN EL SNAKE
	private void generarSnake(int x, int y, Direccion dir, URL url) {
		Entity snakePart = Entities.builder().at(new Point2D(x, y))
//				.viewFromNodeWithBBox(new Rectangle(SNAKE_SIZE, SNAKE_SIZE, Color.BLACK))
				.with("direccion", dir)
				.viewFromNodeWithBBox(new ImageView(new Image(url.toString(), SNAKE_SIZE, SNAKE_SIZE, true, true)))
				.buildAndAttach();
		snakePart.setRenderLayer(RenderLayer.TOP);
		serpiente.add(snakePart);
	}

	//GENERAR CABEZA
	private void generarCabeza(int x, int y, Direccion dir, URL url) {
		cabeza = Entities.builder().at(new Point2D(x, y))
//				.viewFromNodeWithBBox(new Rectangle(SNAKE_SIZE, SNAKE_SIZE, Color.BLACK))
				.with("direccion", dir).with(new CollidableComponent(true))
				.viewFromNodeWithBBox(new ImageView(new Image(url.toString(), SNAKE_SIZE, SNAKE_SIZE, true, true)))
				.buildAndAttach();
		cabeza.setRenderLayer(RenderLayer.TOP);
		serpiente.add(cabeza);
		serpiente.get(0).setRotation(-90);
	}

	//GENERAR COLA
	private void generarCola() {

		Entity ultima = serpiente.get(serpiente.size() - 1);
		Direccion dir = (Direccion) ultima.getObject("direccion");
		Point2D newPosition = ultima.getPosition().subtract(new Point2D(dir.getX(), dir.getY()));
		generarSnake((int) newPosition.getX(), (int) newPosition.getY(), ultima.getObject("direccion"),
				getClass().getResource("/assets/textures/tail.png"));
	}

	//SPAWNEAR OBSTACULO
	private void spawnearObstaculo(int x, int y) {
		ImageView imageView = new ImageView(new Image("/assets/textures/" + 0 + ".gif", 60.0, 60.0, true, false));
		SpawnData sd = new SpawnData(x, y);
		sd.put("node", imageView);
		DSLKt.spawn("Obstaculo", sd);

		obstaculo = Obstaculo.getObstaculo();

		getMasterTimer().runAtInterval(new Runnable() {

			private int frame = 0;
			private Entity obs = obstaculo;
			private boolean invert = false;

			@Override
			public void run() {
				try {

					if (frame == 10) {
						invert = true;
					} else if (frame == 0) {
						invert = false;
					}

					if (!invert) {
						frame++;
					} else {
						frame--;
					}

					ImageView im = (ImageView) obs.getView().getNodes().get(0);
					im.setImage(new Image("/assets/textures/" + frame + ".gif", 60.0, 60.0, true, false));

				} catch (Exception e) {
					// TODO: handle exception
					try {
						exit();
					} catch (Throwable e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}
		}, Duration.millis(80));

		obstaculo.setRenderLayer(RenderLayer.TOP);
		obstaculoList.add(obstaculo);
	}

	//MAIN
	public static void main(String[] args) {
		launch(args);
	}
}
