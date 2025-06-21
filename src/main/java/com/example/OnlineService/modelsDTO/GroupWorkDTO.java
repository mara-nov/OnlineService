package com.example.OnlineService.modelsDTO;

import com.example.OnlineService.models.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class GroupWorkDTO {

    private Long id;
    private String name;
    private CriteriaDTO criteria;
    private TasksDTO tasks;
    private ResultTableDTO resultTable;
    private List<WorkDTO> works;
    private String conditionsPath;

    public GroupWorkDTO(GroupWork group) {
        this.id = group.getId();
        this.name = group.getName();

        if (group.getCriteria() != null) {
            this.criteria = new CriteriaDTO(group.getCriteria());
        }

        if (group.getTasks() != null) {
            this.tasks = new TasksDTO(group.getTasks());
        }

        if (group.getWorks() != null) {
            this.works = group.getWorks().stream()
                    .map(WorkDTO::new)
                    .collect(Collectors.toList());
        } else {
            this.works = new ArrayList<>();
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CriteriaDTO getCriteria() {
        return criteria;
    }

    public void setCriteria(CriteriaDTO criteria) {
        this.criteria = criteria;
    }

    public TasksDTO getTasks() {
        return tasks;
    }

    public void setTasks(TasksDTO tasks) {
        this.tasks = tasks;
    }

    public ResultTableDTO getResultTable() {
        return resultTable;
    }

    public void setResultTable(ResultTableDTO resultTable) {
        this.resultTable = resultTable;
    }

    public List<WorkDTO> getWorks() {
        return works;
    }

    public void setWorks(List<WorkDTO> works) {
        this.works = works;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getConditionsPath() {
        return conditionsPath;
    }

    public void setConditionsPath(String conditionsPath) {
        this.conditionsPath = conditionsPath;
    }
}
