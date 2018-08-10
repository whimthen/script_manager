package io.github.whimthen.script.config;

import com.blade.Blade;
import com.blade.event.BeanProcessor;
import com.blade.ioc.annotation.Bean;
import io.github.biezhi.anima.Anima;

import java.io.File;

/**
 * @project: script_manager
 * @created: with IDEA
 * @author: nzlong
 * @Date: 2018 08 06 下午4:4716 | 八月. 星期一
 */
@Bean
public class DbConfig implements BeanProcessor {

    private static final String DB_NAME = "script.db";
    private static String DB_PATH;
    private static String DB_SRC;

    @Override
    public void processor(Blade blade) {
        DB_PATH = DbConfig.class.getClassLoader().getResource("db").getPath() + File.separator + DB_NAME;
        DB_SRC = "jdbc:sqlite://" + DB_PATH;
        Anima.open(DB_SRC);
    }

}
