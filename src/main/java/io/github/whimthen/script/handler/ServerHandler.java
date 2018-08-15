package io.github.whimthen.script.handler;

import com.blade.kit.JsonKit;
import com.blade.kit.StringKit;
import com.blade.mvc.handler.WebSocketHandler;
import com.blade.mvc.websocket.WebSocketContext;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import io.github.whimthen.script.cache.SessionManager;
import io.github.whimthen.script.constant.GlobalConstant;
import io.github.whimthen.script.entity.SSHInfo;
import io.github.whimthen.script.enums.SysCode;
import io.github.whimthen.script.exception.ScriptException;
import io.github.whimthen.script.service.ServerService;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Map;

/**
 * @project: script_manager
 * @created: with IDEA
 * @author: nzlong
 * @Date: 2018 08 15 上午10:2654 | 八月. 星期三
 */
@Slf4j
public class ServerHandler implements WebSocketHandler {

    private Channel channel;

    @Override
    public void onConnect(WebSocketContext webSocketContext) {
        try {
            String serverId = webSocketContext.getReqText();
            if (StringKit.isNotBlank(serverId)) {
                SSHInfo sshInfo = ServerService.getSIById(serverId);
                if (sshInfo != null) {
                    Session session = SessionManager.put(sshInfo);
                    channel = session.openChannel(GlobalConstant.JSCH_CHANNEL_TYPE_SHELL);
                    execCommand("");
                }
            }
        } catch (Exception ex) {
            log.error("=====>>>>> Connect Fail", ex);
        }
    }

    @Override
    public void onText(WebSocketContext webSocketContext) {
        try {
            Map map = JsonKit.formJson(webSocketContext.getReqText(), Map.class);
            if (channel != null && channel.isConnected()) {
                channel.disconnect();
            }
            if (map.get("serverId") == null) {
                throw new ScriptException(SysCode.PARAM_MISSING);
            }
            channel = SessionManager.getSession(ServerService.getSIById(map.get("serverId").toString())).openChannel(GlobalConstant.JSCH_CHANNEL_TYPE_SHELL);
            execCommand(map.get("command") + "\n");
        } catch (Exception ex) {
            log.error("=====>>>>> Command processing failed", ex);
        }
    }

    @Override
    public void onDisConnect(WebSocketContext webSocketContext) {

    }

    public void execCommand(String command) throws FileNotFoundException, JSchException {
        ByteArrayInputStream in = new ByteArrayInputStream(command.getBytes());
        System.setIn(in);
        channel.setInputStream(System.in);
        String filePath = ServerHandler.class.getClassLoader().getResource("").getPath() + GlobalConstant.SERVER_LOG;
        File file = new File(filePath);
        FileOutputStream fileOut = new FileOutputStream(file, false);
        channel.setOutputStream(fileOut);
        channel.connect(3 * 1000);
    }

}
