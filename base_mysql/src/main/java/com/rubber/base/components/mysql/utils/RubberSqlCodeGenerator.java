package com.rubber.base.components.mysql.utils;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.PackageConfig;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import lombok.Data;

/**
 * mybatis-plus 代码生成工具
 * @author luffyu
 * Created on 2019-10-31
 */
public class RubberSqlCodeGenerator {

    CodeGeneratorConfigBean configBean;


    public RubberSqlCodeGenerator(CodeGeneratorConfigBean codeGeneratorConfigBean) {
        this.configBean = codeGeneratorConfigBean;
    }

    public void create(String... tables){
        AutoGenerator mpg = new AutoGenerator();
        mpg.setTemplateEngine(new FreemarkerTemplateEngine());

        //全局变量配置
        GlobalConfig gc = new GlobalConfig();
        gc.setOutputDir(configBean.getModelName() + "/src/main/java");
        gc.setAuthor(configBean.getAuthor());
        //是否打开输出文件
        gc.setOpen(false);
        //是否覆盖已经存在的文件
        gc.setFileOverride(false);
        mpg.setGlobalConfig(gc);

        /**
         * 数据库的设置
         */
        DataSourceConfig dataSourceConfig = new DataSourceConfig();
        dataSourceConfig.setUrl(configBean.getJdbcUrl());
        dataSourceConfig.setDriverName(configBean.getDriverName());
        dataSourceConfig.setUsername(configBean.getUserName());
        dataSourceConfig.setPassword(configBean.getPassword());
        mpg.setDataSource(dataSourceConfig);


        //包名 配置
        PackageConfig pc = new PackageConfig();
        pc.setParent(configBean.packageParent);
        pc.setXml("mapper");

        mpg.setPackageInfo(pc);


        // 策略配置
        StrategyConfig strategy = new StrategyConfig();
        strategy.setNaming(NamingStrategy.underline_to_camel);
        strategy.setColumnNaming(NamingStrategy.underline_to_camel);

        if (StrUtil.isNotEmpty(configBean.getBaseEntityClass())){
            strategy.setSuperEntityClass(configBean.getBaseEntityClass());
        }
        if (StrUtil.isNotEmpty(configBean.getBaseServiceImpl())){
            strategy.setSuperServiceImplClass(configBean.getBaseServiceImpl());
        }
        //是否开启 lombok 开启之后 文件中没有get set方法
        strategy.setEntityLombokModel(configBean.isLombok);
        //对于controller 是否开启 @RestController注解
        strategy.setRestControllerStyle(configBean.isRestController);

        strategy.setInclude(tables);
        //strategy.setSuperEntityColumns(keyId);
        //乐观锁字段
        strategy.setVersionFieldName("version");

        strategy.setControllerMappingHyphenStyle(true);
        strategy.setTablePrefix(configBean.getTablePrefix());

        mpg.setStrategy(strategy);
        mpg.setTemplateEngine(new FreemarkerTemplateEngine());
        mpg.execute();
    }





    @Data
    public static class CodeGeneratorConfigBean{
        /**
         * 数据库链接信息
         */
        private String jdbcUrl;
        private String userName;
        private String password;
        private String driverName = "com.mysql.jdbc.Driver";

        /**
         * 模块名称
         */
        private String modelName;
        private String author = "RubberSqlCodeGenerator";

        /**
         * 包地址
         */
        private String packageParent = "";
        private String baseEntityClass = "";
        private String baseServiceImpl = "";


        private boolean isLombok = true;
        private boolean isRestController = true;


        private String tablePrefix = "t_";

    }
}
