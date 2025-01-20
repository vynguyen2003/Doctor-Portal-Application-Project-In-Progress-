/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package healthapplicationproject.healthapplicationproject;

import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

/**
 *
 * @author mohawk
 */
@Stateless
public class HomePageEJB {
    @PersistenceContext(unitName = "healthappJPA")
    private EntityManager em;
    
    @EJB
    private AuthenticationEJB authenticationService;
    
    private List<Appointments> appointments;
    
    private boolean updateListRender;  //boolean for the update todo list appear or not
    
    private List<Todo> todoList;
    
    private List<Todo> updateTodoList;  //hold checked todo items if click update button
    
    private boolean buttonRender;  //boolean for update,delete to appear
    
    private boolean emptyTableRender;  //boolean for when table of appointment is empty (example: new doctor has no appointment yet
    
    private boolean emptyTodoListRender;  //boolean for when todo list is empty
   
    private boolean addButtonClicked;  
    
    @EJB
    private AppointmentsEJB appointmentEJB;
    
    public HomePageEJB(){
        appointments = new ArrayList<Appointments>();
        todoList = new ArrayList<Todo>();
        updateTodoList = new ArrayList<Todo>();
        updateListRender = false;
        emptyTableRender = false;
        emptyTodoListRender = false;
        addButtonClicked = false;
    }
    
    //this function will run only once after constructor finish executing 
    @PostConstruct
    public void init()
    {
       appointments.clear();
       todoList.clear();
       try {
         
          appointmentEJB.updateAppointmentStatuses();
          
          loadUpcomingAppointments();
          
          loadTodoList();
          
        }
        catch (NoResultException nre) {
            System.out.println("Init function Exception: " + nre);
        }     
       
   
    }
    
    
    public void loadUpcomingAppointments() {
    // Fetch appointments with status 'Upcoming' for the logged-in doctor
       TypedQuery<Appointments> query = em.createQuery(
        "SELECT a FROM Appointments a WHERE a.doctorId.email = :email AND a.status = 'Upcoming'",
        Appointments.class );
       query.setParameter("email", authenticationService.getLoggedInUser());

       appointments = query.getResultList();
       emptyTableRender = appointments.isEmpty();
    }

    
    
    //method for fetching todo list with for a specific doctor
    public void loadTodoList(){
        TypedQuery<Todo> toDoListQuery = em.createQuery(
                "SELECT a FROM Todo a WHERE a.doctor.email = :email", 
                Todo.class);
          toDoListQuery.setParameter("email",authenticationService.getLoggedInUser());
          todoList = toDoListQuery.getResultList();
          emptyTodoListRender = todoList.isEmpty();
    }
    
    public List<Appointments> getAppointments(){
        return appointments;
    }
    
    public List<Todo> getTodoList(){
        return todoList;
    }
    
    public List<Todo> getUpdateTodoList(){
        return updateTodoList;
    }
    
    public boolean isUpdateListRender(){
        return updateListRender;
    }
    
    public boolean isEmptyTableRender(){
        return emptyTableRender;
    }
   
    public boolean isEmptyTodoListRender(){
        return emptyTodoListRender;
    }
    
    public boolean isAnyTodoGetChecked(){
        for (Todo item : todoList){
            if (item.getIsChecked()){
                return true;
            }
        }
        
        return false;
    }
    
    //if any todo item get checked render out the option buttons
    public boolean isButtonRender(){
        buttonRender = isAnyTodoGetChecked();
        System.out.println("bollean: " + buttonRender);
        return buttonRender;
    }
    
    
    public boolean isAddButtonClicked(){
        return addButtonClicked;
    }
    
    //method when update button is clicked
    public void update(){
        for (Todo item : todoList){
            if (item.getIsChecked()){
                //commit begins from here
                Todo retrieveToDoItem = em.find(Todo.class,item.getId());
                if (retrieveToDoItem != null){
                    //here explicitly set to True in the database
                    retrieveToDoItem.setIsChecked(true);
                    //update the change
                    em.merge(retrieveToDoItem);  
                    
                }
            }
        }
        //select all checked item and put in a list
        updateTodoList = em.createQuery("SELECT t FROM Todo t WHERE t.isChecked = TRUE", Todo.class).getResultList();
        
        if (updateTodoList != null || !updateTodoList.isEmpty()){
            updateListRender = true;
        }
        
    }
    
    //method when done updating and click save
    public void save(){
        for (Todo item : updateTodoList){
           
            Todo retrieveToDoItem = em.find(Todo.class,item.getId());
            if (retrieveToDoItem != null){
                //update in the database
                retrieveToDoItem.setTask(item.getTask()); //getTask from user input
                retrieveToDoItem.setIsChecked(false);
                em.merge(retrieveToDoItem);  
                    
            }    
         
        }
        //call init again to fetch the data from database to update the change in todo list
        init();
        
        updateListRender = false;
      
    }
    // method when delete button is clicked for one or many checked items
    public void delete(){
        for (Todo item : todoList){
            if(item.getIsChecked()){
                Todo retrieveTodoItem = em.find(Todo.class, item.getId());
                if (retrieveTodoItem != null){
                   em.remove(retrieveTodoItem);
                }
            }
            
        }
        init();
    }
   
   //method when add button is clicked
   public void add(){
       addButtonClicked = true; //if true render the add form 
       System.out.println("addbuttonclick: " + buttonRender);
       emptyTodoListRender = false;
   }
   
   //method when save button for add is clicked
   public void addFunction(String todoItem){
       Todo newTodoItem = new Todo();
       newTodoItem.setTask(todoItem);
       newTodoItem.setIsChecked(false);
       newTodoItem.setDoctor(authenticationService.getLoggedInDoctor());
       
       em.persist(newTodoItem);
       
       addButtonClicked = false;
       
       init();
   }
   
   //for cancel button, but also need to uncheck the item if cancel 
   public void cancelUpdate(){
       updateListRender = false;
       for (Todo item : updateTodoList){
           
            Todo retrieveToDoItem = em.find(Todo.class,item.getId());
            if (retrieveToDoItem != null){ 
                retrieveToDoItem.setIsChecked(false);
                em.merge(retrieveToDoItem);  
                    
            }    
         
        }
       init();
   }
   public void cancelAdd(){
       addButtonClicked = false;
   }
   
    public String logout()
    {
        authenticationService.logout();
        
        return "login?faces-redirect=true";
    }
   
   //update boolean for checkbox when user check or uncheck
   public void updateCheckedStateAtCheckbox(Todo item) {
    Todo retrievedItem = em.find(Todo.class, item.getId());
    if (retrievedItem != null) {
        
        retrievedItem.setIsChecked(item.getIsChecked());
        
        em.merge(retrievedItem);
    }
}

}
