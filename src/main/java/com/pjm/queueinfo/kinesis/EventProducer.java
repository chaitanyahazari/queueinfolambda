package com.pjm.queueinfo.kinesis;

import com.amazonaws.services.kinesis.AmazonKinesis;
import com.amazonaws.services.kinesis.model.PutRecordsRequest;
import com.amazonaws.services.kinesis.model.PutRecordsRequestEntry;
import com.pjm.queueinfo.request.EmailRequest;
import org.springframework.stereotype.Component;

import java.nio.ByteBuffer;
import java.util.LinkedList;
import java.util.List;

/**
 * Produces the parrot sqwacks!!
 */
@Component
public class EventProducer {

    public void produce( EmailRequest emailRequest) {
        AmazonKinesis producer = ProducerUtils.getKinesisProducer("us-east-1");

        System.out.println("Creating " + ProducerConfig.RECORDS_TO_TRANSMIT + " records...");
        List<PutRecordsRequestEntry> entries = new LinkedList<>();
        for (int i = 1; i <= ProducerConfig.RECORDS_TO_TRANSMIT; i++) {
            byte[] data = ProducerUtils.jsonPayload(emailRequest);
            entries.add(new PutRecordsRequestEntry()
                    .withPartitionKey(ProducerUtils.randomPartitionKey())
                    .withExplicitHashKey(ProducerUtils.randomExplicitHashKey())
                    .withData(ByteBuffer.wrap(data)));
        }

        PutRecordsRequest request = new PutRecordsRequest().withRecords(entries).withStreamName("parrotstream");

        System.out.println("Sending " + ProducerConfig.RECORDS_TO_TRANSMIT + " records...");
        producer.putRecords(request);
        System.out.println("Complete.");
    }

    public void produce(Object request, String stream) {
        AmazonKinesis producer = ProducerUtils.getKinesisProducer("us-east-1");

        System.out.println("Creating " + ProducerConfig.RECORDS_TO_TRANSMIT + " records...");
        List<PutRecordsRequestEntry> entries = new LinkedList<>();
        for (int i = 1; i <= ProducerConfig.RECORDS_TO_TRANSMIT; i++) {
            byte[] data = ProducerUtils.jsonPayload(request);
            entries.add(new PutRecordsRequestEntry()
                    .withPartitionKey(ProducerUtils.randomPartitionKey())
                    .withExplicitHashKey(ProducerUtils.randomExplicitHashKey())
                    .withData(ByteBuffer.wrap(data)));
        }

        PutRecordsRequest recordsRequest = new PutRecordsRequest().withRecords(entries).withStreamName(stream);

        System.out.println("Sending " + ProducerConfig.RECORDS_TO_TRANSMIT + " records...");
        producer.putRecords(recordsRequest);
        System.out.println("Complete.");
    }
}