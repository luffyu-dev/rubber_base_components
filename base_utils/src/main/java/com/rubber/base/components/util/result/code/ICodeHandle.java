package com.rubber.base.components.util.result.code;

/**
 * @author luffyu
 * Created on 2019-07-12
 */
public interface ICodeHandle {


    /**
     * 定义的状态码
     * 状态码有为五位数
     *
     * *-**-**
     *
     * 第一位 * 表示总业务层面 例如1表示系统层面的
     * 后面两位 ** 表示业务层面的
     * 最后两位 ** 表示具体的错误
     *
     * 其中系统的code为 1-**-** 开始
     *
     * @return 返回code值
     */
    String getCode();

    /**
     *
     * 定义的提示信息
     * @return 返回msg信息
     */
    String getMsg();
}
