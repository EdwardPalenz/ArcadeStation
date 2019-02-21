package games.pong;

import com.almasb.fxgl.animation.Interpolators;
import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.settings.GameSettings;
import com.almasb.fxgl.time.TimerAction;
import com.almasb.fxgl.entity.Entities;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.RenderLayer;
import com.almasb.fxgl.input.UserAction;
import com.almasb.fxgl.particle.ParticleComponent;
import com.almasb.fxgl.particle.ParticleEmitter;
import com.almasb.fxgl.particle.ParticleEmitters;
import com.almasb.fxgl.physics.PhysicsComponent;

import javafx.beans.binding.Bindings;
import javafx.geometry.Point2D;
import javafx.scene.effect.BlendMode;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;

import java.util.Map;

public class PongGame extends GameApplication {

	private static final int PADDLE_WIDTH = 25;
	private static final int PADDLE_HEIGHT = 100;
	private static final int BALL_SIZE = 20;
	private static final double PADDLE_SPEED = 6.2;
	private static final double BALL_SPEED = 6;
	private static final double Incremento = 1.007;

	private Entity paddle1;
	private Entity paddle1Up;
	private Entity paddle1Down;

	private Entity paddle2;
	private Entity paddle2Up;
	private Entity paddle2Down;

	private Entity ball;
	private Entity limit1;
	private Entity limit2;

	private Point2D velocity;

	private int timer = 0;
	TimerAction timerAction;
	TimerAction tA;

	@Override
	protected void initSettings(GameSettings settings) {
		settings.setTitle("Pong");
		settings.setHeight(550);
		settings.setWidth(700);
		settings.setIntroEnabled(false);

//		Para la derecha, tiene que ser:  la X del objeto mas su tamaño.
//		Porque la X se calcula desde la izq del objeto

	}

	@Override
	protected void initGame() {
		getGameScene().setBackgroundColor(Color.rgb(175, 217, 164));

		limit1 = spawnLimit(10, Color.TRANSPARENT);
		limit2 = spawnLimit(getWidth() - 10, Color.TRANSPARENT);

		paddle1 = spawnBat(limit1.getRightX(), getHeight() / 2 - PADDLE_HEIGHT / 2, Color.rgb(57, 176, 223));
		paddle2 = spawnBat(limit2.getX() - PADDLE_WIDTH, getHeight() / 2 - PADDLE_HEIGHT / 2, Color.rgb(223, 68, 57));

		spawnPaddleBorders();

		ball = spawnBall(getWidth() / 2 - BALL_SIZE / 2, getHeight() / 2 - BALL_SIZE / 2);

		initParticles();

		paddle1.addComponent(new PhysicsComponent());
		paddle2.addComponent(new PhysicsComponent());
		ball.addComponent(new PhysicsComponent());

	}

	private void initParticles() {
		ParticleEmitter pEmiter = ParticleEmitters.newFireEmitter();
		pEmiter.setStartColor(Color.rgb(151, 52, 255));
		pEmiter.setEndColor(Color.GHOSTWHITE);
		pEmiter.setBlendMode(BlendMode.SRC_OVER);
		pEmiter.setEmissionRate(300);
		pEmiter.setSize(5, 5);

		ball.addComponent(new ParticleComponent(pEmiter));
		getGameWorld().addEntities(ball);

	}

	@Override
	protected void onUpdate(double tpf) {
		getMasterTimer().runOnceAfter(new Runnable() {

			@Override
			public void run() {

				velocity = ball.getObject("velocity");
//				double vel = Math.sqrt(Math.pow(velocity.getX(), 2) + Math.pow(velocity.getY(), 2));
				double vel = Math.abs(((Point2D) (ball.getObject("velocity"))).getX());
				getGameState().setValue("velocity", String.format("%.2f", vel));
				ball.translate(velocity);
			}
		}, Duration.seconds(2));

		if (ball.isColliding(limit1)) {
			getGameState().increment("score2", +1);
			resetBall();
		} else if (ball.isColliding(limit2)) {
			getGameState().increment("score1", +1);
			resetBall();

		} else if (ball.getX() <= limit1.getRightX() + paddle1.getWidth() && ball.getY() < paddle1.getBottomY()
				&& ball.getBottomY() > paddle1.getY()) {
			ball.setPosition(new Point2D(paddle1.getRightX() + 1, ball.getY()));

			if (velocity.getX() < 9)

				ball.setProperty("velocity",
						new Point2D(-(velocity.getX() * Incremento), velocity.getY() * Incremento));
			if (getGameState().getDouble("Pvelocity") < 9)
				getGameState().increment("Pvelocity", 0.05);

//			getGameScene().getViewport().shake(1, 1);

			animatePaddle(paddle1, 1);

		} else if (ball.getX() + ball.getWidth() >= limit2.getX() - paddle2.getWidth()
				&& ball.getY() < paddle2.getBottomY() && ball.getBottomY() > paddle2.getY()) {

			ball.setPosition(new Point2D(paddle2.getX() - ball.getWidth() - 1, ball.getY()));
			if (velocity.getX() < 9)
				ball.setProperty("velocity", new Point2D(-velocity.getX() * Incremento, velocity.getY() * Incremento));
			if (getGameState().getDouble("Pvelocity") < 9)
				getGameState().increment("Pvelocity", 0.05);

			animatePaddle(paddle2, -1);
		} else if (ball.isColliding(paddle1Up) || ball.isColliding(paddle2Up)) {

			ball.setPosition(new Point2D(ball.getX(), paddle1Up.getY() + 1));
			ball.setProperty("velocity", new Point2D(velocity.getX(), -velocity.getY()));
		} else if (ball.isColliding(paddle1Down) || ball.isColliding(paddle2Down)) {

			ball.setPosition(new Point2D(ball.getX(), paddle1Up.getY() - 1));
			ball.setProperty("velocity", new Point2D(velocity.getX(), -velocity.getY()));
		} else if (ball.getX() <= 0) {
			getGameState().increment("score2", +1);
			resetBall();
		} else if (ball.getRightX() >= getWidth()) {
			getGameState().increment("score1", +1);
			resetBall();
		} else if (ball.getY() <= 0) {
			ball.setY(0);
			ball.setProperty("velocity", new Point2D(velocity.getX(), -velocity.getY()));
		} else if (ball.getBottomY() >= getHeight() - 1) {
			ball.setY(getHeight());
			ball.setProperty("velocity", new Point2D(velocity.getX(), -velocity.getY()));
		}

	}

	private void animatePaddle(Entity paddle, int dir) {
		int angleFrom = -15 * dir;
		if (ball.getY() > (paddle.getBottomY() - (PADDLE_HEIGHT / 2)))
			angleFrom = 15 * dir;

		getAudioPlayer().playSound("ball_hit.wav");
		Entities.animationBuilder().autoReverse(true).duration(Duration.seconds(0.5))
				.interpolator(Interpolators.BOUNCE.EASE_OUT()).rotate(paddle).rotateFrom(angleFrom).rotateTo(0)
				.buildAndPlay();

	}

	private void resetBall() {
		getAudioPlayer().playSound("ball_explosion.wav");
		getGameScene().getViewport().shake(4, 2);
		getGameState().setValue("Pvelocity", PADDLE_SPEED);
		generateExplosion();
		ball.setPosition(getWidth() / 2 - BALL_SIZE / 2, getHeight() / 2 - BALL_SIZE / 2);
		ball.setProperty("velocity", new Point2D(0, 0));

		timer = 3;

		timerAction = getMasterTimer().runAtInterval(new Runnable() {

			@Override
			public void run() {
				if (timer > 0) {
					getGameState().stringProperty("timer").set("" + (timer--));
				} else {
					getGameState().stringProperty("timer").set("¡YA!");
				}
			}

		}, Duration.millis(900));

		getMasterTimer().runOnceAfter(new Runnable() {

			@Override
			public void run() {
				timerAction.pause();
				getGameState().stringProperty("timer").set("");
				int direccionX = -1;
				if (Math.random() > 0.5) {
					direccionX = 1;
				}

				int direccionY = -1;
				if (Math.random() > 0.5) {
					direccionY = 1;
				}

				ball.setProperty("velocity", new Point2D(direccionX * BALL_SPEED, direccionY * BALL_SPEED));

			}
		}, Duration.seconds(4));

	}

	private void generateExplosion() {
		ParticleEmitter offLimitEmiter = ParticleEmitters.newExplosionEmitter(50);
		offLimitEmiter.setStartColor(Color.RED);
		offLimitEmiter.setEndColor(Color.ORANGE);
		offLimitEmiter.setBlendMode(BlendMode.SRC_OVER);
		offLimitEmiter.setMaxEmissions(1);

		Entity explosion = new Entity();
		explosion.addComponent(new ParticleComponent(offLimitEmiter));
		explosion.setPosition(new Point2D(ball.getX() - (18), ball.getY()));
		getGameWorld().addEntities(explosion);

		getMasterTimer().runOnceAfter(new Runnable() {

			@Override
			public void run() {
				getGameWorld().getEntities().remove(explosion);
			}
		}, Duration.millis(300));

	}

	@Override
	protected void initInput() {
		getInput().addAction(new UserAction("Up 1") {
			@Override
			protected void onAction() {
				if (paddle1.getY() > 0) {
					paddle1.translateY(-getGameState().getDouble("Pvelocity"));
					paddle1Up.translateY(-getGameState().getDouble("Pvelocity"));
					paddle1Down.translateY(-getGameState().getDouble("Pvelocity"));
					
				}
			}
		}, KeyCode.W);

		getInput().addAction(new UserAction("Down 1") {
			@Override
			protected void onAction() {
				if (paddle1.getY() + PADDLE_HEIGHT < getHeight()) {
					paddle1.translateY(getGameState().getDouble("Pvelocity"));
					paddle1Up.translateY(getGameState().getDouble("Pvelocity"));
					paddle1Down.translateY(getGameState().getDouble("Pvelocity"));
				}
			}
		}, KeyCode.S);

		getInput().addAction(new UserAction("Up 2") {
			@Override
			protected void onAction() {
				if (paddle2.getY() > 0) {
					paddle2.translateY(-getGameState().getDouble("Pvelocity"));
					paddle2Up.translateY(-getGameState().getDouble("Pvelocity"));
					paddle2Down.translateY(-getGameState().getDouble("Pvelocity"));
				}
			}
		}, KeyCode.UP);

		getInput().addAction(new UserAction("Down 2") {
			@Override
			protected void onAction() {
				if (paddle2.getY() + PADDLE_HEIGHT < getHeight()) {
					paddle2.translateY(getGameState().getDouble("Pvelocity"));
					paddle2Up.translateY(getGameState().getDouble("Pvelocity"));
					paddle2Down.translateY(getGameState().getDouble("Pvelocity"));
				}
			}
		}, KeyCode.DOWN);
	}

	@Override
	protected void initUI() {
		Text textScore1 = getUIFactory().newText("", Color.rgb(57, 176, 223), 22);
		Text textScore2 = getUIFactory().newText("", Color.rgb(223, 68, 57), 22);
		Text textCounter = getUIFactory().newText("", Color.rgb(73, 79, 64), 30);

		Text textWS = getUIFactory().newText("", Color.rgb(57, 176, 223), 22);

		Text textArribaAbajo = getUIFactory().newText("", Color.rgb(223, 68, 57), 22);

		Text textVelocity = getUIFactory().newText("", Color.rgb(73, 79, 64), 22);

		int tx1X = 60;
		textScore1.setTranslateX(tx1X);
		int uiY = 50;
		textScore1.setTranslateY(uiY);

		int tx2X = getWidth() - 60;
		textScore2.setTranslateX(tx2X);
		textScore2.setTranslateY(uiY);

		textCounter.setTranslateX(ball.getX() - 20);
		textCounter.setTranslateY((getHeight() / 2) - 35);

		textWS.setTranslateX(tx1X + 30);
		textWS.setTranslateY(uiY);
		textArribaAbajo.setTranslateX(tx2X - 175);
		textArribaAbajo.setTranslateY(uiY);

		textVelocity.setTranslateX((getWidth() / 2) - 50);
		textVelocity.setTranslateY(getHeight() - uiY);
		textVelocity.setTextAlignment(TextAlignment.CENTER);
//		textVelocity.set

		textScore1.textProperty().bind(getGameState().intProperty("score1").asString());
		textScore2.textProperty().bind(getGameState().intProperty("score2").asString());
		textCounter.textProperty().bind(getGameState().stringProperty("timer"));

		textVelocity.textProperty()
				.bind(Bindings.concat("Ball speed: ").concat(getGameState().stringProperty("velocity")));

		textWS.setText("Player 1: W-S");

		textArribaAbajo.setText("Player 2: ▲-▼");

		getGameScene().addUINodes(textScore1, textScore2, textCounter, textWS, textArribaAbajo, textVelocity);
	}

	@Override
	protected void initGameVars(Map<String, Object> vars) {
		vars.put("score1", 0);
		vars.put("score2", 0);
		vars.put("timer", "");
		vars.put("velocity", "");
		vars.put("Pvelocity", PADDLE_SPEED);
	}

	public static void main(String[] args) {
		launch(args);

	}

	private Entity spawnBat(double x, double y, Paint color) {
		return Entities.builder().at(x, y).viewFromNodeWithBBox(new Rectangle(PADDLE_WIDTH, PADDLE_HEIGHT, color))
				.with("dir",'u').buildAndAttach();
	}

	private Entity spawnBall(double x, double y) {
		int direccionX = -1;
		if (Math.random() > 0.5) {
			direccionX = 1;
		}

		int direccionY = -1;
		if (Math.random() > 0.5) {
			direccionY = 1;
		}

		return Entities.builder().at(x, y)
				.with("velocity", new Point2D(direccionX * BALL_SPEED, direccionY * (BALL_SPEED - 2)))
//				.viewFromNode(new Circle(BALL_SIZE,Color.TRANSPARENT))
				.build();
	}

	private Entity spawnLimit(double x, Paint color) {
		Rectangle view = new Rectangle(3, getHeight(), color);
		view.getStyleClass().add("limit");
		return Entities.builder().at(x, 0).viewFromNodeWithBBox(view).renderLayer(RenderLayer.BOTTOM).buildAndAttach();
	}

	private void spawnPaddleBorders() {

		paddle1Up = Entities.builder().viewFromNodeWithBBox(new Rectangle(PADDLE_WIDTH - 2, 20, Color.TRANSPARENT))
				.at(new Point2D((paddle1.getX() + (PADDLE_WIDTH / 2) - ((PADDLE_WIDTH - 2) / 2)), paddle1.getY() - 2))
				.buildAndAttach();

		paddle1Down = Entities.builder().viewFromNodeWithBBox(new Rectangle(PADDLE_WIDTH - 2, 20, Color.TRANSPARENT))
				.at(new Point2D((paddle1.getX() + (PADDLE_WIDTH / 2) - ((PADDLE_WIDTH - 2) / 2)),
						paddle1.getBottomY() - 20 + 2))
				.buildAndAttach();

		paddle2Up = Entities.builder().viewFromNodeWithBBox(new Rectangle(PADDLE_WIDTH - 2, 20, Color.TRANSPARENT))
				.at(new Point2D((paddle2.getX() + (PADDLE_WIDTH / 2) - ((PADDLE_WIDTH - 2) / 2)), paddle2.getY() - 2))
				.buildAndAttach();

		paddle2Down = Entities.builder().viewFromNodeWithBBox(new Rectangle(PADDLE_WIDTH - 2, 20, Color.TRANSPARENT))
				.at(new Point2D((paddle2.getX() + (PADDLE_WIDTH / 2) - ((PADDLE_WIDTH - 2) / 2)),
						paddle2.getBottomY() - 20 + 2))
				.buildAndAttach();

	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "Pong";
	}
}
