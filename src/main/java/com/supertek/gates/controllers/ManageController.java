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
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("manage")
public class ManageController {

    @Autowired
    EmployeeDao employeeDao;

    @Autowired
    RoleDao roleDao;

    @Autowired
    DepartmentDao departmentDao;

    @RequestMapping(value = "")
    public String index(Model model) {
        model.addAttribute("title", "Manager Section");

        return "manage/index";
    }

    @RequestMapping(value = "manager/{employeeId}", method = RequestMethod.GET)
    public String manager(Model model, @PathVariable int employeeId) {

        Employee employee = employeeDao.findById(employeeId).orElse(null);
        if (employee != null) {
            model.addAttribute(new SearchForm());
            model.addAttribute("title","Set Manager: " + employee.getName());
            model.addAttribute("employee", employee);

            return "manage/manager";
        }
        model.addAttribute("title", "Employee Not Found");
        model.addAttribute("message", "Cannot find Employee with that Id");

        return "error";
    }

    @RequestMapping(value = "results/{employeeId}")
    public String search(Model model, @PathVariable int employeeId, @ModelAttribute @Valid SearchForm searchForm, Errors errors) {

        Employee employee = employeeDao.findById(employeeId).orElse(null);
        if (errors.hasErrors() || employee == null) {
            model.addAttribute("title","Set Manager: " + employee.getName());
            model.addAttribute("employee", employee);

            return "manage/manager";
        }

        String query = searchForm.getKeyword();
        model.addAttribute("employee", employee);

        if (searchForm.getSearchField().equals(SearchFieldType.NAME)) {
            List<Employee> employees = employeeDao.findByNameLike(query, employeeId);
            if (employees.contains(employee)) {
                employees.remove(employee);
            }
            model.addAttribute("managers", employees);
            model.addAttribute("title", "Managers with name like '" + query + "'");

            return "manage/manager";
        } else if (searchForm.getSearchField().equals(SearchFieldType.ID)) {
            List<Employee> employees = new ArrayList<>();
            employees.add(employeeDao.findById(Integer.parseInt(query)).orElse(null));
            model.addAttribute("managers", employees);
            model.addAttribute("title", "Managers with ID like '" + query + "'");

            return "manage/manager";
        } else {
            model.addAttribute("title", "Invalid Search Method");
            model.addAttribute("message", "Please contact the development team");

            return "error";
        }
    }

    @RequestMapping(value = "manager/{employeeId}", method = RequestMethod.POST)
    public String processManager(Model model, @PathVariable int employeeId, @RequestParam(defaultValue = "0") int id) {

        Employee employee = employeeDao.findById(employeeId).orElse(null);
        Employee newManager = employeeDao.findById(id).orElse(null);

        if (employee != null && newManager != null) {
            employee.setManager(newManager);

            employeeDao.save(employee);
            employeeDao.save(newManager);

            return "redirect:/employee/?id=" + employee.getId();
        }
        model.addAttribute("title", "Employee Not Found");
        model.addAttribute("message", "Cannot find Employee with that Id");

        return "error";
    }

    @RequestMapping(value = "edit/{employeeId}", method = RequestMethod.GET)
    public String editEmployee(Model model, @PathVariable int employeeId) {

        Employee employee = employeeDao.findById(employeeId).orElse(null);

        if (employee != null) {
            model.addAttribute(employee);
            model.addAttribute("roles", roleDao.findAll());
            model.addAttribute("departments", departmentDao.findAll());
            model.addAttribute("title", "Edit Employee");
            model.addAttribute("submit", "Save Changes");

            return "manage/edit";
        }
        model.addAttribute("title", "Employee Not Found");
        model.addAttribute("message", "Cannot find Employee with that Id");

        return "error";
    }

    @RequestMapping(value = "edit/{employeeId}", method = RequestMethod.POST)
    public String processEditEmployee(Model model, @PathVariable int employeeId, @ModelAttribute @Valid Employee editedEmployee, Errors errors) {

        Employee employee = employeeDao.findById(employeeId).orElse(null);

        if (errors.hasErrors() || editedEmployee == null) {

            model.addAttribute(editedEmployee);
            model.addAttribute("roles", roleDao.findAll());
            model.addAttribute("departments", departmentDao.findAll());
            model.addAttribute("title", "Edit Employee");
            model.addAttribute("submit", "Save Changes");

            return "manage/edit";
        }

        Role role = roleDao.findById(editedEmployee.getRole().getId()).orElse(null);
        Department department = departmentDao.findById(editedEmployee.getDepartment().getId()).orElse(null);

        if (role != null && department != null) {
            employee.setRole(role);
            employee.setDepartment(department);
            employeeDao.save(employee);
        } else {
            model.addAttribute("title", "Role or Department Not Found");
            model.addAttribute("message", "Cannot find that role or department");

            return "error";
        }

        return "redirect:/employee/?id=" + employee.getId();
    }

    @RequestMapping(value = "terminate/{employeeId}", method = RequestMethod.POST)
    public String terminateEmployee(Model model, @PathVariable int employeeId, @RequestParam String terminate) {

        if (!terminate.equals("Terminate")) {
            return "redirect:/employee/?id=" + employeeId;
        }

        Employee employee = employeeDao.findById(employeeId).orElse(null);

        if (employee != null) {
            employeeDao.delete(employee);
            model.addAttribute("title", "Success");
            model.addAttribute("message", "That individual has been terminated");

            return "success";
        }
        model.addAttribute("title", "Employee Not Found");
        model.addAttribute("message", "Cannot find Employee with that Id");

        return "error";
    }
}
