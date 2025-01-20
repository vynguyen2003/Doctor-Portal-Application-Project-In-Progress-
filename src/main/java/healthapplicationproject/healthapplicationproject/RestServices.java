/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package healthapplicationproject.healthapplicationproject;

import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.InternalServerErrorException;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Response;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author mohawk
 */
@Stateless
@Path("/restservices")
public class RestServices {
    @PersistenceContext(unitName = "healthappJPA")
    private EntityManager em;
    
    //private List<Appointments> appointments;
    @EJB
    private AppointmentsEJB appointmentEJB;
    
    @EJB
    private AuthenticationEJB authenticationService;
    
    
    public RestServices(){
        //appointments = new ArrayList<Appointments>();
    }
    
    //this method for testing if Rest Api is working
    @GET
    @Produces("text/html")
    public String getHTMLMessage()
    {
        return "<p>Hello from a <strong>simple</string> REST service!";
    }
    
    @GET
    @Produces("application/json")
    @Path("/{doctoremail}")
    public List<Appointments> retrieveAppointments(@QueryParam("firstname") String firstname, @PathParam("doctoremail") String doctoremail){
        try{
            
                TypedQuery<Appointments> query;
                
                if (firstname.isEmpty()){ // isEmpty over null for empty string ""
                    query = em.createQuery("SELECT a FROM Appointments a WHERE a.doctorId.email = :doctoremail", Appointments.class);
                    query.setParameter("doctoremail",doctoremail);
                      
                }
                else {
                    query = em.createQuery("SELECT a FROM Appointments a WHERE LOWER(a.patientId.firstname) = LOWER(:firstname) AND a.doctorId.email = :doctoremail", Appointments.class);
                    query.setParameter("firstname", firstname);
                    query.setParameter("doctoremail",doctoremail); //since Jarkarta Rest API is outside of Enterprise Bean, so can not use EJB class directly in rest class
         
                
                }
                return query.getResultList();
            
            
        }
        
        catch(Exception e){
           
            System.err.println("Error in REST endpoint: " + e.getMessage());
            return null;
        
        }
        
        
    }
}
