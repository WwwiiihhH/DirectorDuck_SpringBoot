package org.example.directorduckservertest1.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import okhttp3.*;
import org.example.directorduckservertest1.dto.PracticeCommentReq;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class DeepSeekCommentService {

    @Value("${deepseek.base-url}")
    private String baseUrl;

    @Value("${deepseek.api-key}")
    private String apiKey;

    @Value("${deepseek.timeout-seconds:60}")
    private long timeoutSeconds;

    private final ObjectMapper om = new ObjectMapper();

    public String generatePracticeComment(PracticeCommentReq req) {
        OkHttpClient client = new OkHttpClient.Builder()
                .callTimeout(timeoutSeconds, TimeUnit.SECONDS)
                .connectTimeout(timeoutSeconds, TimeUnit.SECONDS)
                .readTimeout(timeoutSeconds, TimeUnit.SECONDS)
                .writeTimeout(timeoutSeconds, TimeUnit.SECONDS)
                .build();

        try {
            String prompt = buildPrompt(req);

            ObjectNode body = om.createObjectNode();
            body.put("model", "deepseek-chat"); // 或 deepseek-reasoner/deepseek-chat

            ArrayNode messages = body.putArray("messages");

//            ObjectNode sys = messages.addObject();
//            sys.put("role", "system");
//            sys.put("content",
//                    "你是公务员行测学习教练。请基于用户一次练习的数据，分析用户练习情况，做出真实、准确的点评。"
//                            + "要求：不超过250字；包含：一句总体评价 + 2条优势 + 2条改进建议 + 下一步行动(1条)。"
//            );

            ObjectNode sys = messages.addObject();
            sys.put("role", "system");
            sys.put("content",
                    "你是“鸭局长”，一位公务员行测学习教练与阅卷官。你只依据用户本次练习提供的数据进行分析，"
                            + "编造未给出的信息。点评要真实、具体、可执行，避免空泛鼓励。\n"
                            + "输出要求（总字数≤250字，中文）：\n"
                            + "1）总体评价：1句（结合正确率、用时和联系模块题目，给出客观结论）\n"
                            + "2）优势：2条（用“•”列出，必须与用户提供的练习相关）\n"
                            + "3）改进建议：2条（用“•”列出，给出可操作的方法，如审题、排除、时间分配、错因复盘）\n"
                            + "4）下一步行动：1条（明确到下一次训练怎么做，包含题量/题型/复盘步骤）\n"
                            + "语气：专业但带一点生动风格”。"
            );


            ObjectNode user = messages.addObject();
            user.put("role", "user");
            user.put("content", prompt);

            Request request = new Request.Builder()
                    .url(baseUrl + "/chat/completions")
                    .addHeader("Authorization", "Bearer " + apiKey)
                    .addHeader("Content-Type", "application/json")
                    .post(RequestBody.create(
                            body.toString(),
                            MediaType.parse("application/json")
                    ))
                    .build();

            try (Response response = client.newCall(request).execute()) {
                if (!response.isSuccessful()) {
                    return "AI点评生成失败：HTTP " + response.code();
                }

                String json = response.body() != null ? response.body().string() : "";
                JsonNode root = om.readTree(json);
                String text = root.path("choices").path(0).path("message").path("content").asText("");

                return text.isBlank() ? "AI点评生成失败：空响应" : text.trim();
            }

        } catch (Exception e) {
            return "AI点评生成异常：" + e.getMessage();
        }
    }

    private String buildPrompt(PracticeCommentReq r) {
        StringBuilder sb = new StringBuilder();
        sb.append("练习分类：").append(r.getCategoryName()).append("\n");
        sb.append("总题数：").append(r.getTotalQuestions())
                .append("，正确：").append(r.getCorrectCount())
                .append("，错误：").append(r.getIncorrectCount())
                .append("，未答：").append(r.getUnansweredCount())
                .append("，正确率：").append(r.getCorrectRate()).append("%\n");
        sb.append("总用时：").append(r.getTimeSpentSeconds()).append("秒\n");

        if (r.getTopSlowQuestions() != null && !r.getTopSlowQuestions().isEmpty()) {
            sb.append("最耗时题：");
            for (int i = 0; i < r.getTopSlowQuestions().size(); i++) {
                PracticeCommentReq.SlowQuestion q = r.getTopSlowQuestions().get(i);
                sb.append("[").append(q.getQuestionId()).append(":").append(q.getSeconds()).append("秒]");
                if (i != r.getTopSlowQuestions().size() - 1) sb.append("、");
            }
            sb.append("\n");
        }

        sb.append("错题uuid数量：").append(r.getWrongUuids() == null ? 0 : r.getWrongUuids().size()).append("\n");
        sb.append("请给出点评。");
        return sb.toString();
    }
}
