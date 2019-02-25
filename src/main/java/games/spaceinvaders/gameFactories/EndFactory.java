package games.spaceinvaders.gameFactories;

import com.almasb.fxgl.entity.Entities;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;
import com.almasb.fxgl.entity.components.CollidableComponent;
import com.almasb.fxgl.texture.Texture;

import games.spaceinvaders.spritesTypes.SpritesTypes;
import javafx.scene.image.Image;

public class EndFactory implements EntityFactory {

	@Spawns("End")
	public Entity spawnPlayerBullet(SpawnData data) {
		Image image = new Image("/games/spaceinvaders/main/resources/wall.png", 600, 600, true, true);

		return Entities.builder().from(data).type(SpritesTypes.END).viewFromNodeWithBBox(new Texture(image))
				.with(new CollidableComponent(true)).build();
	}
}
