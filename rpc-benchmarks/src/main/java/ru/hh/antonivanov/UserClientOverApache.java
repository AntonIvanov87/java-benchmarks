package ru.hh.antonivanov;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;

import java.io.IOException;

class UserClientOverApache {

  private final HttpClient httpClient;
  private final ObjectMapper objectMapper;

  UserClientOverApache(HttpClient httpClient, ObjectMapper objectMapper) {
    this.httpClient = httpClient;
    this.objectMapper = objectMapper;
  }

  User getUser(int sleepMs) {
    HttpGet httpGet = new HttpGet("http://localhost:8081/user?sleepMs=" + sleepMs);
    HttpResponse response;
    try {
      response = httpClient.execute(httpGet);
    } catch (IOException e) {
      throw new RuntimeException("http request failed", e);
    }
    if (response.getStatusLine().getStatusCode() != 200) {
      throw new RuntimeException("got bad response code " + response.getStatusLine().getStatusCode());
    }
    try {
      return objectMapper.readValue(response.getEntity().getContent(), User.class);
    } catch (IOException e) {
      throw new RuntimeException("failed to deserialize json", e);
    }
  }
}
