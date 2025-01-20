/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package healthapplicationproject.healthapplicationproject;

import jakarta.json.bind.annotation.JsonbTransient;
import jakarta.persistence.Basic;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.xml.bind.annotation.XmlTransient;
import java.io.Serializable;
import java.util.Collection;

/**
 *
 * @author mohawk
 */
@Entity
@Table(name = "DOCTORS")
@NamedQueries({
    @NamedQuery(name = "Doctors.findAll", query = "SELECT d FROM Doctors d"),
    @NamedQuery(name = "Doctors.findByDoctorId", query = "SELECT d FROM Doctors d WHERE d.doctorId = :doctorId"),
    @NamedQuery(name = "Doctors.findByFirstname", query = "SELECT d FROM Doctors d WHERE d.firstname = :firstname"),
    @NamedQuery(name = "Doctors.findByLastname", query = "SELECT d FROM Doctors d WHERE d.lastname = :lastname"),
    @NamedQuery(name = "Doctors.findByEmail", query = "SELECT d FROM Doctors d WHERE d.email = :email"),
    @NamedQuery(name = "Doctors.findByPassword", query = "SELECT d FROM Doctors d WHERE d.password = :password"),
    @NamedQuery(name = "Doctors.findBySpecialty", query = "SELECT d FROM Doctors d WHERE d.specialty = :specialty"),
    @NamedQuery(name = "Doctors.findByContactNumber", query = "SELECT d FROM Doctors d WHERE d.contactNumber = :contactNumber")})
public class Doctors implements Serializable {

    // @Pattern(regexp="[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?", message="Invalid email")//if the field contains email address consider using this annotation to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "EMAIL")
    private String email;
    @Basic(optional = false)
    @NotNull()
    @Size(min = 1, max = 100)
    @Column(name = "PASSWORD")
    private String password;
    
    @Size(max = 50)
    @Column(name = "SPECIALTY")
    private String specialty;

    @Size(max = 15)
    @Column(name = "CONTACT_NUMBER")
    private String contactNumber;
    // @Pattern(regexp="[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?", message="Invalid email")//if the field contains email address consider using this annotation to enforce field validation
    @Size(max = 100)
    @Column(name = "FIRST_NAME")
    private String firstname;
    @Size(max = 100)
    @Column(name = "LAST_NAME")
    private String lastname;
    
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "DOCTOR_ID")
    private Integer doctorId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "doctorId")
    @JsonbTransient //to prevent recursion mapping: ex: in appointments class maps doctor already, so doctor should exclude the appointment mapping in its class.
    private Collection<Appointments> appointmentsCollection;
    @OneToMany(mappedBy = "doctorId")
    @JsonbTransient
    private Collection<Patients> patientsCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "receiverId")
    @JsonbTransient
    private Collection<Messages> messagesCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "senderId")
    @JsonbTransient
    private Collection<Messages> messagesCollection1;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "doctor")
    @JsonbTransient
    private Collection<Todo> todoCollection;


    public Doctors() {
    }

    public Doctors(Integer doctorId) {
        this.doctorId = doctorId;
    }

    public Doctors(Integer doctorId, String firstname, String lastname, String email, String password, String specialty, String contactNumber) {
        this.doctorId = doctorId;
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.password = password;
        this.specialty = specialty;
        this.contactNumber = contactNumber;
    }

    public Integer getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(Integer doctorId) {
        this.doctorId = doctorId;
    }

    public String getFirstname(){
        return firstname;
    }
    
    public void setFirstname(String firstname){
        this.firstname = firstname;
    }
    
    public String getLastname(){
        return lastname;
    }
    
    public void setLastname(String lastname){
        this.lastname = lastname;
    }


    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public Collection<Appointments> getAppointmentsCollection() {
        return appointmentsCollection;
    }

    public void setAppointmentsCollection(Collection<Appointments> appointmentsCollection) {
        this.appointmentsCollection = appointmentsCollection;
    }

    public Collection<Patients> getPatientsCollection() {
        return patientsCollection;
    }

    public void setPatientsCollection(Collection<Patients> patientsCollection) {
        this.patientsCollection = patientsCollection;
    }

    public Collection<Messages> getMessagesCollection() {
        return messagesCollection;
    }

    public void setMessagesCollection(Collection<Messages> messagesCollection) {
        this.messagesCollection = messagesCollection;
    }

    public Collection<Messages> getMessagesCollection1() {
        return messagesCollection1;
    }

    public void setMessagesCollection1(Collection<Messages> messagesCollection1) {
        this.messagesCollection1 = messagesCollection1;
    }
    
    public Collection<Todo> getTodoCollection() {
        return todoCollection;
    }
    public void setTodoCollection(Collection<Todo> todoCollection) {
        this.todoCollection = todoCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (doctorId != null ? doctorId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Doctors)) {
            return false;
        }
        Doctors other = (Doctors) object;
        if ((this.doctorId == null && other.doctorId != null) || (this.doctorId != null && !this.doctorId.equals(other.doctorId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "healthapplicationproject.healthapplicationproject.Doctors[ doctorId=" + doctorId + " ]";
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSpecialty() {
        return specialty;
    }

    public void setSpecialty(String specialty) {
        this.specialty = specialty;
    }

    /*@OneToMany(cascade = CascadeType.ALL, mappedBy = "doctor")
    public Collection<Todo> getTodoCollection() {
        return todoCollection;
    }*/

    /*public void setTodoCollection(Collection<Todo> todoCollection) {
        this.todoCollection = todoCollection;
    }*/
    
}
