package games.ajedrez.fichas;

import java.util.List;

import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.GameWorld;

import games.ajedrez.Ajedrez;
import javafx.geometry.Point2D;

public class Alfil {

	public static void seleccionarAlfil(Entity alfil, GameWorld gameWorld, int height, int width) {

//		Primero movimiento ^>
		Point2D posicionAlfil = alfil.getPosition();
		Point2D posicionPosible = posicionAlfil.add(Ajedrez.TILE_SIZE, -Ajedrez.TILE_SIZE);
		boolean bloqueo = false;

		while (!bloqueo && posicionPosible.getX() < width && posicionPosible.getY() >= 0) {
			bloqueo = comprobarPosicion(alfil, gameWorld, posicionPosible, bloqueo);
			posicionPosible = posicionPosible.add(Ajedrez.TILE_SIZE, -Ajedrez.TILE_SIZE);
		}

//		Segundo movimiento >v
		bloqueo = false;
		posicionPosible = posicionAlfil.add(Ajedrez.TILE_SIZE, Ajedrez.TILE_SIZE);

		while (!bloqueo && posicionPosible.getX() < width && posicionPosible.getY() < height) {
			bloqueo = comprobarPosicion(alfil, gameWorld, posicionPosible, bloqueo);
			posicionPosible = posicionPosible.add(Ajedrez.TILE_SIZE, Ajedrez.TILE_SIZE);
		}

//		tercer movimiento <v
		bloqueo = false;
		posicionPosible = posicionAlfil.add(-Ajedrez.TILE_SIZE, Ajedrez.TILE_SIZE);

		while (!bloqueo && posicionPosible.getX() >= 0 && posicionPosible.getY() < height) {
			bloqueo = comprobarPosicion(alfil, gameWorld, posicionPosible, bloqueo);
			posicionPosible = posicionPosible.add(-Ajedrez.TILE_SIZE, Ajedrez.TILE_SIZE);
		}

//		cuarto movimiento <^
		bloqueo = false;
		posicionPosible = posicionAlfil.add(-Ajedrez.TILE_SIZE, -Ajedrez.TILE_SIZE);

		while (!bloqueo && posicionPosible.getX() >= 0 && posicionPosible.getY() >= 0) {
			bloqueo = comprobarPosicion(alfil, gameWorld, posicionPosible, bloqueo);
			posicionPosible = posicionPosible.add(-Ajedrez.TILE_SIZE, -Ajedrez.TILE_SIZE);
		}
	}

	private static boolean comprobarPosicion(Entity alfil, GameWorld gameWorld, Point2D posicionPosible,
			boolean bloqueo) {
		List<Entity> entitiesAt = gameWorld.getEntitiesAt(posicionPosible);
		if (entitiesAt.size() > 0) {
			for (int i = 0; i < entitiesAt.size(); i++) {
				if (entitiesAt.get(i).getProperties().exists("color")
						&& !entitiesAt.get(i).getString("color").equals(alfil.getString("color"))) {
					Ajedrez.generarTemporal(posicionPosible, Ajedrez.temporalEnemigo, true);

				}
				bloqueo = true;
			}
		} else {
			Ajedrez.generarTemporal(posicionPosible, Ajedrez.temporalVacio, false);
		}
		return bloqueo;
	}

}
