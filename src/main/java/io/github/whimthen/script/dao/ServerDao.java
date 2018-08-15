package io.github.whimthen.script.dao;

import com.blade.ioc.annotation.Bean;
import io.github.whimthen.script.entity.Connect;
import io.github.whimthen.script.entity.ConnectRes;
import io.github.whimthen.script.entity.Page;
import io.github.whimthen.script.enums.StatusEnum;
import io.github.whimthen.script.enums.SysCode;
import io.github.whimthen.script.exception.ScriptException;
import io.github.whimthen.script.utils.GlobalUtils;

import java.util.List;

import static io.github.biezhi.anima.Anima.select;
import static io.github.biezhi.anima.Anima.update;

/**
 * @project: script_manager
 * @created: with IDEA
 * @author: nzlong
 * @Date: 2018 08 08 下午6:0022 | 八月. 星期三
 */
@Bean
public class ServerDao {

    public void addServer(Connect connect) throws ScriptException {
        if (GlobalUtils.checkSqlCount(connect.save().asInt())) {
            throw new ScriptException(SysCode.SYSTEM_ERROR);
        }
    }

    public List<Connect> selectByModel(Connect connect) {
        List<Connect> connectList = select().from(Connect.class).where(connect).all();
        return connectList;
    }

    public List<Connect> getAll() {
        return select().from(Connect.class).all();
    }

    public void getByPage(Page<ConnectRes> page) {
        BaseDao.setPage(page, select().from(Connect.class).count());
        List<ConnectRes> connectList = select().from(ConnectRes.class).order("id").where(Connect::getStatus, StatusEnum.VALID.getCode()).page(page.getFrom(), page.getPageSize()).getRows();
        page.setList(connectList);
    }

    public void deleteByIds(String[] ids) throws ScriptException {
        int count = update().from(Connect.class).set(Connect::getStatus, StatusEnum.NOT_VALID.getCode()).in(Connect::getId, ids).execute();
        if (GlobalUtils.checkSqlCount(count)) {
            throw new ScriptException(SysCode.SYSTEM_ERROR);
        }
    }

    public static Connect getById(String id) {
        return select().from(Connect.class).byId(id);
    }

    public void editServer(Connect connect) throws ScriptException {
        if (GlobalUtils.checkSqlCount(connect.update())) {
            throw new ScriptException(SysCode.SYSTEM_ERROR);
        }
    }

}
