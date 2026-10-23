package com.rubber.base.components.mysql.utils;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.TemplateType;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import lombok.Data;


/**
 * @author luffyu
 * Created on 2022/8/14
 */
public class MybatisBaseCodeGenerator {

    CodeGeneratorConfigBean configBean;
    public MybatisBaseCodeGenerator(CodeGeneratorConfigBean codeGeneratorConfigBean) {
        this.configBean = codeGeneratorConfigBean;
    }

    /**
     * 新增创建代码
     * @param tables 需要创建的tables
     */
    public void create(String... tables) {
        // mybatis-plus 3.5.7 起 AutoGenerator 的配置类改为 Builder 模式，这里使用 FastAutoGenerator 链式构建
        FastAutoGenerator generator = FastAutoGenerator.create(
                configBean.getJdbcUrl(), configBean.getUserName(), configBean.getPassword())
                // 全局变量配置（setOpen(false) → disableOpenDir；fileOverride 默认关闭不覆盖）
                .globalConfig(builder -> builder
                        .outputDir(configBean.getModelName() + "/src/main/java")
                        .author(configBean.getAuthor())
                        .disableOpenDir()
                        .dateType(DateType.ONLY_DATE))
                // 包名配置
                .packageConfig(builder -> builder
                        .parent(configBean.packageParent)
                        .entity("entity")
                        .service("dal")
                        .serviceImpl("dal.impl")
                        .mapper("mapper")
                        .xml("mapper"))
                // 模板配置：不生成 controller
                .templateConfig(builder -> builder.disable(TemplateType.CONTROLLER))
                // 策略配置
                .strategyConfig(builder -> {
                    builder.addInclude(tables)
                            .addTablePrefix(configBean.getTablePrefix())
                            .addFieldPrefix(configBean.getFieldPrefix());
                    // 实体策略
                    builder.entityBuilder()
                            .naming(NamingStrategy.underline_to_camel)
                            .columnNaming(NamingStrategy.underline_to_camel)
                            .enableLombok()
                            .versionPropertyName("version")
                            .formatFileName("%sEntity");
                    if (StrUtil.isNotEmpty(configBean.getBaseEntityClass())) {
                        builder.entityBuilder().superClass(configBean.getBaseEntityClass());
                    }
                    // service 策略（自定义 service 命名：I%sDal / %sDalImpl）
                    if (StrUtil.isNotEmpty(configBean.getBaseServiceImpl())) {
                        builder.serviceBuilder().superServiceImplClass(configBean.getBaseServiceImpl());
                    }
                    if (StrUtil.isNotEmpty(configBean.getBaseService())) {
                        builder.serviceBuilder().superServiceClass(configBean.getBaseService());
                    }
                    builder.serviceBuilder()
                            .formatServiceFileName("I%sDal")
                            .formatServiceImplFileName("%sDalImpl");
                    // mapper 策略（自定义命名：%sMapper）
                    builder.mapperBuilder()
                            .formatMapperFileName("%sMapper")
                            .formatXmlFileName("%sMapper");
                    // controller 策略
                    if (configBean.isRestController) {
                        builder.controllerBuilder().enableRestStyle().enableHyphenStyle();
                    }
                })
                .templateEngine(new FreemarkerTemplateEngine());
        generator.execute();
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
        private String author = "rockyu";
        /**
         * 包地址
         */
        private String packageParent = "";
        private String baseEntityClass = "com.rubber.base.components.mysql.plugins.admin.bean.BaseEntity";
        private String baseServiceImpl = "com.rubber.base.components.mysql.plugins.admin.BaseAdminService";
        private String baseService = "com.rubber.base.components.mysql.plugins.admin.IBaseAdminService";
        private boolean isLombok = true;
        private boolean isRestController = true;
        private String tablePrefix = "t_";
        private String fieldPrefix = "F";

        public CodeGeneratorConfigBean() {
            // 默认读取当前类所在的包地址
            String path = this.getClass().getResource("/").getPath();
            this.modelName = StrUtil.subBefore(path, "/target", false);
            Class<?> mainClass = deduceMainApplicationClass();
            if (mainClass != null) {
                this.packageParent = mainClass.getPackage().getName();
            }
        }
    }

    private static Class<?> deduceMainApplicationClass() {
        try {
            StackTraceElement[] stackTrace = new RuntimeException().getStackTrace();
            for (StackTraceElement stackTraceElement : stackTrace) {
                if ("main".equals(stackTraceElement.getMethodName())) {
                    return Class.forName(stackTraceElement.getClassName());
                }
            }
        } catch (ClassNotFoundException ex) {
            // Swallow and continue
        }
        return null;
    }
}
