package org.example.directorduckservertest1.service;

import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.example.directorduckservertest1.dto.DeepSeekDtos;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DeepSeekService {

    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    private final OkHttpClient okHttpClient;
    private final Gson gson = new Gson();

    @Value("${deepseek.base-url:https://api.deepseek.com}")
    private String baseUrl;

    @Value("${deepseek.api-key:}")
    private String apiKey;

    // 可配置：系统提示词 & 最大历史条数
    @Value("${deepseek.system-prompt:你是“鸭局长”，一个专业的公务员备考助教，用清晰、结构化的方式回答，必要时给出做题技巧和易错点。}")
    private String systemPrompt;

    @Value("${deepseek.max-history:20}")
    private int maxHistory;

    public DeepSeekDtos.ProxyChatResponse chat(DeepSeekDtos.ProxyChatRequest req) throws Exception {
        if (apiKey == null || apiKey.isBlank()) {
            throw new IllegalStateException("DeepSeek API Key 未配置：请在 application.properties 配置 deepseek.api-key");
        }

        String model = (req.getModel() == null || req.getModel().isBlank())
                ? "deepseek-chat"
                : req.getModel().trim();

        if (!model.equals("deepseek-chat") && !model.equals("deepseek-reasoner")) {
            throw new IllegalArgumentException("不支持的模型: " + model + "（仅支持 deepseek-chat / deepseek-reasoner）");
        }

        // 1) 组装 messages：system + (history截断) + (question可选)
        List<DeepSeekDtos.Message> messages = new ArrayList<>();

        // system 提示词（放最前面）
        if (systemPrompt != null && !systemPrompt.isBlank()) {
            messages.add(new DeepSeekDtos.Message("system", systemPrompt.trim()));
        }

        // history（只取最后 maxHistory 条，避免太长）
        if (req.getHistory() != null && !req.getHistory().isEmpty()) {
            List<DeepSeekDtos.Message> h = req.getHistory();
            int from = Math.max(0, h.size() - Math.max(0, maxHistory));
            for (int i = from; i < h.size(); i++) {
                DeepSeekDtos.Message m = h.get(i);
                if (m == null) continue;
                String role = safeRole(m.getRole());
                String content = m.getContent() == null ? "" : m.getContent().trim();
                if (content.isEmpty()) continue;
                messages.add(new DeepSeekDtos.Message(role, content));
            }
        }

        // question（允许不传：如果只传 history，也能工作）
        if (req.getQuestion() != null && !req.getQuestion().isBlank()) {
            messages.add(new DeepSeekDtos.Message("user", req.getQuestion().trim()));
        } else {
            // 如果 question 为空，至少要求 history 里有内容
            if (messages.size() <= 1) { // 只有system
                throw new IllegalArgumentException("question 不能为空（或至少传入 history 形成上下文）");
            }
        }

        DeepSeekDtos.ChatCompletionRequest dsReq = new DeepSeekDtos.ChatCompletionRequest();
        dsReq.setModel(model);
        dsReq.setMessages(messages);
        dsReq.setTemperature(0.7);
        dsReq.setStream(false);

        String url = normalizeBaseUrl(baseUrl) + "/chat/completions";
        String bodyJson = gson.toJson(dsReq);

        Request httpReq = new Request.Builder()
                .url(url)
                .addHeader("Authorization", "Bearer " + apiKey)
                .addHeader("Content-Type", "application/json")
                .post(RequestBody.create(bodyJson.getBytes(StandardCharsets.UTF_8), JSON))
                .build();

        try (Response httpResp = okHttpClient.newCall(httpReq).execute()) {
            String respStr = httpResp.body() != null ? httpResp.body().string() : "";

            if (!httpResp.isSuccessful()) {
                try {
                    DeepSeekDtos.ErrorResponse err = gson.fromJson(respStr, DeepSeekDtos.ErrorResponse.class);
                    if (err != null && err.getError() != null && err.getError().getMessage() != null) {
                        throw new RuntimeException("DeepSeek 调用失败: " + err.getError().getMessage());
                    }
                } catch (Exception ignore) {}

                throw new RuntimeException("DeepSeek HTTP " + httpResp.code() + ": " + respStr);
            }

            DeepSeekDtos.ChatCompletionResponse dsResp =
                    gson.fromJson(respStr, DeepSeekDtos.ChatCompletionResponse.class);

            String answer = "";
            if (dsResp != null
                    && dsResp.getChoices() != null
                    && !dsResp.getChoices().isEmpty()
                    && dsResp.getChoices().get(0).getMessage() != null) {
                answer = dsResp.getChoices().get(0).getMessage().getContent();
            }

            DeepSeekDtos.ProxyChatResponse out = new DeepSeekDtos.ProxyChatResponse();
            out.setModel(model);
            out.setAnswer(answer == null ? "" : answer);
            out.setRaw(respStr); // 稳定后建议去掉或只打印日志
            return out;
        }
    }

    private String safeRole(String role) {
        if (role == null) return "user";
        role = role.trim();
        if (role.equals("system") || role.equals("user") || role.equals("assistant")) return role;
        return "user";
    }

    private String normalizeBaseUrl(String s) {
        if (s == null || s.isBlank()) return "https://api.deepseek.com";
        if (s.endsWith("/")) return s.substring(0, s.length() - 1);
        return s;
    }
}
