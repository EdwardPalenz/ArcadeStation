package games.pong;

import com.almasb.fxgl.app.FXGL;
import com.almasb.fxgl.core.math.Vec2;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.physics.PhysicsComponent;

public class PaddleComponent extends Component {

	private static final float BOUNCE_FACTOR = 1.1f;
	private static final float SPEED_DECAY = 0.66f;

	private PhysicsComponent physics;
	private float speed = 0;

	private Vec2 velocity = new Vec2();

	@Override
	public void onUpdate(double tpf) {
		speed = 600 * (float) tpf;

		velocity.mulLocal(SPEED_DECAY);

		if (entity.getX() < 0) {
			velocity.set(BOUNCE_FACTOR * (float) -entity.getX(), 0);
		} else if (entity.getRightX() > FXGL.getApp().getWidth()) {
			velocity.set(BOUNCE_FACTOR * (float) -(entity.getRightX() - FXGL.getApp().getWidth()), 0);
		}

		physics.setBodyLinearVelocity(velocity);
	}

	public void up() {
		velocity.set(0, -speed);
	}

	public void down() {
		velocity.set(0, speed);
	}

}
