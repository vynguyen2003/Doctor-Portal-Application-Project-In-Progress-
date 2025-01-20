/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package healthapplicationproject.healthapplicationproject;

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
public class HomePageBean implements Serializable {
    
    @EJB
    private AuthenticationEJB authenticationServices;
    @EJB
    private HomePageEJB homePageServices;
    
    private String todoItem;
    
    public List<Appointments> getAppointments(){
        return homePageServices.getAppointments();
    }
    
    public String getLoggedInUser(){
        return authenticationServices.getLoggedInUser();
    }
    
    public Doctors getLoggedInDoctor(){
        return authenticationServices.getLoggedInDoctor();
    }
    
    public List<Todo> getTodoList(){
        return homePageServices.getTodoList();
    }
    
    public List<Todo> getUpdateTodoList(){
        return homePageServices.getUpdateTodoList();
    }
    public boolean isUpdateListRender(){
        return homePageServices.isUpdateListRender();
    }
   
    public boolean isButtonRender(){
        return homePageServices.isButtonRender();
    }
    public boolean isEmptyTableRender(){
        return homePageServices.isEmptyTableRender();
    }
    public boolean isEmptyTodoListRender(){
        return homePageServices.isEmptyTodoListRender();
    }
    
    // this getter,setter toDoItem is for user input
    public String getTodoItem(){
        return todoItem;
    }
    public void setTodoItem(String todoItem){
        this.todoItem = todoItem;
    }
    
    public void update(){
        homePageServices.update();
    }
    public void save(){
        homePageServices.save();
    }
    
    public void delete(){
        homePageServices.delete();
    }
    
    public boolean isAddButtonClicked(){
        return homePageServices.isAddButtonClicked();
    }
    public void add(){
        homePageServices.add();
    }
    public void addFunction(){
        homePageServices.addFunction(todoItem);
        todoItem = "";
    }
    public void cancelUpdate(){
        homePageServices.cancelUpdate();
    }
    public void cancelAdd(){
        homePageServices.cancelAdd();
    }
    
    public String logout(){
        return homePageServices.logout();
    }
    public void updateCheckedStateAtCheckbox(Todo item){
        homePageServices.updateCheckedStateAtCheckbox(item);
    }
}
