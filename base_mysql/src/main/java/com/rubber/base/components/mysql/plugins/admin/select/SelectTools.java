package com.rubber.base.components.mysql.plugins.admin.select;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.rubber.base.components.mysql.plugins.admin.bean.BaseEntity;
import com.rubber.base.components.mysql.utils.ReflectionUtils;
import com.rubber.common.utils.exception.BaseException;
import com.rubber.common.utils.exception.BaseRuntimeException;
import com.rubber.common.utils.result.code.SysCode;
import com.rubber.common.utils.result.exception.BaseResultRunTimeException;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;

/**
 * @author luffyu
 * Created on 2019-05-27
 */
public class SelectTools {

    /**
     * 通过传入的参数信息来比较对象信息
     * @param selectModels 参数信息
     * @param clz 对象信息
     * @param <T> entit的类名称
     * @return 返回查询的参数
     */
    public static  <T extends BaseEntity> QueryWrapper<T> creatSearchWrapper(List<SelectModel> selectModels, Class<T> clz) throws BaseRuntimeException {
        QueryWrapper<T> queryWrapper = new QueryWrapper<>();
        if(!CollectionUtils.isEmpty(selectModels)){
            Map<String,Class<?>> clzFiles = ReflectionUtils.getDBEntityFieldsName(clz);
            for(SelectModel compareModel:selectModels){
                creatSearchWrapper(queryWrapper,clzFiles,compareModel);
            }
        }
        return queryWrapper;
    }

    /**
     * 通过传入的参数信息来比较对象信息
     * @param selectModels 参数信息
     * @param <T> entit的类名称
     * @return 返回查询的参数
     */
    public static  <T extends BaseEntity> QueryWrapper<T> creatSearchWrapper(List<SelectModel> selectModels, Map<String,Class<?>> clzFiles) throws BaseRuntimeException {
        QueryWrapper<T> queryWrapper = new QueryWrapper<>();
        if(!CollectionUtils.isEmpty(selectModels)){
            for(SelectModel compareModel:selectModels){
                creatSearchWrapper(queryWrapper,clzFiles,compareModel);
            }
        }
        return queryWrapper;
    }



    /**
     * 传入单个参数查询用户的基本信息
     * @param selectModel 查询的参数
     * @param clz 实体类
     * @param <T> 泛型
     * @return 返回查询的queryWrapper
     */
    public static  <T extends BaseEntity> QueryWrapper<T> creatSearchWrapper(SelectModel selectModel, Class<T> clz) throws BaseException {
        QueryWrapper<T> queryWrapper = new QueryWrapper<>();
        Map<String,Class<?>> clzFiles = ReflectionUtils.getDBEntityFieldsName(clz);
        creatSearchWrapper(queryWrapper,clzFiles,selectModel);
        return queryWrapper;
    }



    /**
     * 通过单个参数查询参数信息
     * @param queryWrapper
     * @param clzFiles
     * @param selectModel
     * @param <T>
     * @return
     */
    private static  <T extends BaseEntity> QueryWrapper<T> creatSearchWrapper(QueryWrapper<T> queryWrapper, Map<String,Class<?>> clzFiles, SelectModel selectModel) throws BaseRuntimeException {
        if(queryWrapper == null){
            queryWrapper = new QueryWrapper<>();
        }
        if(StringUtils.isEmpty(selectModel.getField())){
            return queryWrapper;
        }
        if(!clzFiles.containsKey(selectModel.getField())){
            throw new BaseResultRunTimeException(SysCode.PARAM_ERROR,selectModel.getField()+"是非法成员变量");
        }
        String column = StrUtil.toUnderlineCase(selectModel.getField());
        Class<?> aClass = clzFiles.get(selectModel.getField());
        //解析比较信息
        switch (selectModel.getType()){
            case eq:
                queryWrapper.eq(column,selectModel.getData());
                break;
            case ne:
                queryWrapper.ne(column,selectModel.getData());
                break;
            case gt:
                queryWrapper.gt(column,selectModel.getData());
                break;
            case ge:
                queryWrapper.gt(column,selectModel.getData());
                break;
            case lt:
                queryWrapper.lt(column,selectModel.getData());
                break;
            case le:
                queryWrapper.le(column,selectModel.getData());
                break;
            case like:
                if(aClass == String.class){
                    queryWrapper.like(column,selectModel.getData());
                    break;
                }else {
                    throw new BaseResultRunTimeException(SysCode.PARAM_ERROR,selectModel.getData()+"不是String对象，不是使用like比较");
                }
            case between:
                queryWrapper.between(column,selectModel.getData(),selectModel.getDateEnd());
                break;
            default:
        }
        return queryWrapper;
    }
}
