package org.example.controller;

import com.google.gson.Gson;
import org.example.model.Post;
import org.example.service.PostService;
import org.springframework.stereotype.Controller;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;

@Controller
public class PostController {
    public static final String APPLICATION_JSON = "application/json";
    private final PostService service;


    public PostController(PostService service) {
        this.service = service;
    }

    public void all(HttpServletResponse response) throws IOException {
        response.setContentType(APPLICATION_JSON);
        var data = service.all();
        var gson = new Gson();
        response.getWriter().print(gson.toJson(data));
    }

    public void getById(long id, HttpServletResponse response) throws IOException {
        response.setContentType(APPLICATION_JSON);
        var data = service.getById(id);
        var gson = new Gson();
        if (data != null) {
            writeJsonResponse(response.getWriter(), gson, data);
        } else {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    public void save(Reader body, HttpServletResponse response) throws IOException {
        response.setContentType(APPLICATION_JSON);
        var gson = new Gson();
        var post = gson.fromJson(body, Post.class);
        var data = service.save(post);
        response.getWriter().print(gson.toJson(data));
    }

    public void removeById(long id, HttpServletResponse response) {
        service.removeById(id);
        response.setStatus(HttpServletResponse.SC_OK);
    }

    private void writeJsonResponse(Writer writer, Gson gson, Object data) throws IOException {
        writer.write(gson.toJson(data));
    }
}