package com.pjm.queueinfo;

import com.pjm.queueinfo.model.QueueInfoRequest;
import com.pjm.queueinfo.model.QueueInfoResponse;
import com.pjm.queueinfo.request.LexRequest;
import com.pjm.queueinfo.response.DialogAction;
import com.pjm.queueinfo.response.LexResponse;
import com.pjm.queueinfo.response.Message;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component("queueInfoFunction")
public class QueueInfoFunction implements Function<LexRequest, LexResponse> {

    @Override
    public LexResponse apply(LexRequest lexRequest) {
        String intentName = lexRequest.getCurrentIntent().getName();
        lexRequest.getUserId();
        LexResponse response = new LexResponse();
        DialogAction action = new DialogAction();
        action.setType("Close");
        action.setFulfillmentState("Fulfilled");
        Message message = new Message();
        message.setContent(lexRequest.getInputTranscript());
        message.setContentType("PlainText");
        action.setMessage(message);
        response.setDialogAction(action);
        return response;
    }
}
