/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package healthapplicationproject.healthapplicationproject;

import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJB;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.List;

/**
 *
 * @author mohawk
 */

@Named
@SessionScoped
public class AppointmentsBean implements Serializable {
    @EJB
    private AppointmentsEJB appointmentPageServices;
    
    @EJB
    private AuthenticationEJB authenticationEJB;
    
    private boolean sorted;
    
    private Appointments newAppointment;
   
    private String inputAppointmentSearch;
    
  
  
    public AppointmentsBean(){
        sorted = false;
        newAppointment = new Appointments();
      
    }
    
    @PostConstruct
    public void init() {
        newAppointment.setPatientId(new Patients()); // for creating new appointment, need to initialize patient
    }
    
    public List<Appointments> getAllAppointments(){
        return appointmentPageServices.getAllAppointments();
    }
    public boolean isUpdateAppointmentListRender(){
        return appointmentPageServices.isUpdateAppointmentListRender();
    }
    public List<Appointments> getUpdateAppointmentList(){
        return appointmentPageServices.getUpdateAppointmentList();
    }
    public void sortAppointment(){
        appointmentPageServices.sortAppointment(appointmentPageServices.getAllAppointments());
    }
    
    public boolean isSorted(){
        return sorted;
    }
    public void setSorted(boolean sorted){
        this.sorted = sorted;
    }
    
    public String getSortedButtonLabel(){
        return sorted ? "Unsort" : "Sort";
    }
    
    public void sortedButtonToggle(){
        if (sorted){
            // sorted = true -> button value = unsort
            if (inputAppointmentSearch != null){
                appointmentPageServices.search(inputAppointmentSearch,authenticationEJB.getLoggedInUser());
            }
            else{
                
                appointmentPageServices.init();
            }
            
        }
        else{
            appointmentPageServices.sortAppointment(appointmentPageServices.getAllAppointments());
        }
        sorted = !sorted;
        appointmentPageServices.setSorted(sorted); // for persist sorting when perform update, add, delete
        
    }
    
    public Appointments getNewAppointment(){
        return newAppointment;
    }
    public void setNewAppointment(Appointments newAppointment){
        this.newAppointment = newAppointment;
    }
    
    public boolean isAddButtonClicked(){
        return appointmentPageServices.isAddButtonClicked();
    }
    
    public String getInputAppointmentSearch(){
        return inputAppointmentSearch;
    }
    
    public void setInputAppointmentSearch(String inputAppointmentSearch){
        this.inputAppointmentSearch = inputAppointmentSearch;
    }
    
    public String getErrorMessage(){
        return appointmentPageServices.getErrorMessage();
    }
    public void search(){
        appointmentPageServices.search(inputAppointmentSearch,authenticationEJB.getLoggedInUser());
       
    }
    
    
    public void update(){
        appointmentPageServices.update();
    }
    public void save(){
        appointmentPageServices.save();
    }
    public void cancelUpdate(){
        appointmentPageServices.cancelUpdate();
    }
    
    public void delete(){
        appointmentPageServices.delete();
    }
    
    public void add(){
        appointmentPageServices.add();
    }
    
    public void addFunction(){
        appointmentPageServices.addFunction(newAppointment);
    }
    
    public void cancelAdd(){
        appointmentPageServices.cancelAdd();
    }
    
    public boolean isButtonRender(){
        return appointmentPageServices.isButtonRender();
    }
    public boolean isEmptyTableRender(){
        return appointmentPageServices.isEmptyTableRender();
    }
    
    public void updateCheckedStateAtCheckbox(Appointments appointment){
        appointmentPageServices.updateCheckedStateAtCheckbox(appointment);
    }
}
