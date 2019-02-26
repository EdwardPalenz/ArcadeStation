package main.launcher.uso;

import java.io.IOException;
import java.util.ArrayList;

import launcher.puntuaciones.Puntuacion;
import launcher.puntuaciones.Puntuaciones;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;

public class InformeFactory implements JRDataSource{

	Puntuaciones puntuaciones;
	private int indice = -1;
	private ArrayList<Puntuacion> puntos;
	public ArrayList<Puntuacion> load() throws IOException{
		puntuaciones=new Puntuaciones();
		puntuaciones.cargarPuntuaciones();
		puntos=new ArrayList<>();
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
		if("nombre".equals(jrField.getName())) {
			valor = puntos.get(indice).getNombre();
		}else if("puntos".equals(jrField.getName())) {
			valor = puntos.get(indice).getPuntos();
		}
		return valor;
	}
	
}
