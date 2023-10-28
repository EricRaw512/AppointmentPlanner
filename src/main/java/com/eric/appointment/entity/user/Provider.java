package com.eric.appointment.entity.user;

import java.util.List;

import com.eric.appointment.entity.Appointment;
import com.eric.appointment.entity.Work;
import com.eric.appointment.entity.WorkingPlan;
import com.eric.appointment.model.UserForm;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@Entity
@Table(name = "providers")
@PrimaryKeyJoinColumn(name = "id_provider")
public class Provider extends User{

    @OneToMany(mappedBy = "provider")
    private List<Appointment> appointments;

    @ManyToMany
    @JoinTable(
        name = "work_providers", 
        joinColumns = @JoinColumn(name = "id_user"), 
        inverseJoinColumns = @JoinColumn(name = "id_work")
    )
    private List<Work> works;

    @OneToOne(mappedBy = "provider", cascade = CascadeType.ALL)
    private WorkingPlan workingPlan;
    
    public Provider(UserForm userFormDTO, String encryptedPassword, Role role, WorkingPlan workingPlan) {
        super(userFormDTO, encryptedPassword, role);
        this.workingPlan = workingPlan;
        this.workingPlan.setProvider(this);
        this.works = userFormDTO.getWorks();
    }

    @Override
    public void update(UserForm userform) {
        super.update(userform);
        this.setWorks(userform.getWorks());
    }
}
