package com.supertek.gates.controllers;

import com.supertek.gates.models.Department;
import com.supertek.gates.models.Role;
import com.supertek.gates.models.data.DepartmentDao;
import com.supertek.gates.models.data.EmployeeDao;
import com.supertek.gates.models.data.RoleDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;

@Controller
@RequestMapping("department")
public class DepartmentController {

    @Autowired
    DepartmentDao departmentDao;

    @RequestMapping(value = "")
    public String department(Model model, @RequestParam(defaultValue = "0") int id) {
        if (id != 0) {
            Department department = departmentDao.findById(id).orElse(null);
            if (department != null) {
                model.addAttribute("title", department.getName());
                model.addAttribute("department", department);
                model.addAttribute("employees", department.getMembers());

                return "department/details";
            } else {
                model.addAttribute("title", "Department Not Found");
                model.addAttribute("message", "No department found with that Id");

                return "error";
            }
        }
        model.addAttribute("title", "All Departments");
        model.addAttribute("departments", departmentDao.findAll());

        return "department/index";
    }

    @RequestMapping(value = "add", method = RequestMethod.GET)
    public String addDepartment(Model model) {

        model.addAttribute(new Department());
        model.addAttribute("title", "Create New Department");

        return "department/add";
    }

    @RequestMapping(value = "add", method = RequestMethod.POST)
    public String processAddEmployee(Model model, @ModelAttribute @Valid Department department, Errors errors) {

        if (errors.hasErrors()) {
            model.addAttribute("title", "Create New Department");

            return "department/add";
        }

        departmentDao.save(department);

        return "redirect:/department/?id=" + department.getId();
    }
}
