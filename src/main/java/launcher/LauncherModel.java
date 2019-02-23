package launcher;

import java.util.List;

import com.almasb.fxgl.app.GameApplication;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.image.Image;

public class LauncherModel {

	private ListProperty<Class<? extends GameApplication>> juegos;
	private IntegerProperty juegoSeleccionado;
	private ListProperty<Image> previews;
	private ListProperty<Image> gifPreviews;
	private BooleanProperty minimizar;

	public LauncherModel() {
		juegos = new SimpleListProperty<>(this, "juegos", FXCollections.observableArrayList());
		juegoSeleccionado = new SimpleIntegerProperty(this, "juegoSeleccionado", 0);
		previews = new SimpleListProperty<Image>(this, "previews", FXCollections.observableArrayList());
		gifPreviews = new SimpleListProperty<>(this, "gifPreviews", FXCollections.observableArrayList());
		minimizar = new SimpleBooleanProperty(this, "minimizar", false);
	}

	public ObservableList<Class<? extends GameApplication>> juegosProperty() {
		return juegos;
	}

	public List<Class<? extends GameApplication>> getJuegos() {
		return juegos.get();
	}

	public IntegerProperty juegoSeleccionadoProperty() {
		return juegoSeleccionado;
	}

	public int getJuegoSeleccionado() {
		return juegoSeleccionadoProperty().get();
	}

	public void setJuegoSeleccionado(int juegoSeleccionado) {
		juegoSeleccionadoProperty().set(juegoSeleccionado);
	}

	public ListProperty<Image> previewsProperty() {
		return previews;
	}

	public List<Image> getPreviews() {
		return previews.get();
	}

	public ListProperty<Image> gifPreviewsProperty() {
		return gifPreviews;
	}

	public List<Image> getGifPreviews() {
		return gifPreviews.get();
	}

	public BooleanProperty minimizarProperty() {
		return minimizar;
	}

	public boolean isMinimizar() {
		return minimizarProperty().get();
	}

	public void setMinimizar(boolean minimizar) {
		minimizarProperty().set(minimizar);
	}
}
