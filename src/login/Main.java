package login;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Main extends Application {

	public static void main(String[] args){
		
		launch(args);
	}

	@Override
	public void start(Stage arg0) throws Exception {
		Parent root=FXMLLoader.load(getClass().getResource("Login.fxml"));
		Scene scene=new Scene(root);
		
		arg0.setScene(scene);
		arg0.setTitle("SISTEMA DE INVENTARIO GOREPA");
		arg0.initStyle(StageStyle.UNDECORATED);
		arg0.show();
		
	}
	
}
