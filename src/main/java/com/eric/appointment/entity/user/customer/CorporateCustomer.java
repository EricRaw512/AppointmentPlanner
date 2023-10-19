package com.eric.appointment.entity.user.customer;

import com.eric.appointment.entity.user.Role;
import com.eric.appointment.model.UserForm;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Table(name = "corporate_customer")
@Entity
@PrimaryKeyJoinColumn(name = "id_customer")
@Data
@EqualsAndHashCode(callSuper = true)
public class CorporateCustomer extends Customers{
    
    @Column(name = "company_name")
    private String companyName;

    @Column(name = "npwp")
    private String NPWP;

    public CorporateCustomer(UserForm userForm, String encryptedPassword, Role role) {
        super(userForm, encryptedPassword, role);
        this.companyName = userForm.getCompanyName();
        this.NPWP = userForm.getNPWP();
    }
}
