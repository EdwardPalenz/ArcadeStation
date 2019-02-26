package games.spaceinvaders.gameFactories;

import com.almasb.fxgl.app.FXGL;
import com.almasb.fxgl.entity.Entities;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.RenderLayer;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;
import com.almasb.fxgl.texture.Texture;

import games.spaceinvaders.spritesTypes.SpritesTypes;
import javafx.scene.image.Image;

public class BackgroundFactory implements EntityFactory {

	@Spawns("Background")
	public Entity spawnEnemyBullet(SpawnData data) {
//		Image image = new Image("/games/spaceinvaders/main/resources/bg.png");
		return Entities.builder().from(data).type(SpritesTypes.BACKGROUND).viewFromNode(FXGL.getAssetLoader().loadTexture("bg.png"))
				.renderLayer(RenderLayer.BACKGROUND).build();
	}
}
