package io.github.whimthen.script.entity;

import java.util.List;

/**
 * @project: script_manager
 * @created: with IDEA
 * @author: nzlong
 * @Date: 2018 08 09 下午2:4958 | 八月. 星期四
 */
public class Page<T> {

    /**
     * 总行数
     */
    private long totalRow;

    /**
     * 总页码数
     */
    private Integer totalPage;

    /**
     * 每页记录数
     */
    private Integer pageSize;

    /**
     * 当前第几页
     */
    private Integer pageNum;

    /**
     * 从第几条数据开始
     */
    private Integer from;

    private List<T> list;

    public Page(){};

    public Page(BaseEntity baseEntity) {
        construct(baseEntity.getPageIndex(), baseEntity.getPageSize());
    }

    public Page(int pageNum, int pageSize) {
        construct(pageNum, pageSize);
    }

    public void construct(int pageNum, int pageSize) {
        //计算当前页
        if (pageNum < 0) {
            this.pageNum = 1;
        } else {
            //当前页
            this.pageNum = pageNum;
        }
        if (pageSize <= 0) {
            //记录每页显示的记录数
            this.pageSize = 20;
        } else {
            this.pageSize = pageSize;
        }
        //计算开始的记录和结束的记录
        this.from = ((this.pageNum <= 0 ? 1 : this.pageNum) - 1) * this.pageSize;
    }

    public long getTotalRow() {
        return totalRow;
    }

    public void setTotalRow(long totalRow) {
        this.totalRow = totalRow;
    }

    public Integer getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(Integer totalPage) {
        this.totalPage = totalPage;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    public Integer getFrom() {
        return from;
    }

    public void setFrom(Integer from) {
        this.from = from;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

}
