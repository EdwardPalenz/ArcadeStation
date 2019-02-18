package games.ajedrez.fichas;

import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.GameWorld;

public class Reina {

	public static void seleccionarReina(Entity newValue, GameWorld gameWorld, int height, int width) {

//		Nada que epxlicar aqui

		Alfil.seleccionarAlfil(newValue, gameWorld, height, width);
		Torre.seleccionarTorre(newValue, gameWorld, height, width);
	}

}
