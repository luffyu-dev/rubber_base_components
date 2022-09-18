package com.rubber.base.components.util.result.code;

/**
 * @author luffyu
 * Created on 2019-07-12
 */
public enum SysCode implements ICodeHandle{


    /**
     * 系统的code 从 1-**-**-** 开始
     *
     * 1-**-**-** 开始
     *
     * 2-**-**-** 系统类型错误
     *
     * 3-**-**-** 用户行为错误
     *
     */

    /**
     * 1-**-**-** 表示系统成功的提示
     */
    SUCCESS("1000000","成功"),

    /**
     * 2-**-**-** 表示服务系统异常的提示
     */
    SYSTEM_ERROR("2000000","系统错误"),
    SYSTEM_BUS("2000001","系统繁忙"),


    /**
     * 3-**-**-** 表示用户的行为错误
     */
    PARAM_ERROR("3000000","参数错误"),
    LOGIN_EXPIRED("3000100","请重新登录");

    ;
    public String code;

    public String msg;


    SysCode(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    @Override
    public String getCode() {
        return this.code;
    }

    @Override
    public String getMsg() {
        return this.msg;
    }}
