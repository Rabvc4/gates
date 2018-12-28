package com.supertek.gates.controllers;

import com.supertek.gates.models.Employee;
import com.supertek.gates.models.Role;
import com.supertek.gates.models.data.EmployeeDao;
import com.supertek.gates.models.data.RoleDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("employee")
public class EmployeeController {

    @Autowired
    EmployeeDao employeeDao;

    @Autowired
    RoleDao roleDao;

    @RequestMapping(value = "")
    public String index(Model model, @RequestParam(defaultValue = "0") int id) {
        if (id != 0) {
            Employee employee = employeeDao.findById(id).orElse(null);
            if (employee != null) {
                model.addAttribute("title", employee.getName());
                model.addAttribute("employee", employee);

                return "employee/details";
            } else {
                model.addAttribute("title", "Employee Not found");
                model.addAttribute("message", "No employee found with that Id");

                return "error";
            }
        }
        model.addAttribute("title", "All Employees");
        model.addAttribute("employees", employeeDao.findAll());

        return "employee/index";
    }

    @RequestMapping(value = "add", method = RequestMethod.GET)
    public String addEmployee(Model model) {

        model.addAttribute(new Employee());
        model.addAttribute("roles", roleDao.findAll());
        model.addAttribute("title", "New Employee");

        return "employee/add";
    }

    @RequestMapping(value = "add", method = RequestMethod.POST)
    public String processAddEmployee(Model model, @ModelAttribute @Valid Employee employee, Errors errors) {

        if (errors.hasErrors()) {
            model.addAttribute("roles", roleDao.findAll());
            model.addAttribute("title", "New Employee");

            return "employee/add";
        }

        employeeDao.save(employee);

        return "redirect:/employee/?id=" + employee.getId();
    }

    @RequestMapping(value = "role", method = RequestMethod.GET)
    public String role(Model model, @RequestParam int id) {

        Role role = roleDao.findById(id).orElse(null);
        List<Employee> employees = role.getEmployees();
        model.addAttribute("employees", employees);
        model.addAttribute("title", role.getName() + " Employees");

        return "employee/index";
    }
}
