package com.supertek.gates.models;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Department {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    @Column(name = "id", updatable = false, nullable = false)
    private int id;

    @Size(min = 2, message = "That name is too short, names must be at least 2 characters")
    @Size(max = 50, message = "That name is too long, names cannot exceed 50 characters")
    private String name;

    @Size(min = 10, message = "Please provide a short description of the new department")
    @Size(max = 500, message = "That description is too long, please use less than 500 characters")
    private String description;

    @OneToMany(mappedBy="department")
    private List<Employee> members = new ArrayList<>();

    public Department() {
    }

    public Department(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Employee> getMembers() {
        return members;
    }

    public void addMember(Employee employee) {
        this.members.add(employee);
    }

    public void addMembers(List<Employee> members) {
        this.members.addAll(members);
    }

    public void setMembers(List<Employee> members) {
        this.members = members;
    }

    public int getAllocation() {
        int allocation = 0;
        for (Employee employee :
                this.members) {
            allocation += employee.getRole().getEarnings();
        }
        return allocation;
    }
}
