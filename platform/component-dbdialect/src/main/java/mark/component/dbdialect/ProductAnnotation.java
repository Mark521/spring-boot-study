package mark.component.dbdialect;

/**
 * 类名称： Dialect
 * 描述：
 * 
 * 创建：   liang, 2013-3-22 9:37:44
 *
 * 修订历史：（降序）
 * BugID/BacklogID  -  开发人员  日期
 * #  
 */

import mark.component.dbdialect.constans.DatabaseProduction;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(value = RetentionPolicy.RUNTIME)
@Target(value = {ElementType.TYPE})
public @interface ProductAnnotation {

    public String productName() default "";

    public int majorVersion() default 0;

    public int minorVersion() default 0;
    
    public DatabaseProduction databaseProduction() default DatabaseProduction.Unknown;
}