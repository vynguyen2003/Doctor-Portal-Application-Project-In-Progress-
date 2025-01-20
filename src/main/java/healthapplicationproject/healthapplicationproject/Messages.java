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
import jakarta.persistence.Lob;
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
@Table(name = "MESSAGES")
@NamedQueries({
    @NamedQuery(name = "Messages.findAll", query = "SELECT m FROM Messages m"),
    @NamedQuery(name = "Messages.findByMessageId", query = "SELECT m FROM Messages m WHERE m.messageId = :messageId"),
    @NamedQuery(name = "Messages.findBySubject", query = "SELECT m FROM Messages m WHERE m.subject = :subject"),
    @NamedQuery(name = "Messages.findBySentDate", query = "SELECT m FROM Messages m WHERE m.sentDate = :sentDate")})
public class Messages implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "MESSAGE_ID")
    private Integer messageId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "SUBJECT")
    private String subject;
    @Basic(optional = false)
    @NotNull
    @Lob
    @Column(name = "MESSAGE_BODY")
    private String messageBody;
    @Column(name = "SENT_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date sentDate;
    @JoinColumn(name = "RECEIVER_ID", referencedColumnName = "DOCTOR_ID")
    @ManyToOne(optional = false)
    private Doctors receiverId;
    @JoinColumn(name = "SENDER_ID", referencedColumnName = "DOCTOR_ID")
    @ManyToOne(optional = false)
    private Doctors senderId;

    public Messages() {
    }

    public Messages(Integer messageId) {
        this.messageId = messageId;
    }

    public Messages(Integer messageId, String subject, String messageBody) {
        this.messageId = messageId;
        this.subject = subject;
        this.messageBody = messageBody;
    }

    public Integer getMessageId() {
        return messageId;
    }

    public void setMessageId(Integer messageId) {
        this.messageId = messageId;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getMessageBody() {
        return messageBody;
    }

    public void setMessageBody(String messageBody) {
        this.messageBody = messageBody;
    }

    public Date getSentDate() {
        return sentDate;
    }

    public void setSentDate(Date sentDate) {
        this.sentDate = sentDate;
    }

    public Doctors getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(Doctors receiverId) {
        this.receiverId = receiverId;
    }

    public Doctors getSenderId() {
        return senderId;
    }

    public void setSenderId(Doctors senderId) {
        this.senderId = senderId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (messageId != null ? messageId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Messages)) {
            return false;
        }
        Messages other = (Messages) object;
        if ((this.messageId == null && other.messageId != null) || (this.messageId != null && !this.messageId.equals(other.messageId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "healthapplicationproject.healthapplicationproject.Messages[ messageId=" + messageId + " ]";
    }
    
}
