package main.launcher.uso;

import java.util.ArrayList;

import launcher.puntuaciones.Puntuacion;
import launcher.puntuaciones.Puntuaciones;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;

public class InformeFactory implements JRDataSource {

	static Puntuaciones puntuaciones;
	private int indice = -1;
	private static ArrayList<Puntuacion> puntos;

	public ArrayList<Puntuacion> load() throws Exception {
		puntuaciones = new Puntuaciones();
		puntos = new ArrayList<>();
		puntos.addAll(puntuaciones.listaInforme());
		return puntos;
	}

	@Override
	public boolean next() throws JRException {
		return ++indice < puntos.size();
	}

	@Override
	public Object getFieldValue(JRField jrField) throws JRException {
		Object valor = null;
		if ("nombre".equals(jrField.getName())) {
			valor = puntos.get(indice).getNombre();
		} else if ("puntos".equals(jrField.getName())) {
			valor = puntos.get(indice).getPuntos();
		}
		return valor;
	}

	public void addPuntuacion(Puntuacion puntuacion) {
		this.puntos.add(puntuacion);
	}

}
