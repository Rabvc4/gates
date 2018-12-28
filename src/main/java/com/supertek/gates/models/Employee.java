package com.supertek.gates.models;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    @Column(name = "id", updatable = false, nullable = false)
    private int id;

    @NotNull(message = "Please enter a first name")
    @Size(min = 2, message = "That name is too short, names must be at least 2 characters")
    @Size(max = 50, message = "That name is too long, names cannot exceed 50 characters")
    private String firstName;

    @NotNull(message = "Please enter a last name")
    @Size(min = 2, message = "That name is too short, names must be at least 2 characters")
    @Size(max = 50, message = "That name is too long, names cannot exceed 50 characters")
    private String lastName;

    @NotNull(message = "All employees require a role")
    @ManyToOne
    @JoinColumn(name="role_id", nullable=false)
    private Role role;

    @ManyToOne
    @JoinColumn(name="employee_id")
    private Employee manager;

    @OneToMany(mappedBy="manager")
    private List<Employee> subordinates = new ArrayList<>();

    private Integer allocation;

    @ManyToOne
    @JoinColumn(name="department_id")
    private Department department;

    public Employee() {
    }

    public Employee(String firstName, String lastName, Role role) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.role = role;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return firstName + " " + lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        if (this.manager != null) {
            this.manager.allocation -= this.allocation;
        }
        this.role = role;
        if (manager != null) {
            this.manager.allocation -= this.allocation;
        }
    }

    public Employee getManager() {
        return manager;
    }

    public void setManager(Employee manager) {
        if (this.manager != null) {
            this.manager.allocation -= this.allocation;
        }
        this.manager = manager;
        if (manager != null) {
            this.manager.allocation -= this.allocation;
        }
    }

    public List<Employee> getSubordinates() {
        return subordinates;
    }

    public void setSubordinates(List<Employee> subordinates) {
        this.subordinates = subordinates;
    }

    public Integer getAllocation() {
        return allocation + role.getEarnings();
    }

    public void setAllocation(Integer allocation) {
        this.allocation = allocation;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        Integer newAllocation = 0;
        if (this.department != null) {
            newAllocation = this.department.getAllocation();
            this.department.setAllocation(newAllocation - role.getEarnings());
        }
        this.department = department;
        newAllocation = this.department.getAllocation();
        this.department.setAllocation(newAllocation - role.getEarnings());
    }
}
