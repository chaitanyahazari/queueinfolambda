package com.pjm.queueinfo;

import com.pjm.queueinfo.request.EmailRequest;
import com.pjm.queueinfo.request.LexRequest;
import com.pjm.queueinfo.response.DialogAction;
import com.pjm.queueinfo.response.LexResponse;
import com.pjm.queueinfo.response.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.aws.messaging.core.QueueMessagingTemplate;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;
import java.util.function.Function;

git@Component("queueInfoFunction")
public class QueueInfoFunction implements Function<LexRequest, LexResponse> {

    @Autowired
    EmailService emailService;

    @Autowired
    QueueMessagingTemplate queueMessagingTemplate;

    public LexResponse apply(LexRequest lexRequest) {
        String email = lexRequest.getInputTranscript();

        EmailRequest emailRequest = new EmailRequest();
        emailRequest.setTo(email != null ? email : "krishnaha@gmail.com");
        emailRequest.setBody("Woeful");
        emailRequest.setSubject("parrot subject");

        queueMessagingTemplate.convertAndSend("https://sqs.us-east-1.amazonaws.com/963929482176/parrot_queue", emailRequest);

        return message("You entered " + email);
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
