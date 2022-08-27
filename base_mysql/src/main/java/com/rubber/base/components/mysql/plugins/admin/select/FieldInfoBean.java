package com.rubber.base.components.mysql.plugins.admin.select;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

/**
 * @author luffyu
 * Created on 2022/8/27
 */
@Data
public class FieldInfoBean {

    private TableField tableField;

    private Class<?> fieldClass;
}
