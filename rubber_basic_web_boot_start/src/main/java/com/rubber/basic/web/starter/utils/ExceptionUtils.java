package com.rubber.basic.web.starter.utils;


import com.rubber.base.components.util.result.code.SysCode;
import com.rubber.base.components.util.result.exception.IResultException;

/**
 * @author luffyu
 * Created on 2021/9/3
 */
public class ExceptionUtils {

    /**
     * 返回异常的信息
     * @param e 异常
     * @return 返回异常的信息
     */
    public static String printErrorMsg(Exception e){
        String code=null,msg=null;
        if(e instanceof IResultException){
            IResultException eh = (IResultException)e;
            if(eh.getResult() != null){
                code = eh.getResult().getCode();
                msg = eh.getResult().getMsg();
            }
        }else{
            code = SysCode.SYSTEM_ERROR.code;
            msg = e.getMessage();
        }
        return printErrorMsg(e,code,msg);
    }

    /**
     * 打印异常信息
     * @param e 异常信息
     * @param code code信息
     * @param msg msg信息
     * @return 返回信息
     */
    public static String printErrorMsg(Exception e,String code,String msg){
        StackTraceElement se = e.getStackTrace()[0];
        StringBuilder sb = new StringBuilder();
        sb.append("错误code:").append(code)
                .append(",错误信息:").append(msg)
                .append(",异常名称:").append(e.getClass().getName())
                .append(",错误来源:").append(se.getClassName())
                .append(",方法名:").append(se.getMethodName())
                .append(",行数:").append(se.getLineNumber());
        return sb.toString();
    }
}
