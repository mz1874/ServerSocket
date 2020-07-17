package com.candy.mz;

import com.candy.mz.socket.GetHttpSessionConfigurator;
import com.candy.mz.util.MessageUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSession;
import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author by wangchong
 * @Description
 * @Date 2020/7/17 10:23
 */
@ServerEndpoint(value = "/socket",configurator = GetHttpSessionConfigurator.class)
@Component
@Slf4j
public class ServerEnpoint {

    private static volatile Integer onlineCount = 0;

    private static ConcurrentHashMap<String, ServerEnpoint> webSocketSet = new ConcurrentHashMap<String, ServerEnpoint>();

    private Session session;

    private HttpSession httpSession;

    private String userno = "";

    /**
     * 连接建立成功调用的方法
     *
     * @param session 可选的参数。session为与某个客户端的连接会话，需要通过它来给客户端发送数据
     */
    @OnOpen
    public void onOpen(Session session, EndpointConfig config) {
        this.session = session;
        this.httpSession = (HttpSession) config.getUserProperties().get(HttpSession.class.getName());
        String userName = (String) httpSession.getAttribute("user");
        webSocketSet.put(userName, this);
        //        userno = param;
//        this.session = session;
//        webSocketSet.put(param, this);
//        addOnlineCount();
//        System.out.println("有新连接加入！当前在线人数为" + getOnlineCount());
        String message = MessageUtil.getMessage(true, null, getAllNames());
    }


    public void sendAllMessage(String message) {
        Set<String> keyNames = webSocketSet.keySet();
        for (String userNames : keyNames) {

        }
    }

    public Set<String> getAllNames(){
        return webSocketSet.keySet();
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() {
        if (!userno.equals("")) {
            webSocketSet.remove(userno);
            subOnlineCount();
            System.out.println("有一连接关闭！当前在线人数为" + getOnlineCount());
        }
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息
     * @param session 可选的参数
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        System.out.println("来自客户端的消息:" + message);
//        session.get
        //群发消息
        if (1 < 2) {
            sendAll(message);
        } else {
            //给指定的人发消息
            sendToUser(message);
        }
    }

    /**
     * 给指定的人发送消息
     * @param message
     */
    private void sendToUser(String message) {
        String sendUserno = message.split("[|]")[1];
        String sendMessage = message.split("[|]")[0];
        String now = getNowTime();
        try {
            if (webSocketSet.get(sendUserno) != null) {
                webSocketSet.get(sendUserno).sendMessage(now + "用户" + userno + "发来消息：" + " <br/> " + sendMessage);
            } else {
                System.out.println("当前用户不在线");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 给所有人发消息
     * @param message
     */
    private void sendAll(String message) {
        String now = getNowTime();
        String sendMessage = message.split("[|]")[0];
        //遍历HashMap
        for (String key : webSocketSet.keySet()) {
            try {
                //判断接收用户是否是当前发消息的用户
                if (!userno.equals(key)) {
                    webSocketSet.get(key).sendMessage(now + "用户" + userno + "发来消息：" + " <br/> " + sendMessage);
                    System.out.println("key = " + key);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 获取当前时间
     *
     * @return
     */
    private String getNowTime() {
        Date date = new Date();
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = format.format(date);
        return time;
    }
    /**
     * 发生错误时调用
     *
     * @param session
     * @param error
     */
    @OnError
    public void onError(Session session, Throwable error) {
        System.out.println("发生错误");
        error.printStackTrace();
    }

    /**
     * 这个方法与上面几个方法不一样。没有用注解，是根据自己需要添加的方法。
     *
     * @param message
     * @throws IOException
     */
    public void sendMessage(String message) throws IOException {
        this.session.getBasicRemote().sendText(message);
        //this.session.getAsyncRemote().sendText(message);
    }

    public static synchronized int getOnlineCount() {
        return onlineCount;
    }

    public static synchronized void addOnlineCount() {
        ServerEnpoint.onlineCount++;
    }

    public static synchronized void subOnlineCount() {
        ServerEnpoint.onlineCount--;
    }


}
