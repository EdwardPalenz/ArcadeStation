package games.ajedrez.fichas;

import java.util.List;

import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.GameWorld;

import games.ajedrez.AjedrezBeta;
import javafx.geometry.Point2D;

public class Peon {

	public static void selccionarPeon(Entity peon, GameWorld gameWorld) {

//		Define el movimiento de un peon en funci�n a su color
		int direccion = 0;
		if (peon.getString("color").equals("blanca")) {
			direccion = -1;
		} else {
			direccion = 1;
		}
		int movimiento = direccion * AjedrezBeta.TILE_SIZE;

//		Si est� en su casilla de origen un pe�n se puede mover dos casillas, siempre y cuando no haya ningun enemigo en la
//		casilla correspondiente
		;
		if ((peon.getString("color").equals("blanca") && peon.getY() == 6 * AjedrezBeta.TILE_SIZE)
				|| (peon.getString("color").equals("negra") && peon.getY() == AjedrezBeta.TILE_SIZE)) {
			if (gameWorld.getEntitiesAt(new Point2D(peon.getX(), peon.getY() + 2 * movimiento)).size() < 1
					&& gameWorld.getEntitiesAt(new Point2D(peon.getX(), peon.getY() + movimiento)).size() < 1) {

				Point2D position = new Point2D(peon.getX(), peon.getY() + 2 * movimiento);

				AjedrezBeta.generarTemporal(position, AjedrezBeta.temporalVacio, false);
			}
		}

//		El movimiento est�ndar del pe�n es una casilla hacia delante, si no hay una ficha en esa casilla
		List<Entity> entitiesAt = gameWorld.getEntitiesAt(new Point2D(peon.getX(), peon.getY() + movimiento));
		if (entitiesAt.size() < 1) {
				Point2D position = new Point2D(peon.getX(), peon.getY() + movimiento);
				AjedrezBeta.generarTemporal(position, AjedrezBeta.temporalVacio, false);
			
		}

//		Si existe un enemigo en una casilla diagonal a la del pe�n en cuesti�n, este podra moverse ah� para matarlo
		entitiesAt = gameWorld.getEntitiesAt(new Point2D(peon.getX() + AjedrezBeta.TILE_SIZE, peon.getY() + movimiento));
		if (entitiesAt.size() > 0) {

			boolean enemigo = false;
			for (int i = 0; i < entitiesAt.size(); i++) {
				if (!entitiesAt.get(i).getString("color").equals(peon.getString("color")))
					enemigo = true;
			}

			if (enemigo) {
				Point2D position = new Point2D(peon.getX() + AjedrezBeta.TILE_SIZE, peon.getY() + movimiento);
				AjedrezBeta.generarTemporal(position, AjedrezBeta.temporalEnemigo, enemigo);
			}
		}

		entitiesAt = gameWorld.getEntitiesAt(new Point2D(peon.getX() - AjedrezBeta.TILE_SIZE, peon.getY() + movimiento));
		if (entitiesAt.size() > 0) {

			boolean enemigo = false;
			for (int i = 0; i < entitiesAt.size(); i++) {
				if (entitiesAt.get(i).getProperties().exists("color")
						&& !entitiesAt.get(i).getString("color").equals(peon.getString("color")))
					enemigo = true;
			}

			if (enemigo) {
				Point2D position = new Point2D(peon.getX() - AjedrezBeta.TILE_SIZE, peon.getY() + movimiento);

				AjedrezBeta.generarTemporal(position, AjedrezBeta.temporalEnemigo, enemigo);
			}
		}

	}

	public static void moverPeon(Entity nuevaPosicion, Entity fichaSeleccionada, GameWorld gameWorld) {
		Point2D position = nuevaPosicion.getPosition();
		if (!nuevaPosicion.getBoolean("enemy")) {

			gameWorld.removeEntities(AjedrezBeta.temporales);
			fichaSeleccionada.setPosition(position);

		} else {
			gameWorld.removeEntities(AjedrezBeta.temporales);
			Entity enemiga = gameWorld.getEntitiesAt(position).get(0);

			if (fichaSeleccionada.getString("color").equals("blanca")) {

				AjedrezBeta.negrasEliminadas.add(enemiga);

			} else {

				AjedrezBeta.negrasEliminadas.add(enemiga);

			}

			gameWorld.removeEntity(enemiga);
			fichaSeleccionada.setPosition(position);
		}

		AjedrezBeta.temporales.clear();
	}
}
