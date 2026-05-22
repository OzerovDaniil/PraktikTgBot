package org.example.praktiktgbot.Repo;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class WebHookUpdateResponse{
    private boolean ok;
    private boolean result;
    private String description;
}