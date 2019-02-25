package games.spaceinvaders.gameControllers;

import javafx.util.Duration;

import com.almasb.fxgl.app.DSLKt;
import com.almasb.fxgl.app.FXGL;
import com.almasb.fxgl.core.math.FXGLMath;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.time.LocalTimer;

public class EnemyControll extends Component {

	protected LocalTimer attackTimer;
	protected Duration nextAttack = Duration.seconds(2);
	protected LocalTimer hTimer;
	protected Duration nextMove = Duration.seconds(2);
	protected LocalTimer vTimer;

	private boolean movinRight;

	public void onAdded() {
		attackTimer = FXGL.newLocalTimer();
		attackTimer.capture();
		hTimer = FXGL.newLocalTimer();
		hTimer.capture();
		vTimer = FXGL.newLocalTimer();
		vTimer.capture();
	}

	protected void shoot() {

		DSLKt.spawn("EnemyBullet", entity.getX() + 7, entity.getY());

	}

	@Override
	public void onUpdate(double tpf) {

		if (hTimer.elapsed(Duration.seconds(2))) {
			movinRight = !movinRight;
			hTimer.capture();
		}
		if (vTimer.elapsed(Duration.seconds(2))) {
			entity.translate(0, 20);
			vTimer.capture();
		}
		if (attackTimer.elapsed(nextAttack)) {
			if (FXGLMath.randomBoolean(0.2f)) {
				shoot();
			}
			nextAttack = Duration.seconds(3 * Math.random());
			attackTimer.capture();
		}
		entity.translate(movinRight ? 1 : -1, 0);
	}

}
