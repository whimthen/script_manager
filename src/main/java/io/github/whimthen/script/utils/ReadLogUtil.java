package io.github.whimthen.script.utils;

import io.github.whimthen.script.constant.GlobalConstant;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * @project: script_manager
 * @created: with IDEA
 * @author: nzlong
 * @Date: 2018 08 16 下午1:3445 | 八月. 星期四
 */
public class ReadLogUtil {

    public static String readServerLog() throws IOException {
        String filePath = ReadLogUtil.class.getClassLoader().getResource("").getPath() + GlobalConstant.SERVER_LOG;
        return read(filePath);
    }

    private static String read(String filPath) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(filPath));
        StringBuilder message = new StringBuilder();
        String line = null;
        while ((line = reader.readLine()) != null) {
            message.append(line).append("\n");
        }
        return message.toString();
    }

    public static void main(String[] args) throws IOException {
        System.out.println(readServerLog());
    }

}
