package com.shuabao.socketServer.command;

import com.shuabao.core.rpc.bean.MessageWrapper;
import org.springframework.stereotype.Service;

/**
 * Created by Scott Wei on 8/9/2018.
 */

@Service(Command.CALLUSERCOMMAND)
public class CallUserCommand implements Command{

    @Override
    public MessageWrapper executeCommand(MessageWrapper messageWrapper) {
        MessageWrapper message = new MessageWrapper();
        message.putData("msg","業務成功");
        message.putData("uid",messageWrapper.getData().get("uid"));
        return message;
    }
}
