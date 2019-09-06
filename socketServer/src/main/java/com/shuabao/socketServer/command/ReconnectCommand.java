package com.shuabao.socketServer.command;

import com.shuabao.core.base.ReturnCode;
import com.shuabao.core.entity.UserSession;
import com.shuabao.core.exception.ShuabaoException;
import com.shuabao.core.manager.EnvironmentManager;
import com.shuabao.core.util.RedisUtil;
import com.shuabao.socketServer.manager.ClientChannelManager;
import com.shuabao.core.rpc.bean.MessageWrapper;
import com.shuabao.socketServer.tcpSocket.processor.NettyChannel;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * Created by Scott Wei on 8/9/2018.
 */

@Service(Command.RECONNECTCOMMAND)//這個bean名字與請求的invodeId對應
public class ReconnectCommand implements Command{

    @Override
    public MessageWrapper executeCommand(MessageWrapper messageWrapper) {
        MessageWrapper resultMessage = new MessageWrapper();
        NettyChannel channel = (NettyChannel) messageWrapper.getData().get("channel");
        String uid = String.valueOf(messageWrapper.getData().get("uid"));
        try{
            if(StringUtils.isEmpty(uid)) {
                throw new ShuabaoException(ReturnCode.INVALID_PARAMETERS);
            }
            //認證成功，channel標記對應uid, 并且關聯uid與channel
            channel.setUid(Integer.valueOf(uid));
            ClientChannelManager.getInstance().addUserChannels(Integer.valueOf(uid), channel);
            //設置緩存用戶為在綫用戶
            UserSession session = RedisUtil.getUserSession(uid, null);
            if(Objects.isNull(session)) {
                throw new ShuabaoException(ReturnCode.USER_NOT_FOUND);
            }
            session.setOnlive("1");//在綫
            session.setHost(EnvironmentManager.getEnvironment().getProperty("rpcserver.ip"));
            session.setPort(EnvironmentManager.getEnvironment().getProperty("rpcserver.port"));
            RedisUtil.setUserSession(session);
            //設置返回結果
            resultMessage.setReturnInfo(ReturnCode.SUCCESS, messageWrapper.getLang());
        }catch(ShuabaoException e) {
            resultMessage.setReturnInfo(e.getCode(), messageWrapper.getLang());
            Command.log.error(">>>>>>>>>ReconnectCommand>>>>>>>>>uid:{},cause:{}", uid, e.getMsg(messageWrapper.getLang()));
        }catch(Exception e) {
            resultMessage.setReturnInfo(ReturnCode.FAILURE, messageWrapper.getLang());
            Command.log.error(">>>>>>>>>ReconnectCommand>>>>>>>>>cause:{}",e.getMessage());
        }finally {
            return resultMessage;
        }
    }
}
