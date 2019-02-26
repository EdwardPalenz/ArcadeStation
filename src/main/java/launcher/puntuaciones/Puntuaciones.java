package launcher.puntuaciones;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.util.ArrayList;
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
import main.launcher.uso.InformeFactory;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporter;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.util.JRLoader;

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

	private InformeFactory informe;

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

	public void cargarPuntuaciones() {

		try {
			// Snake Classic
			String path = LauncherApp.APP_SCORE_DIR + File.separator + SnakeClassic.class.getSimpleName();
			File ficheroPuntutacion = new File(path);

			if (ficheroPuntutacion.exists()) {
				prepararPuntuaciones(new File(path + File.separator + "puntuaciones.txt"), snakeClassicTable, sCPuntos,"Snake Classic");
			}
			// Snake Evolution
			path = LauncherApp.APP_SCORE_DIR + File.separator + SnakeEvolution.class.getSimpleName();
			ficheroPuntutacion = new File(path);

			if (ficheroPuntutacion.exists()) {
				System.out.println("?");
				prepararPuntuaciones(new File(path + File.separator + "puntuaciones.txt"), snakeEvolutionTable,
						sEPuntos, "Snake Evolution");
			}

			// Space Invaders
			path = LauncherApp.APP_SCORE_DIR + File.separator + SpaceInvaders.class.getSimpleName();
			ficheroPuntutacion = new File(path);

			if (ficheroPuntutacion.exists()) {
				System.out.println("?");
				prepararPuntuaciones(new File(path + File.separator + "puntuaciones.txt"), spaceInvadersTable,
						sIPuntos, "Space Invaders");
			}

			// FuriOut
			path = LauncherApp.APP_SCORE_DIR + File.separator + Furiout.class.getSimpleName();
			ficheroPuntutacion = new File(path);

			if (ficheroPuntutacion.exists()) {
				System.out.println("?");
				prepararPuntuaciones(new File(path + File.separator + "puntuaciones.txt"), furiOutTable, fOPuntos, "FuriOut");
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public ArrayList<Puntuacion> listaInforme() throws JRException {
		
		ArrayList<Puntuacion> puntos = new ArrayList<>();
		puntos.addAll(snakeClassicTable.getItems());
		puntos.addAll(snakeEvolutionTable.getItems());
		puntos.addAll(furiOutTable.getItems());
		puntos.addAll(spaceInvadersTable.getItems());


		return puntos;
	}

	private void prepararPuntuaciones(File file, TableView<Puntuacion> tabla, TableColumn<Puntuacion, Number> column, String juego)
			throws IOException {

		List<String> puntuaciones = Files.readAllLines(file.toPath());

		for (String puntuacion : puntuaciones) {
			String[] punt = puntuacion.split(":");
			tabla.getItems().add(new Puntuacion(punt[0], Integer.parseInt(punt[1]),juego));

		}
		tabla.getSortOrder().add(column);
		tabla.getSortOrder().add(column);
		column.setSortType(TableColumn.SortType.DESCENDING);

	}

	public AnchorPane getView() {
		return view;
	}
}
