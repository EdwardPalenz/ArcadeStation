package games.ajedrez.fichas;

import java.util.List;

import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.GameWorld;

import games.ajedrez.AjedrezBeta;
import javafx.geometry.Point2D;

public class Torre {

	public static void seleccionarTorre(Entity torre, GameWorld gameWorld, int altura, int ancho) {

		boolean bloqueo = false;

//		Arriba

		for (int y = (int) torre.getY() - AjedrezBeta.TILE_SIZE; !bloqueo && y >= 0; y -= AjedrezBeta.TILE_SIZE) {
			List<Entity> entitiesAt = gameWorld.getEntitiesAt(new Point2D(torre.getX(), y));
			if (entitiesAt.size() > 0) {
				for (int i = 0; i < entitiesAt.size(); i++) {
					if (entitiesAt.get(i).getProperties().exists("color")
							&& !entitiesAt.get(i).getString("color").equals(torre.getString("color"))) {
						AjedrezBeta.generarTemporal(new Point2D(torre.getX(), y), AjedrezBeta.temporalEnemigo, true);
						
					}
					bloqueo=true;
				}
			} else {
				AjedrezBeta.generarTemporal(new Point2D(torre.getX(), y), AjedrezBeta.temporalVacio, false);
			}

		}
//		Abajo
		bloqueo = false;

		for (

				int y = (int) torre.getY() + AjedrezBeta.TILE_SIZE; !bloqueo && y < ancho; y += AjedrezBeta.TILE_SIZE) {
			List<Entity> entitiesAt = gameWorld.getEntitiesAt(new Point2D(torre.getX(), y));
			if (entitiesAt.size() < 1) {
				AjedrezBeta.generarTemporal(new Point2D(torre.getX(), y), AjedrezBeta.temporalVacio, false);
			} else {
				if (!entitiesAt.get(0).getString("color").equals(torre.getString("color")))
					AjedrezBeta.generarTemporal(new Point2D(torre.getX(), y), AjedrezBeta.temporalEnemigo, true);

				bloqueo = true;
			}

		}

//		Derecha
		bloqueo = false;

		for (int x = (int) torre.getX() + AjedrezBeta.TILE_SIZE; !bloqueo && x < ancho; x += AjedrezBeta.TILE_SIZE) {
			List<Entity> entitiesAt = gameWorld.getEntitiesAt(new Point2D(x, torre.getY()));
			if (entitiesAt.size() < 1) {
				AjedrezBeta.generarTemporal(new Point2D(x, torre.getY()), AjedrezBeta.temporalVacio, false);
			} else {
				if (!entitiesAt.get(0).getString("color").equals(torre.getString("color")))
					AjedrezBeta.generarTemporal(new Point2D(x, torre.getY()), AjedrezBeta.temporalEnemigo, true);

				bloqueo = true;
			}

		}

//		Izquierda
		bloqueo = false;

		for (int x = (int) torre.getX() - AjedrezBeta.TILE_SIZE; !bloqueo && x >= 0; x -= AjedrezBeta.TILE_SIZE) {
			List<Entity> entitiesAt = gameWorld.getEntitiesAt(new Point2D(x, torre.getY()));
			if (entitiesAt.size() < 1) {
				AjedrezBeta.generarTemporal(new Point2D(x, torre.getY()), AjedrezBeta.temporalVacio, false);
			} else {
				if (entitiesAt.get(0).getProperties().exists("color")) {
					if (!entitiesAt.get(0).getString("color").equals(torre.getString("color")))
						AjedrezBeta.generarTemporal(new Point2D(x, torre.getY()), AjedrezBeta.temporalEnemigo, true);

					bloqueo = true;
				} else if (entitiesAt.size() > 1) {

					if (!entitiesAt.get(1).getString("color").equals(torre.getString("color")))
						AjedrezBeta.generarTemporal(new Point2D(x, torre.getY()), AjedrezBeta.temporalEnemigo, true);

					bloqueo = true;
				} else {
					AjedrezBeta.generarTemporal(new Point2D(x, torre.getY()), AjedrezBeta.temporalVacio, false);
				}
			}

		}

	}

	public static void moverTorre(Entity nuevaPosicion, Entity fichaSeleccionada, GameWorld gameWorld) {
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
