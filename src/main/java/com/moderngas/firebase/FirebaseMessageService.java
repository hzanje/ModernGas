package com.moderngas.firebase;

import com.google.firebase.messaging.*;
import com.moderngas.pojo.NotificationRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FirebaseMessageService {

    private FirebaseMessagingInitializer firebaseMessagingInitializer;

    public FirebaseMessageService(FirebaseMessagingInitializer firebaseMessagingInitializer) {
        this.firebaseMessagingInitializer = firebaseMessagingInitializer;
    }

    public String sendNotificationToDevice(NotificationRequest notificationRequest, String token) throws FirebaseMessagingException {

        Notification notification = Notification
                .builder()
                .setTitle(notificationRequest.getTitle())
                .setBody(notificationRequest.getMessage())
                .build();

        Message message = Message
                .builder()
                .setToken(token)
                .setNotification(notification)
                .build();

        return FirebaseMessaging.getInstance().send(message);
    }

    public BatchResponse sendNotificationToMultipleDevice(NotificationRequest notificationRequest, List<String> tokenList) throws FirebaseMessagingException {

        Notification notification = Notification
                .builder()
                .setTitle(notificationRequest.getTitle())
                .setBody(notificationRequest.getMessage())
                .build();

        MulticastMessage message = MulticastMessage
                .builder().setNotification(notification)
                .addAllTokens(tokenList)
                .setNotification(notification)
                .build();

        return FirebaseMessaging.getInstance().sendMulticast(message);
    }
}
