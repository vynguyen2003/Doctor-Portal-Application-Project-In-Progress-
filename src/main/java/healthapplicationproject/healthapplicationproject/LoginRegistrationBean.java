/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package healthapplicationproject.healthapplicationproject;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import jakarta.ejb.EJB;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.component.UIComponent;
import jakarta.faces.component.UIInput;
import jakarta.faces.context.FacesContext;
import jakarta.faces.validator.ValidatorException;
import jakarta.inject.Named;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author mohawk
 */
@Named 
@SessionScoped
public class LoginRegistrationBean implements Serializable {
  @PersistenceContext(unitName = "healthappJPA")
  private EntityManager em;
  
  @Resource(name="HealthApplicationJDBC")
  private jakarta.transaction.UserTransaction utx; 
  
  /*login attributes*/
  @EJB
  private AuthenticationEJB authenticate;
  private String username;
  private String password;
  private String result;
  private boolean renderResult;
  
  /*registration attribute*/  
  private Doctors doctor;
  private String reTypeRegisterPassword;
  @EJB
  private DoctorsEJB doctorService;        
  
  
  List<Doctors> doctors;
  
  public LoginRegistrationBean(){
      doctors = new ArrayList<Doctors>();
      doctor = new Doctors();
      renderResult = false;
  }
  
  @PostConstruct
  public void init()
  {
    doctors.clear();
    try {
      TypedQuery<Doctors> query = em.createQuery("SELECT a FROM Doctors a", Doctors.class);
      doctors = query.getResultList(); // getResultList() is a member function of TypedQuery
    }
    catch (NoResultException nre) {
    }      
  }
  
  /* login get, set*/
  public String getUsername() { return username;}
  public void setUsername(String username) {this.username = username;}
  
  public String getPassword() {return password;}
  public void setPassword(String password) {this.password = password;}
  
  public String getResult() { return result;}
  
  public boolean getRenderResult() {return renderResult;}
  
  /* registration get, set */
  public String getReTypeRegisterPassword() {return reTypeRegisterPassword;}
  public void setReTypeRegisterPassword(String reTypeRegisterPassword) {this.reTypeRegisterPassword = reTypeRegisterPassword;}
  
  
  public Doctors getDoctor(){return doctor;}
  public void setDoctor(Doctors doctor){this.doctor = doctor;}
 
  public List<Doctors> getDoctors()
  {
      return doctors;
  }
  
  /*Login*/
  public String login(){
      if (authenticate.login(username, password)){
          this.result = "";
          return "home?faces-redirect=true";
      }
      else{
          this.result = "Invalid Username or Password!";
          renderResult = true;
          return "login";
      }
  }
  
  public void validatePasswordLogin(FacesContext context, UIComponent component, Object value) throws ValidatorException {
    String password = value.toString();

    String passwordPattern = "^(?=.*[A-Z])(?=.*[@$!%*?&amp;])[A-Za-z\\d@$!%*?&amp;]{8,}$";

    if (!password.matches(passwordPattern)) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, 
                "Invalid Password", 
                "Invalid Password");
        throw new ValidatorException(message);
    }
    renderResult = false;
  }
 
  /*Register*/
  public void validatePasswordRegister(FacesContext context, UIComponent component, Object value) throws ValidatorException {
      
      String retypePass = value.toString();
      
      System.out.println("registerPassword: " + doctor.getPassword()); //here password gets set called / updated immediately after filling in the field to compare (the only way to retrieve property value while validation of all field do not pass all. Because normally, it waits for all validations in the form to pass then set method get invoked to update value for properies later.
      System.out.println("retypePass: " + retypePass);
      
      if (!retypePass.equals(doctor.getPassword())){
          FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, 
                "Passwords do not match", 
                "Passwords do not match!");
           throw new ValidatorException(message);
      }
     
  }
  public String register(){
      doctorService.createNewUser(doctor);
      return "successRegisterMessage?faces-redirect=true";
  }
  
  
  /*Navigate to Login Page and Register Page*/
  public String goToLoginPage(){
      return "login?faces-redirect=true";
  }
  
  public String goToRegisterPage(){
      return "registration?faces-redirect=true";
  }
}
