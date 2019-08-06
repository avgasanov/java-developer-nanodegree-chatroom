package edu.udacity.java.nano.chat;

import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * WebSocket Server
 *
 * @see ServerEndpoint WebSocket Client
 * @see Session   WebSocket Session
 */

@Component
@ServerEndpoint(value = "/chat",
        configurator = WebSocketHttpConfig.class,
        encoders = {MessageEncoder.class})
public class WebSocketChatServer {

    /**
     * All chat sessions.
     */
    private static Map<String, Session> onlineSessions = new ConcurrentHashMap<>();

    private static void sendMessageToAll(String msg) {
        //TODO: add send message method.
        Message message = JSONObject.parseObject(msg, Message.class);
        if(message != null && message.getMsg() != null) {
            message.setType("SPEAK");
        }
        message.setOnlineCount(onlineSessions.size());
        onlineSessions.values().forEach((session -> {
            try {
                session.getBasicRemote().sendObject(message);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (EncodeException e) {
                e.printStackTrace();
            }
        }));
    }

    /**
     * Open connection, 1) add session, 2) add user.
     */
    @OnOpen
    public void onOpen(Session session,  EndpointConfig config) {
        String username = (String) config.getUserProperties()
                .get("username");
       addOnlineSession(session, username);
    }

    /**
     * Send message, 1) get username and session, 2) send message to all.
     */
    @OnMessage
    public void onMessage(Session session, String jsonStr) {
        //TODO: add send message.
        Message message = JSONObject.parseObject(jsonStr, Message.class);
        String userName = message.getUsername(); // session is parameter, username for demonstrational purposes only
        sendMessageToAll(jsonStr);
    }

    /**
     * Close connection, 1) remove session, 2) update user.
     */
    @OnClose
    public void onClose(Session session) {
        //TODO: add close connection.
        removeOnlineSession(session);
    }

    /**
     * Print exception.
     */
    @OnError
    public void onError(Session session, Throwable error) {
        error.printStackTrace();
    }

    private void addOnlineSession(Session session, String username) {
        onlineSessions.put(username, session);
        updateOnlineCount();
    }

    private void removeOnlineSession(Session session) {
        onlineSessions.forEach((userName,sessionInCollection) -> {
            if(session.equals(sessionInCollection)) {
                onlineSessions.remove(userName);
            }
        });
        updateOnlineCount();
    }

    private void updateOnlineCount() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("onlineCount", onlineSessions.size());
        sendMessageToAll(jsonObject.toJSONString());
        System.out.println("update online count: " + onlineSessions.size());
    }

}
