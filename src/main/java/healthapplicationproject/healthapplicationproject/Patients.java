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
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.util.Collection;
import java.util.Date;

/**
 *
 * @author mohawk
 */
@Entity
@Table(name = "PATIENTS")
@NamedQueries({
    @NamedQuery(name = "Patients.findAll", query = "SELECT p FROM Patients p"),
    @NamedQuery(name = "Patients.findByPatientId", query = "SELECT p FROM Patients p WHERE p.patientId = :patientId"),
    @NamedQuery(name = "Patients.findByFirstname", query = "SELECT p FROM Patients p WHERE p.firstname = :firstname"),
    @NamedQuery(name = "Patients.findByLastname", query = "SELECT p FROM Patients p WHERE p.lastname = :lastname"),
    @NamedQuery(name = "Patients.findByDateOfBirth", query = "SELECT p FROM Patients p WHERE p.dateOfBirth = :dateOfBirth"),
    @NamedQuery(name = "Patients.findByEmail", query = "SELECT p FROM Patients p WHERE p.email = :email"),
    @NamedQuery(name = "Patients.findByContactNumber", query = "SELECT p FROM Patients p WHERE p.contactNumber = :contactNumber"),
    @NamedQuery(name = "Patients.findByMedicalHistory", query = "SELECT p FROM Patients p WHERE p.medicalHistory = :medicalHistory")})
public class Patients implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "PATIENT_ID")
    private Integer patientId;
    
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "FIRST_NAME")
    private String firstname;

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "LAST_NAME")
    private String lastname;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "DATE_OF_BIRTH")
    @Temporal(TemporalType.DATE)
    private Date dateOfBirth;
    // @Pattern(regexp="[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?", message="Invalid email")//if the field contains email address consider using this annotation to enforce field validation
    @Size(max = 100)
    @Column(name = "EMAIL")
    private String email;
    @Size(max = 15)
    @Column(name = "CONTACT_NUMBER")
    private String contactNumber;
    @Size(max = 1000)
    @Column(name = "MEDICAL_HISTORY")
    private String medicalHistory;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "patientId")
    @JsonbTransient
    private Collection<Appointments> appointmentsCollection;
    @JoinColumn(name = "DOCTOR_ID", referencedColumnName = "DOCTOR_ID")
    @JsonbTransient
    @ManyToOne
    private Doctors doctorId;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "IS_CHECKED")
    private Boolean isChecked;
    
    @Lob
    @Column(name = "FACE_IMAGE")
    private byte[] faceImage;

    public Patients() {
    }

    public Patients(Integer patientId) {
        this.patientId = patientId;
    }

    public Patients(Integer patientId, String firstname, String lastname, Date dateOfBirth) {
        this.patientId = patientId;
        this.firstname = firstname;
        this.lastname = lastname;
        this.dateOfBirth = dateOfBirth;
    }

    public Integer getPatientId() {
        return patientId;
    }

    public void setPatientId(Integer patientId) {
        this.patientId = patientId;
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
    
    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getMedicalHistory() {
        return medicalHistory;
    }

    public void setMedicalHistory(String medicalHistory) {
        this.medicalHistory = medicalHistory;
    }

    public Collection<Appointments> getAppointmentsCollection() {
        return appointmentsCollection;
    }

    public void setAppointmentsCollection(Collection<Appointments> appointmentsCollection) {
        this.appointmentsCollection = appointmentsCollection;
    }

    public Doctors getDoctorId() {
        return doctorId;
    }
    
    public boolean getIsChecked() {
        return isChecked;
    }

    public void setIsChecked(boolean isChecked) {
        this.isChecked = isChecked;
    }

    
    public byte[] getFaceImage() {
       return faceImage;
    }

    public void setFaceImage(byte[] faceImage) {
       this.faceImage = faceImage;
    }

    public void setDoctorId(Doctors doctorId) {
        this.doctorId = doctorId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (patientId != null ? patientId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Patients)) {
            return false;
        }
        Patients other = (Patients) object;
        if ((this.patientId == null && other.patientId != null) || (this.patientId != null && !this.patientId.equals(other.patientId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "healthapplicationproject.healthapplicationproject.Patients[ patientId=" + patientId + " ]";
    }
    
}
