package com.example.OnlineService.models;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class GroupWork {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    String name;
    @OneToOne(cascade = CascadeType.ALL)
    private Criteria criteria;

    @OneToOne(cascade = CascadeType.ALL)
    private Tasks tasks;

    @OneToOne(cascade = CascadeType.ALL)
    private ResultTable resultTable;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Work> works;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Criteria getCriteria() {
        return criteria;
    }

    public void setCriteria(Criteria criteria) {
        this.criteria = criteria;
    }

    public Tasks getTasks() {
        return tasks;
    }

    public void setTasks(Tasks tasks) {
        this.tasks = tasks;
    }

    public ResultTable getResultTable() {
        return resultTable;
    }

    public void setResultTable(ResultTable resultTable) {
        this.resultTable = resultTable;
    }

    public List<Work> getWorks() {
        return works;
    }

    public void setWorks(List<Work> works) {
        this.works = works;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
