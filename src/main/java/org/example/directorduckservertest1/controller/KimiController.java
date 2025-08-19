package org.example.directorduckservertest1.controller;

import com.google.gson.Gson;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.example.directorduckservertest1.dto.ChatMessage;
import org.example.directorduckservertest1.dto.KimiRequest;
import org.example.directorduckservertest1.dto.KimiResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class KimiController {

    private static final String API_KEY = "sk-9cczwKcItanDs2i6eUcg76QM4sDB0JchRcmVJKOUPlAjRhpt";
    private static final String MOONSHOT_URL = "https://api.moonshot.cn/v1/chat/completions";

    @PostMapping("/kimi")
    public String askKimi(@RequestBody KimiRequest request) throws IOException, IOException {
        OkHttpClient client = new OkHttpClient();

        List<Map<String, String>> messages = new ArrayList<>();
        messages.add(Map.of(
                "role", "system",
                "content", "你是鸭局长，一位专注于公务员考试辅导的人工智能助手，擅长答题解析、时政讲解和行测申论辅导，请以专业且亲切的语气帮助用户备考。"
        ));

        for (ChatMessage msg : request.getHistory()) {
            messages.add(Map.of("role", msg.getRole(), "content", msg.getContent()));
        }

        messages.add(Map.of("role", "user", "content", request.getQuestion()));

        Map<String, Object> body = new HashMap<>();
        body.put("model", "moonshot-v1-8k");
        body.put("temperature", 0.3);
        body.put("messages", messages);

        Gson gson = new Gson();
        okhttp3.RequestBody requestBody = okhttp3.RequestBody.create(
                gson.toJson(body),
                MediaType.parse("application/json")
        );


        Request moonshotRequest = new Request.Builder()
                .url(MOONSHOT_URL)
                .addHeader("Authorization", "Bearer " + API_KEY)
                .addHeader("Content-Type", "application/json")
                .post((okhttp3.RequestBody) requestBody)
                .build();

        Response moonshotResponse = client.newCall(moonshotRequest).execute();

        if (moonshotResponse.body() != null) {
            String responseString = moonshotResponse.body().string();
            KimiResponse kimiResponse = gson.fromJson(responseString, KimiResponse.class);
            return kimiResponse.getChoices().get(0).getMessage().getContent();
        }

        return "鸭局长暂时失联了，请稍后再试~";
    }
}
