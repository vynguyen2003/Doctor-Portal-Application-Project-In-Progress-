/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package healthapplicationproject.healthapplicationproject;

import jakarta.json.bind.annotation.JsonbTransient;
import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author mohawk
 */
@Entity
@Table(name = "APPOINTMENTS")
@NamedQueries({
    @NamedQuery(name = "Appointments.findAll", query = "SELECT a FROM Appointments a"),
    @NamedQuery(name = "Appointments.findByAppointmentId", query = "SELECT a FROM Appointments a WHERE a.appointmentId = :appointmentId"),
    @NamedQuery(name = "Appointments.findByAppointmentDate", query = "SELECT a FROM Appointments a WHERE a.appointmentDate = :appointmentDate"),
    @NamedQuery(name = "Appointments.findByAppointmentTime", query = "SELECT a FROM Appointments a WHERE a.appointmentTime = :appointmentTime"),
    @NamedQuery(name = "Appointments.findByStatus", query = "SELECT a FROM Appointments a WHERE a.status = :status"),
    @NamedQuery(name = "Appointments.findByIsChecked", query = "SELECT a FROM Appointments a WHERE a.isChecked = :isChecked")})

public class Appointments implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "APPOINTMENT_ID")
    private Integer appointmentId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "APPOINTMENT_DATE")
    @Temporal(TemporalType.DATE)
    private Date appointmentDate;
    @Basic(optional = false)
    @NotNull
    @Column(name = "APPOINTMENT_TIME")
    @Temporal(TemporalType.TIME)
    private Date appointmentTime;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "STATUS")
    private String status;
    @Basic(optional = false) // Indicates the field is mandatory
    @NotNull                 // Enforces validation
    @Size(max = 1000)         // Limits the length
    @Column(name = "REASON_TO_VISIT") // Ensures database integrity
    private String reasonToVisit;
    @JoinColumn(name = "DOCTOR_ID", referencedColumnName = "DOCTOR_ID")
    @ManyToOne(optional = false)
    private Doctors doctorId;
    @JoinColumn(name = "PATIENT_ID", referencedColumnName = "PATIENT_ID")
    @ManyToOne(optional = false)
    private Patients patientId;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "IS_CHECKED")
    private Boolean isChecked;

    

    public Appointments() {
    }

    public Appointments(Integer appointmentId) {
        this.appointmentId = appointmentId;
    }

    public Appointments(Integer appointmentId, Date appointmentDate, Date appointmentTime, String status, String reasonToVisit) {
        this.appointmentId = appointmentId;
        this.appointmentDate = appointmentDate;
        this.appointmentTime = appointmentTime;
        this.status = status;
        this.reasonToVisit = reasonToVisit;
    }

    public Integer getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(Integer appointmentId) {
        this.appointmentId = appointmentId;
    }

    public Date getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(Date appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    public Date getAppointmentTime() {
        return appointmentTime;
    }

    public void setAppointmentTime(Date appointmentTime) {
        this.appointmentTime = appointmentTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    
    public String getReasonToVisit() {
        return reasonToVisit;
    }

    public void setReasonToVisit(String reasonToVisit) {
        this.reasonToVisit = reasonToVisit;
    }
    
    public boolean getIsChecked() {
        return isChecked;
    }

    public void setIsChecked(boolean isChecked) {
        this.isChecked = isChecked;
    }

    public Doctors getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(Doctors doctorId) {
        this.doctorId = doctorId;
    }

    public Patients getPatientId() {
        return patientId;
    }

    public void setPatientId(Patients patientId) {
        this.patientId = patientId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (appointmentId != null ? appointmentId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Appointments)) {
            return false;
        }
        Appointments other = (Appointments) object;
        if ((this.appointmentId == null && other.appointmentId != null) || (this.appointmentId != null && !this.appointmentId.equals(other.appointmentId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "healthapplicationproject.healthapplicationproject.Appointments[ appointmentId=" + appointmentId + " ]";
    }
    
}
