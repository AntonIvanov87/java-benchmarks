package ru.hh.antonivanov;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.Response;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

class UserClientOverAsync {

  private final AsyncHttpClient asyncHttpClient;
  private final Executor executor;
  private final ObjectMapper objectMapper;

  UserClientOverAsync(AsyncHttpClient asyncHttpClient, Executor executor, ObjectMapper objectMapper) {
    this.asyncHttpClient = asyncHttpClient;
    this.executor = executor;
    this.objectMapper = objectMapper;
  }

  CompletableFuture<User> getUser(int sleepMs) {
    CompletableFuture<Response> responseFuture =
        asyncHttpClient
            .prepareGet("http://localhost:8081/user?sleepMs=" + sleepMs)
            .execute()
            .toCompletableFuture();
    return responseFuture
               .thenApplyAsync(response -> {
                 if (response.getStatusCode() != 200) {
                   throw new RuntimeException("got bad response code " + response.getStatusCode());
                 }
                 byte[] bytes = response.getResponseBodyAsBytes();
                 try {
                   return objectMapper.readValue(bytes, User.class);
                 } catch (IOException e) {
                   throw new RuntimeException(e);
                 }
               }, executor);

  }
}
