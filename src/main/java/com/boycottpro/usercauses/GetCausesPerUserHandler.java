package com.boycottpro.usercauses;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;

import com.boycottpro.usercauses.model.CauseSummary;
import com.boycottpro.utilities.JwtUtility;
import com.boycottpro.utilities.Logger;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.*;

import java.util.*;
import java.util.stream.Collectors;

public class GetCausesPerUserHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    private static final String TABLE_NAME = "";
    private final DynamoDbClient dynamoDb;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public GetCausesPerUserHandler() {
        this.dynamoDb = DynamoDbClient.create();
    }

    public GetCausesPerUserHandler(DynamoDbClient dynamoDb) {
        this.dynamoDb = dynamoDb;
    }

    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent event, Context context) {
        String sub = null;
        int lineNum = 36;
        try {
            sub = JwtUtility.getSubFromRestEvent(event);
            if (sub == null) {
            Logger.error(39, sub, "user is Unauthorized");
            return response(401, Map.of("message", "Unauthorized"));
            }
            lineNum = 42;
            List<CauseSummary> causes = getUserFollowedCauses(sub);
            lineNum = 45;
            return response(200,causes);
        } catch (Exception e) {
            Logger.error(lineNum, sub, e.getMessage());
            return response(500,Map.of("error", "Unexpected server error: " + e.getMessage()) );
        }
    }

    private APIGatewayProxyResponseEvent response(int status, Object body) {
        String responseBody = null;
        try {
            responseBody = objectMapper.writeValueAsString(body);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return new APIGatewayProxyResponseEvent()
                .withStatusCode(status)
                .withHeaders(Map.of("Content-Type", "application/json"))
                .withBody(responseBody);
    }

    public List<CauseSummary> getUserFollowedCauses(String userId) {
        QueryRequest request = QueryRequest.builder()
                .tableName("user_causes")
                .keyConditionExpression("user_id = :uid")
                .expressionAttributeValues(Map.of(":uid", AttributeValue.fromS(userId)))
                .projectionExpression("cause_id, cause_desc")
                .build();

        QueryResponse response = dynamoDb.query(request);

        return response.items().stream()
                .map(item -> new CauseSummary(
                        item.get("cause_id").s(),
                        item.getOrDefault("cause_desc", AttributeValue.fromS("")).s()
                ))
                .collect(Collectors.toList());
    }

}