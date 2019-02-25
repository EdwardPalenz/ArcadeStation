package games.spaceinvaders.gameControllers;

import com.almasb.fxgl.entity.component.Component;

import games.spaceinvaders.spritesTypes.SpritesTypes;

public class BulletControl extends Component {

	@Override
	public void onUpdate(double tpf) {

		if (entity.isType(SpritesTypes.ENEMY_BULLET)) {
			entity.translate(0, 10);
		} else {
			entity.translate(0, -10);
		}
	}

}
