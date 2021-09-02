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
    
    public ThreadClient(Socket client) {
        this.client = client;
    }

    public static List<Patient> getPatients() {
        return patients;
    }

    @Override
    public void run() {
//            while(true){
//            
//            
//            PrintStream data;
//            try {
//                data = new PrintStream(client.getOutputStream());
//                data.println("GET /list");
//
//                ObjectInputStream entrada = new ObjectInputStream(client.getInputStream());
//                MonitoringController.setPatients((List<Patient>)entrada.readObject());
//                System.out.println("EXECUTEI");
//                Thread.sleep(3000);
//            } catch (IOException ex) {
//                Logger.getLogger(MonitoringController.class.getName()).log(Level.SEVERE, null, ex);
//            } catch (ClassNotFoundException ex) {
//                Logger.getLogger(ThreadClient.class.getName()).log(Level.SEVERE, null, ex);
//            }   catch (InterruptedException ex) {
//                    Logger.getLogger(ThreadClient.class.getName()).log(Level.SEVERE, null, ex);
//                }
//            
//            }

    }
    
}
