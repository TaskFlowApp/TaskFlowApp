package com.taskflowapp.domain.activity.dto.response;

import com.taskflowapp.domain.activity.entity.Activity;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ActivityResponse {

    private final Long id;
    private final String type;
    // private final UserResponse user;
    // private final Long taskId;
    private final String description;
    private final LocalDateTime timestamp;

    public ActivityResponse(Activity activity) {
        this.id = activity.getId();
        this.type = activity.getActionType();
        this.description = activity.getContent();
        this.timestamp = activity.getCreatedAt();

        /*
        this.user = new UserResponse(activity.getUser());
        this.taskId = null;
         */
    }
}
