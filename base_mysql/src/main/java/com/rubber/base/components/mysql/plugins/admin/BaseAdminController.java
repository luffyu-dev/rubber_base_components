package com.rubber.base.components.mysql.plugins.admin;

import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.URLUtil;
import com.alibaba.fastjson.JSON;
import com.rubber.base.components.mysql.plugins.admin.page.PageModel;
import com.rubber.base.components.util.result.code.SysCode;
import com.rubber.base.components.util.result.exception.BaseResultRunTimeException;

/**
 * <p>基础的Controller方法</p>
 *
 * @author luffyu
 * @date 2020-01-14 14:49
 **/
public class BaseAdminController {

    /**
     * 把查询的json结构转化成对象结构
     * @param json json字符串
     * @return 返回对象信息
     */
    protected PageModel decodeForJsonString(String json){
        if(StrUtil.isEmpty(json)){
            return new PageModel();
        }
        try{
            String decode = URLUtil.decode(json);
            return JSON.parseObject(decode,PageModel.class);
        }catch (Exception e){
            throw new BaseResultRunTimeException(SysCode.PARAM_ERROR,"请求的参数必须是json结构");
        }
    }


}
