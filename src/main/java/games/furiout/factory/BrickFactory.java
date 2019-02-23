package games.furiout.factory;

import com.almasb.fxgl.app.FXGL;
import com.almasb.fxgl.entity.Entities;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;
import com.almasb.fxgl.entity.components.CollidableComponent;
import com.almasb.fxgl.physics.PhysicsComponent;

import games.furiout.component.BrickComponent;
import games.furiout.types.FurioutTypes;

public class BrickFactory implements EntityFactory{

	@Spawns("Brick")
	public Entity newBrick(SpawnData data) {
		return Entities.builder().from(data).type(FurioutTypes.BRICK)
				.viewFromNodeWithBBox(FXGL.getAssetLoader().loadTexture("brick_blue.png", 232 / 3, 104 / 3))
				.with(new PhysicsComponent(), new CollidableComponent(true)).with(new BrickComponent()).build();
	}
}
