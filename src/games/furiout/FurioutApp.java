package games.furiout;

import java.util.Map;

import com.almasb.fxgl.app.DSLKt;
import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.audio.Music;
import com.almasb.fxgl.entity.Entities;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.RenderLayer;
import com.almasb.fxgl.entity.components.IrremovableComponent;
import com.almasb.fxgl.entity.view.EntityView;
import com.almasb.fxgl.input.UserAction;
import com.almasb.fxgl.physics.CollisionHandler;
import com.almasb.fxgl.settings.GameSettings;

import games.furiout.component.BallComponent;
import games.furiout.component.BatComponent;
import games.furiout.component.BrickComponent;
import games.furiout.factory.BallFactory;
import games.furiout.factory.BatFactory;
import games.furiout.factory.BrickFactory;
import games.furiout.factory.EndFactory;
import games.furiout.types.FurioutTypes;
import javafx.animation.PathTransition;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.QuadCurve;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class FurioutApp extends GameApplication {

	public final int SCREEN_HEIGHT = 800;
	public final int SCREEN_WIDTH = 600;
	private int contador = 1;
	private Music music;
	private int puntuacion = 0;

	private BatComponent getBatControl() {
		return getGameWorld().getSingleton(FurioutTypes.BAT).get().getComponent(BatComponent.class);
	}

	private BallComponent getBallControl() {
		return getGameWorld().getSingleton(FurioutTypes.BALL).get().getComponent(BallComponent.class);
	}

	@Override
	protected void initSettings(GameSettings settings) {
		settings.setTitle("FuriOut");
		settings.setVersion("0.9.9.9");
		settings.setWidth(SCREEN_WIDTH);
		settings.setHeight(SCREEN_HEIGHT);
		settings.setAppIcon("bat.png");
	}

	@Override
	protected void initInput() {
		getInput().addAction(new UserAction("Move Left") {
			@Override
			protected void onAction() {
				getBatControl().left();
			}
		}, KeyCode.A);

		getInput().addAction(new UserAction("Move Right") {
			@Override
			protected void onAction() {
				getBatControl().right();
			}
		}, KeyCode.D);
	}

	@Override
	protected void initGameVars(Map<String, Object> vars) {
		vars.put("points", 0);
	}

	@Override
	protected void initGame() {
		initBGM();
		initLevel();
		getGameWorld().addEntityFactory(new BallFactory());
		getGameWorld().addEntityFactory(new BatFactory());
		getGameWorld().addEntityFactory(new BrickFactory());
		getGameWorld().addEntityFactory(new EndFactory());
		DSLKt.spawn("Ball", SCREEN_WIDTH / 2, SCREEN_HEIGHT - 200);
		DSLKt.spawn("Bat", SCREEN_WIDTH / 2, SCREEN_HEIGHT - 100);
		DSLKt.spawn("End", 0, SCREEN_HEIGHT);

		generaNivel(contador);

	}

	private void generaNivel(int contador2) {
		indicarNivel(contador2);
		switch (contador2) {
		case (1):
			for (int y = 0; y < 3; y++) {
				for (int x = 0; x < 8; x++) {
					DSLKt.spawn("Brick", (232 / 3) * x, (104 / 3) * y);
				}
			}
			break;
		case (2):
			getAudioPlayer().stopMusic(music);
			music = getAudioPlayer().loopBGM("BGM04.wav");
			getAudioPlayer().playMusic(music);
			for (int x = 0; x < 8; x++) {
				DSLKt.spawn("Brick", (232 / 3) * x, 0);
				DSLKt.spawn("Brick", (232 / 3) * x, (104 / 3) * 7);
			}
			for (int x = 1; x < 7; x++) {
				DSLKt.spawn("Brick", 0, (104 / 3) * x);
				DSLKt.spawn("Brick", (232 / 3) * 7, (104 / 3) * x);
			}
			for (int x = 2; x < 6; x++) {
				DSLKt.spawn("Brick", (232 / 3) * x, (104 / 3) * 2);
				DSLKt.spawn("Brick", (232 / 3) * x, (104 / 3) * 5);
			}

			break;
		case (3):
//			getAudioPlayer().stopMusic(music);
//			music = getAudioPlayer().loopBGM("BGM03.wav");
//			getAudioPlayer().playMusic(music);
			for (int x = 0; x < 8; x++) {
				DSLKt.spawn("Brick", (232 / 3) * x, 0);
			}
			for (int x = 1; x <= 2; x++) {
				DSLKt.spawn("Brick", (232 / 3) * 3, (104 / 3) * x);
				DSLKt.spawn("Brick", (232 / 3) * 4, (104 / 3) * x);
			}

			for (int x = 0; x < 8; x++) {
				DSLKt.spawn("Brick", (232 / 3) * x, (104 / 3) * 4);
			}

			for (int x = 6; x <= 7; x++) {
				DSLKt.spawn("Brick", (232 / 3) * 3, (104 / 3) * x);
				DSLKt.spawn("Brick", (232 / 3) * 4, (104 / 3) * x);
			}

			for (int x = 0; x < 8; x++) {
				DSLKt.spawn("Brick", (232 / 3) * x, (104 / 3) * 8);
			}
			break;
		default:
			getDisplay().showConfirmationBox(
					"¡Has terminado todos los niveles!.\nPuntuacion: " + puntuacion + " \n ¿Volver a jugar?", resp -> {
						if (resp) {
							getGameWorld().getEntitiesCopy().forEach(Entity::removeFromWorld);
							reset();
							startNewGame();
						} else {
							exit();
						}
					});
			break;
		}
	}

	private void initBGM() {
		getAudioPlayer().setGlobalMusicVolume(0.02);
		getAudioPlayer().setGlobalSoundVolume(0.04);
		music = getAudioPlayer().loopBGM("BGM02.wav");
	}

	private void initLevel() {
		initBackground();
	}

	private void initBackground() {
		Rectangle bg0 = new Rectangle(getWidth(), getHeight(), new LinearGradient(getWidth() / 2, 0, getWidth() / 2,
				getHeight(), false, CycleMethod.NO_CYCLE, new Stop(0.2, Color.AQUA), new Stop(0.8, Color.BLACK)));

		EntityView bg = new EntityView();
		bg.addNode(bg0);

		Entities.builder().viewFromNode(bg).renderLayer(RenderLayer.BACKGROUND).with(new IrremovableComponent())
				.buildAndAttach(getGameWorld());

		Entity screenBounds = Entities.makeScreenBounds(40);
		screenBounds.addComponent(new IrremovableComponent());

		getGameWorld().addEntity(screenBounds);
	}

	@Override
	protected void initPhysics() {
		getPhysicsWorld().setGravity(0, 0);

		getPhysicsWorld().addCollisionHandler(new CollisionHandler(FurioutTypes.BALL, FurioutTypes.BRICK) {
			@Override
			protected void onCollisionBegin(Entity ball, Entity brick) {
				brick.getComponent(BrickComponent.class).onHit();
				puntuacion += 5;
			}
		});

		getPhysicsWorld().addCollisionHandler(new CollisionHandler(FurioutTypes.BALL, FurioutTypes.END) {
			@Override
			protected void onCollisionBegin(Entity ball, Entity End) {
				getAudioPlayer().stopMusic(music);
				getAudioPlayer().playSound("gameover.wav");
				getDisplay().showConfirmationBox("Fin de la partida.\nPuntuacion: " + puntuacion + " \n ¿Try Again?",
						resp -> {
							if (resp) {
								getGameWorld().getEntitiesCopy().forEach(Entity::removeFromWorld);
								reset();
								startNewGame();
							} else {
								exit();
							}
						});

			}

		});

		getPhysicsWorld().addCollisionHandler(new CollisionHandler(FurioutTypes.BALL, FurioutTypes.BAT) {
			@Override
			protected void onCollisionBegin(Entity ball, Entity Bat) {
				if (getGameWorld().getEntities().size() == 6) {
					contador++;
					generaNivel(contador);
				}
			}

		});

	}

	private void reset() {
		contador = 1;
		puntuacion = 0;
	}

	@Override
	protected void initUI() {
		getBallControl().release();
	}

	private void indicarNivel(int contador) {
		Text text = getUIFactory().newText("Level " + contador, Color.WHITE, 48);
		getGameScene().addUINode(text);

		QuadCurve curve = new QuadCurve(-100, 0, getWidth() / 2, getHeight(), getWidth() + 100, 0);

		PathTransition transition = new PathTransition(Duration.seconds(3), curve, text);
		transition.setOnFinished(e -> {
			getGameScene().removeUINode(text);
		});
		transition.play();
	}

	public static void main(String[] args) {
		launch(args);
	}
}