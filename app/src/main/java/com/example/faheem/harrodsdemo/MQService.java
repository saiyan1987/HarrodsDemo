package com.example.faheem.harrodsdemo;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.kaazing.gateway.jms.client.JmsConnectionFactory;
import com.kaazing.net.auth.BasicChallengeHandler;
import com.kaazing.net.auth.ChallengeHandler;
import com.kaazing.net.auth.LoginHandler;
import com.kaazing.net.ws.WebSocketFactory;

import java.net.PasswordAuthentication;
import java.net.URI;
import java.util.concurrent.Semaphore;

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;


public class MQService extends Service {

    private Connection connection;
    private JmsConnectionFactory connectionFactory;


    public MQService() {
        super();
    }

    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public int onStartCommand(final Intent intent, int flags, int startId) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                onHandleIntent(intent);
            }
        }).start();

        return super.onStartCommand(intent, flags, startId);
    }

    protected void onHandleIntent(Intent intent) {
        if (connectionFactory == null) {
            try {
                connectionFactory = JmsConnectionFactory.createConnectionFactory();
                WebSocketFactory webSocketFactory = connectionFactory.getWebSocketFactory();
                webSocketFactory.setDefaultChallengeHandler(createChallengehandler());

                //String location = "ws://10.24.117.141:6666/jms"; //CHANGED
                String location = "ws://ec2-52-215-7-248.eu-west-1.compute.amazonaws.com:6666/jms"; //location of Kaazing instance
                connectionFactory.setGatewayLocation(URI.create(location));

                connection = connectionFactory.createConnection();
                connection.start();

                Log.i("connected", "connected");
                Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
                Destination destination = session.createQueue("/queue/REMOTEQ");


                MessageProducer messageProducer = session.createProducer(destination);

                TextMessage m = (TextMessage)session.createTextMessage();

                SharedPreferences sharedPreferences = getSharedPreferences("customerDetail.txt",MODE_PRIVATE);
                String customerid =  sharedPreferences.getString("customerId","error");
                String latitude = sharedPreferences.getString("latitude","error");
                String longitude = sharedPreferences.getString("longitude","error");
                m.setStringProperty("customerid", customerid);
                m.setStringProperty("latitude", latitude);
                m.setStringProperty("longitude", longitude);
                m.setText("body text");


                messageProducer.send(destination, m);


                destination = session.createQueue("/queue/REMOTEQ2");
                MessageConsumer messageConsumer = session.createConsumer(destination);
                session.setMessageListener(new MessageListener() {
                    @Override
                    public void onMessage(Message message) {
                        onMessageImpl(message);
                    }
                });
                //Message m_in = messageConsumer.receive();

                //onMessage(m_in);


            } catch (Exception e) {
                Log.i("FOOOOOOO","FOOOOOOO");
                e.printStackTrace();
            }
        }
    }

    public void onMessageImpl(Message message) {
        final TextMessage m_in = (TextMessage)message;

        Handler handler = new Handler(Looper.getMainLooper());

        handler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), "Notification Received", Toast.LENGTH_SHORT).show();
                NotificationCompat.Builder notificiation_builder = new NotificationCompat.Builder(MQService.this);
                notificiation_builder.setContentTitle("Greetings from Harrods");
                notificiation_builder.setAutoCancel(true);
                notificiation_builder.setContentIntent(PendingIntent.getActivity(getApplicationContext(), 0, new Intent(), 0));
                notificiation_builder.setSmallIcon(R.drawable.ic_stat_name);
                notificiation_builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.harrods_logo));
                notificiation_builder.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));

                try {
                    notificiation_builder.setContentText(m_in.getText());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Notification n = notificiation_builder.build();
                NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                nm.notify(123, n);
            };
        });
    }


            private ChallengeHandler createChallengehandler() {
        final LoginHandler loginHandler = new LoginHandler() {
            private String username;
            private char[] password;
            @Override
            public PasswordAuthentication getCredentials() {
                try {
                    username = "mqm"; //CHANGED
                    password = "mqm".toCharArray(); //CHANGED
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return new PasswordAuthentication(username, password);
            }
        };
        BasicChallengeHandler challengeHandler = BasicChallengeHandler.create();
        challengeHandler.setLoginHandler(loginHandler);
        return challengeHandler;
    }

}