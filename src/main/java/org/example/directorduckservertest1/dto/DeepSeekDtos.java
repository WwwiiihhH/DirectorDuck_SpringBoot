package org.example.directorduckservertest1.dto;

import lombok.Data;

import java.util.List;

public class DeepSeekDtos {

    // ====== 你自己后端对 Android 暴露的请求 ======
    @Data
    public static class ProxyChatRequest {
        /**
         * deepseek-chat 或 deepseek-reasoner
         */
        private String model;

        /**
         * 用户输入问题（最简单只传这一句）
         */
        private String question;

        /**
         * 可选：历史对话（以后你要多轮对话就用它）
         * role: system/user/assistant
         */
        private List<Message> history;
    }

    @Data
    public static class ProxyChatResponse {
        private String model;
        private String answer;

        // 可选：把原始返回留着排查问题（不建议长期给前端）
        private String raw;
    }

    // ====== DeepSeek /chat/completions 的请求体 ======
    @Data
    public static class ChatCompletionRequest {
        private String model;
        private List<Message> messages;
        private Double temperature;
        private Boolean stream;
    }

    @Data
    public static class Message {
        private String role;    // system/user/assistant
        private String content;

        public Message() {}
        public Message(String role, String content) {
            this.role = role;
            this.content = content;
        }
    }

    // ====== DeepSeek /chat/completions 的响应体（只写我们要用的字段） ======
    @Data
    public static class ChatCompletionResponse {
        private String id;
        private List<Choice> choices;
        private Usage usage;

        @Data
        public static class Choice {
            private int index;
            private ResponseMessage message;
            private String finish_reason;
        }

        @Data
        public static class ResponseMessage {
            private String role;
            private String content;

            // DeepSeek 文档里还有 reasoning_content 等字段（部分特性/模式会出现）
            private String reasoning_content;
        }

        @Data
        public static class Usage {
            private Integer prompt_tokens;
            private Integer completion_tokens;
            private Integer total_tokens;
        }
    }

    // DeepSeek 报错时可能返回的结构（兜底）
    @Data
    public static class ErrorResponse {
        private Error error;

        @Data
        public static class Error {
            private String message;
            private String type;
            private String code;
        }
    }
}
