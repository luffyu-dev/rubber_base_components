package com.rubber.base.components.tools;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.FatalBeanException;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author luffyu
 * Created on 2020/10/28
 */
public class RubberBeanUtils {



    public static void copyProperties2(Object source, Object target,boolean isFilterSourceNull,boolean isCheckTargetNull) throws BeansException, IllegalAccessException {
        Field[] targetFields = target.getClass().getDeclaredFields();
        Field[] sourceFields = source.getClass().getDeclaredFields();
        List<Field> list = Arrays.asList(sourceFields);
        Map<String, Field> sourceMap = list.stream().collect(Collectors.toMap(Field::getName, i -> i));

        for (Field field:targetFields){
            System.out.println(field.getName());
            Field sourceFile = sourceMap.get(field.getName());
            if (sourceFile == null){
                continue;
            }
            field.setAccessible(true);
            sourceFile.setAccessible(true);
            Object targetValue = field.get(target);
            Object sourceValue = sourceFile.get(source);
            System.out.println(targetValue+">>>"+sourceValue);

        }
    }




    /**
     *
     * @param source
     * @param target
     * @param isFilterSourceNull 当为true的时候，source里面为空的值，不会进行写入到target中
     * @param isCheckTargetNull 当为true的时候，target中以及存在来值，则不会被source中的值覆盖
     * @throws BeansException
     */
    public static void copyProperties(Object source, Object target,boolean isFilterSourceNull,boolean isCheckTargetNull) throws BeansException {
        copyProperties(source, target, isFilterSourceNull,isCheckTargetNull,null, (String[]) null);
    }


    private static void copyProperties(Object source, Object target,boolean isFilterSourceNull,boolean isCheckTargetNull, @Nullable Class<?> editable,
                                       @Nullable String... ignoreProperties) throws BeansException {

        Assert.notNull(source, "Source must not be null");
        Assert.notNull(target, "Target must not be null");

        Class<?> actualEditable = target.getClass();
        if (editable != null) {
            if (!editable.isInstance(target)) {
                throw new IllegalArgumentException("Target class [" + target.getClass().getName() +
                        "] not assignable to Editable class [" + editable.getName() + "]");
            }
            actualEditable = editable;
        }
        PropertyDescriptor[] targetPds = BeanUtils.getPropertyDescriptors(actualEditable);
        List<String> ignoreList = (ignoreProperties != null ? Arrays.asList(ignoreProperties) : null);

        for (PropertyDescriptor targetPd : targetPds) {
            Method writeMethod = targetPd.getWriteMethod();
            if (writeMethod != null && (ignoreList == null || !ignoreList.contains(targetPd.getName()))) {
                PropertyDescriptor sourcePd = BeanUtils.getPropertyDescriptor(source.getClass(), targetPd.getName());
                if (sourcePd != null) {
                    Method readMethod = sourcePd.getReadMethod();
                    if (readMethod != null &&
                            ClassUtils.isAssignable(writeMethod.getParameterTypes()[0], readMethod.getReturnType())) {
                        try {
                            if (!Modifier.isPublic(readMethod.getDeclaringClass().getModifiers())) {
                                readMethod.setAccessible(true);
                            }
                            Object sourceValue = readMethod.invoke(source);
                            Object targetValue = readMethod.invoke(target);
                            if (isFilterSourceNull && sourceValue == null){
                                continue;
                            }
                            if (isCheckTargetNull && targetValue != null){
                                continue;
                            }
                            if (!Modifier.isPublic(writeMethod.getDeclaringClass().getModifiers())) {
                                writeMethod.setAccessible(true);
                            }
                            writeMethod.invoke(target, sourceValue);
                        }
                        catch (Throwable ex) {
                            throw new FatalBeanException(
                                    "Could not copy property '" + targetPd.getName() + "' from source to target", ex);
                        }
                    }
                }
            }
        }
    }
}
