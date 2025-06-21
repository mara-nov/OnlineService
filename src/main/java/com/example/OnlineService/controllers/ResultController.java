package com.example.OnlineService.controllers;


import com.example.OnlineService.models.CheckResult;
import com.example.OnlineService.models.GroupWork;
import com.example.OnlineService.models.ResultTable;
import com.example.OnlineService.models.Work;
import com.example.OnlineService.modelsDTO.FinalResultDTO;
import com.example.OnlineService.repositories.GroupWorkRepository;
import com.example.OnlineService.repositories.ProjectRepository;
import com.example.OnlineService.repositories.ResultTableRepository;
import com.example.OnlineService.repositories.WorkRepository;
import com.example.OnlineService.services.FileService;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Controller
public class ResultController {

    private final GroupWorkRepository groupWorkRepository;
    private final WorkRepository workRepository;
    private final ProjectRepository projectRepository;
    private final ResultTableRepository resultTableRepository;
    private final FileService fileService;

    public ResultController(
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

    @GetMapping("/result-table")
    public String showResultTable(@RequestParam("resultTableId") Long resultTableId, Model model) {
        ResultTable resultTable = resultTableRepository.findById(resultTableId).orElseThrow();
        GroupWork groupWork = groupWorkRepository.findById(resultTable.getGroupWorkId()).orElseThrow();
        List<Work> works = workRepository.findByGroupId(groupWork.getId());

        List<FinalResultDTO> results = new ArrayList<>();
        for (Work work : works) {
            List<CheckResult> checkResults = work.getCheckResults();
            if (!checkResults.isEmpty()) {
                CheckResult last = checkResults.get(checkResults.size() - 1);
                results.add(new FinalResultDTO(work.getName(), last.getScores()));
            }
        }

        model.addAttribute("groupWork", groupWork);
        model.addAttribute("results", results);
        model.addAttribute("resultTableId", resultTableId);

        return "result-table";
    }

    @GetMapping("/result-table/download")
    public void downloadExcel(@RequestParam("resultTableId") Long resultTableId, HttpServletResponse response) throws IOException {
        ResultTable resultTable = resultTableRepository.findById(resultTableId).orElseThrow();
        GroupWork groupWork = groupWorkRepository.findById(resultTable.getGroupWorkId()).orElseThrow();
        List<Work> works = workRepository.findByGroupId(groupWork.getId());

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=final-table.xlsx");

        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("Итоговая таблица");

        XSSFRow header = sheet.createRow(0);
        header.createCell(0).setCellValue("ФИО");
        for (int i = 1; i <= 7; i++) {
            header.createCell(i).setCellValue(String.valueOf(i));
        }

        int rowNum = 1;
        for (Work work : works) {
            List<CheckResult> checkResults = work.getCheckResults();
            if (!checkResults.isEmpty()) {
                CheckResult last = checkResults.get(checkResults.size() - 1);
                XSSFRow row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(work.getName());
                List<Integer> scores = last.getScores();
                for (int i = 0; i < scores.size(); i++) {
                    row.createCell(i + 1).setCellValue(scores.get(i) != null ? scores.get(i) : 0);
                }
            }
        }

        workbook.write(response.getOutputStream());
        workbook.close();
    }


}

