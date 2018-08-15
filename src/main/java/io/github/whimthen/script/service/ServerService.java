package io.github.whimthen.script.service;

import com.blade.ioc.annotation.Bean;
import com.blade.ioc.annotation.Inject;
import com.blade.kit.StringKit;
import io.github.whimthen.script.cache.SessionManager;
import io.github.whimthen.script.dao.ServerDao;
import io.github.whimthen.script.entity.Connect;
import io.github.whimthen.script.entity.ConnectRes;
import io.github.whimthen.script.entity.Page;
import io.github.whimthen.script.entity.SSHInfo;
import io.github.whimthen.script.enums.StatusEnum;
import io.github.whimthen.script.enums.SysCode;
import io.github.whimthen.script.exception.ScriptException;
import io.github.whimthen.script.utils.EncryptUtils;
import io.github.whimthen.script.utils.GlobalUtils;

/**
 * @project: script_manager
 * @created: with IDEA
 * @author: nzlong
 * @Date: 2018 08 08 下午5:5959 | 八月. 星期三
 */
@Bean
public class ServerService {

    @Inject
    private ServerDao serverDao;

    public void addServer(Connect connect) throws Exception {
        if (GlobalUtils.isAnyBlank(connect.getHost(), connect.getUser(), connect.getPassword(), connect.getAlias()) || connect.getPort() == null) {
            throw new ScriptException(SysCode.PARAM_MISSING);
        }
        if (serverDao.selectByModel(connect.setStatus(StatusEnum.VALID.getCode())).size() > 0) {
            throw new ScriptException(SysCode.REPLICATE_DATA);
        }
        serverDao.addServer(connect.setCreateTime(System.currentTimeMillis()).setPassword(EncryptUtils.dbEncrypt(connect.getPassword(), connect.getUser())));
    }

    public void getByPage(Page<ConnectRes> page) {
        serverDao.getByPage(page);
    }

    public void deleteByIds(String ids) throws ScriptException {
        if (StringKit.isBlank(ids)) {
            throw new ScriptException(SysCode.PARAM_MISSING);
        }
        String[] split = ids.split(",");
        serverDao.deleteByIds(split);
    }

    public Connect getById(String id) throws Exception {
        if (StringKit.isBlank(id)) {
            throw new ScriptException(SysCode.PARAM_MISSING);
        }
        Connect connect = ServerDao.getById(id);
        return connect;
    }

    public void editServer(Connect connect) throws Exception {
        if (GlobalUtils.isAnyBlank(connect.getHost(), connect.getUser(), connect.getPassword(), connect.getAlias()) || connect.getPort() == null) {
            throw new ScriptException(SysCode.PARAM_MISSING);
        }
        Connect exitsConnect = ServerDao.getById(String.valueOf(connect.getId()));
        if (!exitsConnect.getPassword().equals(connect.getPassword())) {
            connect.setPassword(EncryptUtils.dbEncrypt(connect.getPassword(), connect.getUser()));
        }
        serverDao.editServer(connect.setUpdateTime(System.currentTimeMillis()));
    }

    public static SSHInfo getSIById(String serverId) throws ScriptException {
        if (StringKit.isBlank(serverId)) {
            throw new ScriptException(SysCode.PARAM_MISSING);
        }
        Connect connect = ServerDao.getById(serverId);
        if (connect != null) {
            return SSHInfo.newInstance().setHost(connect.getHost()).setUser(connect.getUser()).setPassword(connect.getPassword()).setPort(connect.getPort());
        }
        return null;
    }

}
