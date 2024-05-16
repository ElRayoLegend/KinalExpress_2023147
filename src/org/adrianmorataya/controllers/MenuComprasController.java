package org.adrianmorataya.controllers;

import java.net.URL;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javax.swing.JOptionPane;
import org.adrianmorataya.bean.Compras;
import org.adrianmorataya.dao.Conexion;
import org.adrianmorataya.system.Main;

public class MenuComprasController implements Initializable   {
    private Main escenarioPrincipal;
    private enum operaciones {AGREGAR, ELIMINAR, EDITAR, ACTUALIZAR, CANCELAR, NINGUNO}
    private operaciones tipoDeOperaciones = operaciones.NINGUNO;
    private ObservableList<Compras> listaCompras;
    @FXML private Button btnRegresar3;
    @FXML private TextField txtNumDoc;
    @FXML private DatePicker txtFechaDoc;
    @FXML private TextField txtDescComp;
    @FXML private TextField txtTotComp;
    @FXML private TableView tblCompras;
    @FXML private TableColumn colNumDoc;
    @FXML private TableColumn colFechaDoc;
    @FXML private TableColumn colDescComp;
    @FXML private TableColumn colTotComp;
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
        txtNumDoc.setDisable(true);
        txtFechaDoc.setDisable(true);
        txtDescComp.setDisable(true);
        txtTotComp.setDisable(true);
    }
    
    public void cargarDatos(){
       tblCompras.setItems(getCompras());
       colNumDoc.setCellValueFactory(new PropertyValueFactory<Compras, Integer>("numeroDocumento"));
       colFechaDoc.setCellValueFactory(new PropertyValueFactory<Compras, LocalDate>("fechaDocumento"));
       colDescComp.setCellValueFactory(new PropertyValueFactory<Compras, String>("descripcion"));
       colTotComp.setCellValueFactory(new PropertyValueFactory<Compras, Double>("totalDocumento"));
    }
    
    public void seleccionarElemento(){
        txtNumDoc.setText(String.valueOf(((Compras)tblCompras.getSelectionModel().getSelectedItem()).getNumeroDocumento()));
        txtDescComp.setText((((Compras)tblCompras.getSelectionModel().getSelectedItem()).getDescripcion()));
        txtTotComp.setText(String.valueOf(((Compras)tblCompras.getSelectionModel().getSelectedItem()).getTotalDocumento()));
    }
    
    public ObservableList<Compras> getCompras(){
        ArrayList<Compras>lista = new ArrayList<>();
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarCompras()}");
            ResultSet resultado = procedimiento.executeQuery();
            while (resultado.next()) {
                java.sql.Date fechaSql = resultado.getDate("fechaDocumento");
                LocalDate fechaTemp = fechaSql.toLocalDate();
                lista.add(new Compras (resultado.getInt("numeroDocumento"),
                                        fechaTemp,
                                        resultado.getString("descripcion"),
                                        resultado.getDouble("totalDocumento")
                ));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return listaCompras = FXCollections.observableArrayList(lista);
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
            default:
                throw new AssertionError();
        }
    }
    
    public void guardar(){
    Compras registro = new Compras();
    registro.setNumeroDocumento(Integer.parseInt(txtNumDoc.getText()));
    registro.setDescripcion(txtDescComp.getText());
    LocalDate fechaSelec = txtFechaDoc.getValue();
    Date fechaDocumento = Date.valueOf(fechaSelec);
    registro.setTotalDocumento(Double.parseDouble(txtTotComp.getText()));
    
    try{
        PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_AgregarCompras(?, ?, ?, ?)}");
        procedimiento.setInt(1, registro.getNumeroDocumento());
        procedimiento.setDate(2, fechaDocumento);
        procedimiento.setString(3, registro.getDescripcion());
        procedimiento.setDouble(4, registro.getTotalDocumento());
        procedimiento.execute();
        listaCompras.add(registro);
    }catch(Exception e){
        e.printStackTrace();
    }
}

public void actualizar(){
    try{
        PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_EditarCompras(?, ?, ?, ?)}");
        Compras registro = ((Compras)tblCompras.getSelectionModel().getSelectedItem());
        registro.setDescripcion(txtDescComp.getText());
        LocalDate fechaSeleccionada = txtFechaDoc.getValue();
        
        // Convertir LocalDate a java.sql.Date
        Date fechaDocumento = Date.valueOf(fechaSeleccionada);
        
        registro.setTotalDocumento(Double.parseDouble(txtTotComp.getText()));
        procedimiento.setInt(1, registro.getNumeroDocumento());
        procedimiento.setDate(2, fechaDocumento); // Aquí se establece la fecha
        procedimiento.setString(3, registro.getDescripcion());
        procedimiento.setDouble(4, registro.getTotalDocumento());
        procedimiento.execute();
        listaCompras.add(registro);
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
                if (tblCompras.getSelectionModel().getSelectedItem() != null) {
                    int respuesta = JOptionPane.showConfirmDialog(null, "Confirmar para eliminar el registro", "Confirmacion Eliminar Compra", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if (respuesta == JOptionPane.YES_NO_OPTION) {
                        try{
                            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_EliminarCompras(?)}");
                            procedimiento.setInt(1, ((Compras)tblCompras.getSelectionModel().getSelectedItem()).getNumeroDocumento());
                            procedimiento.execute();
                            listaCompras.remove(tblCompras.getSelectionModel().getSelectedItem());
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
                if (tblCompras.getSelectionModel().getSelectedItem() != null) {
                    btnEditar.setText("ACTUALIZAR");
                    btnReporte.setText("CANCELAR");
                    btnAgregar.setDisable(true);
                    btnEliminar.setDisable(true);
                    imgEditar.setImage(new Image("/org/adrianmorataya/image/GUARDAR.png"));
                    imgReporte.setImage(new Image("/org/adrianmorataya/image/CANCELAR.png"));
                    activarControles();
                    txtNumDoc.setEditable(false);
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
        txtNumDoc.setEditable(false);
        txtFechaDoc.setEditable(false);
        txtDescComp.setEditable(false);
        txtTotComp.setEditable(false);
        txtNumDoc.setDisable(true);
        txtFechaDoc.setDisable(true);
        txtDescComp.setDisable(true);
        txtTotComp.setDisable(true);
    }
    
    public void activarControles(){
        txtNumDoc.setEditable(true);
        txtFechaDoc.setEditable(true);
        txtDescComp.setEditable(true);
        txtTotComp.setEditable(true);
        txtNumDoc.setDisable(false);
        txtFechaDoc.setDisable(false);
        txtDescComp.setDisable(false);
        txtTotComp.setDisable(false);
    }
    
    public void limpiarControles(){
        txtNumDoc.clear();
        txtDescComp.clear();
        txtTotComp.clear();
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
