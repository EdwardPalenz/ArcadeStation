package games.pong;

import com.almasb.fxgl.core.math.Vec2;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.physics.PhysicsComponent;

public class BallComponent extends Component {

	private PhysicsComponent physics;

	@Override
	public void onUpdate(double tpf) {
		limitVelocity();
	}

	private void limitVelocity() {
		if (Math.abs(physics.getLinearVelocity().getX()) < 5 * 60) {
			physics.setLinearVelocity(Math.signum(physics.getLinearVelocity().getX()) * 5 * 60,
					physics.getLinearVelocity().getY());
		}

		if (Math.abs(physics.getLinearVelocity().getY()) < 5 * 60) {
			physics.setLinearVelocity(physics.getLinearVelocity().getX(),
					Math.signum(physics.getLinearVelocity().getY()) * 5 * 60);
		}
	}

	public void release(int x, int y) {
		physics.setBodyLinearVelocity(new Vec2(x*6.3, y*6.3));
	}
}
