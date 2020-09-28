package web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import web.dao.UserService;
import web.dao.UserServiceImpl;
import web.model.Role;
import web.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/")
public class UserController {

    @Autowired
    private UserServiceImpl userService;

    @GetMapping("/user")
    public String showUser(Model model) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        model.addAttribute("user", user);
        return "user";
    }

    @GetMapping("/admin")
    public ModelAndView home() {
        List<User> listUser = userService.listAll();
        ModelAndView mav = new ModelAndView();
        mav.setViewName("admin");
        mav.addObject("listUser", listUser);
        return mav;
    }

    @PostMapping("/new")
    public ModelAndView newCustomerForm(@ModelAttribute("user") User user) {
        ModelAndView mav = new ModelAndView("redirect:/admin");
        userService.saveUser(user);
        return mav;
    }

    @GetMapping("/new")
    public ModelAndView newCustomerForm() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = (User) userService.loadUserByUsername(username);
        Set <Role> role = user.getRoles();
        ModelAndView mav = new ModelAndView("new_user");
        mav.addObject("user", user);
        mav.addObject("role", role);
        return mav;
    }

    @GetMapping("/edit/{id}")
    public ModelAndView editUserForm(@PathVariable long id) {
        ModelAndView mav = new ModelAndView("edit_user");
        User user = userService.findUserById(id);
        Set<Role> role = user.getRoles();
        mav.addObject("user", user);
        mav.addObject("role", role);
        return mav;
    }

    @PostMapping("/edit")
    public ModelAndView editUserForm(@ModelAttribute("user") User user) {
        ModelAndView mav = new ModelAndView("redirect:/admin");
        userService.saveUser(user);
        return mav;
    }

    @GetMapping("/delete/{id}")
    public String deleteUserForm(@PathVariable long id) {
        userService.deleteUser(id);
        return "redirect:/admin";
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public String saveUser(@ModelAttribute("user") User user) {
        userService.saveUser(user);
        return "redirect:/admin";
    }
}