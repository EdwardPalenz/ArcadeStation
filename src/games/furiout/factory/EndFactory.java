package games.furiout.factory;

import com.almasb.fxgl.app.FXGL;
import com.almasb.fxgl.entity.Entities;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;
import com.almasb.fxgl.entity.components.CollidableComponent;
import com.almasb.fxgl.physics.PhysicsComponent;
import com.almasb.fxgl.physics.box2d.dynamics.BodyType;

import games.furiout.component.BatComponent;
import games.furiout.types.FurioutTypes;

public class EndFactory implements EntityFactory{

	@Spawns("End")
	public Entity newEnd(SpawnData data) {
		PhysicsComponent physics = new PhysicsComponent();
		physics.setBodyType(BodyType.KINEMATIC);

		return Entities.builder().from(data).type(FurioutTypes.END)
				.viewFromNodeWithBBox(FXGL.getAssetLoader().loadTexture("brick_blue.png", FXGL.getAppWidth(), 10))
				.with(physics, new CollidableComponent(true)).with(new BatComponent()).build();
	}
}
