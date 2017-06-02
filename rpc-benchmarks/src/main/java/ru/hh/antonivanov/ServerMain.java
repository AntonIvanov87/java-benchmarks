package ru.hh.antonivanov;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.jersey.spi.container.servlet.ServletContainer;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import javax.servlet.Servlet;
import javax.ws.rs.core.Application;
import java.util.Set;

import static java.util.Collections.singleton;

class ServerMain {

  public static void main(String[] args) throws Exception {
    UserService userService = new UserService();
    ObjectMapper objectMapper = ObjectMapperFactory.create();
    UserResource userResource = new UserResource(userService, objectMapper);
    Application application = createApplication(userResource);
    Servlet servlet = new ServletContainer(application);
    Handler requestsHandler = createHandler(servlet);
    Server server = new Server(8081);
    server.setHandler(requestsHandler);
    server.start();
  }

  private static Application createApplication(Object resource) {
    return new Application() {
      @Override
      public Set<Object> getSingletons() {
        return singleton(resource);
      }
    };
  }

  private static Handler createHandler(Servlet mainServlet) {
    ServletHolder servletHolder = new ServletHolder("MainServlet", mainServlet);

    ServletContextHandler servletContextHandler = new ServletContextHandler();
    servletContextHandler.addServlet(servletHolder, "/*");
    return servletContextHandler;
  }

  private ServerMain() {
  }
}
