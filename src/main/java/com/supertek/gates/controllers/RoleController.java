package com.supertek.gates.controllers;

import com.supertek.gates.models.Department;
import com.supertek.gates.models.Employee;
import com.supertek.gates.models.Role;
import com.supertek.gates.models.data.DepartmentDao;
import com.supertek.gates.models.data.EmployeeDao;
import com.supertek.gates.models.data.RoleDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("role")
public class RoleController {

    @Autowired
    RoleDao roleDao;

    @RequestMapping(value = "")
    public String index(Model model, @RequestParam(defaultValue = "0") int id) {
        if (id != 0) {
            Role role = roleDao.findById(id).orElse(null);
            if (role != null) {
                model.addAttribute("title", role.getName());
                model.addAttribute("role", role);

                return "role/details";
            } else {
                model.addAttribute("title", "Role Not found");
                model.addAttribute("message", "No role found with that Id");

                return "error";
            }
        }
        model.addAttribute("title", "All Roles");
        model.addAttribute("roles", roleDao.findAll());

        return "role/index";
    }

    @RequestMapping(value = "add", method = RequestMethod.GET)
    public String addRole(Model model) {

        model.addAttribute(new Role());
        model.addAttribute("title", "New Role");

        return "role/add";
    }

    @RequestMapping(value = "add", method = RequestMethod.POST)
    public String processAddRole(Model model, @ModelAttribute @Valid Role role, Errors errors) {

        if (errors.hasErrors()) {
            model.addAttribute("roles", roleDao.findAll());
            model.addAttribute("title", "New Role");

            return "role/add";
        }

        roleDao.save(role);

        return "redirect:/role/?id=" + role.getId();
    }
}
