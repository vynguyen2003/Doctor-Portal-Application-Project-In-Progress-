/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package healthapplicationproject.healthapplicationproject;

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
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.Serializable;

/**
 *
 * @author mohawk
 */
@Entity
@Table(name = "TODO")
@NamedQueries({
    @NamedQuery(name = "Todo.findAll", query = "SELECT t FROM Todo t"),
    @NamedQuery(name = "Todo.findById", query = "SELECT t FROM Todo t WHERE t.id = :id"),
    @NamedQuery(name = "Todo.findByTask", query = "SELECT t FROM Todo t WHERE t.task = :task"),
    @NamedQuery(name = "Todo.findByIsChecked", query = "SELECT t FROM Todo t WHERE t.isChecked = :isChecked")
})
    
    
public class Todo implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "TASK")
    private String task;
    @Basic(optional = false)
    @NotNull
    @Column(name = "IS_CHECKED")
    private Boolean isChecked;

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;

    
    @JoinColumn(name = "DOCTOR_ID", referencedColumnName = "DOCTOR_ID")
    @ManyToOne(optional = false)
    private Doctors doctor;
    

    public Todo() {
    }

    public Todo(Integer id) {
        this.id = id;
    }

    public Todo(Integer id, String task) {
        this.id = id;
        this.task = task;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    
    public boolean getIsChecked() {
        return isChecked;
    }

    public void setIsChecked(boolean isChecked) {
        this.isChecked = isChecked;
    }

    public Doctors getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctors doctor) {
        this.doctor = doctor;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Todo)) {
            return false;
        }
        Todo other = (Todo) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "healthapplicationproject.healthapplicationproject.Todo[ id=" + id + " ]";
    }

  
}
