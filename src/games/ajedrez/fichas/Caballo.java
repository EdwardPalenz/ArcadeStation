package games.ajedrez.fichas;

import java.util.List;

import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.GameWorld;

import games.ajedrez.AjedrezBeta;
import javafx.geometry.Point2D;

public class Caballo {

	public static void seleccionarCaballo(Entity caballo, GameWorld gameWorld, int height, int width) {
		Point2D posCaballo = caballo.getPosition();
		Point2D posPosible = new Point2D(posCaballo.getX(), posCaballo.getY());

		// Primer movimiento posible ^^<
		posPosible = posPosible.add(new Point2D(-AjedrezBeta.TILE_SIZE, (-2 * AjedrezBeta.TILE_SIZE)));
		List<Entity> entitiesAt = gameWorld.getEntitiesAt(posPosible);

		if (posPosible.getX() >= 0 && posPosible.getY() >= 0) {
			comprobarPosicion(caballo, posPosible, entitiesAt);
		}

		// segundo movimiento posible ^^>
		posPosible = posCaballo.add(new Point2D(AjedrezBeta.TILE_SIZE, (-2 * AjedrezBeta.TILE_SIZE)));

		entitiesAt = gameWorld.getEntitiesAt(posPosible);

		if (posPosible.getX() < width && posPosible.getY() >= 0) {
			comprobarPosicion(caballo, posPosible, entitiesAt);
		}
		
		// tercero movimiento posible ^>>
		posPosible = posCaballo.add(new Point2D(2*AjedrezBeta.TILE_SIZE, -AjedrezBeta.TILE_SIZE));

		entitiesAt = gameWorld.getEntitiesAt(posPosible);

		if (posPosible.getX() < width && posPosible.getY() >= 0) {
			comprobarPosicion(caballo, posPosible, entitiesAt);
		}
		
		
		// cuarto movimiento posible v>>
		posPosible = posCaballo.add(new Point2D(2*AjedrezBeta.TILE_SIZE, AjedrezBeta.TILE_SIZE));

		entitiesAt = gameWorld.getEntitiesAt(posPosible);

		if (posPosible.getX() < width && posPosible.getY() <height) {
			comprobarPosicion(caballo, posPosible, entitiesAt);
		}
		
		// quinto movimiento posible vv>
		posPosible = posCaballo.add(new Point2D(AjedrezBeta.TILE_SIZE, 2*AjedrezBeta.TILE_SIZE));

		entitiesAt = gameWorld.getEntitiesAt(posPosible);

		if (posPosible.getX() < width && posPosible.getY() <height) {
			comprobarPosicion(caballo, posPosible, entitiesAt);
		}

		// sexto movimiento posible vv<
		posPosible = posCaballo.add(new Point2D(-AjedrezBeta.TILE_SIZE, 2*AjedrezBeta.TILE_SIZE));

		entitiesAt = gameWorld.getEntitiesAt(posPosible);

		if (posPosible.getX() >= 0 && posPosible.getY() <height) {
			comprobarPosicion(caballo, posPosible, entitiesAt);
		}
		
		// septimo movimiento posible v<<
		posPosible = posCaballo.add(new Point2D(-2*AjedrezBeta.TILE_SIZE, AjedrezBeta.TILE_SIZE));

		entitiesAt = gameWorld.getEntitiesAt(posPosible);

		if (posPosible.getX() >=0 && posPosible.getY() <height) {
			comprobarPosicion(caballo, posPosible, entitiesAt);
		}	
		
		// octavo movimiento posible ^<<
		posPosible = posCaballo.add(new Point2D(-2*AjedrezBeta.TILE_SIZE, -AjedrezBeta.TILE_SIZE));

		entitiesAt = gameWorld.getEntitiesAt(posPosible);

		if (posPosible.getX() >=0 && posPosible.getY() <height) {
			comprobarPosicion(caballo, posPosible, entitiesAt);
		}
		
	}

	private static void comprobarPosicion(Entity caballo, Point2D posPosible, List<Entity> entitiesAt) {
		if (entitiesAt.size() > 0) {
			for (int j = 0; j < entitiesAt.size(); j++) {
				if (entitiesAt.get(j).getProperties().exists("color")
						&& !entitiesAt.get(j).getString("color").equals(caballo.getString("color"))) {
					AjedrezBeta.generarTemporal(posPosible, AjedrezBeta.temporalEnemigo, true);
				}
			}
		} else {
			AjedrezBeta.generarTemporal(posPosible, AjedrezBeta.temporalVacio, false);
		}
	}

}
