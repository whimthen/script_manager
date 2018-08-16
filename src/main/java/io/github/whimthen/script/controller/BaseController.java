package io.github.whimthen.script.controller;

import com.blade.Environment;
import com.blade.mvc.annotation.GetRoute;
import com.blade.mvc.annotation.JSON;
import com.blade.mvc.annotation.Path;
import com.blade.mvc.ui.RestResponse;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

/**
 * @project: script_manager
 * @created: with IDEA
 * @author: nzlong
 * @Date: 2018 08 16 上午10:0341 | 八月. 星期四
 */
@Slf4j
@Path(value = "base")
public class BaseController {

    @GetRoute(value = "getProp")
    @JSON
    public RestResponse getProp() {
        try {
            Environment environment = Environment.of("app.properties");
            Map propMap = new HashMap();
            propMap.put("host", environment.get("server.host").get());
            propMap.put("port", environment.get("server.port").get());
            return RestResponse.ok(propMap);
        } catch (Exception ex) {
            log.error("=====>>>>> get properties fail", ex);
            return RestResponse.fail("get properties fail");
        }
    }

}
