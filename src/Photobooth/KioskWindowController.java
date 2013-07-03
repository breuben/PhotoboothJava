package Photobooth;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;
import java.nio.file.*;
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

	private DirectoryWatcherThread directoryWatcherThread;

	@FXML //  fx:id="kioskPane"
	private VBox kioskPane; // Value injected by FXMLLoader

	public void setStageAndScene(Stage stage, Scene scene)
	{
		this.stage = stage;
		this.scene = scene;
		setEventHandlers();
	}

	@Override // This method is called by the FXMLLoader when initialization is complete
	public void initialize(URL fxmlFileLocation, ResourceBundle resources)
	{
		assert kioskPane != null : "fx:id=\"kioskPane\" was not injected: check your FXML file 'KioskWindow.fxml'.";

		// initialize your logic here: all @FXML variables will have been injected

		showPrompt();
		startMonitoringImageDirectory();
	}

	private void setEventHandlers()
	{
		scene.setOnKeyPressed(new EventHandler<KeyEvent>()
		{
			public void handle(final KeyEvent keyEvent)
			{
				if (keyEvent.getCode() == KeyCode.ESCAPE)
				{
					keyEvent.consume();
					closeWindow();
				}
			}
		});
	}

	private void closeWindow()
	{
		directoryWatcherThread.interrupt();
		stage.close();
	}

	private void startMonitoringImageDirectory()
	{
		String filepath = "C:\\Captures";
		try
		{
			directoryWatcherThread = new DirectoryWatcherThread(filepath, this);
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
				String promptText = "Press the button on the remote to take a picture.\nDon't forget to smile!\n茄子!";

				double fontSize = 100;
				Font englishFont = new Font("Gabriola", fontSize);
				Font mandarinFont = new Font("FangSong", fontSize);
				kioskPane.getChildren().clear();

				String[] promptLines = promptText.split("\n");

				for (String line : promptLines)
				{
					if(line.codePointAt(0) > 127)
						addTextLine(line, mandarinFont);
					else
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
				FileInputStream fileInputStream;

				try
				{
					fileInputStream = new FileInputStream(filepath);
				}
				catch (FileNotFoundException e)
				{
					System.out.println(e.getMessage());
					return;
				}

				Image image = new Image(fileInputStream);
				ImageView imageView = new ImageView(image);

				// TODO : figure out the image scaling for ones that are too large

				kioskPane.getChildren().clear();
				kioskPane.getChildren().add(imageView);
			}
		});



//		Image image = imagePane.getImage();
//		Rectangle2D screenBounds = Screen.getPrimary().getBounds();
//
//		if (image.getHeight() > screenBounds.getHeight() || image.getWidth() > screenBounds.getWidth())
//		{
//			imagePane.setFitHeight(screenBounds.getHeight());
//			imagePane.setFitWidth(screenBounds.getWidth());
//		}
//		else
//		{
//			imagePane.setFitHeight(image.getHeight());
//			imagePane.setFitWidth(image.getWidth());
//		}
//		imagePane.setVisible(false);
//		labelPrompt.setText("Press the button on the remote to take a picture.\nDon't forget to smile!"); labelPrompt.setFont(new Font("Gabriola", 90));
	}
}
