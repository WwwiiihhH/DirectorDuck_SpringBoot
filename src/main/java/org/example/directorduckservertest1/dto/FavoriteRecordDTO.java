package org.example.directorduckservertest1.dto;

import lombok.Data;
import java.sql.Timestamp;

@Data
public class FavoriteRecordDTO {
    private String questionUuid;
    private Timestamp createdAt;
}
