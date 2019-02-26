package launcher;

import java.io.File;
import java.io.InputStream;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import main.launcher.uso.InformeFactory;
import net.sf.jasperreports.engine.JRExporter;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.export.JRPdfExporter;

public class LauncherApp extends Application {

	public static final String APP_SCORE_DIR = System.getProperty("user.home") + File.separator + "Documents"
			+ File.separator + "My Games" + File.separator + "ArcadeStation";
	private static Stage primaryStage;

	private LauncherController controller;

	private static InformeFactory informe;

	@Override
	public void init() throws Exception {
		controller = new LauncherController();
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		LauncherApp.primaryStage = primaryStage;

		crearDirPuntuaciones();

		primaryStage.initStyle(StageStyle.UNDECORATED);
		primaryStage.setTitle("Arcade Station v0.1");
		Scene scene = new Scene(controller.getView());
		primaryStage.setScene(scene);
		scene.getStylesheets().add("/styles/LauncherBaseStyle.css");
		primaryStage.getIcons().add(new Image("/assets/textures/launcherIcon.png"));
		primaryStage.show();

	}

	@Override
	public void stop() throws Exception {

		informe = new InformeFactory();
		informe.load();

		InputStream is = this.getClass().getResourceAsStream("/reports/Informe3.jrxml");
		JasperReport reporte = JasperCompileManager.compileReport(is);

		JasperPrint jasperPrint = JasperFillManager.fillReport(reporte, null, informe);
//		JasperViewer.viewReport(jasperPrint, false);

		JRExporter exporter = new JRPdfExporter();
		exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
		exporter.setParameter(JRExporterParameter.OUTPUT_FILE, new java.io.File("reporte.pdf"));
		exporter.exportReport();

	}

	private void crearDirPuntuaciones() {
		File dirMyGames = new File(
				System.getProperty("user.home") + File.separator + "Documents" + File.separator + "My Games");
		if (!dirMyGames.exists())
			dirMyGames.mkdir();

		File dir = new File(APP_SCORE_DIR);
		try {
			if (!dir.exists())
				dir.mkdir();
		} catch (SecurityException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		launch(args);
	}

	public static Stage getPrimaryStage() {
		return primaryStage;
	}

	public static void setPrimaryStage(Stage primaryStage) {
		LauncherApp.primaryStage = primaryStage;
	}

}
