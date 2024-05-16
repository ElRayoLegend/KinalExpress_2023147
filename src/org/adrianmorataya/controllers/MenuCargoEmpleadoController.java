package org.adrianmorataya.controllers;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javax.swing.JOptionPane;
import org.adrianmorataya.bean.CargoEmpleado;
import org.adrianmorataya.dao.Conexion;
import org.adrianmorataya.system.Main;

public class MenuCargoEmpleadoController implements Initializable   {
    private Main escenarioPrincipal;
    private enum operaciones {AGREGAR, ELIMINAR, EDITAR, ACTUALIZAR, CANCELAR, NINGUNO}
    private operaciones tipoDeOperaciones = operaciones.NINGUNO;
    private ObservableList<CargoEmpleado> listaCargoEmpleado;
    @FXML private Button btnRegresar3;
    @FXML private TextField txtCodCE;
    @FXML private TextField txtNomCE;
    @FXML private TextField txtDescCE;
    @FXML private TableView tblCargoEmpleado;
    @FXML private TableColumn colCodCE;
    @FXML private TableColumn colNomCE;
    @FXML private TableColumn colDescCE;
    @FXML private Button btnAgregar;
    @FXML private Button btnEliminar;
    @FXML private Button btnEditar;
    @FXML private Button btnReporte;
    @FXML private ImageView imgAgregar;
    @FXML private ImageView imgEliminar;
    @FXML private ImageView imgEditar;
    @FXML private ImageView imgReporte;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
        txtCodCE.setDisable(true);
        txtNomCE.setDisable(true);
        txtDescCE.setDisable(true);
    }
    
    public void cargarDatos(){
       tblCargoEmpleado.setItems(getCargoEmpleado());
       colCodCE.setCellValueFactory(new PropertyValueFactory<CargoEmpleado, Integer>("codigoCargoEmpleado"));
       colNomCE.setCellValueFactory(new PropertyValueFactory<CargoEmpleado, String>("nombreCargo"));
       colDescCE.setCellValueFactory(new PropertyValueFactory<CargoEmpleado, String>("descripcionCargo"));
    }
    
    public void seleccionarElemento(){
        txtCodCE.setText(String.valueOf(((CargoEmpleado)tblCargoEmpleado.getSelectionModel().getSelectedItem()).getCodigoCargoEmpleado()));
        txtNomCE.setText((((CargoEmpleado)tblCargoEmpleado.getSelectionModel().getSelectedItem()).getNombreCargo()));
        txtDescCE.setText((((CargoEmpleado)tblCargoEmpleado.getSelectionModel().getSelectedItem()).getDescripcionCargo()));
    }
    
    public ObservableList<CargoEmpleado> getCargoEmpleado(){
        ArrayList<CargoEmpleado>lista = new ArrayList<>();
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarCargoEmpleado()}");
            ResultSet resultado = procedimiento.executeQuery();
            while (resultado.next()) {
                lista.add(new CargoEmpleado (resultado.getInt("codigoCargoEmpleado"),
                                        resultado.getString("nombreCargo"),
                                        resultado.getString("descripcionCargo")
                ));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return listaCargoEmpleado = FXCollections.observableArrayList(lista);
    }
    
    public void agregar(){
        switch (tipoDeOperaciones) {
            case NINGUNO:
                activarControles();
                btnAgregar.setText("GUARDAR");
                btnEliminar.setText("CANCELAR");
                btnEditar.setDisable(true);
                btnReporte.setDisable(true);
                imgAgregar.setImage(new Image("/org/adrianmorataya/image/GUARDAR.png"));
                imgEliminar.setImage(new Image("/org/adrianmorataya/image/CANCELAR.png"));
                tipoDeOperaciones = operaciones.ACTUALIZAR;
                break;
            case ACTUALIZAR:
                guardar();
                desactivarControles();
                limpiarControles();
                btnAgregar.setText("AGREGAR");
                btnEliminar.setText("ELIMINAR");
                btnEditar.setDisable(false);
                btnReporte.setDisable(false);
                imgAgregar.setImage(new Image("/org/adrianmorataya/image/UsuarioAnadir.png"));
                imgEliminar.setImage(new Image("/org/adrianmorataya/image/UsuarioEliminar.png"));
                tipoDeOperaciones = operaciones.NINGUNO;
                break;
            default:
                throw new AssertionError();
        }
    }
    
    public void guardar(){
        CargoEmpleado registro = new CargoEmpleado();
        registro.setCodigoCargoEmpleado(Integer.parseInt(txtCodCE.getText()));
        registro.setDescripcionCargo(txtDescCE.getText());
        registro.setNombreCargo(txtNomCE.getText());
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_AgregarCargoEmpleado(?, ?, ?)}");
            procedimiento.setInt(1, registro.getCodigoCargoEmpleado());
            procedimiento.setString(2, registro.getNombreCargo());
            procedimiento.setString(3, registro.getDescripcionCargo());
            procedimiento.execute();
            listaCargoEmpleado.add(registro);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void eliminar(){
        switch (tipoDeOperaciones) {
            case ACTUALIZAR:
                desactivarControles();
                limpiarControles();
                btnAgregar.setText("AGREGAR");
                btnEliminar.setText("ELIMINAR");
                btnEditar.setDisable(false);
                btnReporte.setDisable(false);
                imgAgregar.setImage(new Image("/org/adrianmorataya/image/UsuarioAnadir.png"));
                imgEliminar.setImage(new Image("/org/adrianmorataya/image/UsuarioEliminar.png"));
                tipoDeOperaciones = operaciones.NINGUNO;
                break;
            default:
                if (tblCargoEmpleado.getSelectionModel().getSelectedItem() != null) {
                    int respuesta = JOptionPane.showConfirmDialog(null, "Confirmar para eliminar el registro", "Confirmacion Eliminar Cargo", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if (respuesta == JOptionPane.YES_NO_OPTION) {
                        try{
                            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_EliminarCargoEmpleado(?)}");
                            procedimiento.setInt(1, ((CargoEmpleado)tblCargoEmpleado.getSelectionModel().getSelectedItem()).getCodigoCargoEmpleado());
                            procedimiento.execute();
                            listaCargoEmpleado.remove(tblCargoEmpleado.getSelectionModel().getSelectedItem());
                        }catch(Exception e){
                            e.printStackTrace();
                        }
                    }
                }else{
                    JOptionPane.showMessageDialog(null, "Debe seleccionar un elemento!");
                }
        }
    }
    
    public void editar(){
        switch (tipoDeOperaciones) {
            case NINGUNO:
                if (tblCargoEmpleado.getSelectionModel().getSelectedItem() != null) {
                    btnEditar.setText("ACTUALIZAR");
                    btnReporte.setText("CANCELAR");
                    btnAgregar.setDisable(true);
                    btnEliminar.setDisable(true);
                    imgEditar.setImage(new Image("/org/adrianmorataya/image/GUARDAR.png"));
                    imgReporte.setImage(new Image("/org/adrianmorataya/image/CANCELAR.png"));
                    activarControles();
                    txtCodCE.setEditable(false);
                    tipoDeOperaciones = operaciones.ACTUALIZAR;
                }else{
                    JOptionPane.showMessageDialog(null, "Debe seleccionar algun elemento!");
                }
                break;
            case ACTUALIZAR:
                actualizar();
                btnEditar.setText("EDITAR");
                btnReporte.setText("REPORTES");
                btnAgregar.setDisable(false);
                btnEliminar.setDisable(false);
                imgEditar.setImage(new Image("/org/adrianmorataya/image/UsuarioEditar.png"));
                imgReporte.setImage(new Image("/org/adrianmorataya/image/UsuarioReportes.png"));
                desactivarControles();
                limpiarControles();
                tipoDeOperaciones = operaciones.NINGUNO;
                cargarDatos();
                break;
            default:
                throw new AssertionError();
        }
    }
    
    public void actualizar(){
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_EditarCargoEmpleado(?, ?, ?)}");
            CargoEmpleado registro = ((CargoEmpleado)tblCargoEmpleado.getSelectionModel().getSelectedItem());
            registro.setNombreCargo(txtNomCE.getText());
            registro.setDescripcionCargo(txtDescCE.getText());
            procedimiento.setInt(1, registro.getCodigoCargoEmpleado());
            procedimiento.setString(2, registro.getNombreCargo());
            procedimiento.setString(3, registro.getDescripcionCargo());
            procedimiento.execute();
            listaCargoEmpleado.add(registro);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void reporte(){
        switch (tipoDeOperaciones) {
            case ACTUALIZAR:
                desactivarControles();
                limpiarControles();
                btnEditar.setText("EDITAR");
                btnReporte.setText("REPORTES");
                btnAgregar.setDisable(false);
                btnEliminar.setDisable(false);
                imgEditar.setImage(new Image("/org/adrianmorataya/image/UsuarioEditar.png"));
                imgReporte.setImage(new Image("/org/adrianmorataya/image/UsuarioReportes.png"));
                tipoDeOperaciones = operaciones.NINGUNO;
                cargarDatos();
                break;
            default:
                throw new AssertionError();
        }
    }
    
    public void desactivarControles(){
        txtCodCE.setEditable(false);
        txtNomCE.setEditable(false);
        txtDescCE.setEditable(false);
        txtCodCE.setDisable(true);
        txtNomCE.setDisable(true);
        txtDescCE.setDisable(true);
    }
    
    public void activarControles(){
        txtCodCE.setEditable(true);
        txtNomCE.setEditable(true);
        txtDescCE.setEditable(true);
        txtCodCE.setDisable(false);
        txtNomCE.setDisable(false);
        txtDescCE.setDisable(false);
    }
    
    public void limpiarControles(){
        txtCodCE.clear();
        txtNomCE.clear();
        txtDescCE.clear();
    }
    
    
    public Main getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Main escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }

    @FXML
    public void handleButtonAction(ActionEvent event) {
        if (event.getSource() == btnRegresar3){
            escenarioPrincipal.menuPrincipalView();
        }
    }
}
