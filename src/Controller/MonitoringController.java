/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Model.Patient;
import Util.ThreadClient;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

/**
 *
 * @author João Erick Barbosa
 */
public class MonitoringController implements Initializable {
    
    @FXML
    private Pane paneInfo;

    @FXML
    private Label lblSelectPatient;

    @FXML
    private Label txtUserName;

    @FXML
    private Label lblUserName;

    @FXML
    private Label txtRespiratoryFrequency;

    @FXML
    private Label lblRespiratoryFrequency;

    @FXML
    private Label txtTemperature;

    @FXML
    private Label lblTemperature;

    @FXML
    private Label txtBloodOxygen;

    @FXML
    private Label lblBloodOxygen;

    @FXML
    private Label txtHeartRate;

    @FXML
    private Label lblHeartRate;

    @FXML
    private Label txtBloodPressure;

    @FXML
    private Label lblBloodPressure;
    
    @FXML
    private TableView<Patient> table;

    @FXML
    private TableColumn<Patient, String> clmID;

    @FXML
    private TableColumn<Patient, String> clmUserName;
    
    @FXML
    private TableColumn<Patient, String> clmSituation;
    
    private static Socket client;
    
    private static List<Patient> patients = new ArrayList();
    
    private static List<Patient> patientsSeriousness = new ArrayList();
    
    private Patient selected;
    private ObservableList<Patient> patientsTable = FXCollections.observableArrayList();
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initClient();
        
        try {
            initTable();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(MonitoringController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        System.out.println("Resposta do servidor: Existem " + patientsSeriousness.size() + " pacientes em estado grave!" );
        
        table.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                selected = (Patient) newValue;
                mostraDetalhes();
            }
        
        });
            
        
//        try {
//            TimeUnit.SECONDS.sleep(30);
//        } catch (InterruptedException ex) {
//            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
//        }
            
    }    
    
    //Inicializa a conexão cliente com o servidor 60000, o IP 127.0.0.2 indica que o servidor está na mesma máquina que o cliente.
    private static void initClient(){
        try {
            client = new Socket("127.0.0.2", 60000);
            System.out.println("Conexão estabelecida!");
        } catch (IOException ex) {
            System.out.println("Erro, a conexão com o servidor não foi estabelecida!");
        }
    }
    
    public void initTable() throws ClassNotFoundException{
        clmID.setCellValueFactory(new PropertyValueFactory("id"));
        clmUserName.setCellValueFactory(new PropertyValueFactory("userName"));
        clmSituation.setCellValueFactory(new PropertyValueFactory("situation"));
        
        if(client != null){
            atualizaTabela();
//            new ThreadClient(client).atualizaTabela();
            table.setItems(getPatientsSeriousness());
        } else{
            System.out.println("A aplicação não conseguiu se conectar ao servidor!");
        }
    }
    
    public ObservableList<Patient> getPatientsSeriousness(){
        for (int i = 0; i < patients.size(); i++) {
            if(patients.get(i).isSeriousness()){
                patientsSeriousness.add(patients.get(i));
            }
        }
        for (int i = 0; i < patients.size(); i++) {
            if(!patients.get(i).isSeriousness()){
                patientsSeriousness.add(patients.get(i));
            }
        }
        
        patientsTable = FXCollections.observableArrayList(patientsSeriousness);
        return patientsTable;
    }
    
    public List<Patient> atualizaTabela() throws ClassNotFoundException{
        PrintStream data;
        try {
            data = new PrintStream(client.getOutputStream());
            data.println("GET /list");
            
            ObjectInputStream entrada = new ObjectInputStream(client.getInputStream());
            patients = (List<Patient>)entrada.readObject();
            
        } catch (IOException ex) {
            Logger.getLogger(MonitoringController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return patients;
    }
    
    public void mostraDetalhes(){
        if(selected != null){
            paneInfo.setStyle("-fx-background-color: #eaeaea; -fx-border-color: #dfdfdf; -fx-border-radius: 8;");
            lblSelectPatient.setVisible(false);
            
            txtUserName.setVisible(true);
            txtRespiratoryFrequency.setVisible(true);
            txtTemperature.setVisible(true);
            txtBloodOxygen.setVisible(true);
            txtHeartRate.setVisible(true);
            txtBloodPressure.setVisible(true);
            
            lblUserName.setText(selected.getUserName());
            lblRespiratoryFrequency.setText(selected.getRespiratoryFrequency() + " movimentos/min");
            lblTemperature.setText(selected.getTemperature() + " ºC");
            lblBloodOxygen.setText(selected.getBloodOxygen() + " %");
            lblHeartRate.setText(selected.getHeartRate() + " batimentos/min");
            lblBloodPressure.setText(selected.getBloodPressure() + " mmHg");
        } 
    }

    public static void setPatients(List<Patient> patients) {
        MonitoringController.patients = patients;
    }
    
    
}
