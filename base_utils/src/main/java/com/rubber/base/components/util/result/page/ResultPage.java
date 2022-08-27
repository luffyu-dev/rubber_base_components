package com.rubber.base.components.util.result.page;

import lombok.Data;

import java.util.Collections;
import java.util.List;

/**
 * @author luffyu
 * Created on 2022/8/15
 */
@Data
public class ResultPage<T> {


    /**
     * 总数
     */
    protected long total = 0;

    /**
     * 每页显示条数，默认 10
     */
    protected long size = 10;

    /**
     * 当前页
     */
    protected long current = 1;

    /**
     * 总页数
     */
    protected long pages;

    /**
     * 查询数据列表
     */
    protected List<T> records = Collections.emptyList();

}
