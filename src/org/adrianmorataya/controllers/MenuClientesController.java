package org.adrianmorataya.controllers;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
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
import org.adrianmorataya.bean.Clientes;
import org.adrianmorataya.dao.Conexion;
import org.adrianmorataya.report.GenerarReportes;
import org.adrianmorataya.system.Main;

public class MenuClientesController implements Initializable   {
    private Main escenarioPrincipal;
    private enum operaciones {AGREGAR, ELIMINAR, EDITAR, ACTUALIZAR, CANCELAR, NINGUNO}
    private operaciones tipoDeOperaciones = operaciones.NINGUNO;
    private ObservableList<Clientes> listaClientes;
    @FXML private Button btnRegresar;
    @FXML private TextField txtDireccionC;
    @FXML private TextField txtCorreoC;
    @FXML private TextField txtCodigoC;
    @FXML private TextField txtNitC;
    @FXML private TextField txtNombreC;
    @FXML private TextField txtApellidoC;
    @FXML private TextField txtTelefonoC;
    @FXML private TableView tblClientes;
    @FXML private TableColumn colCodigoC;
    @FXML private TableColumn colDireccionC;
    @FXML private TableColumn colCorreoC;
    @FXML private TableColumn colNitC;
    @FXML private TableColumn colNombreC;
    @FXML private TableColumn colApellidoC;
    @FXML private TableColumn colTelefonoC;
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
        txtCodigoC.setDisable(true);
        txtNitC.setDisable(true);
        txtNombreC.setDisable(true);
        txtApellidoC.setDisable(true);
        txtDireccionC.setDisable(true);
        txtTelefonoC.setDisable(true);
        txtCorreoC.setDisable(true);
    }
    
    public void cargarDatos(){
       tblClientes.setItems(getClientes());
       colCodigoC.setCellValueFactory(new PropertyValueFactory<Clientes, Integer>("codigoCliente"));
       colNitC.setCellValueFactory(new PropertyValueFactory<Clientes, Integer>("NITCliente"));
       colNombreC.setCellValueFactory(new PropertyValueFactory<Clientes, Integer>("nombreCliente"));
       colApellidoC.setCellValueFactory(new PropertyValueFactory<Clientes, Integer>("apellidoCliente"));
       colDireccionC.setCellValueFactory(new PropertyValueFactory<Clientes, Integer>("direccionCliente"));
       colTelefonoC.setCellValueFactory(new PropertyValueFactory<Clientes, Integer>("telefonoCliente"));
       colCorreoC.setCellValueFactory(new PropertyValueFactory<Clientes, Integer>("correoCliente"));
    }
    
    public void seleccionarElemento(){
        txtCodigoC.setText(String.valueOf(((Clientes)tblClientes.getSelectionModel().getSelectedItem()).getCodigoCliente()));
        txtNitC.setText((((Clientes)tblClientes.getSelectionModel().getSelectedItem()).getNITCliente()));
        txtNombreC.setText((((Clientes)tblClientes.getSelectionModel().getSelectedItem()).getNombreCliente()));
        txtApellidoC.setText((((Clientes)tblClientes.getSelectionModel().getSelectedItem()).getApellidoCliente()));
        txtDireccionC.setText((((Clientes)tblClientes.getSelectionModel().getSelectedItem()).getDireccionCliente()));
        txtTelefonoC.setText((((Clientes)tblClientes.getSelectionModel().getSelectedItem()).getTelefonoCliente()));
        txtCorreoC.setText((((Clientes)tblClientes.getSelectionModel().getSelectedItem()).getCorreoCliente()));  
    }
    
    public ObservableList<Clientes> getClientes(){
        ArrayList<Clientes>lista = new ArrayList<>();
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarClientes()}");
            ResultSet resultado = procedimiento.executeQuery();
            while (resultado.next()) {
                lista.add(new Clientes (resultado.getInt("codigoCliente"),
                                        resultado.getString("NITCliente"),
                                        resultado.getString("nombreCliente"),
                                        resultado.getString("apellidoCliente"),
                                        resultado.getString("direccionCliente"),
                                        resultado.getString("telefonoCliente"),
                                        resultado.getString("correoCliente")
                ));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return listaClientes = FXCollections.observableArrayList(lista);
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
        Clientes registro = new Clientes();
        registro.setCodigoCliente(Integer.parseInt(txtCodigoC.getText()));
        registro.setNITCliente(txtNitC.getText());
        registro.setNombreCliente(txtNombreC.getText());
        registro.setApellidoCliente(txtApellidoC.getText());
        registro.setDireccionCliente(txtDireccionC.getText());
        registro.setTelefonoCliente(txtTelefonoC.getText());
        registro.setCorreoCliente(txtCorreoC.getText());
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_AgregarClientes(?, ?, ?, ?, ?, ?, ?)}");
            procedimiento.setInt(1, registro.getCodigoCliente());
            procedimiento.setString(2, registro.getNITCliente());
            procedimiento.setString(3, registro.getNombreCliente());
            procedimiento.setString(4, registro.getApellidoCliente());
            procedimiento.setString(5, registro.getDireccionCliente());
            procedimiento.setString(6, registro.getTelefonoCliente());
            procedimiento.setString(7, registro.getCorreoCliente());
            procedimiento.execute();
            listaClientes.add(registro);
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
                if (tblClientes.getSelectionModel().getSelectedItem() != null) {
                    int respuesta = JOptionPane.showConfirmDialog(null, "Confirmar para eliminar el registro", "Confirmacion Eliminar Cliente", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if (respuesta == JOptionPane.YES_NO_OPTION) {
                        try{
                            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_EliminarClientes(?)}");
                            procedimiento.setInt(1, ((Clientes)tblClientes.getSelectionModel().getSelectedItem()).getCodigoCliente());
                            procedimiento.execute();
                            listaClientes.remove(tblClientes.getSelectionModel().getSelectedItem());
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
                if (tblClientes.getSelectionModel().getSelectedItem() != null) {
                    btnEditar.setText("ACTUALIZAR");
                    btnReporte.setText("CANCELAR");
                    btnAgregar.setDisable(true);
                    btnEliminar.setDisable(true);
                    imgEditar.setImage(new Image("/org/adrianmorataya/image/GUARDAR.png"));
                    imgReporte.setImage(new Image("/org/adrianmorataya/image/CANCELAR.png"));
                    activarControles();
                    txtCodigoC.setEditable(false);
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
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_EditarClientes(?, ?, ?, ?, ?, ?, ?)}");
            Clientes registro = ((Clientes)tblClientes.getSelectionModel().getSelectedItem());
            registro.setNITCliente(txtNitC.getText());
            registro.setNombreCliente(txtNombreC.getText());
            registro.setApellidoCliente(txtApellidoC.getText());
            registro.setDireccionCliente(txtDireccionC.getText());
            registro.setTelefonoCliente(txtTelefonoC.getText());
            registro.setCorreoCliente(txtCorreoC.getText());
            procedimiento.setInt(1, registro.getCodigoCliente());
            procedimiento.setString(2, registro.getNITCliente());
            procedimiento.setString(3, registro.getNombreCliente());
            procedimiento.setString(4, registro.getApellidoCliente());
            procedimiento.setString(5, registro.getDireccionCliente());
            procedimiento.setString(6, registro.getTelefonoCliente());
            procedimiento.setString(7, registro.getCorreoCliente());
            procedimiento.execute();
            listaClientes.add(registro);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void reporte() {
        switch (tipoDeOperaciones) {
            case NINGUNO:
                imprimirReporte();
                break;
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
        }
    }

    public void imprimirReporte(){
         Map parametros = new HashMap();
         parametros.put("codigoCliente", null);
         GenerarReportes.mostrarReportes("reportCliente.jasper", "Reporte de Cliente", parametros);

    }
    
    public void desactivarControles(){
        txtCodigoC.setEditable(false);
        txtDireccionC.setEditable(false);
        txtCorreoC.setEditable(false);
        txtNitC.setEditable(false);
        txtNombreC.setEditable(false);
        txtApellidoC.setEditable(false);
        txtTelefonoC.setEditable(false);
        txtCodigoC.setDisable(true);
        txtNitC.setDisable(true);
        txtNombreC.setDisable(true);
        txtApellidoC.setDisable(true);
        txtDireccionC.setDisable(true);
        txtTelefonoC.setDisable(true);
        txtCorreoC.setDisable(true);
    }
    
    public void activarControles(){
        txtCodigoC.setEditable(true);
        txtDireccionC.setEditable(true);
        txtCorreoC.setEditable(true);
        txtNitC.setEditable(true);
        txtNombreC.setEditable(true);
        txtApellidoC.setEditable(true);
        txtTelefonoC.setEditable(true);
        txtCodigoC.setDisable(false);
        txtNitC.setDisable(false);
        txtNombreC.setDisable(false);
        txtApellidoC.setDisable(false);
        txtDireccionC.setDisable(false);
        txtTelefonoC.setDisable(false);
        txtCorreoC.setDisable(false);
    }
    
    public void limpiarControles(){
        txtCodigoC.clear();
        txtDireccionC.clear();
        txtCorreoC.clear();
        txtNitC.clear();
        txtNombreC.clear();
        txtApellidoC.clear();
        txtTelefonoC.clear();
    }
    
    
    public Main getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Main escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }

    @FXML
    public void handleButtonAction(ActionEvent event) {
        if (event.getSource() == btnRegresar){
            escenarioPrincipal.menuPrincipalView();
        }
    }
}
