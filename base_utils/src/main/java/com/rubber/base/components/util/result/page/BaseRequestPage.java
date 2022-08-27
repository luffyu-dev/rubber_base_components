package com.rubber.base.components.util.result.page;

import lombok.Data;

/**
 * @author luffyu
 * Created on 2022/8/15
 */
@Data
public class BaseRequestPage {

    /**
     * 当前页
     */
    private int page = 1;

    /**
     * 每页大小
     */
    private int size = 20;

}
