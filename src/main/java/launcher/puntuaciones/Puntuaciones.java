package launcher.puntuaciones;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import com.almasb.fxgl.app.GameApplication;

import games.snakeClassic.SnakeClassic;
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

	private List<Class<? extends GameApplication>> juegos;

	public Puntuaciones(List<Class<? extends GameApplication>> juegos) throws IOException {
		this.juegos = juegos;

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
		// Snake Classic
		String path = LauncherApp.APP_SCORE_DIR + SnakeClassic.class.getSimpleName();
		File ficheroPuntutacion = new File(path);

		if (ficheroPuntutacion.exists()) {
			prepararPuntuaciones(new File(path + File.separator + "puntuaciones.txt"), snakeClassicTable);
		}

	}

	private void prepararPuntuaciones(File file, TableView<Puntuacion> snakeClassicTable2) {
		// TODO Auto-generated method stub
		
	}

	public AnchorPane getView() {
		return view;
	}
}
