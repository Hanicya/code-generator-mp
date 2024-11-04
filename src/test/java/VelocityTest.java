import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.junit.Test;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;

/**
 * @author HuangYiCheng
 * @since 2024/11/1
 */
public class VelocityTest {

    @Test
    public void test() throws IOException {
        //1.设置velocity的资源加载类
        Properties prop = new Properties();
        prop.put("file.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
        //2.加载velocity引擎
        Velocity.init(prop);
        //3.加载velocity容器
        VelocityContext velocityContext = new VelocityContext();
        // 最重要一步，设置模板中填充的数据
        velocityContext.put("fieldType", "String");
        velocityContext.put("fieldName", "studentName");
        //4.加载velocity模板
        Template template = Velocity.getTemplate("template/test.vm", "utf-8");
        //5.合并数据
        FileWriter fileWriter = new FileWriter("src/main/java/MyTest.java");
        template.merge(velocityContext, fileWriter);
        //6.释放资源
        fileWriter.close();
    }


}
