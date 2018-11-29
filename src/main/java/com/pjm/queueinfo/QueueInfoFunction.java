package com.pjm.queueinfo;

import com.pjm.queueinfo.kinesis.EventProducer;
import com.pjm.queueinfo.request.CalendarRequest;
import com.pjm.queueinfo.request.EmailRequest;
import com.pjm.queueinfo.request.LexRequest;
import com.pjm.queueinfo.response.DialogAction;
import com.pjm.queueinfo.response.LexResponse;
import com.pjm.queueinfo.response.Message;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.function.Function;

@Component("queueInfoFunction")
public class QueueInfoFunction implements Function<LexRequest, LexResponse> {

    public static final String EMAIL_DESTINATION = "https://sqs.us-east-1.amazonaws.com/963929482176/parrot_queue";

    public LexResponse apply(LexRequest lexRequest) {

        String email = lexRequest.getInputTranscript();

        String currentIntent = lexRequest.getCurrentIntent().getName();
        if (StringUtils.equals(currentIntent, "TechnicalServiceAssistant")) {
            return handlePasswordReset(lexRequest);
        }

        if (StringUtils.equals(currentIntent, "CalenderRequest")) {
            return handleCalendarRequest(lexRequest);
        }

        return message("You entered " + email);
    }

    LexResponse handlePasswordReset(LexRequest lexRequest) {
        String success = "Thanks for talking to the parrot, your account is now unlocked";


        Map<String, String> slots = lexRequest.getCurrentIntent().getSlots();
        for (Map.Entry<String, String> slot : slots.entrySet()) {
            if (StringUtils.equals(slot.getKey(), "maiden_name") && !StringUtils.equals(slot.getValue(), "adi")) {
                return message("Invalid password answers, please try again later");
            }
        }
        EventProducer eventProducer = new EventProducer();

        eventProducer.produce(
                emailRequest(null, success,
                        "Account unlocked"), "parrotstream");
        return message(success);
    }

    LexResponse handleCalendarRequest(LexRequest lexRequest) {
        String success = "Your room is booked and emailed the details";


        Map<String, String> slots = lexRequest.getCurrentIntent().getSlots();
        String day = slots.get("Day");
        String time = slots.get("Time");
        String duration = slots.get("Duration");

        for (Map.Entry<String, String> slot : slots.entrySet()) {
            System.out.println(slot.getKey() + " :: " + slot.getValue());

        }
        EventProducer eventProducer = new EventProducer();

        eventProducer.produce(calendarRequest(day, time, duration), "parrot_scheduling");
        return message(success);
    }

    EmailRequest emailRequest(String to, String body, String subject) {
        EmailRequest emailRequest = new EmailRequest();
        emailRequest.setTo(to == null ? "krishnaha@gmail.com" : to);
        emailRequest.setBody(body);
        emailRequest.setSubject(subject);
        return emailRequest;
    }

    CalendarRequest calendarRequest(String day, String time, String duration) {
        CalendarRequest calendarRequest = new CalendarRequest();
        calendarRequest.setTime(time);
        calendarRequest.setDay(day);
        calendarRequest.setDuration(duration);
        return calendarRequest;
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
