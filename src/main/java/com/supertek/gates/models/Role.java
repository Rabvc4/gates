package com.supertek.gates.models;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Role  {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    @Column(name = "id", updatable = false, nullable = false)
    private int id;

    @NotNull(message = "Please enter a first name")
    @Size(min = 2, message = "That name is too short, names must be at least 2 characters")
    @Size(max = 50, message = "That name is too long, names cannot exceed 50 characters")
    private String name;

    @NotNull
    @Min(value = 1, message = "Employees need to earn at least $1 for tax reasons")
    @Max(value = 4000, message = "That's more than twice what Developers earn, contact them if this is correct.")
    private Integer earnings;

    @OneToMany(mappedBy="role")
    private List<Employee> employees = new ArrayList<>();

    public Role() {
    }

    public Role(String name, Integer earnings) {
        this.name = name;
        this.earnings = earnings;
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

    public Integer getEarnings() {
        return earnings;
    }

    public void setEarnings(Integer earnings) {
        this.earnings = earnings;
    }

    public List<Employee> getEmployees() {
        return employees;
    }

    public void addEmployee(Employee employee) {
        this.employees.add(employee);
    }

    public void addEmployees(List<Employee> employees) {
        this.employees.addAll(employees);
    }

    public void setEmployees(List<Employee> employees) {
        this.employees = employees;
    }
}
