package io.github.whimthen.script.handler;

import com.blade.kit.JsonKit;
import com.blade.kit.StringKit;
import com.blade.mvc.handler.WebSocketHandler;
import com.blade.mvc.websocket.WebSocketContext;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSchException;
import io.github.whimthen.script.cache.ChannelTypeManager;
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
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.concurrent.TimeUnit;

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
        BufferedReader reader = null;
        try {
            if (StringKit.isBlank(serverId)) {
                throw new ScriptException(SysCode.PARAM_MISSING);
            }
            if (channel != null && channel.isConnected()) {
                channel.disconnect();
            }
            SSHInfo sshInfo = ServerService.getSIById(serverId);
            if ((channel == null || !channel.isConnected()) && sshInfo != null) {
                channel = SessionManager.getSession(sshInfo).openChannel(ChannelTypeManager.get(sshInfo));
            }
            String command = map.get("command").toString();
            if (StringKit.isBlank(command)) {
                ChannelTypeManager.put(sshInfo, GlobalConstant.JSCH_CHANNEL_TYPE_EXEC);
                execCommand(command);
                reader = new BufferedReader(new FileReader(filePath));
                String line = null;
                while ((line = reader.readLine()) != null) {
                    webSocketContext.message(line + "\n");
                }
                reader.close();
            } else {
                command += "\n";
                ChannelExec channelExec = (ChannelExec)channel;
                channelExec.setCommand(command);
                channelExec.setInputStream(null);
                channelExec.setErrStream(System.err);
                InputStream in = channelExec.getInputStream();
                channelExec.connect();

                StringBuffer buf = new StringBuffer(1024);
                byte[] tmp = new byte[ 1024 ];
                while (true) {
                    while (in.available() > 0) {
                        int i = in.read(tmp, 0, 1024);
                        if (i < 0) break;
                        buf.append(new String(tmp, 0, i));
                    }
                    if (channelExec.isClosed()) {
                        break;
                    }
                    TimeUnit.MILLISECONDS.sleep(100);
                }
                webSocketContext.message(buf.toString());
                channelExec.disconnect();
            }
        } catch (Exception ex) {
            log.error("=====>>>>> Command processing failed", ex);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void onDisConnect(WebSocketContext webSocketContext) {

    }

    public void execCommand(String command) throws FileNotFoundException, JSchException {
        if (channel != null) {
            channel.setInputStream(new ByteArrayInputStream(command.getBytes()));
            File file = new File(filePath);
            FileOutputStream fileOut = new FileOutputStream(file, false);
            channel.setOutputStream(fileOut);
            channel.connect(3 * 1000);
        }
    }

}
