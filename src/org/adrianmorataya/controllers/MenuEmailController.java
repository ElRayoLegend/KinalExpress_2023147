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
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javax.swing.JOptionPane;
import org.adrianmorataya.bean.Email;
import org.adrianmorataya.bean.Proveedores;
import org.adrianmorataya.dao.Conexion;
import org.adrianmorataya.system.Main;

public class MenuEmailController implements Initializable{
    private Main escenarioPrincipal;
    private enum operaciones {AGREGAR, ELIMINAR, EDITAR, ACTUALIZAR, CANCELAR, NINGUNO}
    private operaciones tipoDeOperaciones = operaciones.NINGUNO;
    private ObservableList <Email> listaEmail;
    private ObservableList <Proveedores> listaProveedor;
    @FXML private TextField txtEmailId;
    @FXML private TextField txtDesc;
    @FXML private TextField txtEmailP;
    @FXML private ComboBox cmbProveedorId;
    @FXML private TableView tblEmail;
    @FXML private TableColumn colEmailId;
    @FXML private TableColumn colEmailP;
    @FXML private TableColumn colDesc;
    @FXML private TableColumn colProveedorId;
    @FXML private Button btnAgregar;
    @FXML private Button btnEliminar;
    @FXML private Button btnEditar;
    @FXML private Button btnReporte;
    @FXML private Button btnRegresar;
    @FXML private ImageView imgAgregar;
    @FXML private ImageView imgEliminar;
    @FXML private ImageView imgEditar;
    @FXML private ImageView imgReporte;
    
            
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
        cmbProveedorId.setItems(getProveedores());
        txtEmailId.setDisable(true);
        txtDesc.setDisable(true);
        txtEmailP.setDisable(true);
        txtEmailId.setEditable(false);
        txtDesc.setEditable(false);
        txtEmailP.setEditable(false);
        cmbProveedorId.setDisable(true);
    }
    
    public void cargarDatos(){
        tblEmail.setItems(getEmail());
        colEmailId.setCellValueFactory(new PropertyValueFactory<Email, Integer>("codigoEmailProveedor"));
        colEmailP.setCellValueFactory(new PropertyValueFactory<Email, String>("emailProveedor"));
        colDesc.setCellValueFactory(new PropertyValueFactory<Email, String>("descripcion"));
        colProveedorId.setCellValueFactory(new PropertyValueFactory<Email, Integer>("codigoProveedor"));
    }
    
    public void selecionarElemento(){
        txtDesc.setText(String.valueOf(((Email)tblEmail.getSelectionModel().getSelectedItem()).getCodigoEmailProveedor()));
        txtEmailP.setText(((Email)tblEmail.getSelectionModel().getSelectedItem()).getEmailProveedor());
        txtEmailId.setText(((Email)tblEmail.getSelectionModel().getSelectedItem()).getDescripcion());
        txtDesc.setText(String.valueOf(((Email)tblEmail.getSelectionModel().getSelectedItem()).getCodigoProveedor()));
        cmbProveedorId.getSelectionModel().select(buscarP(((Email)tblEmail.getSelectionModel().getSelectedItem()).getCodigoProveedor()));
    }
    
    public Proveedores buscarP (int codPro){
        Proveedores resultado = null;
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_BuscarProveedores(?)}");
            procedimiento.setInt(1, codPro);
            ResultSet registro = procedimiento.executeQuery();
            while(registro.next()){
                resultado = new Proveedores(registro.getInt("codigoProveedor"),
                        registro.getString("NITProveedor"),
                        registro.getString("nombreProveedor"),
                        registro.getString("apellidoProveedor"),
                        registro.getString("direccionProveedor"),
                        registro.getString("razonSocial"),
                        registro.getString("contactoProveedor")
                );
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return resultado;
    }
    
    public ObservableList<Proveedores> getProveedores(){
        ArrayList<Proveedores>lista = new ArrayList<>();
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarProveedores()}");
            ResultSet resultado = procedimiento.executeQuery();
            while (resultado.next()) {
                lista.add(new Proveedores (resultado.getInt("codigoProveedor"),
                                        resultado.getString("NITProveedor"),
                                        resultado.getString("nombreProveedor"),
                                        resultado.getString("apellidoProveedor"),
                                        resultado.getString("razonSocial"),
                                        resultado.getString("contactoProveedor"),
                                        resultado.getString("direccionProveedor")
                ));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return listaProveedor = FXCollections.observableArrayList(lista);
    }
    
    public ObservableList<Email> getEmail(){
        ArrayList<Email>lista = new ArrayList<>();
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarEmailProveedor()}");
            ResultSet resultado = procedimiento.executeQuery();
            while (resultado.next()) {
                lista.add(new Email (resultado.getInt("codigoEmailProveedor"),
                                        resultado.getString("emailProveedor"),
                                        resultado.getString("descripcion"),
                                        resultado.getInt("codigoProveedor")
                ));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        
        return listaEmail = FXCollections.observableArrayList(lista);
    }

    public void agregar(){
        switch (tipoDeOperaciones) {
            case NINGUNO:
                activarControles();
                limpiarControles();
                btnAgregar.setText("GUARDAR");
                btnEliminar.setText("CANCELAR");
                btnEditar.setDisable(true);
                btnReporte.setDisable(true);
                imgAgregar.setImage(new Image("/org/adrianmorataya/image/GUARDAR.png"));
                imgEliminar.setImage(new Image("/org/adrianmorataya/image/CANCELAR.png"));
                tipoDeOperaciones = operaciones.ACTUALIZAR;
                cargarDatos();
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
                cargarDatos();
                break;
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
        }
    }
    
    public void editar(){
        switch (tipoDeOperaciones) {
            case NINGUNO:
                if (tblEmail.getSelectionModel().getSelectedItem() != null) {
                    btnEditar.setText("ACTUALIZAR");
                    btnReporte.setText("CANCELAR");
                    btnAgregar.setDisable(true);
                    btnEliminar.setDisable(true);
                    imgEditar.setImage(new Image("/org/adrianmorataya/image/GUARDAR.png"));
                    imgReporte.setImage(new Image("/org/adrianmorataya/image/CANCELAR.png"));
                    activarControles();
                    cmbProveedorId.setDisable(true);
                    tipoDeOperaciones = operaciones.ACTUALIZAR;
                }else{
                    JOptionPane.showMessageDialog(null, "Debe seleccionar algun elemento!");
                }
                break;
            case ACTUALIZAR:
                actualizar();
                limpiarControles();
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
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_EditarEmailProveedor(?, ?, ?, ?)}");
            Email registro = ((Email)tblEmail.getSelectionModel().getSelectedItem());
            registro.setEmailProveedor(txtEmailP.getText());
            registro.setDescripcion(txtDesc.getText());
            procedimiento.setInt(1, registro.getCodigoEmailProveedor());
            procedimiento.setString(2, registro.getEmailProveedor());
            procedimiento.setString(3, registro.getDescripcion());
            procedimiento.setDouble(4, registro.getCodigoProveedor());
            procedimiento.execute();
            listaEmail.add(registro);
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
    
    public void guardar(){
        Email registro = new Email();
        registro.setCodigoProveedor(((Proveedores)cmbProveedorId.getSelectionModel().getSelectedItem()).getCodigoProveedor());
        registro.setCodigoEmailProveedor(Integer.parseInt(txtEmailId.getText()));
        registro.setEmailProveedor(txtEmailP.getText());
        registro.setDescripcion(txtDesc.getText());
            try{
                PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_AgregarEmailProveedor(?, ?, ?, ?)}");
                procedimiento.setInt(1, registro.getCodigoEmailProveedor());
                procedimiento.setString(2, registro.getEmailProveedor());
                procedimiento.setString(3, registro.getDescripcion());
                procedimiento.setDouble(4, registro.getCodigoProveedor());
                procedimiento.execute();
                listaEmail.add(registro);
            }catch(Exception e){
                e.printStackTrace();
            }
        
    }
    
    public void activarControles(){
        txtEmailId.setDisable(false);
        txtDesc.setDisable(false);
        txtEmailP.setDisable(false);
        txtEmailId.setEditable(true);
        txtDesc.setEditable(true);
        txtEmailP.setEditable(true);
        cmbProveedorId.setDisable(false);
    }
    
    public void desactivarControles(){
        txtEmailId.setDisable(true);
        txtDesc.setDisable(true);
        txtEmailP.setDisable(true);
        txtEmailId.setEditable(false);
        txtDesc.setEditable(false);
        txtEmailP.setEditable(false);
        cmbProveedorId.setDisable(true);
    }
    
    public void limpiarControles(){
        txtEmailId.clear();
        txtDesc.clear();
        txtEmailP.clear();
        cmbProveedorId.getSelectionModel().clearSelection();
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
