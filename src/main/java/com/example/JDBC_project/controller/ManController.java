package com.example.JDBC_project.controller;

import com.example.JDBC_project.model.Man;
import com.example.JDBC_project.service.ManService;
import com.example.JDBC_project.util.ManValidator;
import jakarta.validation.Valid;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/people")
public class ManController {

    private static final Logger logger = LogManager.getLogger(ManController.class);

    private final ManService manService;
    private final ManValidator manValidator;

    @Autowired
    public ManController(ManService manService, ManValidator manValidator) {
        this.manService = manService;
        this.manValidator = manValidator;
    }

    @GetMapping()
    public String getPeoplePage(Model model) {
        model.addAttribute("people", manService.getAllPeople());
        return "people/mans";
    }

    @GetMapping("/new")
    public String getCreateManPage(@ModelAttribute("man") Man man) {
        return "people/new_man";
    }

    @PostMapping("/new")
    public String createMan(@ModelAttribute("man") @Valid Man man, BindingResult bindingResult) {
        manValidator.validate(man, bindingResult);
        if (bindingResult.hasErrors()) {
            logger.info("Введи невалидные данные при попытке создать человека");
            return "people/new_man";
        }
        manService.saveMan(man);
        logger.info("Человек был успешно создан");
        return "redirect:/people";
    }

    @GetMapping("/{id}/edit")
    public String getEditPage(Model model, @ModelAttribute("man") Man man, @PathVariable("id") int id) {
        model.addAttribute("man", manService.findById(id));
        return "people/edit";
    }

    @PostMapping("/{id}/edit")
    public String editMan(@PathVariable("id") int id, @ModelAttribute("man") @Valid Man man, BindingResult bindingResult) {
        manValidator.validate(man, bindingResult);
        if (bindingResult.hasErrors()) {
            logger.info("Ввели некорректные данные ри попытке изменить человека с id: {}", id);
            return "people/edit";
        }
        manService.updateMan(man, id);
        logger.info("Человек с id: {} был успешно обновлен", id);
        return "redirect:/people";
    }

    @GetMapping("/{id}")
    public String getManPage(@PathVariable("id") int id, Model model) {
        model.addAttribute("man", manService.findById(id));
        model.addAttribute("books", manService.getBooksFromMan(id));
        return "people/man";
    }

    @PostMapping("/{id}/delete")
    public String deleteMan(@PathVariable("id") int id) {
        manService.deleteMan(id);
        logger.info("Человек с id: {} был удален", id);
        return "redirect:/people";
    }

}
