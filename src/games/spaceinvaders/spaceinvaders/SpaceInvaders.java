package games.spaceinvaders.spaceinvaders;

import java.io.IOException;

import com.almasb.fxgl.app.DSLKt;
import com.almasb.fxgl.app.FXGL;
import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.audio.Music;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.input.Input;
import com.almasb.fxgl.input.UserAction;
import com.almasb.fxgl.physics.CollisionHandler;
import com.almasb.fxgl.physics.PhysicsWorld;
import com.almasb.fxgl.settings.GameSettings;

import games.spaceinvaders.gameFactories.ParticleExplosion;
import games.spaceinvaders.gameControllers.PlayerControll;
import games.spaceinvaders.gameControllers.WallControll;
import games.spaceinvaders.gameFactories.BackgroundFactory;
import games.spaceinvaders.gameFactories.EndFactory;
import games.spaceinvaders.gameFactories.EnemyBulletFactory;
import games.spaceinvaders.gameFactories.EnemyFactory;
import games.spaceinvaders.gameFactories.PlayerBulletFactory;
import games.spaceinvaders.gameFactories.PlayerFactory;
import games.spaceinvaders.gameFactories.WallFactory;
import games.spaceinvaders.gameUi.GameUi;
import games.spaceinvaders.spritesTypes.SpritesTypes;
import javafx.concurrent.Task;
import javafx.scene.input.KeyCode;

public class SpaceInvaders extends GameApplication {

	private Entity player;
	private PlayerControll playerControll;
	private WallControll wallControll;

	private int numEnemies = 0;

	private GameUi gameUi;
	private Music music;

	@Override
	protected void initSettings(GameSettings settings) {
		settings.setTitle("Sapce Invaders");
		settings.setVersion("0.0.1");
		settings.setWidth(600);
		settings.setHeight(800);
		settings.setIntroEnabled(false);
		settings.setMenuEnabled(false);
		settings.setAppIcon("invaders.png");

	}

	@Override
	protected void initGame() {

		getGameWorld().addEntityFactory(new WallFactory());
		getGameWorld().addEntityFactory(new EnemyBulletFactory());
		getGameWorld().addEntityFactory(new PlayerFactory());
		getGameWorld().addEntityFactory(new BackgroundFactory());
		getGameWorld().addEntityFactory(new ParticleExplosion());
		getGameWorld().addEntityFactory(new EndFactory());

		// DSLKt.spawn("Boss",300,100);

		gameUi = new GameUi(getGameScene());
		for (int i = 0; i < 3; i++) {
			gameUi.addLife();
		}

		DSLKt.spawn("End", 0, 801);
		gameUi.addScore();

		music = getAudioPlayer().loopBGM("bg.mp3");
		getAudioPlayer().setGlobalMusicVolume(0.5);

		getGameWorld().addEntityFactory(new EnemyFactory());
		for (int y = 0; y < 5; y++) {
			for (int x = 2; x < 8; x++) {
				DSLKt.spawn("Enemy", x * (40 + 35), y * (40 + 20));
				numEnemies++;

			}
		}
		for (int x = 1; x < 10; x++) {
			DSLKt.spawn("Wall", x * 55, 690);
			x++;
		}
		getGameWorld().addEntityFactory(new PlayerBulletFactory());

		spawnPlayer();
		DSLKt.spawn("Background", 0, 0);

	}

	private void spawnPlayer() {
		player = DSLKt.spawn("Player", getWidth() / 2 - 20, getHeight() - 40);
		playerControll = player.getComponent(PlayerControll.class);

	}

	@Override
	protected void initInput() {
		Input input = getInput();

		input.addAction(new UserAction("Move Left") {
			@Override
			protected void onAction() {
				playerControll.left();
			}
		}, KeyCode.A);

		input.addAction(new UserAction("Move Right") {
			@Override
			protected void onAction() {
				playerControll.right();
			}
		}, KeyCode.D);

		input.addAction(new UserAction("Shoot") {
			@Override
			protected void onAction() {
				playerControll.shoot();

			}
		}, KeyCode.SPACE);

	}

	@Override
	protected void initPhysics() {

		PhysicsWorld physicsWorld = getPhysicsWorld();

		physicsWorld.addCollisionHandler(new CollisionHandler(SpritesTypes.ENEMY, SpritesTypes.PLAYER_BULLET) {
			@Override
			protected void onCollisionBegin(Entity bullet, Entity enemy) {
				bullet.removeFromWorld();
				DSLKt.spawn("ParticleExplosion", enemy.getCenter());
				DSLKt.play("Explosion.wav");
				enemy.removeFromWorld();
				gameUi.addPoints();
				numEnemies--;
				if (numEnemies == 0) {
					victory();
				}

			}
		});

		physicsWorld.addCollisionHandler(new CollisionHandler(SpritesTypes.ENEMY, FXGL.getAppHeight()) {
			@Override
			protected void onCollisionBegin(Entity enemy, Entity e) {
				gameOver();
				numEnemies = 0;
			}
		});

		physicsWorld.addCollisionHandler(new CollisionHandler(SpritesTypes.PLAYER, SpritesTypes.ENEMY_BULLET) {
			@Override
			protected void onCollisionBegin(Entity player, Entity enemyBullet) {
				enemyBullet.removeFromWorld();
				// DSLKt.spawn("ParticleExplosion",player.getX(),player.getY());
				gameUi.removeLife();
				getGameScene().getViewport().shake(14, 0.35);
				playerControll = player.getComponent(PlayerControll.class);
				playerControll.playerHit();
				if (gameUi.getLives().isEmpty()) {
					player.removeFromWorld();
					gameOver();
				}

			}
		});

		physicsWorld.addCollisionHandler(new CollisionHandler(SpritesTypes.WALL, SpritesTypes.ENEMY_BULLET) {
			@Override
			protected void onCollisionBegin(Entity wall, Entity enemyBullet) {
				enemyBullet.removeFromWorld();
				wallControll = wall.getComponent(WallControll.class);
				wallControll.wallHit();
				gameUi.removePoints();
				DSLKt.play("wallImpact.wav");

			}
		});

		physicsWorld.addCollisionHandler(new CollisionHandler(SpritesTypes.END, SpritesTypes.ENEMY) {
			@Override
			protected void onCollisionBegin(Entity end, Entity enemy) {
				enemy.removeFromWorld();
				numEnemies--;
				gameUi.removeLife();

				if (gameUi.getLives().isEmpty()) {
					gameOver();
				} else if (numEnemies == 0) {
					victory();
				}
			}
		});

		physicsWorld.addCollisionHandler(new CollisionHandler(SpritesTypes.ENEMY, SpritesTypes.PLAYER) {
			@Override
			protected void onCollisionBegin(Entity enemy, Entity player) {
				gameOver();
			}
		});

		physicsWorld.addCollisionHandler(new CollisionHandler(SpritesTypes.ENEMY, SpritesTypes.WALL) {
			@Override
			protected void onCollisionBegin(Entity enemy, Entity wall) {
				wall.removeFromWorld();
				DSLKt.spawn("ParticleExplosion", enemy.getCenter());
				DSLKt.play("Explosion.wav");
				enemy.removeFromWorld();
			}
		});

	}

	private void gameOver() {
		getAudioPlayer().stopMusic(music);
		getDisplay().showConfirmationBox("Has perdido \n ¿Intentarlo otra vez?", yes -> {
			if (yes) {
				getGameWorld().getEntitiesCopy().forEach(Entity::removeFromWorld);
				startNewGame();
				


			} else {
				FXGL.getDisplay().showInputBox("Introduzca su nombre: ", nombre -> {
					Task<Void> guardarTask = new Task<Void>() {

						@Override
						protected Void call() throws Exception {
							gameUi.savePoints(nombre);
							return null;
						}
					};
					new Thread(guardarTask).start();
					exit();
				});

			}
		});
	}

	private void victory() {
		getAudioPlayer().stopMusic(music);
		getDisplay().showConfirmationBox("Victoria\n ¿Volver a jugar?", yes -> {
			if (yes) {
				getGameWorld().getEntitiesCopy().forEach(Entity::removeFromWorld);
					startNewGame();
				

			} else {
				FXGL.getDisplay().showInputBox("Introduzca su nombre: ", nombre -> {
					try {
						gameUi.savePoints(nombre);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					exit();
				});

			}
		});
	}

	public static void main(String[] args) {
		launch(args);

	}

}
