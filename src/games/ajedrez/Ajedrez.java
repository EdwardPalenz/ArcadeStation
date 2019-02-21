package games.ajedrez;

import java.util.ArrayList;
import java.util.Map;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.entity.Entities;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.RenderLayer;
import com.almasb.fxgl.entity.components.SelectableComponent;
import com.almasb.fxgl.parser.tiled.TiledMap;
import com.almasb.fxgl.settings.GameSettings;
import com.almasb.fxgl.ui.Position;

import games.ajedrez.fichas.Alfil;
import games.ajedrez.fichas.Caballo;
import games.ajedrez.fichas.Peon;
import games.ajedrez.fichas.Reina;
import games.ajedrez.fichas.Rey;
import games.ajedrez.fichas.Torre;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Ajedrez extends GameApplication {

//	private Entity player;
	public static Entity fichaSeleccionada;

	public final static int TILE_SIZE = 64;
//	private boolean fichaSeleccionada = false;

	public static ArrayList<Entity> temporales = new ArrayList<>();
	public static ArrayList<Entity> negrasEliminadas = new ArrayList<>();
	public static ArrayList<Entity> Eliminadas = new ArrayList<>();

	public static Color temporalVacio = Color.rgb(0, 255, 255, 0.5);
	public static Color temporalEnemigo = Color.rgb(255, 255, 0, 0.5);

//	private static String colorTurno = "blancas";
	public static boolean reyEnPeligro = false;


	@Override
	protected void initSettings(GameSettings settings) {
		settings.setHeight(8 * TILE_SIZE);
		settings.setWidth(8 * TILE_SIZE);
		settings.setTitle("Ajedrez");
		settings.setVersion("0.1");

		settings.setMenuEnabled(false);
		settings.setIntroEnabled(false);
		settings.setAppIcon("03_04.png");

	}

	@Override
	protected void initGame() {
		TiledMap tileMap = getAssetLoader().loadJSON("tablero.json", TiledMap.class);

		getGameWorld().addEntityFactory(new ObjectFactory());
		getGameWorld().setLevelFromMap(tileMap);

		getGameWorld().selectedEntityProperty().addListener(new ChangeListener<Entity>() {
			@Override
			public void changed(ObservableValue<? extends Entity> observable, Entity oldValue, Entity newValue) {
				if (newValue != null) {
					if (!newValue.getString("name").equals("temporal")) {
						getGameWorld().removeEntities(temporales);
						temporales.clear();

						fichaSeleccionada = newValue;
						String name = newValue.getString("name");

						switch (name) {
						case "peon":
							Peon.selccionarPeon(newValue, getGameWorld());
							break;
						case "torre":
							Torre.seleccionarTorre(newValue, getGameWorld(), getHeight(), getWidth());
							break;
						case "caballo":
							Caballo.seleccionarCaballo(newValue, getGameWorld(), getHeight(), getWidth());
							break;
						case "alfil":
							Alfil.seleccionarAlfil(newValue, getGameWorld(), getHeight(), getWidth());
							break;
						case "reina":
							Reina.seleccionarReina(newValue, getGameWorld(), getHeight(), getWidth());
						case "rey":
							Rey.seleccionarRey(newValue, getGameWorld(), getHeight(), getWidth());
							break;
						default:
							break;
						}
					} else {
						mover(newValue);

					}
				}
			}

		});

		ArrayList<Entity> entities = getGameWorld().getEntities();
		for (int i = 1; i < entities.size(); i++) {
			if (entities.get(i).getProperties().exists("color") && entities.get(i).getString("color").equals("negra")) {
				entities.get(i).getComponent(SelectableComponent.class).setValue(false);
			} else {
				entities.get(i).getComponent(SelectableComponent.class).setValue(true);
			}
		}
	}

	@Override
	protected void initGameVars(Map<String, Object> vars) {
		vars.put("colorturno", "");
	}

	@Override
	protected void initUI() {
		getNotificationService().setBackgroundColor(Color.YELLOW);
		getNotificationService().setPosition(Position.RIGHT);
		cambioTurno("negra");
	}

	public static void generarTemporal(Point2D position, Color rgb, boolean enemy) {
		Entity posibleMov = Entities.builder().at(position)
				.viewFromNode(new Rectangle(Ajedrez.TILE_SIZE, Ajedrez.TILE_SIZE, rgb)).renderLayer(RenderLayer.TOP)
				.with(new SelectableComponent(true)).with("name", "temporal").with("enemy", enemy).buildAndAttach();
		Ajedrez.temporales.add(posibleMov);
	}

	public void mover(Entity nuevaPosicion) {
		Point2D position = nuevaPosicion.getPosition();
		if (!nuevaPosicion.getBoolean("enemy")) {

			getGameWorld().removeEntities(Ajedrez.temporales);
			fichaSeleccionada.setPosition(position);

		} else {

			getGameWorld().removeEntities(Ajedrez.temporales);
			Entity enemiga = null;

			if (position.getX() == 0 && position.getY() == 0) {
				enemiga = getGameWorld().getEntitiesAt(position).get(1);
			} else {
				enemiga = getGameWorld().getEntitiesAt(position).get(0);
			}

			if (fichaSeleccionada.getString("color").equals("blanca")) {
				Ajedrez.negrasEliminadas.add(enemiga);

			} else {
				Ajedrez.negrasEliminadas.add(enemiga);

			}

			getGameWorld().removeEntity(enemiga);
			fichaSeleccionada.setPosition(position);
		}

		Ajedrez.temporales.clear();
		String color = fichaSeleccionada.getString("color");
//		if (!Rey.hayJaque(color,getGameWorld())) {
		cambioTurno(color);
//		} else {
//			System.out.println("Jaque");
//		}
	}

	private void cambioTurno(String color) {

		ArrayList<Entity> entities = getGameWorld().getEntities();
		for (int i = 1; i < entities.size(); i++) {
			if (entities.get(i).getString("color").equals(color)) {
				entities.get(i).getComponent(SelectableComponent.class).setValue(false);
			} else {
				entities.get(i).getComponent(SelectableComponent.class).setValue(true);
			}
		}

		if (color.equals("blanca")) {
			getGameState().setValue("colorturno", "negras");
			getNotificationService().setTextColor(Color.BLACK);

		} else {
			getGameState().setValue("colorturno", "blancas");
			getNotificationService().setTextColor(Color.WHITE);
		}

		getNotificationService().pushNotification("Turno de las " + getGameState().getString("colorturno"));

	}

	@Override
	public String toString() {
		return "AJEDREZ";
	}
}
