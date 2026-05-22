package org.example.praktiktgbot.Model;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_requests")
@Getter
@Setter
public class UserRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long chatId;
    @Column(columnDefinition = "TEXT")
    private String message;

    @Column(columnDefinition = "TEXT")
    private String response;

    private LocalDateTime timestamp;

    public UserRequest() {}

    public UserRequest(Long chatId, String message, String response) {
        this.chatId = chatId;
        this.message = message;
        this.response = response;
        this.timestamp = LocalDateTime.now();
    }
}
