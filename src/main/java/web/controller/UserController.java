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
import web.repository.RoleRepository;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/")
public class UserController {

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private RoleRepository roleRepository;
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
    public ModelAndView newCustomerForm (String password, String username) {
        ModelAndView mav = new ModelAndView("redirect:/admin");
        User user = new User();
        user.setPassword(password);
        user.setUsername(username);
        Set<Role> role = roleRepository.getRolesById(user.getId());
        user.setRoles(role);//поменять в зависимости от типа передаваемых значений
        userService.saveUser(user);
        return mav;
    }

    @GetMapping("/new")
    public ModelAndView newCustomerForm() {
        ModelAndView mav = new ModelAndView("new_user");

        User user = new User();
        mav.addObject("user", user);

        List<Role> roles = roleRepository.findAll();
        mav.addObject("allRoles", roles);

        return mav;
    }

    @GetMapping("/edit/{id}")
    public ModelAndView editUserForm(@PathVariable long id) {

        User user = userService.findUserById(id);
        ModelAndView mav = new ModelAndView("edit_user");
        mav.addObject("user", user);

        List<Role> roles = roleRepository.findAll();
        mav.addObject("allRoles", roles);
        return mav;
    }

    @PostMapping("/edit")
    public ModelAndView editUserForm(Long id,
                                     String password, String username) {
        //System.out.println(roles);
        ModelAndView mav = new ModelAndView("redirect:/admin");
        User user = userService.findUserById(id);
        user.setId(id);
        user.setPassword(password);
        user.setUsername(username);
        Set<Role> role = roleRepository.getRolesById(id);
        user.setRoles(role);//поменять в зависимости от типа передаваемых значений
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