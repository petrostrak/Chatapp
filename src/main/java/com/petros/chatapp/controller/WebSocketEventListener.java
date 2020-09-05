package com.petros.chatapp.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;

import com.petros.chatapp.model.ChatMessage;

@Component
public class WebSocketEventListener {
	
	/*
	 * We’ll use event listeners to listen for socket connect and disconnect 
	 * events so that we can log these events and also broadcast them when a 
	 * user joins or leaves the chat room -
	 * */

	private static final Logger logger = LoggerFactory.getLogger(WebSocketEventListener.class);
	
	@Autowired
	private SimpMessageSendingOperations messagingTemplate;
	
	@EventListener
	public void handleWebSocketConnectListener(SessionConnectEvent event) {
		StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
		
		String username = (String) headerAccessor.getSessionAttributes().get("username");
		
		if(username != null) {
			logger.info("User disconnected : " + username);
			
			ChatMessage chatMessage = new ChatMessage();
			chatMessage.setType(ChatMessage.MessageType.LEAVE);
			chatMessage.setSender(username);
			
			messagingTemplate.convertAndSend("/topic/public", chatMessage);
		}
	}
}

/*
 * We’re already broadcasting user join event in the addUser() 
 * method defined inside ChatController. So, we don’t need to 
 * do anything in the SessionConnected event. In the 
 * SessionDisconnect event, we’ve written code to extract the 
 * user’s name from the websocket session and broadcast a user 
 * leave event to all the connected clients.
 * 
 * */