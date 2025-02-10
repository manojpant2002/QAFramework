package com.lseg.digital.framework.qa.util;

import software.amazon.awssdk.services.sqs.model.*;
import java.util.List;
import java.util.concurrent.*;
import software.amazon.awssdk.services.sqs.model.SendMessageResponse;
import com.lseg.digital.framework.qa.util.SqsUtils;
import java.util.stream.IntStream;

public class ConcurrentSqsUtils {
    private final SqsUtils sqsUtils;
    private final ExecutorService executor;
    
    public ConcurrentSqsUtils(String region, int threadPoolSize) {
        this.sqsUtils = new SqsUtils(region);
        this.executor = Executors.newFixedThreadPool(threadPoolSize);
    }

    // Parallel message sending
    public Future<SendMessageResponse> sendMessageAsync(String queueUrl, String messageBody) {
        return executor.submit(() -> sqsUtils.sendMessage(queueUrl, messageBody));
    }

    // Batch message sending
    public Future<List<SendMessageResponse>> sendMessagesAsync(String queueUrl, List<String> messages) {
        return executor.submit(() -> messages.parallelStream()
            .map(msg -> sqsUtils.sendMessage(queueUrl, msg))
            .toList());
    }

    // Parallel message processing
    public Future<List<Message>> processMessagesAsync(String queueUrl, int maxMessages, int waitTimeSeconds) {
        return executor.submit(() -> {
            List<Message> messages = sqsUtils.receiveMessages(queueUrl, maxMessages, waitTimeSeconds);
            messages.parallelStream().forEach(message -> 
                sqsUtils.deleteMessage(queueUrl, message.receiptHandle())
            );
            return messages;
        });
    }

    // Shutdown hook
    public void shutdown() {
        executor.shutdown();
        try {
            if (!executor.awaitTermination(5, TimeUnit.SECONDS)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
} 