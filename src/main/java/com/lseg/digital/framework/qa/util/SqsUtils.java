package com.lseg.digital.framework.qa.util;

import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sqs.model.*;

import java.util.List;

public class SqsUtils {
    private final SqsClient sqsClient;
    
    public SqsUtils(String region) {
        this.sqsClient = SqsClient.builder()
            .region(Region.of(region))
            .build();
    }

    public SendMessageResponse sendMessage(String queueUrl, String messageBody) {
        return sqsClient.sendMessage(SendMessageRequest.builder()
            .queueUrl(queueUrl)
            .messageBody(messageBody)
            .build());
    }

    public DeleteMessageResponse deleteMessage(String queueUrl, String receiptHandle) {
        return sqsClient.deleteMessage(DeleteMessageRequest.builder()
            .queueUrl(queueUrl)
            .receiptHandle(receiptHandle)
            .build());
    }

    public List<Message> receiveMessages(String queueUrl, int maxMessages, int waitTimeSeconds) {
        ReceiveMessageRequest request = ReceiveMessageRequest.builder()
            .queueUrl(queueUrl)
            .maxNumberOfMessages(maxMessages)
            .waitTimeSeconds(waitTimeSeconds)
            .build();
        return sqsClient.receiveMessage(request).messages();
    }
} 