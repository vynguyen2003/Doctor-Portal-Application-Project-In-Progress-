/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package healthapplicationproject.healthapplicationproject;

import jakarta.annotation.PostConstruct;
import jakarta.ejb.Asynchronous;
import jakarta.ejb.EJB;
import jakarta.ejb.Schedule;
import jakarta.ejb.Stateless;
import jakarta.ejb.TransactionAttribute;
import jakarta.ejb.TransactionAttributeType;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.ws.rs.InternalServerErrorException;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.Response;
import java.sql.Time;
import static java.time.Instant.now;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TimeZone;

/**
 *
 * @author mohawk
 */
@Stateless
public class AppointmentsEJB {
   @PersistenceContext(unitName = "healthappJPA")
    private EntityManager em; 
   
   @EJB
   private AuthenticationEJB authenticationService;
   
   @EJB
   private HomePageEJB homePageServices;
   
   private AppointmentsBean appointmentsBeanServices;
   
   private List<Appointments> appointments;
   
   private List<Appointments> updateAppointmentList;
   
   private boolean updateAppointmentListRender;
   
   private boolean addButtonClicked; 
   
   private boolean emptyTableRender;
   
   private String errorMessage;
   
   private boolean isSorted;
   
   private List<Appointments> originalAppointment;
   
   public AppointmentsEJB(){
       appointments = new ArrayList<Appointments>();
       updateAppointmentListRender = false;
       updateAppointmentList = new ArrayList<Appointments>();
       addButtonClicked = false;
       emptyTableRender = false;
       errorMessage = "";
       appointmentsBeanServices = new AppointmentsBean();
       isSorted = false;
       originalAppointment = new ArrayList<Appointments>();
   }
   
   @PostConstruct
    public void init()
    {
       appointments.clear();
       
       try {
          
          updateAppointmentStatuses();
          
          TypedQuery<Appointments> allAppointments = em.createQuery(
                "SELECT a FROM Appointments a WHERE a.doctorId.email = :email", 
                Appointments.class);
          allAppointments.setParameter("email",authenticationService.getLoggedInUser());
          appointments = allAppointments.getResultList();
    
          
          emptyTableRender = appointments.isEmpty();
         
        }
        catch (NoResultException nre) {
            System.out.println("Init function Exception: " + nre);
        }  
       
       
    }
    
    //getter, setter
    
    public void setSorted(boolean isSorted){
        this.isSorted = isSorted;
    }
    
    public List<Appointments> getAllAppointments(){
        return appointments;
    }
    public void setAllAppointments(List<Appointments> appointments){
        this.appointments = appointments;
    }
    public boolean isUpdateAppointmentListRender(){
        return updateAppointmentListRender;
    }
   
    public List<Appointments> getUpdateAppointmentList(){
        return updateAppointmentList;
    }
    
    public boolean isAddButtonClicked(){
        return addButtonClicked;
    }
   
    public boolean isEmptyTableRender(){
        return emptyTableRender;
    }
    
    public String getErrorMessage(){
        return errorMessage;
    }
    
    public List<Appointments> getOriginalAppointment(){
        return originalAppointment;
    }
    
    //This method is for sorting appointments based on date and 
    public List<Appointments> sortAppointment(List<Appointments> appointmentsToBeSorted){
        Collections.sort(appointmentsToBeSorted, (a1, a2) -> {
           int dateComparison = a1.getAppointmentDate().compareTo(a2.getAppointmentDate());
           if (dateComparison == 0) {
              int timeComparison = a1.getAppointmentTime().compareTo(a2.getAppointmentTime());
              if (timeComparison == 0) {
                 return a1.getAppointmentId().compareTo(a2.getAppointmentId());
              }
              return timeComparison;
           }
           return dateComparison;
        });
        return appointmentsToBeSorted;

        
    }
    
    
    //NOTE: modifying the database function need to call through a EJB class.function, here EJB class. it does not work
    //This method is to be called in homePageEJB init() to update the appointment status.
    public void updateAppointmentStatuses() {
    LocalDate today = LocalDate.now();
    LocalDate oneWeekFromNow = today.plusWeeks(1);

    Date startDate = Date.from(today.atStartOfDay(ZoneId.systemDefault()).toInstant());
    Date endDate = Date.from(oneWeekFromNow.atStartOfDay(ZoneId.systemDefault()).toInstant());

    // Update Future to Upcoming for appointments within the next 7 days
    em.createQuery("UPDATE Appointments a SET a.status = 'Upcoming' WHERE a.appointmentDate BETWEEN :startDate AND :endDate")
        .setParameter("startDate", startDate)
        .setParameter("endDate", endDate)
        .executeUpdate();
    
    em.createQuery("UPDATE Appointments a SET a.status = 'Future' WHERE a.appointmentDate > :endDate")
        .setParameter("endDate", endDate)
        .executeUpdate();
    
    em.createQuery("UPDATE Appointments a SET a.status = 'Completed' WHERE (a.appointmentDate < :today OR (a.appointmentDate = :today AND a.appointmentTime < :now))")
       .setParameter("today", startDate)
       .setParameter("now", Time.valueOf(LocalTime.now()))
       .executeUpdate();


    }
   
    //This method is for shifting the appointment date 1 day so that when saved in database it is the same as the choosen appointment date.
    public Date shift1DayForDatabaseToBeTheSame(Date date) {
   
       Calendar cal = Calendar.getInstance();    
       cal.setTime(date);

        // Set the time to midnight (start of the day)
       cal.set(Calendar.HOUR_OF_DAY, 0);
       cal.set(Calendar.MINUTE, 0);
       cal.set(Calendar.SECOND, 0);
       cal.set(Calendar.MILLISECOND, 0);

       // Add 1 day to correct the date 
       cal.add(Calendar.DAY_OF_MONTH, 1);

    
       return cal.getTime();
    }

    
    public void update(){
        for (Appointments appointment : appointments){
            if (appointment.getIsChecked()){
                //commit begins from here
                Appointments retrieveAppointment = em.find(Appointments.class,appointment.getAppointmentId());
                if (retrieveAppointment != null){
                    //here explicitly set to True in the database
                    retrieveAppointment.setIsChecked(true);
                   
                    //update the change
                    em.merge(retrieveAppointment);  
                    
                }
            }
        }
        //select all checked item and put in a list
        updateAppointmentList = em.createQuery("SELECT t FROM Appointments t WHERE t.isChecked = TRUE", Appointments.class).getResultList();
        
        if (updateAppointmentList != null || !updateAppointmentList.isEmpty()){
            updateAppointmentListRender = true;
        }
      
    }
    
     public void save(){
        for (Appointments appointment : updateAppointmentList){
           
               Appointments retrieveAppointment = em.find(Appointments.class,appointment.getAppointmentId());
               if (retrieveAppointment != null){
                //update in the database
                retrieveAppointment.setAppointmentDate(shift1DayForDatabaseToBeTheSame(appointment.getAppointmentDate())); 
                retrieveAppointment.setAppointmentTime(appointment.getAppointmentTime());
                retrieveAppointment.setReasonToVisit(appointment.getReasonToVisit());
                retrieveAppointment.setIsChecked(false);
                
                em.merge(retrieveAppointment);  
               
                    
             }
             
        }
        
        em.flush(); //Synchronizes changes in the persistence context with the database
        em.clear(); //fetch fresh data

        init();
        
        //if sort or unsort make it persisted when perform crud as well
        if (isSorted){
            appointments = sortAppointment(appointments);
           
        }
          
        homePageServices.init();
        
        updateAppointmentListRender = false;
      
    }
     
    public void cancelUpdate(){
       updateAppointmentListRender = false;
       for (Appointments appointment : updateAppointmentList){
           
            Appointments retrieveAppointment = em.find(Appointments.class,appointment.getAppointmentId());
            if (retrieveAppointment != null){ 
                retrieveAppointment.setIsChecked(false);
                em.merge(retrieveAppointment);  
                    
            }    
         
        }
       init();
       if (isSorted){
            appointments = sortAppointment(appointments);
           
        }
    }
    
    public void delete(){
        for (Appointments appointment : appointments){
            if(appointment.getIsChecked()){
                Appointments retrieveAppointment = em.find(Appointments.class, appointment.getAppointmentId());
                if (retrieveAppointment != null){
                   em.remove(retrieveAppointment);
                }
            }
            
        }
        
        init();
        if (isSorted){
            appointments = sortAppointment(appointments);
           
        }
               
        //reload upcoming appointments in homepage
        homePageServices.loadUpcomingAppointments();
    }

    public void add(){
       addButtonClicked = true; 
    }
    
    public void addFunction(Appointments newAppointment){
      try {
        
        TypedQuery<Patients> patientQuery = em.createQuery(
            "SELECT p FROM Patients p WHERE p.doctorId.email = :doctorEmail AND p.firstname = :firstname AND p.lastname = :lastname AND p.email = :email",
            Patients.class
        );
        patientQuery.setParameter("doctorEmail", authenticationService.getLoggedInUser());
        patientQuery.setParameter("firstname", newAppointment.getPatientId().getFirstname());
        patientQuery.setParameter("lastname", newAppointment.getPatientId().getLastname());
        patientQuery.setParameter("email", newAppointment.getPatientId().getEmail());

        Patients patient = patientQuery.getSingleResult();

        // If a patient is found, set the patient ID and save the appointment
        if (patient != null) {
            newAppointment.setPatientId(patient); 
            newAppointment.setDoctorId(authenticationService.getLoggedInDoctor());
            newAppointment.setIsChecked(false);
            
            newAppointment.setAppointmentDate(shift1DayForDatabaseToBeTheSame(newAppointment.getAppointmentDate()));
            
            // Determine the current date and time
            ZoneId torontoZone = ZoneId.of("America/Toronto");
            
            LocalDate currentDate = LocalDate.now(torontoZone);
            LocalTime currentTime = LocalTime.now(torontoZone);
            
            LocalDate oneWeekFromNow = currentDate.plusWeeks(1);

            LocalDate appointmentDate = newAppointment.getAppointmentDate().toInstant().atZone(torontoZone).toLocalDate();
            LocalTime appointmentTime = newAppointment.getAppointmentTime().toInstant().atZone(torontoZone).toLocalTime();

            // Determine the status based on date and time
            if (appointmentDate.isAfter(oneWeekFromNow)) {
               newAppointment.setStatus("Future");
            }else if (appointmentDate.isAfter(currentDate) && appointmentDate.isBefore(oneWeekFromNow) || appointmentDate.isEqual(currentDate) && appointmentTime.isAfter(currentTime)) {
               newAppointment.setStatus("Upcoming");
            }else {
               newAppointment.setStatus("Completed");
            }
        
            em.persist(newAppointment);

            // Refresh the appointments list
            em.flush();
            em.clear();
            
            init();
            if (isSorted){
               appointments = sortAppointment(appointments);
           
            }
            
            homePageServices.init();

            addButtonClicked = false;
        }
      } catch(Exception e){
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error: Patient Information may be incorrect. Please try again!", "Failed to add appointment. Please try again."));
      }
    }
    
    public void cancelAdd(){
       addButtonClicked = false;
    }
    
    //Use Jakarta Rest API 
    public void search(String firstname, String doctorEmail){
        errorMessage = "";
        
        Client client = ClientBuilder.newClient();
        WebTarget target = client.target("http://localhost:8080/HealthApplicationProject/rest/restservices");
        
        
        if (firstname == null) {
          
            appointments = target.path(doctorEmail).request().get(new GenericType<List<Appointments>>() {});
           
            
        }else {
           List<Appointments> result = target.path(doctorEmail).queryParam("firstname", firstname).request().get(new GenericType<List<Appointments>>() {});
           if (result == null || result.isEmpty()){
               errorMessage = "No appointments found for '" + firstname + "'";
           }
           else {
               appointments = result;
           }
        }
        
        originalAppointment = new ArrayList<Appointments>(appointments);
        
        if (isSorted && appointments != null) {
           appointments = sortAppointment(appointments);
        }
  
    }
    
       
    public boolean isButtonRender(){
        for (Appointments appointment : appointments){
            if (appointment.getIsChecked()){
                return true;
            }
        }
        return false;
    }
    
    
   public void updateCheckedStateAtCheckbox(Appointments appointment) {
    Appointments retrievedAppointment = em.find(Appointments.class, appointment.getAppointmentId());
    if (retrievedAppointment != null) {
        retrievedAppointment.setIsChecked(appointment.getIsChecked());
        em.merge(retrievedAppointment);
    }
}

}
