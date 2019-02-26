package launcher.puntuaciones;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Puntuacion {

	public StringProperty nombre;
	public StringProperty juego;
	public IntegerProperty puntos;

	public Puntuacion(String name, int points, String game) {
		juego = new SimpleStringProperty(this, "juego", game);
		nombre = new SimpleStringProperty(this, "nombre", name);
		puntos = new SimpleIntegerProperty(this, "puntos", points);
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

	public StringProperty juegoProperty() {
		return this.juego;
	}
	

	public String getJuego() {
		return this.juegoProperty().get();
	}
	

	public void setJuego(final String juego) {
		this.juegoProperty().set(juego);
	}
	
}
