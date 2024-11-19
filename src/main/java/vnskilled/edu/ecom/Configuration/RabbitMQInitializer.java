package vnskilled.edu.ecom.Configuration;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class RabbitMQInitializer implements CommandLineRunner {

    private final RabbitMQConsumer rabbitMQConsumer;

    public RabbitMQInitializer(RabbitMQConsumer rabbitMQConsumer) {
        this.rabbitMQConsumer = rabbitMQConsumer;
    }

    @Override
    public void run(String... args) throws Exception {
        rabbitMQConsumer.consume();
    }
}
