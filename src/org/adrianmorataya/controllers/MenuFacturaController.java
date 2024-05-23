package org.adrianmorataya.controllers;

import com.jfoenix.controls.JFXTimePicker;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javax.swing.JOptionPane;
import org.adrianmorataya.bean.Clientes;
import org.adrianmorataya.bean.Empleados;
import org.adrianmorataya.bean.Facturas;
import org.adrianmorataya.dao.Conexion;
import org.adrianmorataya.system.Main;

public class MenuFacturaController implements Initializable{
    private Main escenarioPrincipal;
    private enum operaciones {AGREGAR, ELIMINAR, EDITAR, ACTUALIZAR, CANCELAR, NINGUNO}
    private operaciones tipoDeOperaciones = operaciones.NINGUNO;
    private ObservableList <Empleados> listaEmpleado;
    private ObservableList <Clientes> listaClientes;
    private ObservableList <Facturas> listaFacturas;
    @FXML private TextField txtFacturaId;
    @FXML private TextField txtTotalF;
    @FXML private ComboBox cmbClienteId;
    @FXML private ComboBox cmbEmpleadoId;
    @FXML private TableView tblFacturas;
    @FXML private TableColumn colFacturaId;
    @FXML private TableColumn colFechaF;
    @FXML private TableColumn colHoraF;
    @FXML private TableColumn colTotalF;
    @FXML private TableColumn colClienteId;
    @FXML private TableColumn colEmpleadoId;
    @FXML private Button btnAgregar;
    @FXML private Button btnEliminar;
    @FXML private Button btnEditar;
    @FXML private Button btnReporte;
    @FXML private Button btnRegresar;
    @FXML private DatePicker txtFechaF;
    @FXML private JFXTimePicker tmpHoraF;
    @FXML private ImageView imgAgregar;
    @FXML private ImageView imgEliminar;
    @FXML private ImageView imgEditar;
    @FXML private ImageView imgReporte;
    
            
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
        cmbClienteId.setItems(getClientes());
        cmbEmpleadoId.setItems(getEmpleados());
        txtFacturaId.setDisable(true);
        txtTotalF.setDisable(true);
        cmbClienteId.setDisable(true);
        cmbEmpleadoId.setDisable(true);
        txtFechaF.setDisable(true);
        tmpHoraF.setDisable(true);
    }
    
    public void cargarDatos(){
        tblFacturas.setItems(getFacturas());
        colFacturaId.setCellValueFactory(new PropertyValueFactory<Facturas, Integer>("facturaId"));
        colFechaF.setCellValueFactory(new PropertyValueFactory<Facturas, LocalDate>("fecha"));
        colHoraF.setCellValueFactory(new PropertyValueFactory<Facturas, Time>("hora"));
        colTotalF.setCellValueFactory(new PropertyValueFactory<Facturas, Double>("total"));
        colClienteId.setCellValueFactory(new PropertyValueFactory<Facturas, Integer>("codigoCliente"));
        colEmpleadoId.setCellValueFactory(new PropertyValueFactory<Facturas, Integer>("empleadoId"));
    }
    
    public void selecionarElemento(){
        Facturas facturaSeleccionada = (Facturas) tblFacturas.getSelectionModel().getSelectedItem();
        txtFacturaId.setText(String.valueOf(((Facturas)tblFacturas.getSelectionModel().getSelectedItem()).getFacturaId()));
        txtTotalF.setText(String.valueOf(((Facturas)tblFacturas.getSelectionModel().getSelectedItem()).getTotal()));
        cmbEmpleadoId.getSelectionModel().select(buscarE(((Facturas)tblFacturas.getSelectionModel().getSelectedItem()).getEmpleadoId()));
        cmbClienteId.getSelectionModel().select(buscarC(((Facturas)tblFacturas.getSelectionModel().getSelectedItem()).getCodigoCliente()));
    }
    
    public Empleados buscarE (int empId){
        Empleados resultado = null;
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_buscarEmpleados(?)}");
            procedimiento.setInt(1, empId);
            ResultSet registro = procedimiento.executeQuery();
            while(registro.next()){
                resultado = new Empleados(registro.getInt("empleadoId"),
                        registro.getString("nombreEmpleado"),
                        registro.getString("apellidoEmpleado"),
                        registro.getDouble("sueldo"),
                        registro.getTime("horaEntrada"),
                        registro.getTime("horaSalida"),
                        registro.getInt("cargoId")
                );
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return resultado;
    }
    
    public Clientes buscarC (int codCli){
        Clientes resultado = null;
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_BuscarClientes(?)}");
            procedimiento.setInt(1, codCli);
            ResultSet registro = procedimiento.executeQuery();
            while(registro.next()){
                resultado = new Clientes(registro.getInt("codigoCliente"),
                        registro.getString("NITCliente"),
                        registro.getString("nombreCliente"),
                        registro.getString("apellidoCliente"),
                        registro.getString("direccionCliente"),
                        registro.getString("telefonoCliente"),
                        registro.getString("correoCliente")
                );
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return resultado;
    }
    
    public ObservableList<Empleados> getEmpleados(){
        ArrayList<Empleados>lista = new ArrayList<>();
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarEmpleados()}");
            ResultSet resultado = procedimiento.executeQuery();
            while (resultado.next()) {
                lista.add(new Empleados (resultado.getInt("empleadoId"),
                        resultado.getString("nombreEmpleado"),
                        resultado.getString("apellidoEmpleado"),
                        resultado.getDouble("sueldo"),
                        resultado.getTime("horaEntrada"),
                        resultado.getTime("horaSalida"),
                        resultado.getInt("cargoId")
                ));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return listaEmpleado = FXCollections.observableArrayList(lista);
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
    
    public ObservableList<Facturas> getFacturas(){
        ArrayList<Facturas>lista = new ArrayList<>();
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarFacturas()}");
            ResultSet resultado = procedimiento.executeQuery();
            while (resultado.next()) {
                java.sql.Date fechaSql = resultado.getDate("fecha");
                LocalDate fechaTemp = fechaSql.toLocalDate();
                lista.add(new Facturas (resultado.getInt("facturaId"),
                                        fechaTemp,
                                        resultado.getTime("hora"),
                                        resultado.getDouble("total"),
                                        resultado.getInt("codigoCliente"),
                                        resultado.getInt("empleadoId")
                ));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        
        return listaFacturas = FXCollections.observableArrayList(lista);
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
                if (tblFacturas.getSelectionModel().getSelectedItem() != null) {
                    btnEditar.setText("ACTUALIZAR");
                    btnReporte.setText("CANCELAR");
                    btnAgregar.setDisable(true);
                    btnEliminar.setDisable(true);
                    imgEditar.setImage(new Image("/org/adrianmorataya/image/GUARDAR.png"));
                    imgReporte.setImage(new Image("/org/adrianmorataya/image/CANCELAR.png"));
                    activarControles();
                    txtFacturaId.setEditable(false);
                    cmbEmpleadoId.setDisable(true);
                    cmbClienteId.setDisable(true);
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
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_EditarFacturas(?, ?, ?, ?, ?, ?)}");
            Facturas registro = ((Facturas)tblFacturas.getSelectionModel().getSelectedItem());
            registro.setTotal(Integer.parseInt(txtTotalF.getText()));
            LocalDate fechaSelec = txtFechaF.getValue();
            Date fechaFac = Date.valueOf(fechaSelec);
            LocalTime horaLocal = tmpHoraF.getValue();
             if (horaLocal != null) {
                Time horaF = Time.valueOf(horaLocal);
                registro.setHora(horaF);
            } else {

                JOptionPane.showMessageDialog(null, "Debe seleccionar las horas de entrada y salida.");
                return;
            }
            procedimiento.setInt(1, registro.getFacturaId());
            procedimiento.setDate(2, fechaFac);
            procedimiento.setTime(3, registro.getHora());
            procedimiento.setDouble(4, registro.getTotal());
            procedimiento.setInt(5, registro.getCodigoCliente());
            procedimiento.setInt(6, registro.getEmpleadoId());
            procedimiento.execute();
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
        Facturas registro = new Facturas();
        registro.setFacturaId(Integer.parseInt(txtFacturaId.getText()));
        registro.setEmpleadoId(((Empleados)cmbEmpleadoId.getSelectionModel().getSelectedItem()).getEmpleadoId());
        registro.setCodigoCliente(((Clientes)cmbClienteId.getSelectionModel().getSelectedItem()).getCodigoCliente());
        registro.setTotal(Integer.parseInt(txtTotalF.getText()));
        LocalDate fechaSelec = txtFechaF.getValue();
        Date fechaFac = Date.valueOf(fechaSelec);
        LocalTime horaLocal = tmpHoraF.getValue();
         if (horaLocal != null) {
            Time horaF = Time.valueOf(horaLocal);
            registro.setHora(horaF);
        } else {

            JOptionPane.showMessageDialog(null, "Debe seleccionar las horas de entrada y salida.");
            return;
        }
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_AgregarFacturas(?, ?, ?, ?, ?, ?)}");
            procedimiento.setInt(1, registro.getFacturaId());
            procedimiento.setDate(2, fechaFac);
            procedimiento.setTime(3, registro.getHora());
            procedimiento.setDouble(4, registro.getTotal());
            procedimiento.setInt(5, registro.getCodigoCliente());
            procedimiento.setInt(6, registro.getEmpleadoId());
            procedimiento.execute();
            listaFacturas.add(registro);
        }catch(Exception e){
            e.printStackTrace();
        }
        
    }
    
    public void activarControles(){
        txtFacturaId.setDisable(false);
        txtTotalF.setDisable(false);
        txtFechaF.setDisable(false);
        txtFacturaId.setEditable(true);
        txtTotalF.setEditable(true);
        txtFechaF.setEditable(true);
        cmbClienteId.setDisable(false);
        cmbEmpleadoId.setDisable(false);
        tmpHoraF.setDisable(false);
    }
    
    public void desactivarControles(){
        txtFacturaId.setDisable(true);
        txtTotalF.setDisable(true);
        txtFechaF.setDisable(true);
        txtFacturaId.setEditable(false);
        txtTotalF.setEditable(false);
        txtFechaF.setEditable(false);
        cmbClienteId.setDisable(true);
        cmbEmpleadoId.setDisable(true);
        tmpHoraF.setDisable(true);
    }
    
    public void limpiarControles(){
        txtFacturaId.clear();
        txtTotalF.clear();
        txtFechaF.setValue(null);
        cmbClienteId.getSelectionModel().clearSelection();
        cmbEmpleadoId.getSelectionModel().clearSelection();
        tmpHoraF.setValue(null);
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
}
