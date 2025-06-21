package com.example.OnlineService.controllers;

import com.example.OnlineService.models.*;
import com.example.OnlineService.modelsDTO.GroupWorkDTO;
import com.example.OnlineService.repositories.*;
import com.example.OnlineService.services.FileService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


import java.io.IOException;

import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;


@Controller
@RequestMapping("/group")
@SessionAttributes({"uploadedTasks", "uploadedCriteria", "uploadedWorks"})
public class UploadGroupController {

    private final GroupWorkRepository groupWorkRepository;
    private final TasksRepository tasksRepository;
    private final CriteriaRepository criteriaRepository;
    private final WorkRepository workRepository;
    private final ProjectRepository projectRepository;
    private final FileService fileService;

    public UploadGroupController(
            GroupWorkRepository groupWorkRepository,
            TasksRepository tasksRepository,
            CriteriaRepository criteriaRepository,
            WorkRepository workRepository,
            ProjectRepository projectRepository,
            FileService fileService
    ) {
        this.groupWorkRepository = groupWorkRepository;
        this.tasksRepository = tasksRepository;
        this.criteriaRepository = criteriaRepository;
        this.workRepository = workRepository;
        this.projectRepository = projectRepository;
        this.fileService = fileService;
    }

    @ModelAttribute("uploadedWorks")
    public List<Work> uploadedWorks() {
        return new ArrayList<>();
    }


    @ModelAttribute("uploadedTasks")
    public Tasks uploadedTasks() {
        return new Tasks();
    }

    @ModelAttribute("uploadedCriteria")
    public Criteria uploadedCriteria() {
        return new Criteria();
    }


    @PostMapping("/upload/tasks")
    @ResponseBody
    public void uploadTasks(@RequestParam("file") MultipartFile file,
                                              Model model) throws IOException {
        String fileName = fileService.saveFile(file);
        Tasks tasks = new Tasks();
        tasks.setPdfPath(fileName);
        model.addAttribute("uploadedTasks", tasks);
    }


    @PostMapping("/upload/criteria")
    @ResponseBody
    public void uploadCriteria(@RequestParam("file") MultipartFile file,
                                                 Model model) throws IOException {
        String fileName = fileService.saveFile(file);
        Criteria criteria = new Criteria();
        criteria.setPdfPath(fileName);
        model.addAttribute("uploadedCriteria", criteria);
    }

    @PostMapping("/upload/work")
    @ResponseBody
    public void uploadWork(@RequestParam("file") MultipartFile file,
                                             @RequestParam("name") String name,
                                             @ModelAttribute("uploadedWorks") List<Work> uploadedWorks) throws IOException {
        String fileName = fileService.saveFile(file);
        Work work = new Work();
        work.setName(name);
        work.setPdfPath(fileName);
        uploadedWorks.add(work);
    }

    @PostMapping("/upload/save")
    @ResponseBody
    public GroupWorkDTO saveGroup(@RequestParam("identifier") Long projectIdentifier,
                                  @RequestParam("name") String name, @ModelAttribute("uploadedTasks") Tasks tasks,
                                  @ModelAttribute("uploadedCriteria") Criteria criteria,
                                  @ModelAttribute("uploadedWorks") List<Work> works,
                                  SessionStatus sessionStatus) {

        Project project = projectRepository.findByIdentifier(projectIdentifier);
        GroupWork group = new GroupWork();
        group.setName(name);
        group.setTasks(tasks);
        group.setCriteria(criteria);
        group.setWorks(new ArrayList<>());
        group = groupWorkRepository.save(group);

        Long groupId = group.getId();
        ResultTable resultTable = new ResultTable();
        resultTable.setGroupWorkId(groupId);
        group.setResultTable(resultTable);

        for (Work work : works) {
            work.setGroupId(groupId);
            group.getWorks().add(work);
        }

        project.addGroupWork(group);
        projectRepository.save(project);
        sessionStatus.setComplete();

        return new GroupWorkDTO(group);
    }
}
