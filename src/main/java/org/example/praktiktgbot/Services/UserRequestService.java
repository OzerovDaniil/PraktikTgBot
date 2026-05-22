package org.example.praktiktgbot.Services;

import org.example.praktiktgbot.Model.UserRequest;
import org.example.praktiktgbot.Repo.UserRequestRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserRequestService {

    private final UserRequestRepository repository;

    public UserRequestService(UserRequestRepository repository) {
        this.repository = repository;
    }

    public void saveRequest(Long chatId, String message, String response) {
        UserRequest request = new UserRequest(chatId, message, response);
        repository.save(request);
    }

    public String getUserHistory(Long chatId) {
        List<UserRequest> requests = repository.findTop5ByChatIdOrderByTimestampDesc(chatId);

        if (requests.isEmpty()) {
            return "Історія повідомлень порожня";
        }

        return requests.stream()
                .map(req -> "👤: " + req.getMessage() + "\n🤖: " + req.getResponse())
                .collect(Collectors.joining("\n\n"));
    }
}
