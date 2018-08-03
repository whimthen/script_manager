package io.github.whimthen.script.controller;

import com.blade.mvc.annotation.Path;
import com.blade.mvc.annotation.Route;

/**
 * @project: script_manager
 * @created: with IDEA
 * @author: nzlong
 * @Date: 2018 08 03 下午4:3506 | 八月. 星期五
 */
@Path
public class PageController {

    @Route
    public String index() {
        return "index.html";
    }

}
