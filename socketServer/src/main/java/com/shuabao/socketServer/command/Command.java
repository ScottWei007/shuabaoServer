package com.shuabao.socketServer.command;

import com.shuabao.core.rpc.bean.MessageWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Scott Wei on 8/9/2018.
 */
public interface Command {
    //日志
    Logger log = LoggerFactory.getLogger(Command.class);

    /*命令號,與tcp請求的invodeid對應*/
    String AUTHUSERCOMMAND = "1000";
    String RECONNECTCOMMAND = "1010";

    String CALLUSERCOMMAND = "2000";

    MessageWrapper executeCommand(MessageWrapper messageWrapper);

}
