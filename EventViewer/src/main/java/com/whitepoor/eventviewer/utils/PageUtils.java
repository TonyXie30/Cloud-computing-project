package com.whitepoor.eventviewer.utils;

import com.whitepoor.eventviewer.pojo.PageInfo;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

public class PageUtils {

    /**
     * 获取一个PageRequest对象，用于指导repository进行分页
     * @param page 第k页（从0开始）
     * @param limit 每页的最大元素数量
     * @param sort "+"(升序)/ "-"(降序)
     * @param attr 依照什么属性进行排序，顺序按数组顺序
     * @return PageRequest对象
     */
    public static PageRequest getPageRequest(Integer page, Integer limit, String sort, String[] attr) {
        PageInfo info = new PageInfo();
        info.setPage(page);
        info.setLimit(limit);
        info.setSort(sort);
        return getPageRequest(info,attr);
    }

    public static PageRequest getPageRequest(PageInfo info, String[] attr) {
        Sort sort_;
        String sort = info.getSort();
        Integer page = info.getPage();
        Integer limit = info.getLimit();

        if ((sort == null|| sort.equals("+"))){
            sort_ = Sort.by(attr).ascending();
        } else {
            sort_ = Sort.by(attr).descending();
        }
        PageRequest pageable;
        pageable = PageRequest.of(page, limit, sort_);
        return pageable;
    }
}
