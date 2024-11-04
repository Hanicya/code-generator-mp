package com.cbz;

import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author HuangYiCheng
 * @since 2024/11/1
 */
public class CodeGeneration {

    // 生成
    public static void main(String[] args) {
        generation("user_info");
    }

    // 作者
    private static final String AUTHOR = "HuangYiCheng";
    // 数据库连接地址
    private static final String DB_URL = "jdbc:mysql://119.23.74.163:3306/test";
    private static final String DB_USER = "root";
    private static final String DB_SECRET = "hcxtec888";

    // 父包名
    private static final String PARENT = "com.cbz";

    // 不同文件的映射路径(xml默认在resources)
    private static final String ENTITY = "entity";
    private static final String MAPPER = "mapper";
    private static final String SERVICE = "service";
    private static final String IMPL = "service.impl";
    private static final String CONTROLLER = "controller";


    // Controller映射路径前缀
    private static final String MAPPING_PREFIX = "user/xxx";

    /**
     * 根据表名生成相应结构代码
     *
     * @param tableName 表名
     */
    public static void generation(String tableName) {
        FastAutoGenerator.create(DB_URL, DB_USER, DB_SECRET)

                // 1、全局配置
                .globalConfig(builder ->
                        builder
                                // 作者名称
                                .author(AUTHOR)
                                .disableOpenDir() // 禁止打开输出目录（否则一直弹窗打开文件夹）
                                .dateType(DateType.ONLY_DATE) // 设置时间类型（java.util.date)
                                .fileOverride() // 覆盖已有文件（默认 false）
                                // 指定输出目录
                                .outputDir(System.getProperty("user.dir") + "/src/main/java")
                )
                // 2、包配置(生成位置)
                .packageConfig(builder ->
                        builder.parent(PARENT)  // 父包名。如果为空，将下面子包名必须写全部，否则就只需写子包名
                                .entity(ENTITY)  // 实体类包名
                                .mapper(MAPPER)// mapper层包名
                                .service(SERVICE)// service层包名
                                .serviceImpl(IMPL)// service实现类包名
                                .controller(CONTROLLER)// 控制层包名
                                // .other("dto") // 生成dto目录
                                // 自定义mapper.xml文件输出目录
                                .pathInfo(Collections.singletonMap(OutputFile.mapperXml,
                                        System.getProperty("user.dir") + "/src/main/resources/mapper"))
                )

                // 3、策略配置
                .strategyConfig(builder -> {
                    // 设置要生成的表名
                    builder.addInclude(tableName)
                            // 3.1 实体配置
                            .entityBuilder()
                            // .superClass(BaseEntity.class) // 设置实体类父类-父类中存在的字段不会在实体类中存在
                            .enableLombok()
                            .naming(NamingStrategy.underline_to_camel) // 数据表映射实体命名策略：默认下划线转驼峰
                            .columnNaming(NamingStrategy.underline_to_camel)// 表字段映射实体属性命名规则：默认null，不指定按照naming执行
                            //.idType(IdType.AUTO) // 添加全局主键类型
                            .formatFileName("%s") // 格式化实体名称，%s取消首字母I,
                            // 3.2 mapper配置
                            .mapperBuilder()
                            .enableMapperAnnotation() // 开启mapper注解
                            .enableBaseResultMap() // 启用xml文件中的BaseResultMap 生成
                            .enableBaseColumnList() // 启用xml文件中的BaseColumnList
                            .formatMapperFileName("%sMapper") // 格式化Dao类名称
                            .formatXmlFileName("%sMapper") // 格式化xml文件名称
                            // 3.3 service配置
                            .serviceBuilder()
                            .formatServiceFileName("%sService")// 格式化 service 接口文件名称
                            .formatServiceImplFileName("%sServiceImpl")// 格式化 service 接口文件名称
                            // 3.4 controller配置
                            .controllerBuilder()
                            .enableRestStyle();
                })
                // 4、自定义模板配置
                .templateConfig(builder -> {
                    builder.entity("/template/entity.java.vm");
                    builder.mapper("/template/mapper.java.vm");
                    builder.mapperXml("/template/mapper.xml.vm");
                    builder.service("/template/service.java");
                    builder.serviceImpl("/template/serviceImpl.java");
                    builder.controller("/template/controller.java");
                })
                // 5、注入配置
                .injectionConfig(consumer -> {
//                    // 自定义模板，如dto
//                    Map<String, String> customFile = new HashMap<>();
//                    customFile.put("Save" + CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, tableName) + "Req.java", "/templates/saveReq.java.vm");
//                    customFile.put("Update" + CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, tableName) + "Req.java", "/templates/updateReq.java.vm");
//                    customFile.put("Select" + CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, tableName) + "Req.java", "/templates/selectReq.java.vm");
//                    customFile.put("Select" + CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, tableName) + "Resp.java", "/templates/selectResp.java.vm");
//                    consumer.customFile(customFile);
                    // 自定义变量，可以在模板中使用
                    Map<String, Object> customMap = new HashMap<>();
                    customMap.put("mappingPrefix", MAPPING_PREFIX);  // Controller映射路径前缀
                    consumer.customMap(customMap);
                })
                // 6、模板引擎设置
                .templateEngine(new TimerVelocityTemplateEngine())
                .execute();
    }
}

