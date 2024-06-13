package org.adrianmorataya.system;

import java.io.IOException;
import java.io.InputStream;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.adrianmorataya.controllers.MenuCargoEmpleadoController;
import org.adrianmorataya.controllers.MenuClientesController;
import org.adrianmorataya.controllers.MenuComprasController;
import org.adrianmorataya.controllers.MenuEmailController;
import org.adrianmorataya.controllers.MenuEmpleadoController;
import org.adrianmorataya.controllers.MenuFacturaController;
import org.adrianmorataya.controllers.MenuPrincipalController;
import org.adrianmorataya.controllers.MenuProductosController;
import org.adrianmorataya.controllers.MenuProgramadorController;
import org.adrianmorataya.controllers.MenuProveedoresController;
import org.adrianmorataya.controllers.MenuTipoProductoController;

/*
Nombre: Steven Adrian Soto Morataya
Fecha de creacion: 11/04
Fecha de actualizacion: 11/04

*/

public class Main extends Application{
    private Stage escenarioPrincipal;
    private Scene escena;
    private final String URLVIEW = "/org/adrianmorataya/views/";
    
        /*
        FXMLLoades, Parent
        */
    
        @Override
        public void start(Stage escenarioPrincipal) throws IOException {
            this.escenarioPrincipal = escenarioPrincipal;
            this.escenarioPrincipal.setTitle("Mega Market");
            menuPrincipalView();
            //Parent root = FXMLLoader.load(getClass().getResource("/org/adrianmorataya/views/MenuPrincipalView.fxml"));
            //Scene escena = new Scene(root);
            //escenarioPrincipal.setScene(escena);
            escenarioPrincipal.show();
        }
        
        public Initializable cambiarEscena(String fxmlName, int width, int heigth) throws IOException{
            Initializable resultado = null;
            FXMLLoader loader = new FXMLLoader();
            InputStream file = Main.class.getResourceAsStream(URLVIEW + fxmlName);
            loader.setBuilderFactory(new JavaFXBuilderFactory());
            loader.setLocation(Main.class.getResource(URLVIEW + fxmlName));
            escena = new Scene((AnchorPane)loader.load(file), width, heigth);
            escenarioPrincipal.setScene(escena);
            escenarioPrincipal.sizeToScene();
            resultado = (Initializable)loader.getController();
            return resultado;
        }
        
        public void menuPrincipalView(){
            try{
                MenuPrincipalController menuPrincipalView = (MenuPrincipalController)cambiarEscena("MenuPrincipalView.fxml", 600, 400);
                menuPrincipalView.setEscenarioPrincipal(this);
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        
        public void menuClientesView(){
            try{
                MenuClientesController menuClientesView = (MenuClientesController)cambiarEscena("MenuClientesView.fxml", 1023, 575);
                menuClientesView.setEscenarioPrincipal(this);
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        
        public void menuProgramadorView(){
            try{
                MenuProgramadorController menuProgramadorView = (MenuProgramadorController)cambiarEscena("MenuProgramadorView.fxml", 820, 460);
                menuProgramadorView.setEscenarioPrincipal(this);
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        
        public void menuProveedoresView(){
            try{
                MenuProveedoresController menuProveedoresView = (MenuProveedoresController)cambiarEscena("MenuProveedoresView.fxml", 1002, 568);
                menuProveedoresView.setEscenarioPrincipal(this);
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        
        public void menuComprasView(){
            try{
                MenuComprasController menuComprasView = (MenuComprasController)cambiarEscena("MenuComprasView.fxml", 1053, 567);
                menuComprasView.setEscenarioPrincipal(this);
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        
        public void menuTipoProductoView(){
            try{
                MenuTipoProductoController menuTipoProductoView = (MenuTipoProductoController)cambiarEscena("MenuTipoProductoView.fxml", 1053, 567);
                menuTipoProductoView.setEscenarioPrincipal(this);
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        
        public void menuCargoEmpleadoView(){
            try{
                MenuCargoEmpleadoController menuCargoEmpleadoView = (MenuCargoEmpleadoController)cambiarEscena("MenuCargoEmpleadoView.fxml", 1053, 567);
                menuCargoEmpleadoView.setEscenarioPrincipal(this);
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        
        public void menuProductosView(){
            try{
                MenuProductosController menuProductosView = (MenuProductosController)cambiarEscena("MenuProductosView.fxml", 1026, 580);
                menuProductosView.setEscenarioPrincipal(this);
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        
        public void menuEmpleadosView(){
            try{
                MenuEmpleadoController menuEmpleadosView = (MenuEmpleadoController)cambiarEscena("MenuEmpleadosView.fxml", 1053, 567);
                menuEmpleadosView.setEscenarioPrincipal(this);
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        
        public void menuFacturasView(){
            try{
                MenuFacturaController menuFacturasView = (MenuFacturaController)cambiarEscena("MenuFacturasView.fxml", 1053, 567);
                menuFacturasView.setEscenarioPrincipal(this);
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        
        public void menuEmailView(){
            try{
                MenuEmailController menuEmailView = (MenuEmailController)cambiarEscena("MenuEmailPView.fxml", 1053, 567);
                menuEmailView.setEscenarioPrincipal(this);
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        
        public static void main(String args[]){
            launch(args);
        }
        
}
