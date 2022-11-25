package com.rubber.base.components.util.result.page;

import com.rubber.base.components.util.session.BaseUserSession;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author luffyu
 * Created on 2022/8/15
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class BaseRequestPage extends BaseUserSession {

    /**
     * 当前页
     */
    private int page = 1;

    /**
     * 每页大小
     */
    private int size = 20;

}
