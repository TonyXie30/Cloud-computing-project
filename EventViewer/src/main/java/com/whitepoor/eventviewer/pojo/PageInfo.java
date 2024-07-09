package com.whitepoor.eventviewer.pojo;


import lombok.Getter;

/**
 * <p>该类包装了分页所需的信息，包括：页码，单页最大元素数量，排序（正序/倒序），根据什么元素排序。
 * <p>默认页码为0，单页最大元素为2147483647,排序为正序("+")
 * <p>注意"根据什么元素排序"的信息默认值为{"id"}
 */
@Getter
public class PageInfo {
    Integer page = 0;
    Integer limit = Integer.MAX_VALUE;

    String sort = "+";
    String[] attr = new String[]{"id"};

    public PageInfo(){}

    public PageInfo(Integer page,Integer limit,String sort) {
        setPage(page);
        setLimit(limit);
        setSort(sort);
    }

    public void setLimit(Integer limit) {
        if (limit == null) return;
        this.limit = limit;
    }

    public void setPage(Integer page) {
        if (page == null) return;
        this.page = page;
    }

    public void setSort(String sort) {
        if (sort == null) return;
        this.sort = sort;
    }
}
