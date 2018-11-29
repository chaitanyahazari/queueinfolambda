package com.pjm.queueinfo.kinesis;

/**
 * A helper class for configuring the Kinesis sample producer behavior.
 */
public class ProducerConfig {
    /**
     * The size of each record that is transmitted.
     */
    public static final int RECORD_SIZE_BYTES = 1024;

    /**
     * The number of records to send per application run.
     */
    public static final int RECORDS_TO_TRANSMIT = 1;
}
