package io.github.jiajun2001.community.community.entity;

public class Page {
    // Selected Page
    private int current = 1;

    // Limited number of posts showed
    private int limit = 10;

    // Number of all posts
    private int rows;

    // Search Path
    private String path;

    public int getCurrent() {
        return current;
    }

    public void setCurrent(int current) {
        if (current >= 1) {
            this.current = current;
        }
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        if (limit >= 1 && limit <= 100) {
            this.limit = limit;
        }
    }

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        if (rows >= 0) {
            this.rows = rows;
        }
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    // Get the starting row in current page
    public int getOffset() {
       return (current - 1) * limit;
    }

    // Get the total number of pages
    public int getTotal () {
        if (rows % limit == 0) {
            return rows / limit;
        } else {
            return rows / limit + 1;
        }
    }

    // Get the starting page down the bottom page
    public int getFrom() {
        int from = current - 2;
        return from < 1 ? 1 : from;
    }

    // Get the ending page down the bottom page
    public int getTo() {
        int to = current + 2;
        int total = getTotal();
        return to > total ? total : to;
    }
}
