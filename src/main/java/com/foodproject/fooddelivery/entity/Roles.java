package com.foodproject.fooddelivery.entity;

import jakarta.persistence.*;

import java.util.Date;
import java.util.Set;

@Entity(name="roles")
public class Roles {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name="role_name")
    private String roleName;

    @OneToMany(mappedBy = "roles")
    private Set<Users> listUser;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }


    public Set<Users> getListUser() {
        return listUser;
    }

    public void setListUser(Set<Users> listUser) {
        this.listUser = listUser;
    }
}
