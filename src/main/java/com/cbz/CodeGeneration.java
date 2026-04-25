package com.cbz;

import cn.hutool.core.util.StrUtil;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.config.builder.CustomFile;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.VelocityTemplateEngine;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author HuangYiCheng
 * @since 2024/11/1
 */
public class CodeGeneration {

    // 数据库连接地址
    private static final String DB_URL = "jdbc:mysql://127.0.0.7:3306/demo";
    private static final String DB_USER = "root";
    private static final String DB_SECRET = "123456";

    // 生成
    public static void main(String[] args) {
        generation("django_demo");
    }

    // 作者
    private static final String AUTHOR = "HuangYiCheng";

    // 后端项目地址
    private static final String BACK_PATH = "F:\\Code\\ruoyi-vue-pro\\yudao-module-system";


    // Java项目路径
    private static final String JAVA_PATH = "\\src\\main\\java\\";

    // java版本前缀
    private static final String JAVA_PREFIX = "javax";


    // 新包名
    private static final String PACKAGE_COMMON = "test";

    // 实体包名
    private static final String PACKAGE_ENTITY = "cn.iocoder.yudao.module.system.dal.dataobject.";

    // 持久包名
    private static final String PACKAGE_MAPPER = "cn.iocoder.yudao.module.system.dal.mysql.";

    // 服务包名
    private static final String PACKAGE_SERVICE = "cn.iocoder.yudao.module.system.service.";

    // 控制层包名
    private static final String PACKAGE_CONTROLLER = "cn.iocoder.yudao.module.system.controller.admin.";

    // VO包名
    private static final String PACKAGE_VO = "cn.iocoder.yudao.module.system.controller.admin." + PACKAGE_COMMON + ".vo";


    // 输出文件路径
    private static final Map<OutputFile, String> fileUrlMap = new HashMap<OutputFile, String>() {{
        put(OutputFile.entity, BACK_PATH + JAVA_PATH + PACKAGE_ENTITY.replace(".", File.separator) + PACKAGE_COMMON);
        put(OutputFile.mapper, BACK_PATH + JAVA_PATH + PACKAGE_MAPPER.replace(".", File.separator) + PACKAGE_COMMON);
        put(OutputFile.service, BACK_PATH + JAVA_PATH + PACKAGE_SERVICE.replace(".", File.separator) + PACKAGE_COMMON);
        put(OutputFile.serviceImpl, BACK_PATH + JAVA_PATH + PACKAGE_SERVICE.replace(".", File.separator) + PACKAGE_COMMON);
        put(OutputFile.controller, BACK_PATH + JAVA_PATH + PACKAGE_CONTROLLER.replace(".", File.separator) + PACKAGE_COMMON);
    }};

    // vo输出路径
    private static final String VO_FILE_PATH = BACK_PATH + JAVA_PATH + PACKAGE_VO.replace(".", File.separator);


    // sql输出路径
    private static final String SQL_FILE_PATH = "F:\\Code\\code-generator-mp\\src\\main\\java\\com\\cbz";


    /**
     * 根据表名生成相应结构代码
     *
     * @param tableName 表名
     */
    public static void generation(String tableName) {
        // controller 路径和权限名
        String pathAndPermission = "demo";
        // 业务前导名
        String schemaName = "";
        FastAutoGenerator.create(DB_URL, DB_USER, DB_SECRET)

                // 1、全局配置
                .globalConfig(builder ->
                        builder
                                // 作者名称
                                .author(AUTHOR)
                                .disableOpenDir() // 禁止打开输出目录（否则一直弹窗打开文件夹）
                                .dateType(DateType.TIME_PACK) // 设置时间类型
                                .outputDir("") // 不能为空，否则报错
                )
                // 2、包配置
                .packageConfig(builder ->
                                builder
                                        .parent("")  // 父包名。如果为空，将下面子包名必须写全部，否则就只需写子包名
                                        .entity(PACKAGE_ENTITY + PACKAGE_COMMON)  // 实体类包名
                                        .mapper(PACKAGE_MAPPER + PACKAGE_COMMON)// mapper层包名
                                        .service(PACKAGE_SERVICE + PACKAGE_COMMON)// service层包名
                                        .serviceImpl(PACKAGE_SERVICE + PACKAGE_COMMON)// service实现类包名
                                        .controller(PACKAGE_CONTROLLER + PACKAGE_COMMON)// 控制层包名
                                        // 自定义输出目录
                                        .pathInfo(fileUrlMap)
                )

                // 3、策略配置
                .strategyConfig(builder -> {
                    // 设置要生成的表名
                    builder.addInclude(tableName)
                            // 3.1 实体配置
                            .entityBuilder()
                            .javaTemplate("template/back/entity.java.vm")
                            .superClass(BaseDO.class) // 设置实体类父类-父类中存在的字段不会在实体类中存在
                            .disableSerialVersionUID() // 禁用序列化
                            .naming(NamingStrategy.underline_to_camel) // 数据表映射实体命名策略：默认下划线转驼峰
                            .columnNaming(NamingStrategy.underline_to_camel)// 表字段映射实体属性命名规则：默认null，不指定按照naming执行
                            .formatFileName("%sDO") // 格式化实体名称,
                            // 3.2 mapper配置
                            .mapperBuilder()
                            .mapperTemplate("template/back/mapper.java.vm")
//                            .enableBaseResultMap() // 启用xml文件中的BaseResultMap 生成
//                            .enableBaseColumnList() // 启用xml文件中的BaseColumnList
                            .formatMapperFileName("%sMapper") // 格式化Dao类名称
                            // 3.3 service配置
                            .serviceBuilder()
                            .serviceTemplate("template/back/service.java.vm")
                            .serviceImplTemplate("template/back/serviceImpl.java.vm")
                            .formatServiceFileName("%sService")// 格式化 service 接口文件名称
                            .formatServiceImplFileName("%sServiceImpl")// 格式化 service 接口文件名称
                            // 3.4 controller配置
                            .controllerBuilder()
                            .template("template/back/controller.java.vm")
                            .formatFileName("%sController")
                            .enableRestStyle();
                })
                // 4、注入配置
                .injectionConfig(injectConfig -> {
                    String camelCaseName = StrUtil.toCamelCase(tableName);
                    String businessName = StrUtil.upperFirst(camelCaseName);
                    // 预处理变量
                    injectConfig.beforeOutputFile((tableInfo, objectMap) -> {
                        // 读取表注释并去掉末尾“表”字，例如“用户表” -> “用户”
                        String tableComment = StrUtil.blankToDefault(tableInfo.getComment(), businessName);
                        String businessComment = StrUtil.removeSuffix(tableComment.trim(), "表");
                        objectMap.put("tableComment", tableComment);
                        objectMap.put("businessComment", businessComment);
                    });


                    // 自定义变量，可以在模板中使用
                    String businessNameUpper = camelCaseName.toUpperCase();
                    Map<String, Object> customMap = new HashMap<>();
                    customMap.put("javaPrefix", JAVA_PREFIX);
                    customMap.put("businessName", businessName);
                    customMap.put("businessNameUpper", businessNameUpper);
                    customMap.put("packageVo", PACKAGE_VO);
                    customMap.put("pathAndPermission", pathAndPermission);
                    customMap.put("schemaName", schemaName);


                    injectConfig.customMap(customMap); //注入自定义属性



                    // 自定义模板，VO和前端文件
                    List<CustomFile> customFileList = new ArrayList<>();
                    // 分页请求对象
                    customFileList.add(new CustomFile.Builder()
                            .templatePath("/template/back/PageReqVO.java.vm")
                            .formatNameFunction(tableInfo -> businessName + "PageReqVO.java") // 自定义文件名
                            .fileName("")
                            .filePath(VO_FILE_PATH)
                            .build());
                    // 分页响应对象
                    customFileList.add(new CustomFile.Builder()
                            .templatePath("/template/back/PageRespVO.java.vm")
                            .formatNameFunction(tableInfo -> businessName + "PageRespVO.java") // 自定义文件名
                            .fileName("")
                            .filePath(VO_FILE_PATH)
                            .build());
                    // 详情响应对象
                    customFileList.add(new CustomFile.Builder()
                            .templatePath("/template/back/RespVO.java.vm")
                            .formatNameFunction(tableInfo -> businessName + "RespVO.java") // 自定义文件名
                            .fileName("")
                            .filePath(VO_FILE_PATH)
                            .build());
                    // 保存请求对象
                    customFileList.add(new CustomFile.Builder()
                            .templatePath("/template/back/SaveReqVO.java.vm")
                            .formatNameFunction(tableInfo -> businessName + "SaveReqVO.java") // 自定义文件名
                            .fileName("")
                            .filePath(VO_FILE_PATH)
                            .build());
                    // 菜单sql
                    customFileList.add(new CustomFile.Builder()
                            .templatePath("/template/sql/menu.sql.vm")
                            .formatNameFunction(tableInfo -> "menu.sql") // 自定义文件名
                            .fileName("")
                            .filePath(SQL_FILE_PATH)
                            .build());
                    injectConfig.customFile(customFileList);

                })
                // 6、模板引擎设置
                .templateEngine(new VelocityTemplateEngine())
                .execute();
    }
}

