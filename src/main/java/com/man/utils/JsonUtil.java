package com.man.utils;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class JsonUtil {
    public static void printToJson(Object object, HttpServletResponse response) throws IOException {
        ObjectMapper objectMapper= new ObjectMapper();
        String json = objectMapper.writeValueAsString(object);
        response.setContentType("application/json;charset=utf-8");
        // 获取输出流并将 JSON 字符串写入到输出流中
        PrintWriter writer = response.getWriter();
        writer.write(json);
        writer.flush();
    }
}
