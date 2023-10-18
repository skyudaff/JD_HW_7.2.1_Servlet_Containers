package org.example.servlet;


import org.example.controller.PostController;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;


import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class MainServlet extends HttpServlet {
    private PostController controller;
    private static final String GET = "GET";
    private static final String POST = "POST";
    private static final String DELETE = "DELETE";
    private static final String API_POSTS = "/api/posts";
    private static final String API_POSTS_REGEX = "/api/posts/\\d+";


    @Override
    public void init() {
        final var context = new AnnotationConfigApplicationContext("org.example");
        controller = context.getBean(PostController.class);
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) {
        try {
            var path = req.getRequestURI();
            var method = req.getMethod();

            if (method.equals(GET) && path.equals(API_POSTS)) {
                controller.all(resp);
                return;
            }
            if (method.equals(GET) && path.matches(API_POSTS_REGEX)) {
                final var id = parseLong(path);
                controller.getById(id, resp);
                return;
            }
            if (method.equals(POST) && path.equals(API_POSTS)) {
                controller.save(req.getReader(), resp);
                return;
            }
            if (method.equals(DELETE) && path.matches(API_POSTS_REGEX)) {
                final var id = parseLong(path);
                controller.removeById(id, resp);
                return;
            }
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    private Long parseLong(String path) {
        return Long.parseLong(path.substring(path.lastIndexOf("/") + 1));
    }
}
