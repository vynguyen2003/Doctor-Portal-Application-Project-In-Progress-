/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package healthapplicationproject.healthapplicationproject;

import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import jakarta.faces.context.FacesContext;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

/**
 *
 * @author mohawk
 */
//by using ejb, it will automatically handle transaction for us
@Stateless
public class AuthenticationEJB {
    @PersistenceContext(unitName = "healthappJPA")
    private EntityManager em;
    
    @EJB
    private HomePageEJB homepageEJB;
    
    @EJB 
    private AppointmentsEJB appointmentsEJB;
    
    public HttpSession getSession(){
      FacesContext context = FacesContext.getCurrentInstance();
      HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
      return request.getSession(false); //null or HttpSession object
    }
    
    public boolean login(String username, String password){
       HttpSession session = getSession();
     
       Query userQuery = em.createQuery("SELECT COUNT(u) FROM Doctors u WHERE u.email = :email AND u.password = :password")
                       .setParameter("email", username).setParameter("password", password);
     
       long count = (long) userQuery.getSingleResult();     
       
   
       System.out.println("Count: " + count);
     
       if (count > 0)
       {
        session.setAttribute("authenticated", Boolean.TRUE);     
        session.setAttribute("username", username);  
        /*here if there is a valid account retrieve that doctor information as well*/
        Doctors doctor = em.createQuery(
                "SELECT d FROM Doctors d WHERE d.email = :email AND d.password = :password", 
                Doctors.class)
                .setParameter("email", username)
                .setParameter("password", password)
                .getSingleResult();
        session.setAttribute("doctor", doctor);
        
        homepageEJB.init();
        appointmentsEJB.init();
        
        return true;
       }
       else 
       {
        session.setAttribute("authenticated", Boolean.FALSE);               
        return false;
       }
       
      
    }
    
    public String getLoggedInUser()
    {
      return (String) getSession().getAttribute("username");
    }
    
    public Doctors getLoggedInDoctor(){
        return (Doctors) getSession().getAttribute("doctor");
    }
    
    public void logout()
    {
      HttpSession session = getSession();
    
      if (session != null) {
        session.invalidate(); // Invalidate the session, clear all session attributes
      }    
    }
  
}   
