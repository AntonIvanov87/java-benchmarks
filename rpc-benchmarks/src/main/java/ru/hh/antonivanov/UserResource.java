package ru.hh.antonivanov;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import java.io.IOException;

@Path("user")
public class UserResource {

  private final UserService userService;
  private final ObjectMapper objectMapper;

  UserResource(UserService userService, ObjectMapper objectMapper) {
    this.userService = userService;
    this.objectMapper = objectMapper;
  }

  @GET
  public void get(@QueryParam("sleepMs") int sleepMs, @Context HttpServletResponse response) throws InterruptedException, IOException {
    User user = userService.generate(sleepMs);

    response.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON);
    objectMapper.writeValue(response.getWriter(), user);
  }

}
