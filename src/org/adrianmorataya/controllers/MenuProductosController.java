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
import org.adrianmorataya.bean.Productos;
import org.adrianmorataya.bean.Proveedores;
import org.adrianmorataya.bean.TipoProducto;
import org.adrianmorataya.dao.Conexion;
import org.adrianmorataya.system.Main;

public class MenuProductosController implements Initializable{
    private Main escenarioPrincipal;
    private enum operaciones {AGREGAR, ELIMINAR, EDITAR, ACTUALIZAR, CANCELAR, NINGUNO}
    private operaciones tipoDeOperaciones = operaciones.NINGUNO;
    private ObservableList <Productos> listaProducto;
    private ObservableList <Proveedores> listaProveedor;
    private ObservableList <TipoProducto> listaTP;
    @FXML private TextField txtCodigoProd;
    @FXML private TextField txtDescPro;
    @FXML private TextField txtPrecioU;
    @FXML private TextField txtPrecioD;
    @FXML private TextField txtPrecioM;
    @FXML private TextField txtExistencia;
    @FXML private ComboBox cmbCodigoTipoP;
    @FXML private ComboBox cmbCodProv;
    @FXML private TableView tblProductos;
    @FXML private TableColumn colCodProd;
    @FXML private TableColumn colDescProd;
    @FXML private TableColumn colPrecioU;
    @FXML private TableColumn colPrecioD;
    @FXML private TableColumn colPrecioM;
    @FXML private TableColumn colExistencia;
    @FXML private TableColumn colCodTipoProd;
    @FXML private TableColumn colCodProv;
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
        cmbCodigoTipoP.setItems(getProveedores());
        txtCodigoProd.setDisable(true);
        txtDescPro.setDisable(true);
        txtPrecioU.setDisable(true);
        txtPrecioD.setDisable(true);
        txtPrecioM.setDisable(true);
        txtExistencia.setDisable(true);
        cmbCodigoTipoP.setDisable(true);
        cmbCodProv.setDisable(true);
    }
    
    public void cargarDatos(){
        tblProductos.setItems(getProducto());
        colCodProd.setCellValueFactory(new PropertyValueFactory<Productos, String>("codigoProducto"));
        colDescProd.setCellValueFactory(new PropertyValueFactory<Productos, String>("descripcionProducto"));
        colPrecioU.setCellValueFactory(new PropertyValueFactory<Productos, String>("precioUnitario"));
        colPrecioD.setCellValueFactory(new PropertyValueFactory<Productos, String>("precioDocena"));
        colPrecioM.setCellValueFactory(new PropertyValueFactory<Productos, String>("precioMayor"));
        colExistencia.setCellValueFactory(new PropertyValueFactory<Productos, String>("existencia"));
        colCodTipoProd.setCellValueFactory(new PropertyValueFactory<Productos, Integer>("codigoTipoProducto"));
        colCodProv.setCellValueFactory(new PropertyValueFactory<Productos, Integer>("codigoProveedor"));
    }
    
    public void seleccionarElemento(){
        txtCodigoProd.setText(String.valueOf(((Productos)tblProductos.getSelectionModel().getSelectedItem()).getCodigoProducto()));
        txtDescPro.setText((((Productos)tblProductos.getSelectionModel().getSelectedItem()).getDescripcionProducto()));
        txtPrecioU.setText((((Productos)tblProductos.getSelectionModel().getSelectedItem()).getPrecioUnitario()));
        txtPrecioD.setText((((Productos)tblProductos.getSelectionModel().getSelectedItem()).getPrecioDocena()));
        txtPrecioM.setText((((Productos)tblProductos.getSelectionModel().getSelectedItem()).getPrecioMayor()));
        txtExistencia.setText((((Productos)tblProductos.getSelectionModel().getSelectedItem()).getExistencia()));
        cmbCodigoTipoP.getSelectionModel().select(buscarTipoP(((Productos)tblProductos.getSelectionModel().getSelectedItem()).getCodigoTipoProducto()));
    }
    
    public TipoProducto buscarTipoP (int codigoTP){
        TipoProducto resultado = null;
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_buscarTipoProducto(?)}");
            procedimiento.setInt(1, codigoTP);
            ResultSet registro = procedimiento.executeQuery();
            while(registro.next()){
                resultado = new TipoProducto(registro.getInt("codigoTipoProducto"),
                        registro.getString("descripcionProducto")
                );
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return resultado;
    }
    
    public ObservableList<TipoProducto> getTipoProducto(){
        ArrayList<TipoProducto>lista = new ArrayList<>();
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarTipoProducto()}");
            ResultSet resultado = procedimiento.executeQuery();
            while (resultado.next()) {
                lista.add(new TipoProducto (resultado.getInt("codigoTipoProducto"),
                                        resultado.getString("descripcion")
                ));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return listaTP = FXCollections.observableArrayList(lista);
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
    
    public ObservableList<Productos> getProducto(){
        ArrayList<Productos> lista = new ArrayList<Productos>();
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarProductos()}");
            ResultSet resultado = procedimiento.executeQuery();
            while (resultado.next()) {
                lista.add(new Productos (resultado.getString("codigoProducto"),
                                        resultado.getString("descripcionProducto"),
                                        resultado.getString("imagenProducto"),
                                        resultado.getDouble("precioUnitario"),
                                        resultado.getDouble("precioDocena"),
                                        resultado.getDouble("precioMayor"),
                                        resultado.getInt("existencia"),
                                        resultado.getInt("codigoTipoProducto"),
                                        resultado.getInt("codigoProveedor")
                ));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        
        return listaProducto = FXCollections.observableArrayList(lista);
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
        }
    }
    
    public void guardar(){
        Productos registro = new Productos();
        registro.setCodigoProducto(txtCodigoProd.getText());
        registro.setCodigoProveedor(((Proveedores)cmbCodProv.getSelectionModel().getSelectedItem()).getCodigoProveedor());
        registro.setCodigoTipoProducto(((TipoProducto)cmbCodigoTipoP.getSelectionModel().getSelectedItem()).getCodigoTipoProducto());
        registro.setDescripcionProducto(txtDescPro.getText());
        registro.setPrecioDocena(Double.parseDouble(txtPrecioD.getText()));
        registro.setPrecioUnitario(Double.parseDouble(txtPrecioU.getText()));
        registro.setPrecioMayor(Double.parseDouble(txtPrecioM.getText()));
        registro.setExistencia(Integer.parseInt(txtExistencia.getText()));
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_AgregarProductos(?, ?, ?, ?, ?, ?, ?, ?)}");
            procedimiento.setString(1, registro.getCodigoProducto());
            procedimiento.setInt(2, registro.getCodigoProveedor());
            procedimiento.setInt(3, registro.getCodigoTipoProducto());
            procedimiento.setString(4, registro.getDescripcionProducto());
            procedimiento.setDouble(5, registro.getPrecioDocena());
            procedimiento.setDouble(6, registro.getPrecioUnitario());
            procedimiento.setDouble(7, registro.getPrecioMayor());
            procedimiento.setInt(7, registro.getExistencia());
            procedimiento.execute();
            listaProducto.add(registro);
        }catch(Exception e){
            e.printStackTrace();
        }
        
    }
    
    public void activarControles(){
        txtCodigoProd.setEditable(true);
        txtDescPro.setEditable(true);
        txtPrecioU.setEditable(true);
        txtPrecioD.setEditable(true);
        txtPrecioM.setEditable(true);
        txtExistencia.setEditable(true);
        cmbCodigoTipoP.setDisable(false);
        cmbCodProv.setDisable(false);
        txtCodigoProd.setDisable(false);
        txtDescPro.setDisable(false);
        txtPrecioU.setDisable(false);
        txtPrecioD.setDisable(false);
        txtPrecioM.setDisable(false);
        txtExistencia.setDisable(false);
        cmbCodProv.getSelectionModel().getSelectedItem();
    }
    
    public void desactivarControles(){
        txtCodigoProd.setEditable(false);
        txtDescPro.setEditable(false);
        txtPrecioU.setEditable(false);
        txtPrecioD.setEditable(false);
        txtPrecioM.setEditable(false);
        txtExistencia.setEditable(false);
        cmbCodigoTipoP.setDisable(true);
        cmbCodProv.setDisable(true);
        txtCodigoProd.setDisable(true);
        txtDescPro.setDisable(true);
        txtPrecioU.setDisable(true);
        txtPrecioD.setDisable(true);
        txtPrecioM.setDisable(true);
        txtExistencia.setDisable(true);
    }
    
    public void limpiarControles(){
        txtCodigoProd.clear();
        txtDescPro.clear();
        txtPrecioU.clear();
        txtPrecioD.clear();
        txtPrecioM.clear();
        txtExistencia.clear();
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
