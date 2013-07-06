package Photobooth;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import static java.lang.Thread.sleep;

public class KioskWindowController implements Initializable, IDirectoryWatcherObserver
{
	private Stage stage;
	private Scene scene;
	private PhotoboothConfigModel configuration;

	private DirectoryWatcherThread directoryWatcherThread;

	@FXML //  fx:id="kioskPane"
	private VBox kioskPane; // Value injected by FXMLLoader

	public void setStageAndScene(Stage stage, Scene scene)
	{
		this.stage = stage;
		this.scene = scene;
		setEventHandlers();
	}

	public void setConfiguration(PhotoboothConfigModel configuration)
	{
		this.configuration = configuration;
		showPrompt();
		startMonitoringImageDirectory();
	}

	@Override // This method is called by the FXMLLoader when initialization is complete
	public void initialize(URL fxmlFileLocation, ResourceBundle resources)
	{
		assert kioskPane != null : "fx:id=\"kioskPane\" was not injected: check your FXML file 'KioskWindow.fxml'.";

		// initialize your logic here: all @FXML variables will have been injected
	}

	private void setEventHandlers()
	{
		scene.setOnKeyPressed(new EventHandler<KeyEvent>()
		{
			public void handle(final KeyEvent keyEvent)
			{
				if (keyEvent.getCode() == KeyCode.ESCAPE) {
					keyEvent.consume();
					closeWindow();
				}
			}
		});
	}

	private void closeWindow()
	{
		System.out.println("KioskWindowController.closeWindow()");
		directoryWatcherThread.sendStopCommand();
		stage.close();
	}

	private void startMonitoringImageDirectory()
	{
		try
		{
			directoryWatcherThread = new DirectoryWatcherThread(configuration.CaptureDirectory, this);
			directoryWatcherThread.start();
		}
		catch (java.io.IOException e)
		{
			// We'll end up here if we pass in a bad directory path
			return;
		}
	}

	@Override
	public void onNewFile(String filepath)
	{
		System.out.format("New file in directory: %s\n", filepath);
		showImage(filepath);

		try
		{
			sleep(5 * 1000);
		}
		catch (InterruptedException e)
		{
			// Do nothing
		}

		showPrompt();
	}

	private void showPrompt()
	{
		Platform.runLater(new Runnable()
		{
			@Override
			public void run()
			{
				Font englishFont = new Font(configuration.FontName, configuration.FontSize);
				kioskPane.getChildren().clear();

				String[] promptLines = configuration.PromptText.split("\n");

				for (String line : promptLines)
				{
					addTextLine(line, englishFont);
				}
			}
		});
	}

	private void addTextLine(String text, Font font)
	{
		Label label = new Label(text);
		label.setTextFill(Paint.valueOf("#1000cc"));
		label.setFont(font);
		kioskPane.getChildren().add(label);
	}

	private void showImage(final String filepath)
	{
		Platform.runLater(new Runnable() {
			@Override
			public void run()
			{

				Image image = new Image("file:" + filepath);

				ImageView imageView = new ImageView(image);

				// TODO : figure out the image scaling for ones that are too large
				if (imageView.getImage().getHeight() > scene.getHeight() || imageView.getImage().getWidth() > scene.getWidth())
				{
					imageView.setPreserveRatio(true);
					imageView.setSmooth(true);
					imageView.setFitHeight(scene.getHeight());
					imageView.setFitWidth(scene.getWidth());
				}

				kioskPane.getChildren().clear();
				kioskPane.getChildren().add(imageView);
			}
		});
	}
}
