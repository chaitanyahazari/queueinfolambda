package com.pjm.queueinfo;

import com.pjm.queueinfo.kinesis.EventProducer;
import com.pjm.queueinfo.request.EmailRequest;
import com.pjm.queueinfo.request.LexRequest;
import com.pjm.queueinfo.response.DialogAction;
import com.pjm.queueinfo.response.LexResponse;
import com.pjm.queueinfo.response.Message;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component("queueInfoFunction")
public class QueueInfoFunction implements Function<LexRequest, LexResponse> {

//    @Autowired
//    QueueMessagingTemplate queueMessagingTemplate;
//    @Autowired
//    EventProducer eventProducer;

    public static final String EMAIL_DESTINATION = "https://sqs.us-east-1.amazonaws.com/963929482176/parrot_queue";

    public LexResponse apply(LexRequest lexRequest) {

        String email = lexRequest.getInputTranscript();

        EmailRequest emailRequest = getEmailRequest(email);

//        queueMessagingTemplate.convertAndSend(EMAIL_DESTINATION, emailRequest);
        EventProducer eventProducer = new EventProducer();
        eventProducer.produce("parrotstream", "us-east-1", emailRequest);

        System.out.println("test");

        return message("You entered " + email);
    }


    EmailRequest getEmailRequest(String email) {
        EmailRequest emailRequest = new EmailRequest();
        emailRequest.setTo(email != null ? email : "krishnaha@gmail.com");
        emailRequest.setBody("Woeful");
        emailRequest.setSubject("parrot subject");
        return emailRequest;
    }

    private LexResponse message(String msg) {
        LexResponse response = new LexResponse();
        DialogAction action = new DialogAction();
        action.setType(DialogAction.CLOSE_TYPE);
        action.setFulfillmentState(DialogAction.FULFILLMENT_STATE_FULFILLED);
        Message message = new Message();
        message.setContent(msg);
        message.setContentType(Message.CONTENT_TYPE_PLAIN_TEXT);
        action.setMessage(message);
        response.setDialogAction(action);
        return response;
    }
}
