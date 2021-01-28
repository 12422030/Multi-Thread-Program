package tf.epccfe.sys;

import java.util.Properties;

import org.apache.commons.lang.StringUtils;

public class CfgParmValue {

    public static String getValue(Properties prop, String parmName) {
        
        String parmValue = prop.getProperty(parmName);
        
        if (StringUtils.isEmpty(parmValue)) {
            return "";
        }

        int idx = parmValue.indexOf('#');
        if (idx >= 0) {
            parmValue = parmValue.substring(0, idx).trim();
        } else {
            parmValue = parmValue.trim();
        }
        return parmValue;
    }
}
