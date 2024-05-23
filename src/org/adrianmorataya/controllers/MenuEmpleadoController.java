package org.adrianmorataya.controllers;

import com.jfoenix.controls.JFXTimePicker;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Time;
import java.time.LocalTime;
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
import javafx.scene.text.Text;
import javax.swing.JOptionPane;
import org.adrianmorataya.bean.CargoEmpleado;
import org.adrianmorataya.bean.Empleados;
import org.adrianmorataya.dao.Conexion;
import org.adrianmorataya.system.Main;

public class MenuEmpleadoController implements Initializable{
    private Main escenarioPrincipal;
    private enum operaciones {AGREGAR, ELIMINAR, EDITAR, ACTUALIZAR, CANCELAR, NINGUNO}
    private operaciones tipoDeOperaciones = operaciones.NINGUNO;
    private ObservableList <Empleados> listaEmpleado;
    private ObservableList <CargoEmpleado> listaCargo;
    @FXML private TextField txtEmpleadoId;
    @FXML private TextField txtNombE;
    @FXML private TextField txtApellidoE;
    @FXML private TextField txtSueldoE;
    @FXML private ComboBox cmbCargoId;
    @FXML private JFXTimePicker tmpHoraEntrada;
    @FXML private JFXTimePicker tmpHoraSalida;
    @FXML private TableView tblEmpleados;
    @FXML private TableColumn colEmpleadoId;
    @FXML private TableColumn colNombE;
    @FXML private TableColumn colApellidoE;
    @FXML private TableColumn colSueldoE;
    @FXML private TableColumn colCargoId;
    @FXML private TableColumn colHoraEntrada;
    @FXML private TableColumn colHoraSalida;
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
        cmbCargoId.setItems(getCargoEmpleado());
        txtEmpleadoId.setDisable(true);
        txtNombE.setDisable(true);
        txtApellidoE.setDisable(true);
        txtSueldoE.setDisable(true);
        cmbCargoId.setDisable(true);
        tmpHoraEntrada.setDisable(true);
        tmpHoraSalida.setDisable(true);
    }
    
    public void cargarDatos(){
        tblEmpleados.setItems(getEmpleados());
        colEmpleadoId.setCellValueFactory(new PropertyValueFactory<Empleados, Integer>("empleadoId"));
        colNombE.setCellValueFactory(new PropertyValueFactory<Empleados, String>("nombreEmpleado"));
        colApellidoE.setCellValueFactory(new PropertyValueFactory<Empleados, String>("apellidoEmpleado"));
        colSueldoE.setCellValueFactory(new PropertyValueFactory<Empleados, Double>("sueldo"));
        colHoraEntrada.setCellValueFactory(new PropertyValueFactory<Empleados, Time>("horaEntrada"));
        colHoraSalida.setCellValueFactory(new PropertyValueFactory<Empleados, Time>("horaSalida"));
        colCargoId.setCellValueFactory(new PropertyValueFactory<Empleados, Integer>("cargoId"));
    }
    
    public void selecionarElemento(){
        txtEmpleadoId.setText(String.valueOf(((Empleados)tblEmpleados.getSelectionModel().getSelectedItem()).getEmpleadoId()));
        txtNombE.setText(((Empleados)tblEmpleados.getSelectionModel().getSelectedItem()).getNombreEmpleado());
        txtApellidoE.setText(((Empleados)tblEmpleados.getSelectionModel().getSelectedItem()).getApellidoEmpleado());
        txtSueldoE.setText(String.valueOf(((Empleados)tblEmpleados.getSelectionModel().getSelectedItem()).getSueldo()));
        cmbCargoId.getSelectionModel().select(buscarC(((Empleados)tblEmpleados.getSelectionModel().getSelectedItem()).getCargoId()));
    }
    
    public CargoEmpleado buscarC (int codCargo){
        CargoEmpleado resultado = null;
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_BuscarCargoEmpleado(?)}");
            procedimiento.setInt(1, codCargo);
            ResultSet registro = procedimiento.executeQuery();
            while(registro.next()){
                resultado = new CargoEmpleado(registro.getInt("codigoCargoEmpleado"),
                                        registro.getString("nombreCargo"),
                                        registro.getString("descripcionCargo")
                );
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return resultado;
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
        return listaCargo = FXCollections.observableArrayList(lista);
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
                if (tblEmpleados.getSelectionModel().getSelectedItem() != null) {
                    btnEditar.setText("ACTUALIZAR");
                    btnReporte.setText("CANCELAR");
                    btnAgregar.setDisable(true);
                    btnEliminar.setDisable(true);
                    imgEditar.setImage(new Image("/org/adrianmorataya/image/GUARDAR.png"));
                    imgReporte.setImage(new Image("/org/adrianmorataya/image/CANCELAR.png"));
                    activarControles();
                    cmbCargoId.setDisable(true);
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
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_EditarEmpleados(?, ?, ?, ?, ?, ?, ?)}");
            Empleados registro = ((Empleados)tblEmpleados.getSelectionModel().getSelectedItem());
            registro.setNombreEmpleado(txtNombE.getText());
            registro.setApellidoEmpleado(txtApellidoE.getText());
            registro.setSueldo(Double.parseDouble(txtSueldoE.getText()));
            LocalTime horaSelec = tmpHoraEntrada.getValue();
            Time horaEntrada = Time.valueOf(horaSelec);
            LocalTime horaSelec2 = tmpHoraSalida.getValue();
            Time horaSalida = Time.valueOf(horaSelec2);
            procedimiento.setInt(1, registro.getEmpleadoId());
            procedimiento.setString(2, registro.getNombreEmpleado());
            procedimiento.setString(3, registro.getApellidoEmpleado());
            procedimiento.setDouble(4, registro.getSueldo());
            procedimiento.setTime(5, registro.getHoraEntrada());
            procedimiento.setTime(6, registro.getHoraSalida());
            procedimiento.setInt(7, registro.getCargoId());
            procedimiento.execute();
            listaEmpleado.add(registro);
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
        Empleados registro = new Empleados();
        registro.setEmpleadoId(Integer.parseInt(txtEmpleadoId.getText()));
        registro.setCargoId(((CargoEmpleado)cmbCargoId.getSelectionModel().getSelectedItem()).getCodigoCargoEmpleado());
        registro.setNombreEmpleado(txtNombE.getText());
        registro.setApellidoEmpleado(txtApellidoE.getText());
        registro.setSueldo(Double.parseDouble(txtSueldoE.getText()));
        LocalTime entLocal = tmpHoraEntrada.getValue();
        LocalTime salLocal = tmpHoraSalida.getValue();
         if (entLocal != null && salLocal != null) {
            Time horaEntrada = Time.valueOf(entLocal);
            Time horaSalida = Time.valueOf(salLocal);
            registro.setHoraEntrada(horaEntrada);
            registro.setHoraSalida(horaSalida);
        } else {

            JOptionPane.showMessageDialog(null, "Debe seleccionar las horas de entrada y salida.");
            return;
        }
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_AgregarEmpleados(?, ?, ?, ?, ?, ?, ?)}");
            procedimiento.setInt(1, registro.getEmpleadoId());
            procedimiento.setString(2, registro.getNombreEmpleado());
            procedimiento.setString(3, registro.getApellidoEmpleado());
            procedimiento.setDouble(4, registro.getSueldo());
            procedimiento.setTime(5, registro.getHoraEntrada());
            procedimiento.setTime(6, registro.getHoraSalida());
            procedimiento.setInt(7, registro.getCargoId());
            procedimiento.execute();
            listaEmpleado.add(registro);
        }catch(Exception e){
            e.printStackTrace();
        }
        
    }
    
    public void activarControles(){
        txtEmpleadoId.setEditable(true);
        txtNombE.setEditable(true);
        txtApellidoE.setEditable(true);
        txtSueldoE.setEditable(true);
        txtEmpleadoId.setDisable(false);
        txtNombE.setDisable(false);
        txtApellidoE.setDisable(false);
        txtSueldoE.setDisable(false);
        cmbCargoId.setDisable(false);
        tmpHoraEntrada.setDisable(false);
        tmpHoraSalida.setDisable(false);
    }
    
    public void desactivarControles(){
        txtEmpleadoId.setEditable(false);
        txtNombE.setEditable(false);
        txtApellidoE.setEditable(false);
        txtSueldoE.setEditable(false);
        txtEmpleadoId.setDisable(true);
        txtNombE.setDisable(true);
        txtApellidoE.setDisable(true);
        txtSueldoE.setDisable(true);
        cmbCargoId.setDisable(true);
        tmpHoraEntrada.setDisable(true);
        tmpHoraSalida.setDisable(true);
    }
    
    public void limpiarControles(){
        txtEmpleadoId.clear();
        txtNombE.clear();
        txtApellidoE.clear();
        txtSueldoE.clear();
        cmbCargoId.getSelectionModel().clearSelection();
        tmpHoraEntrada.setValue(null);
        tmpHoraSalida.setValue(null);
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
