package launcher.puntuaciones;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.util.List;
import java.util.ResourceBundle;

import games.furiout.Furiout;
import games.snakeClassic.SnakeClassic;
import games.snakeevolution.SnakeEvolution;
import games.spaceinvaders.spaceinvaders.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import launcher.LauncherApp;

public class Puntuaciones implements Initializable {

	@FXML
	private AnchorPane view;

	@FXML
	private TabPane puntuacionesTabPane;

	@FXML
	private Tab snakeClassicTab;

	@FXML
	private TableView<Puntuacion> snakeClassicTable;

	@FXML
	private TableColumn<Puntuacion, String> sCNombre;

	@FXML
	private TableColumn<Puntuacion, Number> sCPuntos;

	@FXML
	private Tab snakeEvolutionTab;

	@FXML
	private TableView<Puntuacion> snakeEvolutionTable;

	@FXML
	private TableColumn<Puntuacion, String> sENombre;

	@FXML
	private TableColumn<Puntuacion, Number> sEPuntos;

	@FXML
	private Tab spaceInvadersTab;

	@FXML
	private TableView<Puntuacion> spaceInvadersTable;

	@FXML
	private TableColumn<Puntuacion, String> sINombre;

	@FXML
	private TableColumn<Puntuacion, Number> sIPuntos;

	@FXML
	private Tab furiOutTab;

	@FXML
	private TableView<Puntuacion> furiOutTable;

	@FXML
	private TableColumn<Puntuacion, String> fONombre;

	@FXML
	private TableColumn<Puntuacion, Number> fOPuntos;

	public Puntuaciones() throws IOException {

		FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/PuntuacionesView.fxml"));
		loader.setController(this);
		loader.load();

	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		/* Bindeo de las propiedades a las colunmas */
		bindProperties(sCNombre, sCPuntos);
		bindProperties(sENombre, sEPuntos);
		bindProperties(sINombre, sIPuntos);
		bindProperties(fONombre, fOPuntos);

		cargarPuntuaciones();

	}

	private void bindProperties(TableColumn<Puntuacion, String> nombreColumn,
			TableColumn<Puntuacion, Number> puntosColumn) {

		nombreColumn.setCellValueFactory(value -> value.getValue().nombreProperty());

		puntosColumn.setCellValueFactory(value -> value.getValue().puntosProperty());

	}

	private void cargarPuntuaciones() {

		try {
			// Snake Classic
			String path = LauncherApp.APP_SCORE_DIR + File.separator + SnakeClassic.class.getSimpleName();
			File ficheroPuntutacion = new File(path);

			if (ficheroPuntutacion.exists()) {
				prepararPuntuaciones(new File(path + File.separator + "puntuaciones.txt"), snakeClassicTable, sCPuntos);
			}
			// Snake Evolution
			path = LauncherApp.APP_SCORE_DIR + File.separator + SnakeEvolution.class.getSimpleName();
			ficheroPuntutacion = new File(path);

			if (ficheroPuntutacion.exists()) {
				System.out.println("?");
				prepararPuntuaciones(new File(path + File.separator + "puntuaciones.txt"), snakeEvolutionTable,
						sEPuntos);
			}

			// Space Invaders
			path = LauncherApp.APP_SCORE_DIR + File.separator + SpaceInvaders.class.getSimpleName();
			ficheroPuntutacion = new File(path);

			if (ficheroPuntutacion.exists()) {
				System.out.println("?");
				prepararPuntuaciones(new File(path + File.separator + "puntuaciones.txt"), spaceInvadersTable,
						sIPuntos);
			}

			// FuriOut
			path = LauncherApp.APP_SCORE_DIR + File.separator + Furiout.class.getSimpleName();
			ficheroPuntutacion = new File(path);

			if (ficheroPuntutacion.exists()) {
				System.out.println("?");
				prepararPuntuaciones(new File(path + File.separator + "puntuaciones.txt"), furiOutTable, fOPuntos);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void prepararPuntuaciones(File file, TableView<Puntuacion> tabla, TableColumn<Puntuacion, Number> column)
			throws IOException {

		List<String> puntuaciones = Files.readAllLines(file.toPath());

		for (String puntuacion : puntuaciones) {
			String[] punt = puntuacion.split(":");
			tabla.getItems().add(new Puntuacion(punt[0], Integer.parseInt(punt[1])));

		}
		tabla.getSortOrder().add(column);
		tabla.getSortOrder().add(column);
		column.setSortType(TableColumn.SortType.DESCENDING);

	}

	public AnchorPane getView() {
		return view;
	}
}
