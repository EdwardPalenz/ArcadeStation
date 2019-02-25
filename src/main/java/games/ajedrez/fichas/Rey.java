package games.ajedrez.fichas;

import java.util.List;

import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.GameWorld;

import games.ajedrez.AjedrezBeta;
import javafx.geometry.Point2D;

public class Rey {

	public static void seleccionarRey(Entity rey, GameWorld gameWorld, int height, int width) {

		Point2D posicionRey = rey.getPosition();

//		primer movimiento ^
		Point2D posicionPosible = posicionRey.add(new Point2D(0, -AjedrezBeta.TILE_SIZE));

		if (posicionPosible.getY() >= 0) {
			comprobarPosicion(gameWorld, posicionPosible, rey);
		}

//		segundo movimiento ^>
		posicionPosible = posicionRey.add(new Point2D(AjedrezBeta.TILE_SIZE, -AjedrezBeta.TILE_SIZE));

		if (posicionPosible.getY() >= 0 && posicionPosible.getX() < width) {
			comprobarPosicion(gameWorld, posicionPosible, rey);
		}

//		tercer movimiento >
		posicionPosible = posicionRey.add(new Point2D(AjedrezBeta.TILE_SIZE, 0));

		if (posicionPosible.getX() < width) {
			comprobarPosicion(gameWorld, posicionPosible, rey);
		}

//		cuarto movimiento >v
		posicionPosible = posicionRey.add(new Point2D(AjedrezBeta.TILE_SIZE, AjedrezBeta.TILE_SIZE));

		if (posicionPosible.getX() < width && posicionPosible.getY() < height) {
			comprobarPosicion(gameWorld, posicionPosible, rey);
		}

//		quinto movimiento v
		posicionPosible = posicionRey.add(new Point2D(0, AjedrezBeta.TILE_SIZE));

		if (posicionPosible.getY() < height) {
			comprobarPosicion(gameWorld, posicionPosible, rey);
		}

// 		sexto movimiento v<
		posicionPosible = posicionRey.add(new Point2D(-AjedrezBeta.TILE_SIZE, AjedrezBeta.TILE_SIZE));

		if (posicionPosible.getX() >= 0 && posicionPosible.getY() < height) {
			comprobarPosicion(gameWorld, posicionPosible, rey);
		}

// 		septimo movimiento <
		posicionPosible = posicionRey.add(new Point2D(-AjedrezBeta.TILE_SIZE, 0));

		if (posicionPosible.getX() >= 0) {
			comprobarPosicion(gameWorld, posicionPosible, rey);
		}

// 		octava movimiento <^
		posicionPosible = posicionRey.add(new Point2D(-AjedrezBeta.TILE_SIZE, -AjedrezBeta.TILE_SIZE));

		if (posicionPosible.getX() >= 0 && posicionPosible.getY() >= 0) {
			comprobarPosicion(gameWorld, posicionPosible, rey);
		}

	}

	private static void comprobarPosicion(GameWorld gameWorld, Point2D posicionPosible, Entity rey) {
		List<Entity> entitiesAt = gameWorld.getEntitiesAt(posicionPosible);
		if (entitiesAt.size() > 0) {
			for (int i = 0; i < entitiesAt.size(); i++) {
				if (entitiesAt.get(i).getProperties().exists("color")
						&& !entitiesAt.get(i).getString("color").equals(rey.getString("color"))) {
					AjedrezBeta.generarTemporal(posicionPosible, AjedrezBeta.temporalEnemigo, true);

				}
			}
		} else {
			AjedrezBeta.generarTemporal(posicionPosible, AjedrezBeta.temporalVacio, false);
		}
	}

	public static boolean hayJaque(String color, GameWorld gameWorld) {
		boolean escape = true;
		boolean peligro = false;
		AjedrezBeta.reyEnPeligro = false;
		Entity rey = null;

		@SuppressWarnings("unused")
		int direccion = 0;
		if (color.equals("blanca")) {
			direccion = -1;
		} else {
			direccion = 1;
		}

		List<Entity> lista = gameWorld.getEntities();
		for (int i = 1; i < lista.size(); i++) {
			Entity entity = lista.get(i);
			if (entity.getString("name").equals("rey") && !entity.getString("color").equals(color)) {
				rey = entity;
				System.out.println(rey.getY() + " " + rey.getX());
			}
		}

//		Comprobar vertical ^
//		Primero comprobar� la casilla inmediatamene arriba, si es un rey, reina o una torre se considera que esta en peligro
//		en las casillas mas arriba solo lo considerar� si es una reina o una torre y no hay un aliado bloqueando el camino
		boolean arrSegura = false;
		Point2D pos = rey.getPosition().add(0, -AjedrezBeta.TILE_SIZE);
		List<Entity> entitiesAt = gameWorld.getEntitiesAt(pos);

		if (entitiesAt.size() > 0) {
			for (int i = 0; i < entitiesAt.size() && (!arrSegura || !peligro); i++) {
//				Si encima hay un aliado no est� en peligro en esa posici�n
				if (comprobarAliado(rey, entitiesAt, i)) {
					arrSegura = true;
					peligro = false;
				} else if (entitiesAt.get(i).getProperties().exists("color")) {

					String name = entitiesAt.get(i).getString("name");
					if (name.equals("reina") || name.equals("torre") || name.equals("rey")) {
						arrSegura = false;
						peligro = true;
						AjedrezBeta.reyEnPeligro = true;
						System.out.println("Peligro arriba");
					}

				}
			}
		}

//		Abajo
		boolean abajoSegura = false;
		pos = rey.getPosition().add(0, AjedrezBeta.TILE_SIZE);
		entitiesAt = gameWorld.getEntitiesAt(pos);

		if (entitiesAt.size() > 0) {
			for (int i = 0; i < entitiesAt.size() && (!abajoSegura || !peligro); i++) {
//				Si encima hay un aliado no est� en peligro en esa posici�n
				if (comprobarAliado(rey, entitiesAt, i)) {
					abajoSegura = true;
					peligro = false;
				} else if (entitiesAt.get(i).getProperties().exists("color")) {

					String name = entitiesAt.get(i).getString("name");
					if (name.equals("reina") || name.equals("torre") || name.equals("rey")) {
						abajoSegura = false;
						peligro = true;
						AjedrezBeta.reyEnPeligro = true;
						System.out.println("Peligro abajo");
					}

				}
			}
		}

//		Derecha
		boolean derechaSegura = false;
		pos = rey.getPosition().add(AjedrezBeta.TILE_SIZE, 0);
		entitiesAt = gameWorld.getEntitiesAt(pos);

		if (entitiesAt.size() > 0) {
			for (int i = 0; i < entitiesAt.size() && (!derechaSegura || !peligro); i++) {
//				Si encima hay un aliado no est� en peligro en esa posici�n
				if (comprobarAliado(rey, entitiesAt, i)) {
					derechaSegura = true;
					peligro = false;
				} else if (entitiesAt.get(i).getProperties().exists("color")) {

					String name = entitiesAt.get(i).getString("name");
					if (name.equals("reina") || name.equals("torre") || name.equals("rey")) {
						derechaSegura = false;
						peligro = true;
						AjedrezBeta.reyEnPeligro = true;
						System.out.println("Peligro derecha");
					}

				}
			}

//			Izquierda
			boolean IzquierdaSegura = false;
			pos = rey.getPosition().add(-AjedrezBeta.TILE_SIZE, 0);
			entitiesAt = gameWorld.getEntitiesAt(pos);

			while (pos.getX() >= 0) {
				if (entitiesAt.size() > 0) {

					for (int i = 0; i < entitiesAt.size() && (!IzquierdaSegura || !peligro); i++) {
//					Si encima hay un aliado no est� en peligro en esa posici�n
						if (comprobarAliado(rey, entitiesAt, i)) {
							IzquierdaSegura = true;
							peligro = false;
						} else if (entitiesAt.get(i).getProperties().exists("color")) {

							String name = entitiesAt.get(i).getString("name");

							if (name.equals("reina") || name.equals("torre") || name.equals("rey")) {
								IzquierdaSegura = false;
								peligro = true;
								AjedrezBeta.reyEnPeligro = true;
								System.out.println("Peligro Izquierda");
							}

						}
					}
				}
			}
			pos = pos.add(-AjedrezBeta.TILE_SIZE, 0);
			entitiesAt = gameWorld.getEntitiesAt(pos);
		}

		boolean jaque = !escape;
		return jaque;
	}

	private static boolean comprobarAliado(Entity rey, List<Entity> entitiesAt, int i) {
		return entitiesAt.get(i).getProperties().exists("color")
				&& entitiesAt.get(i).getString("color").equals(rey.getString("color"));
	}

}
