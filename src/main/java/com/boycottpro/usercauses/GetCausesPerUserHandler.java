package com.boycottpro.usercauses;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;

import com.boycottpro.usercauses.model.CauseSummary;
import com.boycottpro.utilities.JwtUtility;
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
        try {
            String sub = JwtUtility.getSubFromRestEvent(event);
            if (sub == null) return response(401, "Unauthorized");
            List<CauseSummary> causes = getUserFollowedCauses(sub);
            String responseBody = objectMapper.writeValueAsString(causes);
            return response(200,responseBody);
        } catch (Exception e) {
            e.printStackTrace();
            return response(500,"error : Unexpected server error: " + e.getMessage());
        }
    }

    private APIGatewayProxyResponseEvent response(int status, String body) {
        return new APIGatewayProxyResponseEvent()
                .withStatusCode(status)
                .withHeaders(Map.of("Content-Type", "application/json"))
                .withBody(body);
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