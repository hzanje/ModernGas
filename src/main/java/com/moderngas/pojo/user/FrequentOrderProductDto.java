package com.moderngas.pojo.user;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class FrequentOrderProductDto {

    private boolean isAvailable;

    private Long gasId;

    private String gasName;

    private String categoryName;

    public FrequentOrderProductDto(boolean isAvailable, Long gasId, String gasName, String categoryName) {
        this.isAvailable = isAvailable;
        this.gasId = gasId;
        this.gasName = gasName;
        this.categoryName = categoryName;
    }
}
