package main.launcher.uso;

import java.io.IOException;
import java.util.ArrayList;

import launcher.puntuaciones.Puntuacion;
import launcher.puntuaciones.Puntuaciones;

public class InformeFactory {

	Puntuaciones puntuaciones;
	public ArrayList<Puntuacion> load() throws IOException{
		puntuaciones=new Puntuaciones();
		puntuaciones.cargarPuntuaciones();
		ArrayList<Puntuacion> puntos=new ArrayList<>();
		puntos.addAll(puntuaciones.listaInforme());
		return puntos;
	}
	
}
