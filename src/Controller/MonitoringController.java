/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Model.Patient;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
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
 * Classe controladora responsável por executar funções dispararadas por ações 
 * realizadas na janela da aplicação do monitoramento do médico.
 * 
 * @author João Erick Barbosa
 */
public class MonitoringController implements Initializable {
    
    /**
     * Componente servirá de plano de fundo para a exibição de mais informações 
     * sobre o paciente.
     */
    @FXML
    private Pane paneInfo;

    /**
     * Indicativo de que o usuário deve selecionar um paciente.
     */
    @FXML
    private Label lblSelectPatient;

    /**
     * Componentes para exibição do nome do paciente.
     */
    @FXML
    private Label txtUserName;

    @FXML
    private Label lblUserName;

    /**
     * Componentes para exibição da frequência respiratória do paciente.
     */
    @FXML
    private Label txtRespiratoryFrequency;

    @FXML
    private Label lblRespiratoryFrequency;

    /**
     * Componentes para exibição da temperatura do paciente.
     */
    @FXML
    private Label txtTemperature;

    @FXML
    private Label lblTemperature;

    /**
     * Componentes para exibição da oxigenação do sangue do paciente.
     */
    @FXML
    private Label txtBloodOxygen;

    @FXML
    private Label lblBloodOxygen;

    /**
     * Componentes para exibição da frequência cardíaca do paciente.
     */
    @FXML
    private Label txtHeartRate;

    @FXML
    private Label lblHeartRate;

    /**
     * Componentes para exibição da pressão arterial do paciente.
     */
    @FXML
    private Label txtBloodPressure;

    @FXML
    private Label lblBloodPressure;
    
    /**
     * Componentes para a listagem dos pacientes em uma tabela selecionável.
     */
    @FXML
    private TableView<Patient> table;

    @FXML
    private TableColumn<Patient, String> clmID;

    @FXML
    private TableColumn<Patient, String> clmUserName;
    
    @FXML
    private TableColumn<Patient, String> clmSituation;
    
    /**
     * Cliente Socket que fará conexão com o servidor.
     */
    private static Socket client;
    
    /**
     * Armazena a lista de pacientes resgata do servidor.
     */
    private static List<Patient> patients = new ArrayList();
    
    /**
     * Armazena a lista de pacientes ordenada para que pacientes graves fiquem 
     * no início da lista.
     */
    private static List<Patient> orderedPatients = new ArrayList();
    
    //Armazena o paciente selecionado da tabela de pacientes.
    private Patient selected; 
    //Armazena o paciente que estava selecionado para atualizar suas informações na tela.
    private Patient selectedRefresh; 
    //Transforma a lista de pacientes em uma ObservableList para ser possível a exibição na tabela.
    private ObservableList<Patient> patientsTable = FXCollections.observableArrayList(); 
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //Faz a conexão do cliente com o servidor.
        initClient();
        
        //Inicializa a tabela de pacientes.
        try {
            initTable();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(MonitoringController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        System.out.println("Resposta do servidor: Existem " + orderedPatients.size() + " pacientes em estado grave!" );
        
        //Armazena um paciente caso algum item da tabela de pacientes seja selecionado.
        table.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                selected = (Patient) newValue;
                if(selected != null){
                    selectedRefresh = selected;
                }
                showDetails();
            }
        });
            
        /**
         * Uma nova thread é inicializada concorrentemente ao sistema para fazer
         * requisições ao servidor. A cada 5 segundos, uma nova lista de 
         * pacientes é requisitada e a tabela é atualizada. 
         * 
         */
        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
                Runnable updater = new Runnable() {

                    @Override
                    public void run() {
                        orderedPatients = new ArrayList();
                        try {
                            refreshTable();
                            showRefreshDetails();
                        } catch (ClassNotFoundException ex) {
                            Logger.getLogger(MonitoringController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        table.setItems(getOrderedPatients());
                    }
                };

                while (true) {
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException ex) {
                    }

                    // A atualização é feita na thread da aplicação.
                    Platform.runLater(updater);
                }
            }

        });
        // Impede a thread de finalizar a JVM
        thread.setDaemon(true);
        thread.start();
    }    
    
    /**
     * Inicializa a conexão cliente com o servidor através do endereço IP e 
     * porta especificados.
     */
    private static void initClient(){
        try {
            client = new Socket("127.0.0.2", 60000);
            System.out.println("Conexão estabelecida!");
        } catch (IOException ex) {
            System.out.println("Erro, a conexão com o servidor não foi estabelecida!");
        }
    }
    
    /**
     * Inicializa a tabela de pacientes com dados presentes na 
     * lista de pacientes ordenada.
     * 
     * @throws ClassNotFoundException - Exceção lançada caso uma classe não 
     * seja encontrada.
     */
    public void initTable() throws ClassNotFoundException{
        clmID.setCellValueFactory(new PropertyValueFactory("id"));
        clmUserName.setCellValueFactory(new PropertyValueFactory("userName"));
        clmSituation.setCellValueFactory(new PropertyValueFactory("situation"));
        
        if(client != null){
            refreshTable();
            table.setItems(getOrderedPatients());
        } else{
            System.out.println("A aplicação não conseguiu se conectar ao servidor!");
        }
    }
    
    /**
     * Ordena a lista de pacientes de forma que os pacientes graves fiquem no 
     * início da lista.
     * 
     * @return ObservableList<Patient> - Pacientes ordenados.
     */
    public ObservableList<Patient> getOrderedPatients(){
        for (int i = 0; i < patients.size(); i++) {
            if(patients.get(i).isSeriousness()){
                orderedPatients.add(patients.get(i));
            }
        }
        for (int i = 0; i < patients.size(); i++) {
            if(!patients.get(i).isSeriousness()){
                orderedPatients.add(patients.get(i));
            }
        }
        
        Collections.sort(orderedPatients);
        patientsTable = FXCollections.observableArrayList(orderedPatients);
        return patientsTable;
    }
    
    /**
     * Faz requisição ao servidor para resgatar a lista de pacientes do sistema.
     * 
     * @return List<Patient> - Lista de pacientes.
     * @throws ClassNotFoundException - Exceção lançada caso uma classe não 
     * seja encontrada.
     */
    public List<Patient> refreshTable() throws ClassNotFoundException{
        PrintStream data;
        try {
            data = new PrintStream(client.getOutputStream());
            data.println("GET /list");
            
            ObjectInputStream input = new ObjectInputStream(client.getInputStream());
            patients = (List<Patient>)input.readObject();
            
            String text = (String)input.readObject();
            System.out.println("Resposta do servidor: " + text);
            
        } catch (IOException ex) {
            Logger.getLogger(MonitoringController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return patients;
    }
    
    /**
     * Exibe todas as informações do paciente selecionado.
     */
    public void showDetails(){
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
            
            selectedRefresh = selected;
        } 
    }
    
    /**
     * Atualiza as informações do paciente selecionado.
     */
    public void showRefreshDetails(){
        if (selectedRefresh != null) {
            for (int i = 0; i < patients.size(); i++) {
                if (patients.get(i).getId().equals(selectedRefresh.getId())) {
                    lblUserName.setText(patients.get(i).getUserName());
                    lblRespiratoryFrequency.setText(patients.get(i).getRespiratoryFrequency() + " movimentos/min");
                    lblTemperature.setText(patients.get(i).getTemperature() + " ºC");
                    lblBloodOxygen.setText(patients.get(i).getBloodOxygen() + " %");
                    lblHeartRate.setText(patients.get(i).getHeartRate() + " batimentos/min");
                    lblBloodPressure.setText(patients.get(i).getBloodPressure() + " mmHg");
                }
            }

        }
    }
    
}
