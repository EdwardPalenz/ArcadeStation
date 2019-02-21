package games.snakeevolution;

import com.almasb.fxgl.core.math.FXGLMath;
import com.almasb.fxgl.entity.Entities;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;
import com.almasb.fxgl.entity.components.CollidableComponent;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class BlueApple implements EntityFactory {

	static Entity blueApple;
	
	@Spawns("BlueApple")
	public Entity spawnFruta(SpawnData data) {
		int x = (int) FXGLMath.random(0, SnakeEvolution.SCREEN_SIZE-30);
		int y = (int) FXGLMath.random(0, SnakeEvolution.SCREEN_SIZE-30);
		blueApple = Entities.builder().at(x, y).with(new CollidableComponent(true)).type(Entidades.APPLE).viewFromNodeWithBBox(new ImageView(new Image("/assets/textures/blue_apple.png",30.0,30.0,true,true)))
				.build();
		return blueApple;
	}


	public static Entity getBlueApple() {
		return blueApple;
	}

}
