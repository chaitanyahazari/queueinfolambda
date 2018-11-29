package com.pjm.queueinfo.kinesis;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.kinesis.AmazonKinesis;
import com.amazonaws.services.kinesis.AmazonKinesisClient;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.pjm.queueinfo.request.EmailRequest;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.Random;
import java.util.UUID;

/**
 * A set of utility functions for use by the sample Kinesis producer functions.
 */
public class ProducerUtils {
    // Use this is you want to send the same records every time (useful for testing)
    // private static final Random RANDOM = new Random(0);

    // Use this to send random records
    private static final Random RANDOM = new Random();

    private static final String ALPHABET = "abcdefghijklmnopqrstuvwxyz";

    /**
     * @return A randomly generated partition key.
     */
    public static String randomPartitionKey() {
        return UUID.randomUUID().toString();
    }

    /**
     * @return A randomly generated explicit hash key.
     */
    public static String randomExplicitHashKey() {
        return new BigInteger(128, RANDOM).toString(10);
    }

    /**
     * Generate a new semi-random Kinesis record data byte array.
     * <p>
     * A sample record is a UTF-8 string that looks like:
     * <p>
     * RECORD 5 bnasdfnueghlasdhallaeeafelaijfjgwhgczmvc
     *
     * @param sequenceNumber The sequence number of the record in the overall stream of records.
     * @param desiredLength  The desired length of the record.
     * @return Kinesis record data with random alphanumeric characters.
     */
    public static byte[] randomData(long sequenceNumber, int desiredLength) {
        StringBuilder sb = new StringBuilder();
        sb.append("RECORD ");
        sb.append(Long.toString(sequenceNumber));
        sb.append(" ");
        while (sb.length() < desiredLength - 1) {
            sb.append(ALPHABET.charAt(RANDOM.nextInt(ALPHABET.length())));
        }
        sb.append("\n");

        return sb.toString().getBytes(StandardCharsets.UTF_8);
    }

    public static byte[] jsonPayload(Object emailRequest) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        try {
            String payload = mapper.writeValueAsString(emailRequest);
            System.out.println("Sending Pay load is " + payload);
            return payload.getBytes(StandardCharsets.UTF_8);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Create a new Kinesis producer for publishing to Kinesis.
     *
     * @param region The region of the Kinesis stream to publish to.
     * @return An Amazon Kinesis producer for publishing to a Kinesis stream.
     */
    public static AmazonKinesis getKinesisProducer(String region) {
        ClientConfiguration config = new ClientConfiguration();
        config.setMaxConnections(25);
        config.setConnectionTimeout(60000);
        config.setSocketTimeout(60000);

        AmazonKinesis producer = new AmazonKinesisClient(new DefaultAWSCredentialsProviderChain(), config);
        producer.setRegion(Region.getRegion(Regions.fromName(region)));

        return producer;
    }
}
