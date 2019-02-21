package main.launcher.uso;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javafx.concurrent.Task;

public class ControlDeUSo {

	private HashMap<String, Integer> usoApps;
	private File ficheroUso;
	private List<String> usos = new ArrayList<>();

	public ControlDeUSo() throws IOException {
		ficheroUso = new File("src/main/launcher/uso/UsoApps.txt");

		if (!ficheroUso.exists())
			ficheroUso.createNewFile();

		usoApps = new HashMap<>();

		leerFicheroUsos();

	}

	private void leerFicheroUsos() throws IOException {

		Task<List<String>> leer = new Task<List<String>>() {

			@Override
			protected List<String> call() throws Exception {
				System.out.println("Leyendo usos");
				return Files.readAllLines(ficheroUso.toPath());
			}
		};

		leer.setOnSucceeded(e -> {
			System.out.println("usos leyendos");
			usos = (List<String>) e.getSource().getValue();
			generarHashMap();
		});

		leer.setOnFailed(e -> {
			System.err.println("nope");
		});
		new Thread(leer).start();

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


}
