package Photobooth;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class PhotoboothConfigController implements Initializable
{
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

	@Override
	public void initialize(URL url, ResourceBundle resources)
	{
		PhotoboothConfigModel config = new PhotoboothConfigModel();
		config.CaptureDirectory = "C:\\Captures\\";
		config.CaptureExtension = "*.jpg";
		config.PreviewSeconds = 5;
		config.FontName = "Gabriola";
		config.FontSize = 90;
		config.PromptText = "Press the button on the remote to take a picture.\nDon't forget to smile!";

		setFormConfiguration(config);
	}

	public void StartKiosk_OnClicked()
	{
		PhotoboothConfigModel photoboothConfig = readFormConfiguration();
		Parent root;
		try
		{
			root = FXMLLoader.load(getClass().getResource("KioskWindow.fxml"));
			Stage stage = new Stage();
			stage.setTitle("Kiosk Mode");
			stage.setScene(new Scene(root));
			stage.setFullScreen(true);
			stage.showAndWait();
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
