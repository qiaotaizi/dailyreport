package com.jaiz.dailyreport.models;

import java.util.List;

/**
 * 预处理工作内容
 *
 * @author graci
 */
public class PreDealContent {

    private List<String> achievement;

    private List<String> problem;

    private List<String> todo;

    public List<String> getAchievement() {
        return achievement;
    }

    public void setAchievement(List<String> achievement) {
        this.achievement = achievement;
    }

    public List<String> getProblem() {
        return problem;
    }

    public void setProblem(List<String> problem) {
        this.problem = problem;
    }

    public List<String> getTodo() {
        return todo;
    }

    public void setTodo(List<String> todo) {
        this.todo = todo;
    }


}
