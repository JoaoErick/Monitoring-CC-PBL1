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
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import monitoring.Monitoring;

/**
 *
 * @author Optimus 2020
 */
public class MonitoringController implements Initializable {
    
    @FXML
    private Label label;
    
    @FXML
    private TableView<?> table;

    @FXML
    private TableColumn<?, ?> clmID;

    @FXML
    private TableColumn<?, ?> clmUserName;
    
    private static Socket client;
    
    private static List<Patient> patients = new ArrayList();
    
    @FXML
    private Label lbl;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initClient();
        
        try {
            if(client != null){
                if(sendMessage()){
                    System.out.println("Mensagem enviada com sucesso!");
                    for (int i = 0; i < patients.size(); i++) {
                        System.out.println("Paciente " + (i+1));
                    }
                } else{
                    System.out.println("Erro, falha ao enviar a mensagem!");
                }
            } else {
                System.out.println("Cliente não conectado!");
            }
            

        } catch (ClassNotFoundException ex) {
            Logger.getLogger(MonitoringController.class.getName()).log(Level.SEVERE, null, ex);
        }
            
        
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
    
    //Envia os dados ao servidor a partir do que for digitado.
    private static boolean sendMessage() throws ClassNotFoundException{
        try {
            PrintStream data = new PrintStream(client.getOutputStream());
            data.println("GET");
            
            ObjectInputStream entrada = new ObjectInputStream(client.getInputStream());
            patients = (List<Patient>)entrada.readObject();
            System.out.println("Resposta do servidor: Existem " + patients.size() + " pacientes em estado grave!" );
            
            
            return true;
        } catch (IOException ex) {
            System.out.println("Erro ao enviar a mensagem!");
        }
        return false;
    }
}
