package launcher.puntuaciones;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import com.almasb.fxgl.app.GameApplication;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;

public class Puntuaciones implements Initializable {

    @FXML
    private AnchorPane view;

    @FXML
    private TabPane puntuacionesTabPane;

    @FXML
    private Tab snakeClassicTab;

    @FXML
    private Tab snakeEvolutionTab;

    @FXML
    private Tab spaceInvadersTab;

    @FXML
    private Tab furiOutTab;

	private List<Class<? extends GameApplication>> juegos;

	public Puntuaciones(List<Class<? extends GameApplication>> juegos) throws IOException {
		this.juegos = juegos;

		FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Puntuaciones.View"));
		loader.setController(this);
		loader.load();

	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {

	}

	public AnchorPane getView() {
		return view;
	}
}
