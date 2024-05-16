package org.adrianmorataya.controllers;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import org.adrianmorataya.system.Main;

public class MenuProgramadorController implements Initializable   {
    private Main escenarioPrincipal;
    @FXML Button btnRegresar2;
  
    
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
        if (event.getSource() == btnRegresar2){
            escenarioPrincipal.menuPrincipalView();
        }
    }
}
