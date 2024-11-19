package vnskilled.edu.ecom.Configuration;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@Component
public class RabbitMQProducer {
    private final static String QUEUE_NAME = "otpQueue";
    private Connection connection;
    private Channel channel;

    public RabbitMQProducer() {
        // Constructor
    }

    @PostConstruct
    public void init() {
        try {
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost("localhost"); // Địa chỉ RabbitMQ
            connection = factory.newConnection();
            channel = connection.createChannel();
            // Khai báo hàng đợi chỉ một lần
            channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendOtp(String otpMessage) {
        try {
            // Gửi thông điệp OTP đến hàng đợi
            channel.basicPublish("", QUEUE_NAME, null, otpMessage.getBytes());
            System.out.println(" [x] Sent '" + otpMessage + "'");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @PreDestroy
    public void cleanUp() {
        try {
            if (channel != null && channel.isOpen()) {
                channel.close();
            }
            if (connection != null && connection.isOpen()) {
                connection.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
