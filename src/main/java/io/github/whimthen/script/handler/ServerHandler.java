package io.github.whimthen.script.handler;

import com.blade.kit.JsonKit;
import com.blade.kit.StringKit;
import com.blade.mvc.handler.WebSocketHandler;
import com.blade.mvc.websocket.WebSocketContext;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.JSchException;
import io.github.whimthen.script.cache.SessionManager;
import io.github.whimthen.script.constant.GlobalConstant;
import io.github.whimthen.script.entity.SSHInfo;
import io.github.whimthen.script.enums.SysCode;
import io.github.whimthen.script.exception.ScriptException;
import io.github.whimthen.script.service.ServerService;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
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

    private String filePath = ServerHandler.class.getClassLoader().getResource("").getPath() + GlobalConstant.SERVER_LOG;

    @Override
    public void onConnect(WebSocketContext webSocketContext) {

    }

    @Override
    public void onText(WebSocketContext webSocketContext) {
        Map map = JsonKit.formJson(webSocketContext.getReqText(), Map.class);
        String serverId = map.get("serverId").toString();
        try {
            if (StringKit.isBlank(serverId)) {
                throw new ScriptException(SysCode.PARAM_MISSING);
            }
            if (channel != null && channel.isConnected()) {
                channel.disconnect();
            }
            SSHInfo sshInfo = ServerService.getSIById(serverId);
            if ((channel == null || !channel.isConnected()) && sshInfo != null) {
                channel = SessionManager.getSession(sshInfo).openChannel(GlobalConstant.JSCH_CHANNEL_TYPE_SHELL);
            }
            String command = map.get("command").toString();
            if (StringKit.isNotBlank(command)) {
                command += "\n";
            }
            execCommand(command);
            BufferedReader reader = new BufferedReader(new FileReader(filePath));
            String line = null;
            while ((line = reader.readLine()) != null) {
                if (reader.read() != -1) {
                    line += '\n';
                }
                webSocketContext.message(line.replaceAll("\\[01;\\d{2}m", "").replaceAll("\\[0m", ""));
            }
        } catch (Exception ex) {
            log.error("=====>>>>> Command processing failed", ex);
        }
    }

    @Override
    public void onDisConnect(WebSocketContext webSocketContext) {

    }

    public void execCommand(String command) throws FileNotFoundException, JSchException {
        if (channel != null) {
//            ByteArrayInputStream inputStream = new ByteArrayInputStream(command.getBytes());
//            System.setIn(inputStream);
            channel.setInputStream(new ByteArrayInputStream(command.getBytes()));
            File file = new File(filePath);
            FileOutputStream fileOut = new FileOutputStream(file, false);
            channel.setOutputStream(fileOut);
            channel.connect(3 * 1000);
        }
    }

}
