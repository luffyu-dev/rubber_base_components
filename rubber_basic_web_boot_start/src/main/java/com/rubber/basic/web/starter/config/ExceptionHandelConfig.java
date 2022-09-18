package com.rubber.basic.web.starter.config;

import com.rubber.base.components.util.result.IResultHandle;
import com.rubber.base.components.util.result.ResultMsg;
import com.rubber.base.components.util.result.RubberSystem;
import com.rubber.base.components.util.result.exception.IResultException;
import com.rubber.basic.web.starter.utils.ExceptionUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;


/**
 * @author luffyu
 * Created on 2019-10-31
 */
@ControllerAdvice
@Slf4j
public class ExceptionHandelConfig {


    /**
     * 全局的异常处理器
     * @param e 当前的请求残速回
     * @return
     * @throws Exception
     */
    @ExceptionHandler(value = {Exception.class, RuntimeException.class})
    @ResponseBody
    public ResultMsg handel(Exception e) throws Exception {
        ExceptionUtils.printErrorMsg(e);
        if(e instanceof IResultException){
            IResultException re = (IResultException)e;
            IResultHandle resultHandle = re.getResult();
            if( resultHandle instanceof ResultMsg){
                return doInitResultMsg((ResultMsg) resultHandle);
            }else {
                ResultMsg resultMsg =  ResultMsg.create(resultHandle,resultHandle.getData());
                return doInitResultMsg(resultMsg);
            }
        }else {
            String errMsg = e.getMessage();
            ResultMsg resultMsg =  ResultMsg.error(errMsg != null ? errMsg.substring(Math.min(errMsg.length(),100)) : "");
            return doInitResultMsg(resultMsg);
        }
    }


    private ResultMsg doInitResultMsg(ResultMsg resultMsg){
        if (resultMsg.getSysData() == null){
            resultMsg.setSysData(new RubberSystem());
        }
        return resultMsg;
    }

}
