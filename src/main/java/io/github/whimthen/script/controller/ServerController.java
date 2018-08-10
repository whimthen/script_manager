package io.github.whimthen.script.controller;

import com.blade.ioc.annotation.Inject;
import com.blade.mvc.annotation.BodyParam;
import com.blade.mvc.annotation.GetRoute;
import com.blade.mvc.annotation.JSON;
import com.blade.mvc.annotation.Param;
import com.blade.mvc.annotation.Path;
import com.blade.mvc.annotation.PostRoute;
import com.blade.mvc.ui.RestResponse;
import io.github.whimthen.script.entity.BaseEntity;
import io.github.whimthen.script.entity.Connect;
import io.github.whimthen.script.entity.ConnectRes;
import io.github.whimthen.script.entity.Page;
import io.github.whimthen.script.enums.SysCode;
import io.github.whimthen.script.exception.ScriptException;
import io.github.whimthen.script.service.ServerService;
import lombok.extern.slf4j.Slf4j;

/**
 * @project: script_manager
 * @created: with IDEA
 * @author: nzlong
 * @Date: 2018 08 08 下午5:5614 | 八月. 星期三
 */
@Path(value = "/serverController")
@Slf4j
public class ServerController {

    @Inject
    private ServerService serverService;

    @PostRoute(value = "addServer")
    @JSON
    public RestResponse addServer(@BodyParam(required = true) Connect connect) {
        try {
            serverService.addServer(connect);
            return RestResponse.ok();
        } catch (ScriptException ex) {
            log.error("addServer failed ===>>>", ex);
            return RestResponse.fail(ex.getCode(), ex.getMessage());
        } catch (Exception ex) {
            log.error("addServer failed ===>>>", ex);
            return RestResponse.fail(SysCode.SYSTEM_ERROR.getMessage());
        }
    }

    @GetRoute(value = "getByPage")
    @JSON
    public RestResponse<Page<ConnectRes>> getByPage(@Param BaseEntity baseEntity) {
        try {
            Page page = new Page(baseEntity);
            serverService.getByPage(page);
            return RestResponse.ok(page);
        } catch (Exception ex) {
            log.error("getByPage failed ===>>>", ex);
            return RestResponse.fail(SysCode.SYSTEM_ERROR.getMessage());
        }
    }

    @PostRoute(value = "deleteServers")
    @JSON
    public RestResponse deleteServers(@BodyParam String serverIds) {
        try {
            serverService.deleteByIds(serverIds);
            return RestResponse.ok();
        } catch (ScriptException ex) {
            log.error("deleteServers failed ===>>>", ex);
            return RestResponse.fail(ex.getCode(), ex.getMessage());
        } catch (Exception ex) {
            log.error("deleteServers failed ===>>>", ex);
            return RestResponse.fail(SysCode.SYSTEM_ERROR.getMessage());
        }
    }

    @PostRoute(value = "deleteById")
    @JSON
    public RestResponse deleteById(@Param String serverId) {
        try {
            serverService.deleteByIds(serverId);
            return RestResponse.ok();
        } catch (ScriptException ex) {
            log.error("deleteServers failed ===>>>", ex);
            return RestResponse.fail(ex.getCode(), ex.getMessage());
        } catch (Exception ex) {
            log.error("deleteServers failed ===>>>", ex);
            return RestResponse.fail(SysCode.SYSTEM_ERROR.getMessage());
        }
    }

    @GetRoute(value = "getById")
    @JSON
    public RestResponse getById(@Param String id) {
        try {
            Connect con = serverService.getById(id);
            return RestResponse.ok(con);
        } catch (ScriptException ex) {
            log.error("deleteServers failed ===>>>", ex);
            return RestResponse.fail(ex.getCode(), ex.getMessage());
        } catch (Exception ex) {
            log.error("deleteServers failed ===>>>", ex);
            return RestResponse.fail(SysCode.SYSTEM_ERROR.getMessage());
        }
    }

    @PostRoute(value = "editServer")
    @JSON
    public RestResponse editServer(@BodyParam Connect connect) {
        try {
            serverService.editServer(connect);
            return RestResponse.ok();
        } catch (ScriptException ex) {
            log.error("addServer failed ===>>>", ex);
            return RestResponse.fail(ex.getCode(), ex.getMessage());
        } catch (Exception ex) {
            log.error("addServer failed ===>>>", ex);
            return RestResponse.fail(SysCode.SYSTEM_ERROR.getMessage());
        }
    }

}
