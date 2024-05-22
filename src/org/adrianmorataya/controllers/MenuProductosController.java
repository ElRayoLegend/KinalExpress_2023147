package org.adrianmorataya.controllers;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
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
import javafx.scene.input.DragEvent;
import javafx.scene.input.TransferMode;
import javax.swing.JOptionPane;
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
    @FXML private TextField txtNombProd;
    @FXML private TextField txtExistencia;
    @FXML private ComboBox cmbCodigoTipoP;
    @FXML private ComboBox cmbCodProv;
    @FXML private TableView tblProductos;
    @FXML private TableColumn colNomProd;
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
    @FXML private ImageView imgProducto;
    @FXML private ImageView imgMostrar;
    
            
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
        cmbCodigoTipoP.setItems(getTipoProducto());
        cmbCodProv.setItems(getProveedores());
        txtCodigoProd.setDisable(true);
        txtDescPro.setDisable(true);
        txtPrecioU.setDisable(true);
        txtPrecioD.setDisable(true);
        txtPrecioM.setDisable(true);
        txtExistencia.setDisable(true);
        txtNombProd.setDisable(true);
        cmbCodigoTipoP.setDisable(true);
        cmbCodProv.setDisable(true);
        imgProducto.setDisable(true);
    }
    
    public void cargarDatos(){
        tblProductos.setItems(getProducto());
        colCodProd.setCellValueFactory(new PropertyValueFactory<Productos, String>("productoId"));
        colDescProd.setCellValueFactory(new PropertyValueFactory<Productos, String>("descripcionProducto"));
        colNomProd.setCellValueFactory(new PropertyValueFactory<Productos, String>("nombreProducto"));
        colPrecioU.setCellValueFactory(new PropertyValueFactory<Productos, Double>("precioVentaUnitario"));
        colPrecioD.setCellValueFactory(new PropertyValueFactory<Productos, Double>("precioCompra"));
        colPrecioM.setCellValueFactory(new PropertyValueFactory<Productos, Double>("precioVentaMayor"));
        colExistencia.setCellValueFactory(new PropertyValueFactory<Productos, Integer>("cantidadStock"));
        colCodTipoProd.setCellValueFactory(new PropertyValueFactory<Productos, Integer>("codigoTipoProducto"));
        colCodProv.setCellValueFactory(new PropertyValueFactory<Productos, Integer>("codigoProveedor"));
        
    }
    
    public void selecionarElemento(){
        Productos productoSeleccionado = (Productos) tblProductos.getSelectionModel().getSelectedItem();
        txtCodigoProd.setText(((Productos)tblProductos.getSelectionModel().getSelectedItem()).getProductoId());
        txtDescPro.setText(((Productos)tblProductos.getSelectionModel().getSelectedItem()).getDescripcionProducto());
        txtNombProd.setText(((Productos)tblProductos.getSelectionModel().getSelectedItem()).getNombreProducto());
        txtPrecioU.setText(String.valueOf(((Productos)tblProductos.getSelectionModel().getSelectedItem()).getPrecioVentaUnitario()));
        txtPrecioD.setText(String.valueOf(((Productos)tblProductos.getSelectionModel().getSelectedItem()).getPrecioCompra()));
        txtPrecioM.setText(String.valueOf(((Productos)tblProductos.getSelectionModel().getSelectedItem()).getPrecioVentaMayor()));
        txtExistencia.setText(String.valueOf(((Productos)tblProductos.getSelectionModel().getSelectedItem()).getCantidadStock()));
        cmbCodigoTipoP.getSelectionModel().select(buscarTipoP(((Productos)tblProductos.getSelectionModel().getSelectedItem()).getCodigoTipoProducto()));
        cmbCodProv.getSelectionModel().select(buscarP(((Productos)tblProductos.getSelectionModel().getSelectedItem()).getCodigoProveedor()));
        byte[] imgBytes = productoSeleccionado.getImagenProducto();
        if (imgBytes != null) {
            Image img = new Image(new ByteArrayInputStream(imgBytes));
            imgMostrar.setImage(img);
        }
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
        ArrayList<Productos>lista = new ArrayList<>();
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarProductos()}");
            ResultSet resultado = procedimiento.executeQuery();
            while (resultado.next()) {
                lista.add(new Productos (resultado.getString("productoId"),
                                        resultado.getString("nombreProducto"),
                                        resultado.getString("descripcionProducto"),
                                        resultado.getBytes("imagenProducto"),
                                        resultado.getDouble("precioVentaUnitario"),
                                        resultado.getDouble("precioCompra"),
                                        resultado.getDouble("precioVentaMayor"),
                                        resultado.getInt("cantidadStock"),
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
    
    private byte[] imagenEnBytes;
    private byte[] convertirImagenABytes(File file) throws IOException {
        byte[] bytes = Files.readAllBytes(file.toPath());
        return bytes;
    }
    
    public void guardar(){
        Productos registro = new Productos();
        registro.setProductoId(txtCodigoProd.getText());
        registro.setCodigoProveedor(((Proveedores)cmbCodProv.getSelectionModel().getSelectedItem()).getCodigoProveedor());
        registro.setCodigoTipoProducto(((TipoProducto)cmbCodigoTipoP.getSelectionModel().getSelectedItem()).getCodigoTipoProducto());
        registro.setDescripcionProducto(txtDescPro.getText());
        registro.setNombreProducto(txtNombProd.getText());
        registro.setPrecioCompra(Double.parseDouble(txtPrecioD.getText()));
        registro.setPrecioVentaUnitario(Double.parseDouble(txtPrecioU.getText()));
        registro.setPrecioVentaMayor(Double.parseDouble(txtPrecioM.getText()));
        registro.setCantidadStock(Integer.parseInt(txtExistencia.getText()));
        registro.setImagenProducto(imagenEnBytes);
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_AgregarProductos(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}");
            procedimiento.setString(1, registro.getProductoId());
            procedimiento.setBytes(2, registro.getImagenProducto());
            procedimiento.setString(3, registro.getNombreProducto());
            procedimiento.setString(4, registro.getDescripcionProducto());
            procedimiento.setInt(5, registro.getCantidadStock());
            procedimiento.setDouble(6, registro.getPrecioVentaUnitario());
            procedimiento.setDouble(7, registro.getPrecioVentaMayor());
            procedimiento.setDouble(8, registro.getPrecioCompra());
            procedimiento.setInt(9, registro.getCodigoProveedor());
            procedimiento.setInt(10, registro.getCodigoTipoProducto());
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
        txtNombProd.setEditable(true);
        cmbCodigoTipoP.setDisable(false);
        cmbCodProv.setDisable(false);
        txtCodigoProd.setDisable(false);
        txtDescPro.setDisable(false);
        txtPrecioU.setDisable(false);
        txtPrecioD.setDisable(false);
        txtPrecioM.setDisable(false);
        txtExistencia.setDisable(false);
        txtNombProd.setDisable(false);
        imgProducto.setDisable(false);
    }
    
    public void desactivarControles(){
        txtCodigoProd.setEditable(false);
        txtDescPro.setEditable(false);
        txtPrecioU.setEditable(false);
        txtPrecioD.setEditable(false);
        txtPrecioM.setEditable(false);
        txtExistencia.setEditable(false);
        txtNombProd.setEditable(false);
        cmbCodigoTipoP.setDisable(true);
        cmbCodProv.setDisable(true);
        txtCodigoProd.setDisable(true);
        txtDescPro.setDisable(true);
        txtPrecioU.setDisable(true);
        txtPrecioD.setDisable(true);
        txtPrecioM.setDisable(true);
        txtExistencia.setDisable(true);
        txtNombProd.setDisable(true);
        imgProducto.setDisable(true);
    }
    
    public void limpiarControles(){
        txtCodigoProd.clear();
        txtDescPro.clear();
        txtPrecioU.clear();
        txtPrecioD.clear();
        txtPrecioM.clear();
        txtExistencia.clear();
        txtNombProd.clear();
        cmbCodProv.getSelectionModel().getSelectedItem();
        cmbCodigoTipoP.getSelectionModel().getSelectedItem();
        imgProducto.setImage(null);
    }
    
    
    
    public Main getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Main escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }

private Image convertirBytesAImagen(byte[] bytes){
    try {
        ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
        Image imagen = new Image(bis);
        bis.close();

        return imagen;
    } catch (IOException e) {
        e.printStackTrace();
        return null;
    }
}

@FXML
    public void handleButtonAction(ActionEvent event) {
        if (event.getSource() == btnRegresar){
            escenarioPrincipal.menuPrincipalView();
        }
    }
    
    @FXML
    public void HandleDrag(DragEvent event) {
        if (event.getDragboard().hasFiles()){
            event.acceptTransferModes(TransferMode.ANY);
        }
    }
    
    @FXML
    public void HandleDrop(DragEvent event) throws FileNotFoundException, IOException {
        List<File> files = event.getDragboard().getFiles();
        File file = files.get(0);
        Image img = new Image(new FileInputStream(file));
        imgProducto.setImage(img);
        imagenEnBytes = convertirImagenABytes(file);
    }
}
