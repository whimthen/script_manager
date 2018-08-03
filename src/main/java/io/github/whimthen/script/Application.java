package io.github.whimthen.script;

import com.blade.Blade;

/**
 * @project: script_manager
 * @created: with IDEA
 * @author: nzlong
 * @Date: 2018 08 03 下午4:3937 | 八月. 星期五
 */
public class Application {

    public static void main(String[] args) {
        Blade.of()
             .start(Application.class, args);
    }

}
