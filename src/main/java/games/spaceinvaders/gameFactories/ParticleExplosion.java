package games.spaceinvaders.gameFactories;

import com.almasb.fxgl.entity.Entities;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;
import com.almasb.fxgl.particle.ParticleComponent;
import com.almasb.fxgl.particle.ParticleEmitter;
import com.almasb.fxgl.particle.ParticleEmitters;

import javafx.scene.paint.Color;


public class ParticleExplosion implements EntityFactory{
		
	@Spawns("ParticleExplosion")
	public Entity spawnParticleExplosion(SpawnData data) {
		ParticleEmitter emitter=ParticleEmitters.newExplosionEmitter(100);
		emitter.setStartColor(Color.ORANGERED);
		emitter.setSize(10, 10);
		emitter.setNumParticles(100);
		
		ParticleComponent particles=new ParticleComponent(emitter);
		particles.setOnFinished(()->particles.getEntity().removeFromWorld());
		
		return Entities.builder().from(data).with(particles).build();
	}
}
