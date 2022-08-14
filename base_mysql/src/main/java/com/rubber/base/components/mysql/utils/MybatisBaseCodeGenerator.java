package com.rubber.base.components.mysql.utils;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.*;
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
    public void create(String... tables){
        AutoGenerator autoGenerator = doCreatGenerator(tables);
        doCreate(autoGenerator);
    }
    /**
     * 开始执行
     */
    public boolean doCreate(AutoGenerator autoGenerator){
        if (autoGenerator != null){
            autoGenerator.execute();
            return true;
        }
        return false;
    }
    /**
     * 创建配置
     */
    public AutoGenerator doCreatGenerator(String... tables){
        AutoGenerator mpg = new AutoGenerator();
        //全局变量配置
        GlobalConfig gc = new GlobalConfig();
        gc.setOutputDir(configBean.getModelName() + "/src/main/java");
        gc.setAuthor(configBean.getAuthor());
        //是否打开输出文件
        gc.setOpen(false);
        //是否覆盖已经存在的文件
        gc.setFileOverride(false);
        //设置service的名称
        gc.setServiceImplName("%sDalImpl");
        gc.setServiceName("I%sDal");
        gc.setMapperName("%sMapper");
        gc.setXmlName("%sMapper");
        gc.setEntityName("%sEntity");
        //使用旧的日期类型
        gc.setDateType(DateType.ONLY_DATE);
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
        pc.setEntity("entity");
        pc.setService("dal");
        pc.setServiceImpl("dal.impl");
        pc.setMapper("mapper");
        pc.setXml("mapper");
        mpg.setPackageInfo(pc);
        // 自定义xml的生成位置 到 resources/mappers 中
//        InjectionConfig cfg = new InjectionConfig() {
//            @Override
//            public void initMap() {
//            }
//        };
//        List<FileOutConfig> fileOutConfigs = new ArrayList<>();
//        fileOutConfigs.add(new FileOutConfig("/templates/mapper.xml.ftl") {
//            @Override
//            public String outputFile(TableInfo tableInfo) {
//                return configBean.getModelName() + "/src/main/resources/mappers/" + tableInfo.getXmlName() + StringPool.DOT_XML;
//            }
//        });
//        cfg.setFileOutConfigList(fileOutConfigs);
//        mpg.setCfg(cfg);
        //模板配置
        TemplateConfig templateConfig = new TemplateConfig();
        //不生成controller
        templateConfig.setController(null);
        // 自定义生成xml的位置
        // templateConfig.setXml(null);
        mpg.setTemplate(templateConfig);
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
        if (StrUtil.isNotEmpty(configBean.getBaseService())){
            strategy.setSuperServiceClass(configBean.getBaseService());
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
        if (StrUtil.isNotEmpty(configBean.getTablePrefix())){
            strategy.setTablePrefix(configBean.getTablePrefix());
        }
        if (StrUtil.isNotEmpty(configBean.getFieldPrefix())){
            strategy.setFieldPrefix(configBean.getFieldPrefix());
        }
        mpg.setStrategy(strategy);
        mpg.setTemplateEngine(new FreemarkerTemplateEngine());
        return mpg;
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
            this.modelName = StrUtil.subBefore(path,"/target",false);
            Class<?>  mainClass = deduceMainApplicationClass();
            if (mainClass != null){
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
