package main.launcher.uso;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ControlDeUSo {

	private HashMap<String, Integer> usoApps;
	private File ficheroUso;
	private List<String> usos = new ArrayList<>();
	private boolean recienCreado = false;

	public ControlDeUSo() throws IOException {
		ficheroUso = new File("src/main/resources/uso/UsoApps.txt");

		usoApps = new HashMap<>();

		if (!ficheroUso.exists()) {
			ficheroUso.createNewFile();
			
			setRecienCreado(true);
		} else {
			leerFicheroUsos();
		}
	}

	public void leerFicheroUsos() throws IOException {

		usos = Files.readAllLines(ficheroUso.toPath());

		generarHashMap();

	}

	private void generarHashMap() {
		for (String linea : usos) {
			String[] partes = linea.split(":");
			System.out.println(partes[0] + " " + partes[1]);
			usoApps.put(partes[0], Integer.parseInt(partes[1]));
		}
	}

	public HashMap<String, Integer> getUsoApps() {
		return usoApps;
	}

	public void setUsoApps(HashMap<String, Integer> usoApps) {
		this.usoApps = usoApps;
	}

	public File getFicheroUso() {
		return ficheroUso;
	}

	public boolean isRecienCreado() {
		return recienCreado;
	}

	public void setRecienCreado(boolean recienCreado) {
		this.recienCreado = recienCreado;
	}

}
