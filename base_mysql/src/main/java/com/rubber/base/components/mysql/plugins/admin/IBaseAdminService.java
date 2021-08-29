package com.rubber.base.components.mysql.plugins.admin;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.rubber.base.components.mysql.plugins.admin.bean.BaseEntity;
import com.rubber.base.components.mysql.plugins.admin.page.PageModel;
import com.rubber.base.components.mysql.plugins.admin.select.SelectModel;

import java.util.List;
import java.util.Set;

/**
 * @author luffyu
 * Created on 2019-10-08
 */
public interface IBaseAdminService<T extends BaseEntity> extends IService<T> {

    /**
     * 通过多维度到查询列表来查询基本信息
     * @param selectModels 查询列表
     * @param clz 实体的类名称
     * @param requiredField 查询时候必要的字段
     * @return
     */
    List<T> listBySelect(List<SelectModel> selectModels, Class<T> clz, Set<String> requiredField);


    /**
     * 通过多维度分页查询列表的基本信息
     * @param page 分页的page
     * @param selectModels 查询列表信息
     * @param clz 实体的类型
     * @param requiredField 查询时候必要的字段
     * @return 返回分页字段信息
     */
    IPage<T> pageBySelect(IPage<T> page, List<SelectModel> selectModels, Class<T> clz, Set<String> requiredField);


    /**
     * 分页查询的结果信息
     * @param pageModel 分页的model
     * @param clz 当前的class类
     * @param requiredField 查询时候必要的字段
     * @return 返回查询信息
     */
    IPage<T> pageBySelect(PageModel pageModel, Class<T> clz, Set<String> requiredField);
}
