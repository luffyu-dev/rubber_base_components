package com.rubber.base.components.mysql.utils;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.generator.FastAutoGenerator;
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

    public void create(String... tables) {
        // mybatis-plus 3.5.7 起 AutoGenerator 配置改为 Builder 模式，使用 FastAutoGenerator 链式构建
        FastAutoGenerator.create(configBean.getJdbcUrl(), configBean.getUserName(), configBean.getPassword())
                // 全局变量配置（setOpen(false) → disableOpenDir；fileOverride 默认关闭不覆盖）
                .globalConfig(builder -> builder
                        .outputDir(configBean.getModelName() + "/src/main/java")
                        .author(configBean.getAuthor())
                        .disableOpenDir())
                // 包名配置
                .packageConfig(builder -> builder
                        .parent(configBean.packageParent)
                        .xml("mapper"))
                // 策略配置
                .strategyConfig(builder -> {
                    builder.addInclude(tables)
                            .addTablePrefix(configBean.getTablePrefix());
                    builder.entityBuilder()
                            .naming(NamingStrategy.underline_to_camel)
                            .columnNaming(NamingStrategy.underline_to_camel)
                            .enableLombok()
                            .versionPropertyName("version");
                    if (StrUtil.isNotEmpty(configBean.getBaseEntityClass())) {
                        builder.entityBuilder().superClass(configBean.getBaseEntityClass());
                    }
                    if (StrUtil.isNotEmpty(configBean.getBaseServiceImpl())) {
                        builder.serviceBuilder().superServiceImplClass(configBean.getBaseServiceImpl());
                    }
                    if (configBean.isRestController) {
                        builder.controllerBuilder().enableRestStyle().enableHyphenStyle();
                    }
                })
                .templateEngine(new FreemarkerTemplateEngine())
                .execute();
    }


    @Data
    public static class CodeGeneratorConfigBean {
        /**
         * 数据库链接信息
         */
        private String jdbcUrl;
        private String userName;
        private String password;
        private String driverName = "com.mysql.cj.jdbc.Driver";

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
