/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package healthapplicationproject.healthapplicationproject;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

/**
 *
 * @author mohawk
 */
@Stateless
public class DoctorsEJB {
    @PersistenceContext(unitName="healthappJPA")
    private EntityManager em;
    
    public void createNewUser(Doctors doctor){
        em.persist(doctor);
    }
}
