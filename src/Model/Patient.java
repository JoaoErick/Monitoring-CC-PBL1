/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import java.io.Serializable;
/**
 *
 * @author João Erick Barbosa
 */
public class Patient implements Serializable {
    private String id;
    private String userName;
    private String respiratoryFrequency;
    private String temperature;
    private String bloodOxygen;
    private String heartRate;
    private String bloodPressure;
    private boolean seriousness = false;
    
    /**
     * Método construtor de paciente.
     * 
     * @param id
     * @param userName
     * @param respiratoryFrequency
     * @param temperature
     * @param bloodOxygen
     * @param heartRate
     * @param bloodPressure 
     */
    public Patient(String id, String userName, String respiratoryFrequency, String temperature, String bloodOxygen, String heartRate, String bloodPressure) {
        this.id = id;
        this.userName = userName;
        this.respiratoryFrequency = respiratoryFrequency;
        this.temperature = temperature;
        this.bloodOxygen = bloodOxygen;
        this.heartRate = heartRate;
        this.bloodPressure = bloodPressure;
        
        verifySeriousness();
    }
    
    /**
     * Método que verifica se o paciente criado está em estado grave de COVID-19.
     */
    public void verifySeriousness(){
        if(Integer.parseInt(this.respiratoryFrequency) >= 21 
                || Integer.parseInt(this.heartRate) >= 111 
                || Integer.parseInt(this.bloodPressure) <= 100 
                || Double.parseDouble(this.temperature) >= 38.6 
                || Double.parseDouble(this.bloodOxygen) <= 96.0){
            
            this.seriousness = true;
        }
    }

    public Patient() {
    }

    /**
     * Método que retorna o ID do paciente.
     * 
     * @return String
     */
    public String getId() {
        return id;
    }
    
    /**
     * Método que retorna o nome do paciente.
     * 
     * @return String
     */
    public String getUserName() {
        return userName;
    }

    /**
     * Método que retorna o valor da frequência respiratória do paciente.
     * 
     * @return String
     */
    public String getRespiratoryFrequency() {
        return respiratoryFrequency;
    }

    /**
     * Método que altera o valor da frequência respiratória do paciente.
     * 
     */
    public void setRespiratoryFrequency(String respiratoryFrequency) {
        this.respiratoryFrequency = respiratoryFrequency;
    }

    /**
     * Método que retorna o valor da temperatura do paciente.
     * 
     * @return String
     */
    public String getTemperature() {
        return temperature;
    }

    /**
     * Método que altera o valor da temperatura do paciente.
     * 
     */
    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }
    
    /**
     * Método que retorna o valor da oxigenação do sangue do paciente.
     * 
     * @return String
     */
    public String getBloodOxygen() {
        return bloodOxygen;
    }

    /**
     * Método que altera o valor da oxigenação do sangue do paciente.
     * 
     */
    public void setBloodOxygen(String bloodOxygen) {
        this.bloodOxygen = bloodOxygen;
    }

    /**
     * Método que retorna o valor dos batimentos cardíacos por minuto do paciente.
     * 
     * @return String
     */
    public String getHeartRate() {
        return heartRate;
    }

    /**
     * Método que altera o valor dos batimentos cardíacos por minuto do paciente.
     * 
     */
    public void setHeartRate(String heartRate) {
        this.heartRate = heartRate;
    }

    /**
     * Método que retorna o valor da pressão arterial do paciente.
     * 
     * @return String
     */
    public String getBloodPressure() {
        return bloodPressure;
    }

    /**
     * Método que altera o valor da pressão arterial do paciente.
     * 
     */
    public void setBloodPressure(String bloodPressure) {
        this.bloodPressure = bloodPressure;
    }

    /**
     * Método que retorna se o paciente está grave ou não.
     * 
     * @return boolean
     */
    public boolean isSeriousness() {
        return seriousness;
    }
    
    
    
}
