/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Util;

import Controller.MonitoringController;
import Model.Patient;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 *
 * @author Optimus 2020
 */
public class ThreadClient extends Thread {
    private Socket client;
    private static List<Patient> patients = new ArrayList();
    private static List<Patient> patientsSeriousness = new ArrayList();
    
    @FXML
    private TableView<Patient> table;

    @FXML
    private TableColumn<Patient, String> clmID;

    @FXML
    private TableColumn<Patient, String> clmUserName;
    
    @FXML
    private TableColumn<Patient, String> clmSituation;

    public ThreadClient(Socket client) {
        this.client = client;
    }

    public static List<Patient> getPatients() {
        return patients;
    }
    
    @Override
    public void run() {
        while(true){
            
            try {
                atualizaTabela();
//                MonitoringController.setPatientsSeriousness(getPatientsSeriousness());
//                MonitoringController.getTable().setItems(FXCollections.observableArrayList(patientsSeriousness));
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(ThreadClient.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            try {
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException ex) {
                Logger.getLogger(ThreadClient.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        }
        
    }
    
    public void initTable() throws ClassNotFoundException{
//        clmID.setCellValueFactory(new PropertyValueFactory("id"));
//        clmUserName.setCellValueFactory(new PropertyValueFactory("userName"));
//        clmSituation.setCellValueFactory(new PropertyValueFactory("situation"));
        
        if(client != null){
            atualizaTabela();
//            table.setItems(getPatientsSeriousness());
//            MonitoringController.setTable(table);
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
        
        return FXCollections.observableArrayList(patientsSeriousness);
    }
    
    public void atualizaTabela() throws ClassNotFoundException{
            PrintStream data;
            try {
                data = new PrintStream(client.getOutputStream());
                data.println("GET");

                ObjectInputStream entrada = new ObjectInputStream(client.getInputStream());
                patients = (List<Patient>)entrada.readObject();
                MonitoringController.setPatients(patients);
                System.out.println(patients.size());

            } catch (IOException ex) {
                Logger.getLogger(MonitoringController.class.getName()).log(Level.SEVERE, null, ex);
            }
        
    }
}
