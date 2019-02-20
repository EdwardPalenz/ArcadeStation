package games.furiout.factory;

import com.almasb.fxgl.entity.Entities;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;
import com.almasb.fxgl.entity.components.CollidableComponent;
import com.almasb.fxgl.particle.ParticleComponent;
import com.almasb.fxgl.particle.ParticleEmitter;
import com.almasb.fxgl.particle.ParticleEmitters;
import com.almasb.fxgl.physics.BoundingShape;
import com.almasb.fxgl.physics.HitBox;
import com.almasb.fxgl.physics.PhysicsComponent;
import com.almasb.fxgl.physics.box2d.dynamics.BodyType;
import com.almasb.fxgl.physics.box2d.dynamics.FixtureDef;

import games.furiout.component.BallComponent;
import games.furiout.types.FurioutTypes;
import javafx.scene.effect.BlendMode;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class BallFactory implements com.almasb.fxgl.entity.EntityFactory {

	@Spawns("Ball")
	public Entity newBall(SpawnData data) {
		PhysicsComponent physics = new PhysicsComponent();
		physics.setBodyType(BodyType.DYNAMIC);
		physics.setFixtureDef(new FixtureDef().restitution(1f).density(0.03f));

		ParticleEmitter emitter = ParticleEmitters.newSmokeEmitter();
		emitter.setNumParticles(5);
		emitter.setEmissionRate(0.5);
		emitter.setBlendMode(BlendMode.SRC_OVER);

		return Entities.builder().from(data).type(FurioutTypes.BALL).bbox(new HitBox("Main", BoundingShape.circle(10)))
				.viewFromNode(new Circle(10, Color.LIGHTCORAL)).with(physics, new CollidableComponent(true))
				.with(new BallComponent(), new ParticleComponent(emitter)).build();
	}
}
