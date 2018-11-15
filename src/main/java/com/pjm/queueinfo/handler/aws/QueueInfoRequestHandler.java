package com.pjm.queueinfo.handler.aws;

import com.pjm.queueinfo.model.QueueInfoResponse;
import com.pjm.queueinfo.request.LexRequest;
import com.pjm.queueinfo.response.LexResponse;
import org.springframework.cloud.function.adapter.aws.SpringBootRequestHandler;

public class QueueInfoRequestHandler extends SpringBootRequestHandler<LexRequest, LexResponse> {
}
