package com.eric.appointment.entity.user;

import java.util.List;
import java.util.Objects;

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
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "providers")
@PrimaryKeyJoinColumn(name = "id_provider")
public class Provider extends User{

    @OneToMany(mappedBy = "provider")
    private List<Appointment> appointments;

    @ManyToMany
    @JoinTable(
        name = "works_providers", 
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

    @Override
    public String toString() {
        return super.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Provider provider = (Provider) o;
        return provider.getId().equals(this.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(appointments, works, workingPlan);
    }
}
