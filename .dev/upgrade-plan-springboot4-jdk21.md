# Rubber Base Components 框架升级技术方案

> 升级目标：`spring-boot-starter-parent` 由 `2.3.2.RELEASE` 升级至 `4.1.1`，JDK 由 `1.8` 升级至 `21`。
> 文档位置：`.dev/upgrade-plan-springboot4-jdk21.md`
> 工程根：`rubber_base_components`（多模块聚合，version `1.2.0-ALPHA`，分支 `dev_1.2.3-alpha`）

---

## 一、升级背景与目标

### 1.1 升级背景
当前工程基于 Spring Boot 2.3.2.RELEASE + JDK 1.8 构建，主要问题：
- Spring Boot 2.3 早已 EOL，存在大量已修复的安全漏洞与 CVE（含 log4j 1.x、fastjson 1.x、jjwt 0.9.x 等高风险依赖）。
- `dubbo:3.0.3`、`mybatis-plus:3.4.0` 等均为多年前的版本，社区已迭代至 Jakarta 化的新主版本；pom 中还残留未使用的 `io.shardingsphere` 死依赖。
- JDK 1.8 长期支持已进入尾声，无法使用 Records / Sealed / Pattern Matching / 虚拟线程等现代语言特性。

### 1.2 升级目标
| 维度 | 现状 | 目标 |
| --- | --- | --- |
| JDK | 1.8 | 21（LTS） |
| Spring Boot | 2.3.2.RELEASE | 4.1.1 |
| Spring Framework | 5.2.x | 7.x |
| Spring Cloud | （兼容 Spring Boot 2.3 的 Hoxton） | 与 Boot 4.1 对应的发布列车 |
| Spring Cloud Alibaba | 2.2.7.RELEASE | 与 Spring Cloud 新列车对齐（Nacos config/discovery） |
| 打包/发布 | 不变 | 不变（仍是 starter 聚合，对外提供 base_xxx 组件） |
| 组件版本号 | 1.2.0-ALPHA | 4.1.1-ALPHA | 与 Spring Boot 主版本对齐，便于识别配套生态 |

### 1.3 升级原则
1. **分阶段、可回退**：先升 Boot 大版本依赖管理，再逐模块适配代码，每个阶段保证可编译可单测。
2. **依赖跟着生态走**：所有 Spring 系生态依赖，统一由 Spring Boot BOM / Spring Cloud BOM 管理版本，不写死在 `<properties>` 中。
3. **先迁移、后优化**：先让工程在 Boot 4 + JDK 21 下编译运行通过，再考虑 Records / 虚拟线程等新特性改造。
4. **破坏性变更最小化**：对外提供的 `rubber` 配置 key、`RubberDbProperties` 等结构尽量保持向后兼容，避免下游业务方联动改造。

---

## 二、现状盘点（升级影响面分析）

### 2.1 模块与依赖矩阵
| 模块 | 关键依赖 | 升级风险点 |
| --- | --- | --- |
| `base_mysql` | druid、mybatis-plus 3.4.0、freemarker 2.3.28 | mybatis-plus starter 改名、druid jakarta 适配；pom 中残留未使用的 `io.shardingsphere` 依赖需清理 |
| `base_redis` | jedis（由 Boot 2.3 BOM 管理，约 3.3.x） | `JedisShardInfo` 在 Jedis 4+ 已删除，需重写客户端代理 |
| `base_mq` | 仅依赖 base_utils | 低风险，按需补 MQ 客户端 |
| `base_config` | spring-cloud-alibaba-nacos-config/discovery、httpclient 4.5.6 | bootstrap 机制变更、httpclient→httpclient5 |
| `base_log` | spring-cloud-sleuth-zipkin 2.2.6、log4j 1.2.17 | Sleuth 已废弃，迁 Micrometer Tracing；log4j 1.x EOL |
| `base_proxy` | spring-boot-starter-web、sleuth-zipkin | 同 base_log |
| `base_utils` | hutool 5.8.5、jjwt 0.9.1、geodesy 1.1.3 | jjwt 拆分包、hutool 升 5.8.x 最新 |
| `rubber_basic_web_boot_start` | spring-boot-starter-web、jaxb-api 2.3.1、javax.servlet.* | javax→jakarta 迁移、spring.factories 改新机制 |
| `rubber_basic_service_boot_start` | dubbo 3.0.3、dubbo-registry-nacos、sentinel | Dubbo 升级、Sentinel 升级 |

### 2.2 代码层面的兼容性风险扫描结果

通过全量源码扫描（96 个 java 文件）确认的关键改造点：

1. **`javax.*` → `jakarta.*` 迁移**
   - 需迁移（Java EE / Jakarta EE 范畴）：
     - `rubber_basic_web_boot_start/.../filter/login/` 下 4 个文件：`javax.servlet.*` → `jakarta.servlet.*`、`javax.servlet.http.*` → `jakarta.servlet.http.*`
   - **无需迁移**（属于 JDK 自身包，不随 Java EE 改名）：
     - `javax.sql.DataSource`（JDK `java.sql` 模块）
     - `javax.net.ssl.*`（JDK `java.net.ssl` 模块）
   - `javax.xml.bind`（jaxb-api）→ `jakarta.xml.bind:jakarta.xml.bind-api`

2. **自动装配机制 `spring.factories` 失效**
   Spring Boot 3.0 起移除了 `spring.factories` 中 `EnableAutoConfiguration` 的注册能力，需改为新文件：
   - `rubber_basic_web_boot_start/.../META-INF/spring.factories` → `META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports`
   - `rubber_basic_service_boot_start/.../META-INF/spring.factories` → 同上
   - `base_config/.../META-INF/spring.factories` 中 `BootstrapConfiguration` 注册 → Spring Cloud 新版需引入 `spring-cloud-starter-bootstrap` 或迁移到 `spring.config.import`

3. **Spring Cloud Sleuth → Micrometer Tracing**
   `base_log` / `base_proxy` 依赖 `spring-cloud-sleuth-zipkin:2.2.6.RELEASE`，Sleuth 在 Spring Cloud 2022 起被移除，需替换为：
   - `micrometer-tracing-bridge-brave`
   - `zipkin-reporter-brave`
   - 配置前缀由 `spring.sleuth.*` 迁移到 `management.tracing.*` / `management.zipkin.tracing.*`

4. **pom 残留未使用的 ShardingSphere 依赖**
   `pom.xml` 第 42、116–118 行仍声明 `io.shardingsphere:sharding-jdbc-core:3.1.0`，但代码中已无引用（原 `RubberShardingRuleBean.java` 已删除）。该依赖为残留死代码，升级时直接移除，无需考虑 ShardingSphere 版本兼容。代码中的分表能力由项目自研的 MyBatis 插件（`RubberShardingMybatisInterceptor`、`RubberYearMonthShardingStrategy` 等）提供，与 ShardingSphere 库无关。

5. **Jedis 客户端代理重写**
   `base_redis` 的 `JedisClientProxy` / `JedisClusterClientProxy` 使用了 `JedisShardInfo`、`HostAndPort` 等老 API。Jedis 4+ 已移除 `JedisShardInfo`（分片模式废弃）。升级后 Jedis 版本由 Boot 4 BOM 管控（5.x），需基于新 API（`JedisPooled` / `JedisCluster`）重写客户端代理。

6. **`BeanDefinitionRegistryPostProcessor` + `@ConfigurationProperties` 冲突**
   `问题笔记.md` 已记录：通过 `BeanDefinitionRegistryPostProcessor` 动态注入的 bean 无法自动获取 `@ConfigurationProperties` 绑定。Boot 4 对 Bean 生命周期与 `ConfigurationPropertiesBindingPostProcessor` 时机要求更严，需复核 `base_mysql` 动态数据源注入流程，确认绑定时机仍正确。

7. **配置加载（bootstrap）**
   `base_config` 当前依赖 Spring Cloud bootstrap 机制（`spring-cloud-context`）拉取 Nacos 配置。Spring Cloud 2022+ 默认关闭 bootstrap，需显式引入 `spring-cloud-starter-bootstrap` 或改造为 `spring.config.import=optional:nacos:...`。

8. **重复的 `maven-compiler-plugin` 声明**
   根 `pom.xml` 第 316–338 行声明了两次 `maven-compiler-plugin`，需合并，并改为 `<release>21</release>`、统一 annotationProcessorPaths。

9. **重复依赖声明**
   `base_mysql/pom.xml` 中 `mysql-connector-java`、`druid`、`mybatis-plus-generator` 各重复声明，清理。

10. **`mysql-connector-java` 改名**
    8.0.31+ 起 groupId/artifactId 变更为 `com.mysql:mysql-connector-j`，旧坐标在 Boot 4 BOM 中不再管理，且与 `com.mysql.cj.jdbc.Driver` 的版本要求一致即可保留。

---

## 三、目标版本矩阵

> 原则：Spring 系生态版本由 Boot BOM / Cloud BOM 统一管理，`<properties>` 只保留第三方版本。下表为最终目标，具体版本以 Boot 4.1.1 对应 BOM 解析为准。

| 依赖 | 现状 | 目标                                                          | 说明 |
| --- | --- |-------------------------------------------------------------| --- |
| spring-boot-starter-parent | 2.3.2.RELEASE | 4.1.1                                                       | parent 直接升级 |
| java.version | 1.8 | 21                                                          | compiler release=21 |
| lombok | 1.18.16 | 1.18.34+                                                    | 支持 JDK 21 |
| spring-cloud | （Hoxton 隐式） | 与 Boot 4.1 对应列车                                             | 由 BOM 管理 |
| spring-cloud-alibaba | 2.2.7.RELEASE | 与新 Spring Cloud 对齐                                          | Nacos config/discovery |
| mybatis-plus | 3.4.0 | 3.5.x（`mybatis-plus-spring-boot3-starter`）                  | Boot 3+ 专用 starter |
| druid | 1.2.1 | `druid-spring-boot-4-starter`（1.2.20+）                      | jakarta 适配 |
| mysql-connector | 8.0.21（mysql:mysql-connector-java） | `com.mysql:mysql-connector-j` 8.4.x/9.x                     | 坐标改名 |
| dubbo | 3.0.3 | 3.3.x                                                       | JDK 21 / Boot 4 兼容 |
| sentinel | （sca 2.2.7 带入） | 随 spring-cloud-alibaba 新版                                   | — |
| sleuth-zipkin | 2.2.6.RELEASE | `micrometer-tracing-bridge-brave` + `zipkin-reporter-brave` | Sleuth 废弃 |
| log4j | 1.2.17 | 移除，统一 logback                                               | log4j 1.x EOL |
| jedis | BOM 3.3.x | BOM 5.x（Boot 4 管理）                                          | 客户端代码重写 |
| hutool | 5.8.5 | 5.8.x 最新（6.x 视情况）                                           | 5.8 支持 JDK 8+ |
| fastjson | 2.0.11 | fastjson2 最新 2.0.x                                          | 1.x 已禁用，2.x 续用 |
| jjwt | 0.9.1 | 0.12.x（jjwt-api/impl/jackson）                               | 拆分包 |
| httpclient | 4.5.6 | httpclient5 5.x                                             | Boot 4 默认 |
| freemarker | 2.3.28 | 2.3.33+                                                     | 代码生成器 |
| jaxb-api | 2.3.1（javax） | `jakarta.xml.bind:jakarta.xml.bind-api` 4.x                 | jakarta 化 |
| maven-compiler-plugin | 3.10.0（重复声明） | 3.13+                                                       | 合并 + release=21 |

---

## 四、升级实施步骤

整体拆为 6 个阶段，建议每阶段独立提交、保证可编译。

### 阶段 0：升级前准备（基线建立）
- 在 `dev_1.2.3-alpha` 基础上拉出升级分支 `dev_1.2.3-boot4-upgrade`。
- 本地用 JDK 1.8 跑通一次全量 `mvn clean install -DskipTests`，确认升级前基线可用。
- 整理下游业务方依赖本组件的版本清单，评估对外 API/配置兼容性约束。

### 阶段 1：环境与构建链升级（JDK 21 + Maven）
1. 安装 JDK 21，配置 `JAVA_HOME`、IDE（IDEA）Project SDK 与 Maven Runner JDK 为 21。
2. 合并根 `pom.xml` 中重复的 `maven-compiler-plugin`，统一为：
   ```xml
   <plugin>
       <groupId>org.apache.maven.plugins</groupId>
       <artifactId>maven-compiler-plugin</artifactId>
       <configuration>
           <release>21</release>
           <annotationProcessorPaths>
               <path>
                   <groupId>org.projectlombok</groupId>
                   <artifactId>lombok</artifactId>
                   <version>${lombok.version}</version>
               </path>
           </annotationProcessorPaths>
       </configuration>
   </plugin>
   ```
3. `<properties>` 中 `<java.version>21</java.version>`、`<lombok.version>1.18.34</lombok.version>`、`<rubber.version>4.1.1-ALPHA</rubber.version>`。
4. 根 `pom.xml` `<version>`（第 16 行）由 `1.2.0-ALPHA` 改为 `4.1.1-ALPHA`，与 Spring Boot 主版本对齐，便于识别配套生态。
5. 同步各子模块 parent 的 `<version>`（`<parent>` 指向 `rubber_base_components`）为 `4.1.1-ALPHA`。
6. 此阶段不升级 Boot，仅验证 JDK 21 下原有 Boot 2.3 能否编译（预期：编译可通过，部分库需小版本上探）。

### 阶段 2：Spring Boot 主版本升级（BOM 切换）
1. 根 `pom.xml` parent 改为 `spring-boot-starter-parent:4.1.1`。
2. 删除 `<properties>` 中 `springboot.version`，BOM 统一管理（除非需锁定子模块的 `spring-boot-starter` 版本，统一去掉显式 version）。
3. 引入与 Boot 4.1 对应的 `spring-cloud-dependencies` BOM（`dependencyManagement` import），再 import `spring-cloud-alibaba-dependencies` 对齐版本。
4. `mvn dependency:tree` 全量核对，解决 BOM 未覆盖的第三方版本。

### 阶段 3：Jakarta 化 + 自动装配迁移（Web / 启动器模块）
1. **`javax.servlet.*` → `jakarta.servlet.*`**：批量替换 `rubber_basic_web_boot_start/.../filter/login/` 下 4 个文件。
2. **`javax.xml.bind` → `jakarta.xml.bind`**：替换 `jaxb-api` 依赖为 `jakarta.xml.bind:jakarta.xml.bind-api`，并补 `org.glassfish.jaxb:jaxb-runtime`（如需运行时）。
3. **`spring.factories` 迁移**：
   - `rubber_basic_web_boot_start`、`rubber_basic_service_boot_start` 新建：
     `META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports`，内容为对应 `@AutoConfiguration` 全限定类名（每行一个）。
   - 删除旧 `spring.factories` 中的 `EnableAutoConfiguration=` 行。
   - 为对应 Starter 类补 `@AutoConfiguration`、`@AutoConfigureBefore/After`（按需）。
4. **`base_config` bootstrap**：`spring.factories` 中 `BootstrapConfiguration` → 显式引入 `spring-cloud-starter-bootstrap`，或改造为 `spring.config.import=optional:nacos:...`（推荐后者，详见阶段 5）。

### 阶段 4：中间件组件适配（逐模块）

#### 4.1 `base_mysql`
- 引入 `mybatis-plus-spring-boot3-starter`（替换 `mybatis-plus-boot-starter`），版本 3.5.x。
- druid：改用 `druid-spring-boot-4-starter`，清理 `base_mysql/pom.xml` 重复依赖。
- `mysql-connector-java` → `com.mysql:mysql-connector-j`，driver class 仍为 `com.mysql.cj.jdbc.Driver`。
- 移除 pom 中残留未使用的 `io.shardingsphere:sharding-jdbc-core` 依赖及其 version 属性（死代码清理，无需版本迁移）。分表能力由项目自研 MyBatis 插件保留，不受影响。
- 复核 `BeanDefinitionRegistryPostProcessor` 动态注入数据源时 `@ConfigurationProperties` 绑定时机（见 2.2 第 6 项）。
- freemarker → 2.3.33+（代码生成器）。

#### 4.2 `base_redis`
- 移除显式 jedis version，由 Boot 4 BOM 管理（Jedis 5.x）。
- 重写 `JedisClientProxy`：废弃 `JedisShardInfo`，改用 `JedisPooled`（单点/连接池）。
- 重写 `JedisClusterClientProxy`：使用新版 `JedisCluster` / `ClusterJedisClient` API。
- 校验 `RedisClusterConfigProperties` 中与老 API 耦合的字段（如 ssl 配置），改用新构造方式。
- `javax.net.ssl.*` 无需改名（JDK 包）。

#### 4.3 `base_log` / `base_proxy`
- 移除 `spring-cloud-sleuth-zipkin`，改用：
  ```xml
  <dependency><groupId>io.micrometer</groupId><artifactId>micrometer-tracing-bridge-brave</artifactId></dependency>
  <dependency><groupId>io.zipkin.reporter2</groupId><artifactId>zipkin-reporter-brave</artifactId></dependency>
  ```
- 配置迁移：`spring.sleuth.*` → `management.tracing.*`、`management.zipkin.tracing.endpoint`。
- 移除 `log4j:log4j:1.2.17`（EOL），统一走 logback；druid `filters` 中 `log4j` → `log4j2` 或 `slf4j`（与实际日志实现一致）。
- `base_proxy` 的 web 依赖随 Boot 4 自带 Tomcat 10+（jakarta），无需额外处理。

#### 4.4 `base_config`
- Nacos config/discovery 跟随 spring-cloud-alibaba 新版。
- bootstrap 迁移二选一：
  - **方案 A（最小改动）**：引入 `spring-cloud-starter-bootstrap`，保留 `BootstrapConfiguration` 机制。
  - **方案 B（推荐）**：改用 `spring.config.import=optional:nacos:application.yml`，移除 bootstrap 依赖，`RubberConfiguration` 改为普通 `@AutoConfiguration`。
- httpclient 4.5.6 → httpclient5（`org.apache.httpcomponents.client5:httpclient5`），相应工具类代码改造。

#### 4.5 `base_utils`
- jjwt 0.9.1 → 0.12.x：拆分为 `jjwt-api` + `jjwt-impl` + `jjwt-jackson`，JWT 构造/解析 API 改为 `Jwts.builder()` 链式 + `Keys.hmacShaKeyFor`，重写工具类。
- hutool 5.8.5 → 5.8.x 最新（如需可上 hutool 6.x，但需评估 JDK 17+ 兼容）。
- geodesy 1.1.3 评估是否仍需要，无强依赖则保留。

#### 4.6 `rubber_basic_service_boot_start`
- dubbo 3.0.3 → 3.3.x，`dubbo-spring-boot-starter` 与 `dubbo-registry-nacos` 同步升级。
- sentinel 跟随 spring-cloud-alibaba 新版；自动装配按阶段 3 迁移。

### 阶段 5：配置与启动验证
1. 全量 `application.yml` / `bootstrap.yml` 核对：
   - `spring.sleuth.*` → `management.tracing.*`
   - druid `filters: stat,wall,log4j` → `stat,wall,log4j2`（或 `slf4j`）
   - nacos 配置地址、`spring.config.import` 写法
2. 启动一个最小 web 服务（依赖 `rubber_basic_web_boot_start`）与最小 service（依赖 `rubber_basic_service_boot_start`），验证自动装配生效、Nacos 配置拉取、数据源/redis/链路追踪可用。
3. 补/修单元测试（`spring-boot-starter-test` 随 Boot 4 升级，JUnit 5 已是默认）。

### 阶段 6：回归与优化
1. 全量 `mvn clean install` 通过。
2. 评估 JDK 21 新特性引入：虚拟线程（`spring.threads.virtual.enabled=true`）、Records 改造 DTO 等，作为后续可选优化。
3. 更新 `README.md` 中 DB 数据源示例（druid filter、驱动坐标说明）。
4. 整理本次升级的破坏性变更清单，同步下游业务方。

---

## 五、风险与对策

| 风险 | 影响 | 对策 |
| --- | --- | --- |
| Jedis 5.x 移除 `JedisShardInfo`，`base_redis` 客户端代理需重写 | 高 | 阶段 4.2 基于 `JedisPooled`/`JedisCluster` 重写，保持 `RedisClientProxy` 对外接口不变 |
| 下游业务方依赖本组件配置 key / DTO 结构 | 中 | 保持 `rubber.proxy.*`、`RubberDbProperties` 等对外结构不变；变更点列入升级通告 |
| 组件版本号由 1.2.0-ALPHA 变 4.1.1-ALPHA，下游业务方引用坐标需联动升级 | 中 | 提前通知下游业务方同步修改 `rubber` 组件依赖版本；旧版本保留一段时间便于灰度切换 |
| spring-cloud-alibaba 与 Boot 4.1 对齐版本可能滞后 | 中 | 若官方对齐版本未出，评估用社区最新 snapshot 或临时锁定可用组合；Nacos 客户端可单独锁版本 |
| `BeanDefinitionRegistryPostProcessor` 绑定时机在 Boot 4 收紧 | 中 | 阶段 4.1 重点验证动态数据源 `@ConfigurationProperties` 绑定；必要时改为 `EnvironmentPostProcessor` 或显式 `Binder` |
| Dubbo 3.3 与 Nacos/Sentinel 版本耦合 | 中 | 阶段 4.6 统一 Dubbo + Nacos client + Sentinel 版本，避免冲突 |
| log4j 1.x 移除影响 druid `log4j` filter | 低 | druid filter 改 `log4j2`/`slf4j`，与 logback 协同 |
| 重复 plugin/依赖声明导致构建歧义 | 低 | 阶段 1 清理 |

---

## 六、验收标准

1. `mvn clean install -DskipTests` 在 JDK 21 下全模块通过。
2. `mvn test` 现有单测全部通过（无 skipped 因兼容性问题）。
3. 启动 web/service 两个最小样例，自动装配、Nacos 配置、MySQL 数据源、Redis、链路追踪（Zipkin）均正常。
4. `mvn dependency:tree` 不存在 `javax.servlet`、`spring-cloud-sleuth`、`log4j:log4j`、`io.shardingsphere`、`jjwt` 0.9.x 等旧依赖。
5. 对外 `rubber.*` 配置 key 与核心 Properties 结构保持兼容，破坏性变更已记录并同步下游。

---

## 七、里程碑与排期（建议）

| 阶段 | 工作量估算 | 里程碑 |
| --- | --- | --- |
| 阶段 0 准备 | 0.5d | 基线可用、升级分支建立 |
| 阶段 1 JDK21 + 构建链 | 1d | JDK 21 下原工程编译通过 |
| 阶段 2 Boot BOM 切换 | 1d | 依赖树收敛、可编译（暂留红） |
| 阶段 3 Jakarta + 自动装配 | 1.5d | web/service starter 自动装配生效 |
| 阶段 4 中间件适配 | 4–5d | 各 base_xxx 模块编译通过（重点 base_mysql/base_redis） |
| 阶段 5 配置与启动验证 | 1.5d | 样例启动、链路追踪可用 |
| 阶段 6 回归与优化 | 1d | 全量构建通过、文档更新 |
| **合计** | **10–11d** | **升级完成** |

---

## 八、变更清单（Chekclist）

- [x] 根 `pom.xml`：parent 升 4.1.1、**组件 version 升 4.1.1-ALPHA（含 `<rubber.version>`）**、合并 compiler plugin、release=21、清理 properties
- [x] 各子模块 `<parent>` version 同步为 `4.1.1-ALPHA`
- [x] `rubber_basic_web_boot_start`：javax→jakarta、`AutoConfiguration.imports` 迁移、`@ServletComponentScan` 包路径更新
- [x] `rubber_basic_service_boot_start`：`AutoConfiguration.imports` 迁移、dubbo/sentinel 升级
- [x] `base_config`：bootstrap 机制保留（引入 spring-cloud-starter-bootstrap）、移除未使用 httpclient、Nacos 升级
- [x] `base_log` / `base_proxy`：Sleuth→Micrometer Tracing、移除 log4j 1.x
- [x] `base_mysql`：mybatis-plus starter3、druid3-starter、mysql-connector-j、移除残留 `io.shardingsphere` 死依赖、清理重复依赖、FastAutoGenerator 重写生成器、Page→OrderItem 分页
- [x] `base_redis`：Jedis 7.x 客户端代理改造（移除 JedisShardInfo / JedisClusterHostAndPortMap，SINGLE 走 JedisClientConfig，接口返回类型对齐基本类型）
- [x] `base_utils`：jjwt 0.12.x 拆分重写、fastjson→fastjson2
- [x] `base_mq`：无 MQ 客户端依赖，保持不变
- [x] 配置文件：README druid filters log4j→slf4j；sleuth→tracing 由依赖切换自动生效
- [x] README / 问题笔记 同步更新
- [x] `mvn clean install -DskipTests` 全模块通过、dependency:tree 无遗留旧依赖（log4j 1.x / javax.servlet / io.shardingsphere / sleuth / jjwt 0.9 / fastjson 1.x / mysql-connector-java / mybatis-plus-boot-starter）

> 实施说明：fastjson 1.2.83_noneautotype 仍作为 `sentinel-transport-simple-http` 的传递依赖存在（autoType 已禁用的安全变体），属上游客户端依赖，本工程代码已全部迁移至 fastjson2。

