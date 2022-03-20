package com.proxym.yacine.codintest.model;

import lombok.Data;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;

@Entity
@Table(name = "super_admins")
@Data
@SuperBuilder
public class SuperAdmin extends AppUser{
    public SuperAdmin() {

    }
}
