package com.dusan.taxiservice.web.api.websocket;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SocketDestinations {

    public static final String APP_PREFIX = "/app";
    public static final String QUEUE_PREFIX = "/queue";
    public static final String TOPIC_PREFIX = "/topic";
    public static final String USER_PREFIX = "/user";

    public static final String CREATED_RIDE = "/created-ride";
    public static final String UPDATED_RIDE = "/updated-ride";
    public static final String CANCELED_RIDE = "/canceled-ride";
    public static final String FORMED_RIDE = "/formed-ride";
    public static final String PROCESSED_RIDE = "/processed-ride";
    public static final String ACCEPTED_RIDE = "/accepted-ride";
    public static final String FAILED_RIDE = "/failed-ride";
    public static final String SUCCESSFUL_RIDE = "/successful-ride";
    public static final String ERROR = "/error";
}
