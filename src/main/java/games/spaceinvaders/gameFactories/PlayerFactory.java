package games.spaceinvaders.gameFactories;

import com.almasb.fxgl.entity.Entities;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;
import com.almasb.fxgl.entity.components.CollidableComponent;
import com.almasb.fxgl.entity.components.TimeComponent;
import com.almasb.fxgl.texture.Texture;

import games.spaceinvaders.gameControllers.PlayerControll;
import games.spaceinvaders.spritesTypes.SpritesTypes;
import javafx.scene.image.Image;

public class PlayerFactory implements EntityFactory {

	@Spawns("Player")
	public Entity spawnPLayer(SpawnData data) {

		Image image = new Image("/games/spaceinvaders/main/resources/player.png");
		return Entities.builder().from(data).type(SpritesTypes.PLAYER).viewFromNodeWithBBox(new Texture(image))
				.with(new CollidableComponent(true)).with(new PlayerControll()).with(new TimeComponent(1.0)).build();
	}

}
