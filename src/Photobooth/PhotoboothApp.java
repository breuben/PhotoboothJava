package Photobooth;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class PhotoboothApp extends Application
{
	public static void main(String[] args)
	{
		launch(args);
	}

    @Override
    public void start(Stage primaryStage) throws Exception
    {
	    FXMLLoader loader = new FXMLLoader(getClass().getResource("PhotoboothConfigForm.fxml"));
	    Parent root = (Parent)loader.load();
	    Scene scene = new Scene(root);
	    Image icon = new Image(getClass().getResourceAsStream("camera.png"));

        primaryStage.setTitle("Photobooth");
        primaryStage.setScene(scene);
	    primaryStage.getIcons().add(icon);
	    PhotoboothConfigController controller = loader.getController();
	    controller.setStageAndScene(primaryStage, scene);
        primaryStage.show();
    }
}
