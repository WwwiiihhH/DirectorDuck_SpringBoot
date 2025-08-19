package org.example.directorduckservertest1.dto;

import lombok.Data;
import java.util.List;

@Data
public class KimiRequest {
    private List<ChatMessage> history;
    private String question;
}
