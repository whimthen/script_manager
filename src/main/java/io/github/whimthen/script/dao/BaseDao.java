package io.github.whimthen.script.dao;

import io.github.whimthen.script.entity.Page;

/**
 * @project: script_manager
 * @created: with IDEA
 * @author: nzlong
 * @Date: 2018 08 09 下午3:4458 | 八月. 星期四
 */
public class BaseDao {

    public static void setPage(Page page, long totalRow) {
        page.setTotalRow(totalRow);
        page.setTotalPage(new Double(Math.ceil((Double.valueOf(totalRow) / Double.valueOf(page.getPageSize())))).intValue());
    }

}
