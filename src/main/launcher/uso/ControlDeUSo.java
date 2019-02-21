package main.launcher.uso;

import java.io.File;
import java.io.IOException;
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
		ficheroUso = new File("main/launcher/uso/UsoApps.txt");

		if (!ficheroUso.exists())
			ficheroUso.createNewFile();

		usoApps = new HashMap<>();

		leerFicheroUsos();

	}

	private void leerFicheroUsos() throws IOException {

		Task<List<String>> leer = new Task<List<String>>() {

			@Override
			protected List<String> call() throws Exception {
				return Files.readAllLines(ficheroUso.toPath());
			}
		};

		leer.setOnSucceeded(e -> {
			usos = (List<String>) e.getSource().getValue();
			generarHashMap();
		});
		
		new Thread(leer).start();
		
		
	}

	private void generarHashMap() {
	// TODO Auto-generated method stub
	
}
public HashMap<String, Integer> getUsoApps() {
		return usoApps;
	}

	public void setUsoApps(HashMap<String, Integer> usoApps) {
		this.usoApps = usoApps;
	}

}
