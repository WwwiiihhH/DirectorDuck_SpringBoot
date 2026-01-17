package org.example.directorduckservertest1.dto;

import lombok.Data;
import java.util.List;

@Data
public class FavoritePageDTO<T> {
    private long total;
    private int page;
    private int size;
    private List<T> list;
}
