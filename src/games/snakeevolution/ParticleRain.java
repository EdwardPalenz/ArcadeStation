package games.snakeevolution;

import com.almasb.fxgl.animation.Interpolators;
import com.almasb.fxgl.entity.Entities;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;
import com.almasb.fxgl.particle.ParticleComponent;
import com.almasb.fxgl.particle.ParticleEmitter;
import com.almasb.fxgl.particle.ParticleEmitters;

import javafx.scene.effect.BlendMode;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public class ParticleRain implements EntityFactory {

	static Entity rain;
	
	@Spawns("ParticleRain")
	public Entity spawnParticleExplosion(SpawnData data) {
		ParticleEmitter emitter = ParticleEmitters.newRainEmitter(600);
		emitter.setSize(2, 2);
		emitter.setBlendMode(BlendMode.SRC_OVER);
//		emitter.setSourceImage(new Image("/dad/resources/drop.png"));
//		emitter.setInterpolator(Interpolators.QUADRATIC.EASE_IN());
		emitter.setNumParticles(1);
		ParticleComponent particles = new ParticleComponent(emitter);
		particles.setOnFinished(()->particles.getEntity().removeFromWorld());
		rain = Entities.builder().from(data).with(particles).build();
		return rain;
	}
}
