package com.rubber.base.components.mysql.utils;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.rubber.base.components.util.result.page.ResultPage;

import java.util.List;

/**
 * @author luffyu
 * Created on 2022/11/28
 */
public class PageUtils {


    /**
     * 数据转换对象
     */
    public  static  <T> ResultPage<T> convertPageResult(List<T> list, IPage page ){
        ResultPage<T> dtoResultPage = new ResultPage<>();
        dtoResultPage.setPages(page.getPages());
        dtoResultPage.setSize(page.getSize());
        dtoResultPage.setTotal(page.getTotal());
        dtoResultPage.setCurrent(page.getCurrent());
        dtoResultPage.setRecords(list);
        return dtoResultPage;
    }
}
