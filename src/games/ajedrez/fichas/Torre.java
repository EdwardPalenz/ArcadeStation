package games.ajedrez.fichas;

import java.util.List;

import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.GameWorld;

import games.ajedrez.Ajedrez;
import javafx.geometry.Point2D;

public class Torre {

	public static void seleccionarTorre(Entity torre, GameWorld gameWorld, int altura, int ancho) {

		boolean bloqueo = false;

//		Arriba

		for (int y = (int) torre.getY() - Ajedrez.TILE_SIZE; !bloqueo && y >= 0; y -= Ajedrez.TILE_SIZE) {
			List<Entity> entitiesAt = gameWorld.getEntitiesAt(new Point2D(torre.getX(), y));
			if (entitiesAt.size() > 0) {
				for (int i = 0; i < entitiesAt.size(); i++) {
					if (entitiesAt.get(i).getProperties().exists("color")
							&& !entitiesAt.get(i).getString("color").equals(torre.getString("color"))) {
						Ajedrez.generarTemporal(new Point2D(torre.getX(), y), Ajedrez.temporalEnemigo, true);
						
					}
					bloqueo=true;
				}
			} else {
				Ajedrez.generarTemporal(new Point2D(torre.getX(), y), Ajedrez.temporalVacio, false);
			}

		}
//		Abajo
		bloqueo = false;

		for (

				int y = (int) torre.getY() + Ajedrez.TILE_SIZE; !bloqueo && y < ancho; y += Ajedrez.TILE_SIZE) {
			List<Entity> entitiesAt = gameWorld.getEntitiesAt(new Point2D(torre.getX(), y));
			if (entitiesAt.size() < 1) {
				Ajedrez.generarTemporal(new Point2D(torre.getX(), y), Ajedrez.temporalVacio, false);
			} else {
				if (!entitiesAt.get(0).getString("color").equals(torre.getString("color")))
					Ajedrez.generarTemporal(new Point2D(torre.getX(), y), Ajedrez.temporalEnemigo, true);

				bloqueo = true;
			}

		}

//		Derecha
		bloqueo = false;

		for (int x = (int) torre.getX() + Ajedrez.TILE_SIZE; !bloqueo && x < ancho; x += Ajedrez.TILE_SIZE) {
			List<Entity> entitiesAt = gameWorld.getEntitiesAt(new Point2D(x, torre.getY()));
			if (entitiesAt.size() < 1) {
				Ajedrez.generarTemporal(new Point2D(x, torre.getY()), Ajedrez.temporalVacio, false);
			} else {
				if (!entitiesAt.get(0).getString("color").equals(torre.getString("color")))
					Ajedrez.generarTemporal(new Point2D(x, torre.getY()), Ajedrez.temporalEnemigo, true);

				bloqueo = true;
			}

		}

//		Izquierda
		bloqueo = false;

		for (int x = (int) torre.getX() - Ajedrez.TILE_SIZE; !bloqueo && x >= 0; x -= Ajedrez.TILE_SIZE) {
			List<Entity> entitiesAt = gameWorld.getEntitiesAt(new Point2D(x, torre.getY()));
			if (entitiesAt.size() < 1) {
				Ajedrez.generarTemporal(new Point2D(x, torre.getY()), Ajedrez.temporalVacio, false);
			} else {
				if (entitiesAt.get(0).getProperties().exists("color")) {
					if (!entitiesAt.get(0).getString("color").equals(torre.getString("color")))
						Ajedrez.generarTemporal(new Point2D(x, torre.getY()), Ajedrez.temporalEnemigo, true);

					bloqueo = true;
				} else if (entitiesAt.size() > 1) {

					if (!entitiesAt.get(1).getString("color").equals(torre.getString("color")))
						Ajedrez.generarTemporal(new Point2D(x, torre.getY()), Ajedrez.temporalEnemigo, true);

					bloqueo = true;
				} else {
					Ajedrez.generarTemporal(new Point2D(x, torre.getY()), Ajedrez.temporalVacio, false);
				}
			}

		}

	}

	public static void moverTorre(Entity nuevaPosicion, Entity fichaSeleccionada, GameWorld gameWorld) {
		Point2D position = nuevaPosicion.getPosition();
		if (!nuevaPosicion.getBoolean("enemy")) {

			gameWorld.removeEntities(Ajedrez.temporales);
			fichaSeleccionada.setPosition(position);

		} else {
			gameWorld.removeEntities(Ajedrez.temporales);
			Entity enemiga = gameWorld.getEntitiesAt(position).get(0);

			if (fichaSeleccionada.getString("color").equals("blanca")) {

				Ajedrez.negrasEliminadas.add(enemiga);

			} else {

				Ajedrez.negrasEliminadas.add(enemiga);

			}

			gameWorld.removeEntity(enemiga);
			fichaSeleccionada.setPosition(position);
		}

		Ajedrez.temporales.clear();
	}

}
