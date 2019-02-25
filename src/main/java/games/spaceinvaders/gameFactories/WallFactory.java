package games.spaceinvaders.gameFactories;

import com.almasb.fxgl.entity.Entities;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;
import com.almasb.fxgl.entity.components.CollidableComponent;
import com.almasb.fxgl.texture.Texture;

import games.spaceinvaders.gameControllers.WallControll;
import games.spaceinvaders.spritesTypes.SpritesTypes;
import javafx.scene.image.Image;

public class WallFactory implements EntityFactory {

	@Spawns("Wall")
	public Entity spawnPlayerBullet(SpawnData data) {
		Image image = new Image("/games/spaceinvaders/main/resources/wall.png", 50, 50, true, true);

		return Entities.builder().from(data).type(SpritesTypes.WALL).viewFromNodeWithBBox(new Texture(image))
				.with(new CollidableComponent(true)).with(new WallControll(7)).build();
	}

}
