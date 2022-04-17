package com.rubber.base.components.util.enums;

import lombok.Getter;

/**
 * 环境的配置
 * @author luffyu
 * Created on 2020/10/25
 */
@Getter
public enum EnvEnum {

    /**
     * dev的开发环境
     */
    dev(100,"开发环境"),

    /**
     * 测试环境
     */
    test(200,"测试环境"),


    /**
     * 预发布的环境
     */
    pre(300,"预发布环境"),


    /**
     * 生产环境
     */
    master(400,"线上环境")
    ;

    private final Integer value;

    private final String label;

    EnvEnum(Integer value, String label) {
        this.value = value;
        this.label = label;
    }

    public static EnvEnum getByValue(Integer value){
        if (value == null){
            return null;
        }
        for (EnvEnum envEnum:EnvEnum.values()){
            if (envEnum.getValue().equals(value)){
                return envEnum;
            }
        }
        return null;
    }
}
