package games.spaceinvaders.gameFactories;

import com.almasb.fxgl.entity.Entities;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;
import com.almasb.fxgl.entity.components.CollidableComponent;
import com.almasb.fxgl.extra.entity.components.ProjectileComponent;
import com.almasb.fxgl.texture.Texture;

import games.spaceinvaders.gameControllers.BulletControl;
import games.spaceinvaders.spritesTypes.SpritesTypes;
import javafx.geometry.Point2D;
import javafx.scene.image.Image;

public class EnemyBulletFactory implements EntityFactory {
	@Spawns("EnemyBullet")
	public Entity spawnEnemyBullet(SpawnData data) {
		Image image = new Image("/games/spaceinvaders/main/resources/disparopplayer.png");
		return Entities.builder().from(data).type(SpritesTypes.ENEMY_BULLET).viewFromNodeWithBBox(new Texture(image))
				.with(new CollidableComponent(true)).with(new ProjectileComponent(new Point2D(0, -1), -100))
				.with(new BulletControl()).build();
	}
}
