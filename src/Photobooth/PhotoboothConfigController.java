package Photobooth;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.input.KeyEvent;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Key;
import java.util.ResourceBundle;

public class PhotoboothConfigController implements Initializable
{
	@FXML //  fx:id="containerPane"
	private GridPane containerPane; // Value injected by FXMLLoader

	@FXML
	private TextField CaptureDirectory;
	@FXML
	private TextField CaptureExtension;
	@FXML
	public TextField PreviewSeconds;
	@FXML
	public TextField FontName;
	@FXML
	public TextField FontSize;
	@FXML
	public TextArea PromptText;


	private Stage primaryStage;
	private Scene configurationScene;

	@Override
	public void initialize(URL url, ResourceBundle resources)
	{
		PhotoboothConfigModel config = new PhotoboothConfigModel();
		config.CaptureDirectory = Paths.get("").toAbsolutePath().toString();
		config.CaptureExtension = "*.jpg";
		config.PreviewSeconds = 5;
		config.FontName = "Gabriola";
		config.FontSize = 90;
		config.PromptText = "Press the button on the remote to take a picture.\nDon't forget to smile!";

		setFormConfiguration(config);
	}

	public void setStageAndScene(Stage stage, Scene scene)
	{
		this.primaryStage = stage;
		this.configurationScene = scene;
	}

	public void StartKiosk_OnClicked()
	{
		PhotoboothConfigModel photoboothConfig = readFormConfiguration();

		try
		{
			FXMLLoader loader = new FXMLLoader(getClass().getResource("KioskWindow.fxml"));
			Parent root = (Parent)loader.load();
			Scene kioskScene = new Scene(root, 600, 400);
			primaryStage.setScene(kioskScene);
			primaryStage.setFullScreen(true);

			KioskWindowController kioskController = loader.getController();
			kioskController.setStageAndScene(primaryStage, kioskScene);
			kioskController.setConfiguration(photoboothConfig);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	private void setFormConfiguration(PhotoboothConfigModel config)
	{
		CaptureDirectory.setText(config.CaptureDirectory);
		CaptureExtension.setText(config.CaptureExtension);
		PreviewSeconds.setText(Integer.toString(config.PreviewSeconds));
		FontName.setText(config.FontName);
		FontSize.setText(Double.toString(config.FontSize));
		PromptText.setText(config.PromptText);
	}

	private PhotoboothConfigModel readFormConfiguration()
	{
		PhotoboothConfigModel photoboothConfig = new PhotoboothConfigModel();
		photoboothConfig.CaptureDirectory = CaptureDirectory.getText();
		photoboothConfig.CaptureExtension = CaptureExtension.getText();
		photoboothConfig.PreviewSeconds = Integer.parseInt(PreviewSeconds.getText());
		photoboothConfig.FontName = FontName.getText();
		photoboothConfig.FontSize = Double.parseDouble(FontSize.getText());
		photoboothConfig.PromptText = PromptText.getText();
		return photoboothConfig;
	}
}
