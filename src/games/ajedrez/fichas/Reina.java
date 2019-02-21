package games.ajedrez.fichas;

import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.GameWorld;

public class Reina {

	public static void seleccionarReina(Entity reina, GameWorld gameWorld, int height, int width) {

//		Nada que epxlicar aqui

		Alfil.seleccionarAlfil(reina, gameWorld, height, width);
		Torre.seleccionarTorre(reina, gameWorld, height, width);
	}

}
