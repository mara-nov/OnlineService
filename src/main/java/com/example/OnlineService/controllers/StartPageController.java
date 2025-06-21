package com.example.OnlineService.controllers;

import com.example.OnlineService.models.Project;
import com.example.OnlineService.repositories.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Random;

@Controller
@RequestMapping("/start-page")
public class StartPageController {
    @Autowired
    private ProjectRepository projectRepository;

    @GetMapping
    public String showStartPage(@RequestParam(required = false) String mode,
                                @RequestParam(required = false) Long identifier,
                                @RequestParam(required = false) String error,  // <--- добавлено
                                Model model) {
        model.addAttribute("mode", mode);
        if (identifier != null) {
            model.addAttribute("identifier", identifier);
        }
        if (error != null) { // <--- добавлено
            model.addAttribute("error", error);
        }
        return "start-page";
    }

    @PostMapping("/create-project")
    public String createProject(@RequestParam("name") String name) {
        long identifier = generateUniqueIdentifier();

        Project project = new Project();
        project.setName(name);
        project.setIdentifier(identifier);
        projectRepository.save(project);

        return "redirect:/start-page?identifier=" + identifier;
    }

    private boolean identifierExists(long identifier) {
        return projectRepository.findAll().stream()
                .anyMatch(p -> p.getIdentifier() == identifier);
    }

    private long generateUniqueIdentifier() {
        Random random = new Random();
        long identifier;
        do {
            identifier = 100000 + random.nextInt(900000);
        } while (identifierExists(identifier));
        return identifier;
    }

    @PostMapping("/enter-project")
    public String enterProject(@RequestParam("projectId") String projectIdStr,
                               RedirectAttributes redirectAttributes) {
        try {
            long projectId = Long.parseLong(projectIdStr);
            Project project = projectRepository.findByIdentifier(projectId);
            if (project == null) {
                redirectAttributes.addFlashAttribute("error", "Неверный идентификатор");
                return "redirect:/start-page?mode=enter";
            }
            return "redirect:/project?identifier=" + project.getIdentifier();
        } catch (NumberFormatException e) {
            redirectAttributes.addFlashAttribute("error", "Некорректный формат идентификатора");
            return "redirect:/start-page?mode=enter";
        }
    }
}
