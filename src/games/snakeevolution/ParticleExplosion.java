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
import javafx.scene.paint.Color;

public class ParticleExplosion implements EntityFactory {

	@Spawns("ParticleExplosion")
	public Entity spawnParticleExplosion(SpawnData data) {
		ParticleEmitter emitter = ParticleEmitters.newExplosionEmitter(100);
		emitter.setSize(20, 20);
		emitter.setBlendMode(BlendMode.COLOR_BURN);
		emitter.setInterpolator(Interpolators.RANDOM.EASE_IN());
		emitter.setNumParticles(20);
		
		ParticleComponent particles = new ParticleComponent(emitter);
		particles.setOnFinished(()->particles.getEntity().removeFromWorld());
		return Entities.builder().from(data).with(particles).build();
	}
	
}
