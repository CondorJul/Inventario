package funciones;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;

import org.apache.commons.codec.digest.DigestUtils;

import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Funciones {
	
	public static final int CORRECTO=1;
	public static final int INCORRECTO=0;
	public void mostrarInformacion(Label label, String mensaje, int validacion ){
		ObservableList<String> styleClass = label.getStyleClass();
        styleClass.removeAll(Collections.singleton("labelInformacionincorrecto"));  
        styleClass.removeAll(Collections.singleton("labelInformacioncorrecto"));
        
        if(validacion==CORRECTO){
        	 if (!styleClass.contains("labelInformacioncorrecto")) {
                 styleClass.add("labelInformacioncorrecto");

             }
             
        }else{
        	 if (! styleClass.contains("labelInformacionincorrecto")) {
                 styleClass.add("labelInformacionincorrecto");  

             }
        }
        label.setText(mensaje);
	}	
	
	
	
	public void setInterfazInterna (BorderPane stPane_ventana, String url , String css) throws IOException{
		
		FXMLLoader fXMLLoader = new FXMLLoader();
		fXMLLoader.load(getClass().getResource(url).openStream());
		BorderPane borderPane =fXMLLoader.getRoot();
		borderPane.getStylesheets().add(getClass().getResource(css ).toExternalForm());
        stPane_ventana.getChildren().clear();
        borderPane.setMinSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);
        borderPane.setPrefSize(400, 600);
        borderPane.setMaxSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);
        stPane_ventana.setCenter(borderPane);   
	}
	
	public void setInterfazInterna(BorderPane stPane_ventana, BorderPane stBorderPane){
		stPane_ventana.getChildren().clear();
		stBorderPane.setMinSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);
		stBorderPane.setPrefSize(400, 600);
		stBorderPane.setMaxSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);
        stPane_ventana.setCenter(stBorderPane);
	}
	
	public BorderPane obtenerBorderPaneInterna(String url , String css) throws IOException{
		FXMLLoader fXMLLoader = new FXMLLoader();
		fXMLLoader.load(getClass().getResource(url).openStream());
		BorderPane borderPane =fXMLLoader.getRoot();
		borderPane.getStylesheets().add(getClass().getResource(css ).toExternalForm());
		return borderPane;
	}
	
	public void  mostrarInterfazModal(String urlFxml, String css) throws IOException{
		FXMLLoader fXMLLoader=new FXMLLoader();
		fXMLLoader.setLocation(getClass().getResource(urlFxml));
		fXMLLoader.load();
		Parent parent= fXMLLoader.getRoot();
		Scene scene=new Scene(parent);
		scene.setFill(new Color(0,0,0,0));
		scene.getStylesheets().add(getClass().getResource(css).toExternalForm());
		Stage stage=new Stage();
		stage.setScene(scene);
		stage.initModality(Modality.APPLICATION_MODAL);
		stage.initStyle(StageStyle.TRANSPARENT);
		stage.show();
	}
	
	public void mostrarInterfazModalShowAndWait(String urlFxml, String css) throws IOException{
		FXMLLoader fXMLLoader=new FXMLLoader();
		fXMLLoader.setLocation(getClass().getResource(urlFxml));
		fXMLLoader.load();
		Parent parent= fXMLLoader.getRoot();
		Scene scene=new Scene(parent);
		scene.setFill(new Color(0,0,0,0));
		scene.getStylesheets().add(getClass().getResource(css).toExternalForm());
		Stage stage=new Stage();
		stage.setScene(scene);
		stage.initModality(Modality.APPLICATION_MODAL);
		stage.initStyle(StageStyle.TRANSPARENT);
		stage.showAndWait();	
	}
	
	public Image seleccionarImage(){
		 FileChooser fileChooser = new FileChooser();
	        FileChooser.ExtensionFilter extFilterjpg = new FileChooser.ExtensionFilter("jpg files (*.jpg)", "*.jpg");
	        FileChooser.ExtensionFilter extFilterpng = new FileChooser.ExtensionFilter("png files (*.png)", "*.png");
	        Image image=null;
	        fileChooser.getExtensionFilters().addAll(extFilterjpg, extFilterpng);
	        File file;
	        file = fileChooser.showOpenDialog(null);

	        if (file != null) {
	            if (file.length() < 6000000) {
					try {
						InputStream inputStream = new FileInputStream(file.getAbsolutePath());
		                 image = new Image(inputStream);
					} catch (IOException e) {
						e.printStackTrace();
					}
	                
	         } else {

	        	    Alert alert = new Alert(Alert.AlertType.INFORMATION);
	                alert.setTitle("Alerta");
	                alert.setHeaderText("Alerta");
	                alert.setContentText("Este archivo es demasidado grande :(.. \n");
	                alert.initStyle(StageStyle.UNDECORATED);
	                return null;

	            }

	        }
	        return image;
		
	}

	public String encriptar(String texto){
		
		return DigestUtils.md5Hex(texto); 
	
	}
	
	public boolean EliminarDatos(){
		Alert alert = new Alert(AlertType.CONFIRMATION, "¿Está seguro que desea Eliminar :( ?", ButtonType.YES, ButtonType.NO, ButtonType.CANCEL);
		alert.showAndWait();

		if (alert.getResult() == ButtonType.YES) {
		    return true;
		}else {
			return false;
		}
	}
}
