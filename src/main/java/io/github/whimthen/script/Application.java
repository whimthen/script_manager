package io.github.whimthen.script;

import com.blade.Blade;
import io.github.whimthen.script.handler.ServerHandler;

/**
 * @project: script_manager
 * @created: with IDEA
 * @author: nzlong
 * @Date: 2018 08 03 下午4:3937 | 八月. 星期五
 */
public class Application {

    public static void main(String[] args) {
        Blade.of()
             .webSocket("/server/connect", new ServerHandler())
             .start(Application.class, args);
    }

}
