package com.veelsplusfueldealerapp.models;

public class DashboardModel {
    String menuTitle;
    String menuitemFirst;

    public DashboardModel(String menuTitle, String menuitemFirst, String menuitemSecond, String menuitemThird, String menuitemFourth) {
        this.menuTitle = menuTitle;
        this.menuitemFirst = menuitemFirst;
        this.menuitemSecond = menuitemSecond;
        this.menuitemThird = menuitemThird;
        this.menuitemFourth = menuitemFourth;
    }

    public DashboardModel(String menuTitle, String menuitemFirst, String menuitemSecond, String menuitemThird, String menuitemFourth, boolean expanded) {
        this.menuTitle = menuTitle;
        this.menuitemFirst = menuitemFirst;
        this.menuitemSecond = menuitemSecond;
        this.menuitemThird = menuitemThird;
        this.menuitemFourth = menuitemFourth;
        this.expanded = expanded;
    }

    public String getMenuTitle() {
        return menuTitle;
    }

    public void setMenuTitle(String menuTitle) {
        this.menuTitle = menuTitle;
    }

    public String getMenuitemFirst() {
        return menuitemFirst;
    }

    public void setMenuitemFirst(String menuitemFirst) {
        this.menuitemFirst = menuitemFirst;
    }

    public String getMenuitemSecond() {
        return menuitemSecond;
    }

    public void setMenuitemSecond(String menuitemSecond) {
        this.menuitemSecond = menuitemSecond;
    }

    public String getMenuitemThird() {
        return menuitemThird;
    }

    public void setMenuitemThird(String menuitemThird) {
        this.menuitemThird = menuitemThird;
    }

    public String getMenuitemFourth() {
        return menuitemFourth;
    }

    public void setMenuitemFourth(String menuitemFourth) {
        this.menuitemFourth = menuitemFourth;
    }

    public boolean isExpanded() {
        return expanded;
    }

    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }

    String menuitemSecond;
    String menuitemThird;
    String menuitemFourth;
    private boolean expanded;

}
