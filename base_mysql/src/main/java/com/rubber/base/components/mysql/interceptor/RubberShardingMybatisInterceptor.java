package com.rubber.base.components.mysql.interceptor;

import cn.hutool.core.map.MapUtil;
import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.util.JdbcConstants;
import com.rubber.base.components.mysql.constant.RubberDbConstant;
import com.rubber.base.components.mysql.plugins.sharding.config.RubberShardingConfig;
import com.rubber.base.components.mysql.interceptor.visit.ShardingTableVisitor;
import com.rubber.base.components.mysql.plugins.sharding.ShardingEnable;
import com.rubber.base.components.mysql.plugins.sharding.ShardingField;
import com.rubber.base.components.mysql.plugins.sharding.ShardingLambdaQueryWrapper;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * rubber的分库分表插件拦截器
 *
 * @author luffyu
 * Created on 2022/10/14
 */
@Intercepts({
        @Signature(type = StatementHandler.class,method = "prepare",args = {Connection.class,Integer.class})
})
public class RubberShardingMybatisInterceptor implements Interceptor {


    private RubberShardingConfig shardingConfig;

    /**
     * 改写sql的字段
     */
    private Field boundSqlField;



    public RubberShardingMybatisInterceptor(RubberShardingConfig shardingConfig) {
        try{
            boundSqlField = BoundSql.class.getDeclaredField("sql");
            boundSqlField.setAccessible(true);
            this.shardingConfig = shardingConfig;
        }catch (Exception e){

        }
    }

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        StatementHandler statementHandler = (StatementHandler)invocation.getTarget();
        BoundSql boundSql =  statementHandler.getBoundSql();
        // 更改sql
        String sql = doShardingSql(boundSql);
        boundSqlField.set(boundSql,sql);
        return invocation.proceed();
    }


    // sql改写
    private String doShardingSql(BoundSql boundSql){
        String sql = boundSql.getSql();
        Map<String,Object> params = doResolveParams(boundSql);
        List<SQLStatement> sqlStatements = SQLUtils.parseStatements(sql, JdbcConstants.MYSQL);
        for (SQLStatement sqlStatement:sqlStatements){
            ShardingTableVisitor visitor = new ShardingTableVisitor(shardingConfig.getRubberShardingConfig(),params);
            sqlStatement.accept(visitor);
        }
        return SQLUtils.toSQLString(sqlStatements , JdbcConstants.MYSQL);
    }


    private Map<String,Object> doResolveParams(BoundSql boundSql) {
        try {
            Map<String, Object> map = new HashMap<>();
            Object object = boundSql.getParameterObject();
            if (object instanceof Map) {
                Map<String, Object> result = (Map<String, Object>) object;
                map.putAll(result);
                for (Object k : result.values()) {
                    Map<String, Object> shardingParams = doResolveShardingParams(k);
                    if (MapUtil.isNotEmpty(shardingParams)) {
                        map.putAll(shardingParams);
                    }
                }
            } else {
                map.putAll(doResolveShardingParams(object));
            }
            return map;
        }catch (Exception e){
            return new HashMap<>();
        }
    }



    private Map<String,Object> doResolveShardingParams(Object object) throws Exception {
        Map<String,Object> map = new HashMap<>();
        if (object instanceof ShardingEnable){
            Field[] fields = object.getClass().getDeclaredFields();
            for (Field field:fields){
                ShardingField shardingField = field.getAnnotation(ShardingField.class);
                if (shardingField == null){
                    continue;
                }
                field.setAccessible(true);
                Object value  = field.get(object);
                if (value == null){
                    continue;
                }
                map.put(RubberDbConstant.SHARDING_VALUE,value);
            }
        }
        if (object instanceof ShardingLambdaQueryWrapper){
            ShardingLambdaQueryWrapper slqw = (ShardingLambdaQueryWrapper)object;
            map.put(RubberDbConstant.SHARDING_VALUE,slqw);
        }
        return map;
    }
}
