package org.adrianmorataya.controllers;

import java.net.URL;
import static java.time.Clock.system;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import org.adrianmorataya.system.Main;

/*
Herencia multiple concepto, Interfaces. POO
*/
public class MenuPrincipalController implements Initializable  {
    private Main escenarioPrincipal;
    @FXML MenuItem btnMenuClientes;
    @FXML MenuItem btnProgramador;
    @FXML MenuItem btnMenuProveedores;
    @FXML MenuItem btnMenuCompras;
    @FXML MenuItem btnTipoProducto;
    @FXML MenuItem btnCargoEmpleado;
    @FXML MenuItem btnProductos;
    @FXML Button btnSalir;
  
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        
    }
      public Main getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Main escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }
    
    @FXML
    public void handleButtonAction(ActionEvent event) {
        if (event.getSource() == btnMenuClientes){
            escenarioPrincipal.menuClientesView();
        }else if(event.getSource() == btnProgramador){
            escenarioPrincipal.menuProgramadorView();
        }else if(event.getSource() == btnMenuProveedores){
            escenarioPrincipal.menuProveedoresView();
        }else if(event.getSource() == btnMenuCompras){
            escenarioPrincipal.menuComprasView();
        }else if(event.getSource() == btnTipoProducto){
            escenarioPrincipal.menuTipoProductoView();
        }else if(event.getSource() == btnCargoEmpleado){
            escenarioPrincipal.menuCargoEmpleadoView();
        }else if(event.getSource() == btnProductos){
            escenarioPrincipal.menuProductosView();
        }else if(event.getSource() == btnSalir){
            System.exit(0);
        }
    }
    
}
