package com.rubber.base.components.util.result.exception;


import cn.hutool.core.util.StrUtil;
import com.rubber.base.components.util.exception.BaseRuntimeException;
import com.rubber.base.components.util.result.IResultHandle;
import com.rubber.base.components.util.result.ResultMsg;
import com.rubber.base.components.util.result.code.ICodeHandle;


/**
 * @author luffyu
 * Created on 2019-07-12
 */
public class BaseResultRunTimeException extends BaseRuntimeException implements IResultException {

    private IResultHandle handle;


    public BaseResultRunTimeException(String msg) {
        super(msg);
        this.handle = ResultMsg.error(msg);
    }

    public BaseResultRunTimeException(IResultHandle handle) {
        super(handle.getMsg());
        this.handle = handle;
    }

    public BaseResultRunTimeException(String code, String msg, Object data) {
        super(msg);
        this.handle = ResultMsg.create(code,msg,data);
    }


    public BaseResultRunTimeException(ICodeHandle handle, Object data) {
        super(handle.getCode() + ":" +handle.getMsg());
        this.handle = ResultMsg.create(handle,data);
    }


    public BaseResultRunTimeException(ICodeHandle handle) {
        super(handle.getCode() + ":" +handle.getMsg());
        this.handle = ResultMsg.create(handle);
    }


    public BaseResultRunTimeException(ICodeHandle handle, String msg, Object... arguments) {
        super(handle.getCode() + ":" +handle.getMsg());
        String logMassage = handle.getMsg() + "," + StrUtil.format(msg,arguments);
        this.handle = ResultMsg.create(handle.getCode(),logMassage,null);
    }


    @Override
    public IResultHandle getResult() {
        return this.handle;
    }
}
