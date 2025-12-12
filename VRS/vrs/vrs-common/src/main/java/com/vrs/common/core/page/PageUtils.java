package com.vrs.common.core.page;

import com.github.pagehelper.PageInfo;

/**
 * 查询分页
 */
public class PageUtils {
    public static PageResult getPageResult( PageInfo<?> pageInfo){
        PageResult pageResult = new PageResult();
        //查询数量
        pageResult.setPageNum(pageInfo.getPageNum());
        //总页数
        pageResult.setPageSize(pageInfo.getSize());

        pageResult.setTotalSize(pageInfo.getTotal());

        pageResult.setTotalPages(pageInfo.getPages());
        pageResult.setResult(pageInfo.getList());
        return pageResult;
    }
}
