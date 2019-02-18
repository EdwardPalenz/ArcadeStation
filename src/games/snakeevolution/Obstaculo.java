package games.snakeevolution;

import com.almasb.fxgl.entity.Entities;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;
import com.almasb.fxgl.entity.components.CollidableComponent;

import javafx.scene.image.ImageView;

public class Obstaculo implements EntityFactory {

	static Entity obstaculo;
	static int x, y;
	
	@Spawns("Obstaculo")
	public Entity spawnFruta(SpawnData data) {
		Obstaculo.x = (int) data.getX();
		Obstaculo.y = (int) data.getY();
		
		obstaculo = Entities.builder().at(x, y).with(new CollidableComponent(true)).type(Entidades.OBSTACULO).viewFromNodeWithBBox((ImageView)(data.get("node")))
				.build();
		
		return obstaculo;
	}
	
	public static Entity getObstaculo() {
		return obstaculo;
	}

	public static int getX() {
		return x;
	}

	public static int getY() {
		return y;
	}
	
}
