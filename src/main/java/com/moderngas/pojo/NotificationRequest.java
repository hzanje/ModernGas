package com.moderngas.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificationRequest {

    private String title;

    private String message;

    private String topic;

    private String token;

}
