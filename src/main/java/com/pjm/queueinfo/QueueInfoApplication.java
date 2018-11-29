package com.pjm.queueinfo;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class QueueInfoApplication {
    public static void main(String[] args) {
        SpringApplication.run(QueueInfoApplication.class, args);

    }

//    @Bean
//    public QueueMessagingTemplate queueMessagingTemplate() {
//        return new QueueMessagingTemplate(amazonSQSClient());
//    }

//    @Lazy
//    @Bean(name = "amazonSQS", destroyMethod = "shutdown")
//    public AmazonSQSAsync amazonSQSClient() {
//        AmazonSQSAsyncClient awsSQSAsyncClient = new AmazonSQSAsyncClient(new DefaultAWSCredentialsProviderChain());
//        awsSQSAsyncClient.setEndpoint("sqs.us-east-1.amazonaws.com");
//        awsSQSAsyncClient.setRegion(Region.getRegion(Regions.fromName("us-east-1")));
//        return awsSQSAsyncClient;
//    }


}



