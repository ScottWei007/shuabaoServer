package com.shuabao.socketServer.command;

import com.shuabao.core.base.ReturnCode;
import com.shuabao.core.entity.UserSession;
import com.shuabao.core.exception.ShuabaoException;
import com.shuabao.core.manager.EnvironmentManager;
import com.shuabao.core.mapper.UserDao;
import com.shuabao.core.util.RedisUtil;
import com.shuabao.socketServer.manager.ClientChannelManager;
import com.shuabao.core.rpc.bean.MessageWrapper;
import com.shuabao.socketServer.tcpSocket.processor.NettyChannel;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * Created by Scott Wei on 8/9/2018.
 */

@Service(Command.AUTHUSERCOMMAND)//這個bean名字與請求的invodeId對應
public class AuthUserCommand implements Command{

    @Autowired
    private UserDao userDao;

    @Override
    public MessageWrapper executeCommand(MessageWrapper messageWrapper) {
        MessageWrapper resultMessage = new MessageWrapper();
        NettyChannel channel = (NettyChannel) messageWrapper.getData().get("channel");
        String uid = messageWrapper.getData().get("uid") == null ? null : String.valueOf(messageWrapper.getData().get("uid"));
        String token = messageWrapper.getData().get("token") == null ? null : String.valueOf(messageWrapper.getData().get("token"));
        try{
            if(StringUtils.isEmpty(uid) || StringUtils.isEmpty(token)) {
                throw new ShuabaoException(ReturnCode.INVALID_PARAMETERS);
            }
            UserSession session = RedisUtil.getUserSession(uid, userDao);
            if(Objects.isNull(session)) {
                throw new ShuabaoException(ReturnCode.USER_NOT_FOUND);
            }
            if("2".equals(session.getStatus())) {
                throw new ShuabaoException(ReturnCode.USER_BLACK_LIST);
            }
            if(userDao.getDriverBlackListCount(uid) > 0) {
                throw new ShuabaoException(ReturnCode.DRIVER_BLACK_LIST);
            }
            if(!token.equals(session.getToken())) {
                throw new ShuabaoException(ReturnCode.FAILED_AUTH);
            }
            //認證成功，channel標記對應uid, 并且關聯uid與channel
            int id = Integer.valueOf(uid);
            channel.setUid(id);
            ClientChannelManager.getInstance().addUserChannels(id, channel);
            //設置緩存用戶為在綫用戶
            session.setOnlive("1");
            session.setHost(EnvironmentManager.getEnvironment().getProperty("rpcserver.ip"));
            session.setPort(EnvironmentManager.getEnvironment().getProperty("rpcserver.port"));
            RedisUtil.setUserSession(session);
            //检查离线消息
            while (true) {
                MessageWrapper message = RedisUtil.rPopMessage(id);
                if(Objects.isNull(message)) {
                    break;
                }
                ClientChannelManager.getInstance().notifyOneOnline(id, message);
            }
            //設置返回結果
            resultMessage.setReturnInfo(ReturnCode.SUCCESS, messageWrapper.getLang());
        }catch(ShuabaoException e) {
            resultMessage.setReturnInfo(e.getCode(), messageWrapper.getLang());
            Command.log.error(">>>>>>>>>AuthUserCommand>>>>>>>>>uid:{},token:{},cause:{}",uid,token,e.getMsg(messageWrapper.getLang()));
        }catch(Exception e) {
            resultMessage.setReturnInfo(ReturnCode.FAILURE, messageWrapper.getLang());
            Command.log.error(">>>>>>>>>AuthUserCommand>>>>>>>>>cause:{}",e.getMessage());
        }finally {
            return resultMessage;
        }
    }
}
