package io.github.whimthen.script.utils;

import com.blade.kit.CollectionKit;
import com.blade.kit.StringKit;

import java.util.stream.Stream;

/**
 * @project: script_manager
 * @created: with IDEA
 * @author: nzlong
 * @Date: 2018 08 08 下午6:1432 | 八月. 星期三
 */
public class GlobalUtils {

    public static boolean checkSqlCount(int count) {
        return count > 0 ? false : true;
    }

    public static boolean isAnyBlank(String... values) {
        if (CollectionKit.isEmpty(values)) {
            return true;
        } else if (Stream.of(values).filter(value -> value.equals("null")).count() > 0) {
            return false;
        } else {
            return Stream.of(values).filter(StringKit::isBlank).count() == (long) values.length;
        }
    }

}
