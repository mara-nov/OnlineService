package com.example.OnlineService.controllers;

import com.example.OnlineService.models.*;
import com.example.OnlineService.modelsDTO.CheckResultDTO;
import com.example.OnlineService.modelsDTO.GroupWorkDTO;
import com.example.OnlineService.repositories.*;
import com.example.OnlineService.services.FileService;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
public class ProjectPageController {

    private final GroupWorkRepository groupWorkRepository;
    private final WorkRepository workRepository;
    private final ProjectRepository projectRepository;
    private final ResultTableRepository resultTableRepository;
    private final FileService fileService;

    public ProjectPageController(
            GroupWorkRepository groupWorkRepository,
            WorkRepository workRepository,
            ProjectRepository projectRepository,
            ResultTableRepository resultTableRepository,
            FileService fileService
    ) {
        this.groupWorkRepository = groupWorkRepository;
        this.workRepository = workRepository;
        this.projectRepository = projectRepository;
        this.resultTableRepository = resultTableRepository;
        this.fileService = fileService;
    }

    @GetMapping("/project")
    public String showProjectPage(@RequestParam("identifier") Long projectIdentifier,
                                  @RequestParam(value = "workId", required = false) Long workId, Model model) {

        Project project = projectRepository.findByIdentifier(projectIdentifier);
        if (project == null) {
            return "redirect:/start-page?error=Проект не найден";
        }

        List<GroupWork> groupWorks = project.getGroupWorks();

        List<ResultTable> resultTables = new ArrayList<>();
        for (GroupWork  groupWork : groupWorks) {
            resultTables.add(groupWork.getResultTable());
        }

        model.addAttribute("project", project);
        model.addAttribute("groupWorks", groupWorks);
        model.addAttribute("resultTables", resultTables);

        Optional<Work> selectedWork = null;
        String conditionsPath = null;
        String criteriaPath = null;

        if (workId != null) {
            selectedWork = workRepository.findById(workId);
            selectedWork.ifPresent(work -> model.addAttribute("selectedWork", work));

            if (selectedWork.isPresent()) {
                Long groupId = selectedWork.get().getGroupId();
                if (groupId != null) {
                    Optional<GroupWork> group = groupWorkRepository.findById(groupId);
                    model.addAttribute("groupForSelectedWork", group);
                    if (group.isPresent()) {
                        Tasks tasks = group.get().getTasks();
                        Criteria criteria = group.get().getCriteria();
                        if (tasks != null) {
                            conditionsPath = tasks.getPdfPath();
                        } else {
                            model.addAttribute("errorMessage", "У данной группы не загружены условия.");
                        }
                        if (criteria != null) {
                            criteriaPath = criteria.getPdfPath();
                        } else {
                            model.addAttribute("errorMessage", "У данной группы не загружены критерии.");
                        }
                    }
                }
            }
        }

        model.addAttribute("conditionsPath", conditionsPath);
        model.addAttribute("criteriaPath", criteriaPath);

        return "project";
    }

    @GetMapping("/project/results")
    public String showResultsPage(Model model) {
        return "result-table"; // имя HTML-шаблона, например: results.html
    }

    @PostMapping("/project/{groupId}/work")
    @ResponseBody
    public GroupWorkDTO addWorkToExistingGroup(@PathVariable("groupId") Long groupId,
                                       @RequestParam("file") MultipartFile file,
                                       @RequestParam("name") String name) throws IOException {

        GroupWork group = groupWorkRepository.findById(groupId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Group not found"));

        String fileName = fileService.saveFile(file);

        Work work = new Work();
        work.setName(name);
        work.setPdfPath(fileName);
        work.setGroupId(groupId);
        group.getWorks().add(work);
        groupWorkRepository.save(group);
        return new GroupWorkDTO(group);
    }

    @PostMapping("/project/save-check")
    @ResponseBody
    public ResponseEntity<String> saveCheckTable(@RequestBody List<CheckResultDTO> checkResults,
                                                 @RequestParam("workId") Long workId) {
        Work work = workRepository.findById(workId).orElseThrow();

        for (CheckResultDTO dto : checkResults) {
            CheckResult result = new CheckResult();
            result.setCheckerName(dto.getCheckerName());
            result.setScores(dto.getScores());
            result.setWorkId(workId);
            work.getCheckResults().add(result);
        }

        workRepository.save(work);
        return ResponseEntity.ok("Сохранено");
    }

    @GetMapping("/project/check-results")
    @ResponseBody
    public List<CheckResultDTO> getCheckResults(@RequestParam("workId") Long workId) {
        Work work = workRepository.findById(workId).orElseThrow();
        return work.getCheckResults().stream().map(result -> {
            CheckResultDTO dto = new CheckResultDTO();
            dto.setCheckerName(result.getCheckerName());
            dto.setScores(result.getScores());
            return dto;
        }).collect(Collectors.toList());
    }


    @GetMapping("/pdf/{filename:.+}")
    public ResponseEntity<Resource> getPdf(@PathVariable String filename) {
        try {
            Path filePath = Paths.get("uploads").resolve(filename).normalize();
            Resource resource = new UrlResource(filePath.toUri());

            if (!resource.exists()) {
                return ResponseEntity.notFound().build();
            }

            String encodedFilename = URLEncoder.encode(resource.getFilename(), StandardCharsets.UTF_8)
                    .replaceAll("\\+", "%20");

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                            "inline; filename*=UTF-8''" + encodedFilename)
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(resource);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}

