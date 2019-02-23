package launcher.puntuaciones;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Puntuacion {

	private StringProperty nombre;

	private IntegerProperty puntos;

	public Puntuacion() {
		nombre = new SimpleStringProperty(this, "nombre");
		puntos = new SimpleIntegerProperty(this, "puntos");
	}

	public IntegerProperty puntosProperty() {
		return puntos;
	}

	public int getPuntos() {
		return puntosProperty().get();
	}

	public void setPuntos(int puntos) {
		puntosProperty().set(puntos);
	}

	public StringProperty nombreProperty() {
		return nombre;
	}

	public String getNombre() {
		return nombreProperty().get();
	}

	public void setNombre(String nombre) {
		nombreProperty().set(nombre);
	}
}
