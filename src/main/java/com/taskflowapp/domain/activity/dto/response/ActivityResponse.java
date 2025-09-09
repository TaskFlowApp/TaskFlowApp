package com.taskflowapp.domain.activity.dto.response;

import com.taskflowapp.domain.activity.entity.Activity;
import com.taskflowapp.domain.user.dto.response.UserResponse;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ActivityResponse {

    private final Long id;
    private final String type;
    private final Long userId;
    private final UserResponse user;
    private final Long taskId;
    private final String description;
    private final LocalDateTime timestamp;

    public ActivityResponse(Activity activity) {
        this.id = activity.getId();
        this.type = activity.getActionType();
        this.description = activity.getContent();
        this.timestamp = activity.getCreatedAt();

        this.user = new UserResponse(activity.getUser());
        this.userId = activity.getUser().getId();
        this.taskId = activity.getTask() != null ? activity.getTask().getId() : null;

    }
}
