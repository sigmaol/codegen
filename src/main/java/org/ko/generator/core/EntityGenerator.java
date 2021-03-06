package org.ko.generator.core;

import org.ko.generator.entity.Column;
import freemarker.template.Template;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * description: EntityGenerator <br>
 * date: 2020/5/31 10:47 <br>
 *
 * @author K.O <br>
 * @version 1.0 <br>
 */
@Component
public class EntityGenerator extends AbstractGenerator {

    private static final String ENTITY_PACKAGE = "/entity/";
    private static final String CONSTANTS_PACKAGE = "/constants/";

    @Override
    public void executor(String... names) throws Exception {
        for (String name : names) {
            //全部字段
            List<Column> columns = findColumnByTableName(name);
            //表名字
            String entityName = reformatTable(name, properties.getPrefix());
            //表注释
            String comment = findTableComment(name);

            String dir = new File(this.getClass().getClassLoader().getResource(".").toURI()).getAbsolutePath();
            int index = dir.indexOf("target");
            String moduleRoot = new File(dir.substring(0, index)).getParent();

            String javaDir = moduleRoot + "/" + properties.getEntity().getModule() +
                    ROOT_DIR + properties.getEntity().getRootPackage().replaceAll("\\.", "/");
            String entityFileName = javaDir + ENTITY_PACKAGE + entityName + ".java";

            Map<String, Object> params = new HashMap<>();

            params.put("name", name);
            params.put("entityName", entityName);
            params.put("comment", comment);
            params.put("columns", columns);
            params.put("rootPackage", properties.getEntity().getRootPackage());

            if (StringUtils.isNotEmpty(entityFileName)) {
                Template template = freemarker.getTemplate(properties.getEntity().getEntityTemplate());
                OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream(
                        new File(entityFileName)), StandardCharsets.UTF_8);
                template.process(params, out);
                out.close();
            }

            String constantsFileName = javaDir + CONSTANTS_PACKAGE + entityName + "Constants.java";

            if (StringUtils.isNotEmpty(constantsFileName)) {
                Template template = freemarker.getTemplate(properties.getEntity().getConstantsTemplate());
                OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream(
                        new File(constantsFileName)), StandardCharsets.UTF_8);
                template.process(params, out);
                out.close();
            }
        }
    }
}
