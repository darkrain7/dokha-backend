package com.dokhabackend.dokha;

import javapns.Push;
import javapns.communication.exceptions.CommunicationException;
import javapns.communication.exceptions.KeystoreException;
import javapns.notification.PushNotificationPayload;
import javapns.notification.PushedNotification;
import javapns.notification.ResponsePacket;
import org.json.JSONException;

import java.util.List;

/**
 * Semenov A.E.
 * Created 18.05.2019
 **/
public class PushServiceInFuture {
    public void push() {
        try {
            PushNotificationPayload payload = PushNotificationPayload.complex();
            payload.addAlert("test notification");
            payload.addBadge(1);
            payload.addSound("default");
            payload.addCustomDictionary("id", "1");
            System.out.println(payload.toString());
            List<PushedNotification> NOTIFICATIONS = Push.payload(payload, "/Users/purva/NetBeansProjects/testNotificationApp/src/main/java/com/archer/testnotificationapp/dev_cert.p12", "Password123", false, "cd7454c24887a0631f68c35828e592285b8dd3f2edfdbae6b26cb0ed9be23008");
            for (PushedNotification NOTIFICATION : NOTIFICATIONS) {
                if (NOTIFICATION.isSuccessful()) {
                    /* APPLE ACCEPTED THE NOTIFICATION AND SHOULD DELIVER IT */
                    System.out.println("PUSH NOTIFICATION SENT SUCCESSFULLY TO: " +
                            NOTIFICATION.getDevice().getToken());
                    /* STILL NEED TO QUERY THE FEEDBACK SERVICE REGULARLY */
                } else {
                    String INVALIDTOKEN = NOTIFICATION.getDevice().getToken();
                    /* ADD CODE HERE TO REMOVE INVALIDTOKEN FROM YOUR DATABASE */
                    /* FIND OUT MORE ABOUT WHAT THE PROBLEM WAS */
                    Exception THEPROBLEM = NOTIFICATION.getException();
                    THEPROBLEM.printStackTrace();
                    /* IF THE PROBLEM WAS AN ERROR-RESPONSE PACKET RETURNED BY APPLE, GET IT */
                    ResponsePacket THEERRORRESPONSE = NOTIFICATION.getResponse();
                    if (THEERRORRESPONSE != null) {
                        System.out.println(THEERRORRESPONSE.getMessage());
                    }
                }
            }
        } catch (CommunicationException | JSONException | KeystoreException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}



