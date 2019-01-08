package com.supertek.gates.controllers;

import com.supertek.gates.models.Department;
import com.supertek.gates.models.Employee;
import com.supertek.gates.models.Role;
import com.supertek.gates.models.data.DepartmentDao;
import com.supertek.gates.models.data.EmployeeDao;
import com.supertek.gates.models.data.RoleDao;
import com.supertek.gates.models.enumerations.SearchFieldType;
import com.supertek.gates.models.forms.SearchForm;
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

    @Autowired
    DepartmentDao departmentDao;

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
        model.addAttribute("title", "Employee Search");
        model.addAttribute(new SearchForm());

        return "search";
    }

    @RequestMapping(value = "add", method = RequestMethod.GET)
    public String addEmployee(Model model) {

        model.addAttribute(new Employee());
        model.addAttribute("roles", roleDao.findAll());
        model.addAttribute("departments", departmentDao.findAll());
        model.addAttribute("title", "New Employee");
        model.addAttribute("submit", "Add New Employee");

        return "employee/add";
    }

    @RequestMapping(value = "add", method = RequestMethod.POST)
    public String processAddEmployee(Model model, @ModelAttribute @Valid Employee employee, Errors errors) {

        if (errors.hasErrors()) {
            model.addAttribute("roles", roleDao.findAll());
            model.addAttribute("departments", departmentDao.findAll());
            model.addAttribute("title", "New Employee");
            model.addAttribute("submit", "Add New Employee");

            return "employee/add";
        }

        String cleanString = employee.getFirstName();
        employee.setFirstName(cleanString.replaceAll("\\s+",""));
        cleanString = employee.getLastName();
        employee.setLastName(cleanString.replaceAll("\\s+",""));

        employeeDao.save(employee);

        return "redirect:/employee/?id=" + employee.getId();
    }

    @RequestMapping(value = "search")
    public String search(Model model) {
        model.addAttribute("title", "Employee Search");
        model.addAttribute(new SearchForm());

        return "search";
    }

    @RequestMapping(value = "results")
    public String search(Model model, @ModelAttribute @Valid SearchForm searchForm, Errors errors) {

        if (errors.hasErrors()) {
            model.addAttribute("title", "Employee Search");

            return "search";
        }

        String query = searchForm.getKeyword();

        if (searchForm.getSearchField().equals(SearchFieldType.NAME)) {
            List<Employee> employees = employeeDao.findByNameLike(query);
            model.addAttribute("employees", employees);
            model.addAttribute("title", "'" + query + "'" + " Employees");

            return "search";
        } else if (searchForm.getSearchField().equals(SearchFieldType.ID)) {
            Employee employee = employeeDao.findById(Integer.parseInt(query)).orElse(null);

            return "redirect:/employee/?id=" + employee.getId();
        } else {
            model.addAttribute("title", "Invalid Search Method");
            model.addAttribute("message", "Please contact the development team");

            return "error";
        }
    }
}
