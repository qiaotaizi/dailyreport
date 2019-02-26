package com.jaiz.dailyreport.models;

/**
 * jira返回的用户信息
 */
public class JiraUserVO {

    private Integer id;

    private String name;

    private String coloe;

    private String source;

    private Boolean changable;

    private Boolean viewable;

    private Boolean visible;

    private Boolean favorite;

    private Boolean hasError;

    private Integer usersCount;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getColoe() {
        return coloe;
    }

    public void setColoe(String coloe) {
        this.coloe = coloe;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public Boolean getChangable() {
        return changable;
    }

    public void setChangable(Boolean changable) {
        this.changable = changable;
    }

    public Boolean getViewable() {
        return viewable;
    }

    public void setViewable(Boolean viewable) {
        this.viewable = viewable;
    }

    public Boolean getVisible() {
        return visible;
    }

    public void setVisible(Boolean visible) {
        this.visible = visible;
    }

    public Boolean getFavorite() {
        return favorite;
    }

    public void setFavorite(Boolean favorite) {
        this.favorite = favorite;
    }

    public Boolean getHasError() {
        return hasError;
    }

    public void setHasError(Boolean hasError) {
        this.hasError = hasError;
    }

    public Integer getUsersCount() {
        return usersCount;
    }

    public void setUsersCount(Integer usersCount) {
        this.usersCount = usersCount;
    }
}
