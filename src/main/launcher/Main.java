package main.launcher;

import javafx.application.Application;

public class Main {

	public static void main(String[] args) {
		System.setProperty("javafx.preloader", PreloaderLauncher.class.getName());
		Application.launch(LauncherApp.class, args);
	}

}
