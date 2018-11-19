package com.pjm.queueinfo;


import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sqs.AmazonSQSAsync;
import com.amazonaws.services.sqs.AmazonSQSAsyncClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.aws.messaging.core.QueueMessagingTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;

@SpringBootApplication
public class QueueInfoApplication {
    public static void main(String[] args) throws Exception{
        SpringApplication.run(QueueInfoApplication.class, args);

    }

    @Bean
    public QueueMessagingTemplate queueMessagingTemplate() {
        return new QueueMessagingTemplate(amazonSQSClient());
    }

    @Lazy
    @Bean(name = "amazonSQS", destroyMethod = "shutdown")
    public AmazonSQSAsync amazonSQSClient() {
        AmazonSQSAsyncClient awsSQSAsyncClient = new AmazonSQSAsyncClient(new DefaultAWSCredentialsProviderChain());
        awsSQSAsyncClient.setEndpoint("sqs.us-east-1.amazonaws.com");
        awsSQSAsyncClient.setRegion(Region.getRegion(Regions.fromName("us-east-1")));
        return awsSQSAsyncClient;
    }
}



