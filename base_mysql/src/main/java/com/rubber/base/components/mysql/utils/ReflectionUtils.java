package com.rubber.base.components.mysql.utils;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.annotation.TableField;
import com.rubber.base.components.mysql.plugins.admin.select.FieldInfoBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author luffyu
 * Created on 2019-05-15
 */
public class ReflectionUtils {

    private static Logger logger = LoggerFactory.getLogger(ReflectionUtils.class);

    /**
     * 通过反射拿到 对象的基本信息
     */
    public static Map<String, Object> getClassFieldsValues(Object modelObj) {
        Map<String, Object> map = new HashMap<>(16);
        Field[] fields = modelObj.getClass().getDeclaredFields();
        for (Field field : fields) {
            TableField tableField = field.getAnnotation(TableField.class);
            if(tableField != null && !tableField.exist()){
                continue;
            }
            Object obj = ReflectionUtils.getFieldValue(modelObj, field.getName());
            if (obj != null && !"".equals(obj)) {
                String fieldName = field.getName();
                if (isJavaClass(field.getType())) {
                    if (obj instanceof Date) {
                        map.put(fieldName, DateUtil.format((Date) obj, DatePattern.NORM_DATETIME_PATTERN));
                    } else {
                        map.put(fieldName, obj);
                    }
                } else {
                    for (Field subField : obj.getClass().getDeclaredFields()) {
                        Object subObj = ReflectionUtils.getFieldValue(obj, subField.getName());
                        if (subObj != null && !"".equals(subObj)) {
                            if (subObj instanceof Date) {
                                map.put(fieldName + "." + subField.getName(), DateUtil.format((Date) obj, DatePattern.NORM_DATETIME_PATTERN));
                            } else {
                                map.put(fieldName + "." + subField.getName(), subObj);
                            }
                        }
                    }
                }
            }

        }
        return map;
    }


    /**
     * 直接读取对象属性值, 无视private/protected修饰符, 不经过getter函数.
     */
    private static Object getFieldValue(final Object obj, final String fieldName) {
        Field field = getAccessibleField(obj, fieldName);

        if (field == null) {
            throw new IllegalArgumentException("Could not find field [" + fieldName + "] on target [" + obj + "]");
        }

        Object result = null;
        try {
            result = field.get(obj);
        } catch (IllegalAccessException e) {
            logger.error("不可能抛出的异常{}", e.getMessage());
        }
        return result;
    }



    /**
     * 循环向上转型, 获取对象的DeclaredField,   并强制设置为可访问.
     * <p/>
     * 如向上转型到Object仍无法找到, 返回null.
     */
    private static Field getAccessibleField(final Object obj, final String fieldName) {
        Assert.notNull(obj, "object不能为空");
        Assert.hasText(fieldName, "fieldName");
        for (Class<?> superClass = obj.getClass(); superClass != Object.class; superClass = superClass.getSuperclass()) {
            try {
                Field field = superClass.getDeclaredField(fieldName);
                field.setAccessible(true);
                return field;
            } catch (NoSuchFieldException e) {//NOSONAR
                // Field不在当前类定义,继续向上转型
            }
        }
        return null;
    }

    /**
     * 判断当前是否JAVA还是自定义类
     */
    private static boolean isJavaClass(Class<?> clz) {
        return clz != null && clz.getClassLoader() == null;
    }

    /**
     * 通过反射拿到类的私有信息和类型
     * @param clz 类名
     * @return
     */
    public static Map<String, FieldInfoBean> getDBEntityFieldsName(Class<?> clz) {
        Map<String,FieldInfoBean> map = new HashMap<>(20);
        Field[] fields = clz.getDeclaredFields();
        for(Field field:fields){
            TableField tableField = field.getAnnotation(TableField.class);
            if(tableField != null){
                if(!tableField.exist()){
                    //表示数据库中不存在改实体信息
                    continue;
                }
            }
            FieldInfoBean fieldInfoBean = new FieldInfoBean();
            fieldInfoBean.setFieldClass(field.getType());
            fieldInfoBean.setTableField(tableField);
            map.put(field.getName(), fieldInfoBean);
        }
        return map;
    }
}
