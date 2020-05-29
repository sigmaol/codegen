package ${rootPackage}.constants;

import com.luxshare.sigma.data.bean.BasicColumnConstants;

public class ${entityName}Constants {

    public static class Columns extends BasicColumnConstants {
    <#list columns as column>

        /**
         * ${column.comment}
         */
        public static String ${column.columnName?upper_case} = "${column.columnName}";
    </#list>
    }

}