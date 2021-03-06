package games.spaceinvaders.gameFactories;

import com.almasb.fxgl.app.FXGL;
import com.almasb.fxgl.entity.Entities;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;
import com.almasb.fxgl.entity.components.CollidableComponent;
import com.almasb.fxgl.entity.components.TimeComponent;
import com.almasb.fxgl.texture.Texture;

import games.spaceinvaders.gameControllers.EnemyControll;
import games.spaceinvaders.spritesTypes.SpritesTypes;
import javafx.scene.image.Image;

public class EnemyFactory implements EntityFactory {

	@Spawns("Enemy")
	public Entity spawnEnemy(SpawnData data) {
//		Image image = new Image("/games/spaceinvaders/main/resources/invaders.png");
		return Entities.builder().from(data).type(SpritesTypes.ENEMY).viewFromNodeWithBBox(FXGL.getAssetLoader().loadTexture("invaders.png"))
				.with(new CollidableComponent(true)).with(new EnemyControll()).with(new TimeComponent(2.0)).build();
	}

}
