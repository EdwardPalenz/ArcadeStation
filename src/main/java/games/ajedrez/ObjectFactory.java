package games.ajedrez;

import com.almasb.fxgl.entity.Entities;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;
import com.almasb.fxgl.entity.components.SelectableComponent;

import javafx.geometry.Point2D;

public class ObjectFactory implements EntityFactory {

	@Spawns("fichaAjedrez")
	public Entity newFicha(SpawnData data) {
		return Entities.builder().from(data).with(new SelectableComponent(true)).with("velocity", new Point2D(0, 10)).build();
	}
}
