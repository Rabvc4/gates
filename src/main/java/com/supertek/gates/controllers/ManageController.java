package com.supertek.gates.controllers;

import com.supertek.gates.models.Role;
import com.supertek.gates.models.data.EmployeeDao;
import com.supertek.gates.models.data.RoleDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("manage")
public class ManageController {

    @Autowired
    EmployeeDao employeeDao;

    @Autowired
    RoleDao roleDao;

    @RequestMapping(value = "")
    public String index(Model model) {
        model.addAttribute("title", "Manager Section");

        return "manage/index";
    }

    @RequestMapping(value = "role")
    public String role(Model model, @RequestParam(defaultValue = "0") int id) {
        if (id != 0) {
            Role role = roleDao.findById(id).orElse(null);
            if (role != null) {
                model.addAttribute("title", role.getName() + " Position");
                model.addAttribute("role", role);

                return "manage/details";
            } else {
                model.addAttribute("title", "Role Not Found");
                model.addAttribute("message", "No role found with that Id");

                return "error";
            }
        }
        model.addAttribute("title", "All Roles");
        model.addAttribute("roles", roleDao.findAll());

        return "manage/roles";
    }

    @RequestMapping(value = "add-role", method = RequestMethod.GET)
    public String addEmployee(Model model) {

        model.addAttribute(new Role());
        model.addAttribute("title", "Create New Role");

        return "manage/add-role";
    }

    @RequestMapping(value = "add-role", method = RequestMethod.POST)
    public String processAddEmployee(Model model, @ModelAttribute @Valid Role role, Errors errors) {

        if (errors.hasErrors()) {
            model.addAttribute("title", "Create New Role");

            return "manage/add-role";
        }

        roleDao.save(role);

        return "redirect:/manage/role/?id=" + role.getId();
    }
}
