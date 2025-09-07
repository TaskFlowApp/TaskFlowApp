package com.taskflowapp.domain.user.service;

import com.taskflowapp.domain.user.dto.response.AssigneeResponse;
import com.taskflowapp.domain.user.dto.response.UserResponse;
import com.taskflowapp.domain.user.entity.User;
import com.taskflowapp.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    // 현재 사용자 정보 조회
    @Transactional
    public UserResponse findUserInfo(String username) {
        User user = userRepository.findByUsernameAndDeletedFalse(username).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다.")
        );
        return new UserResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getName(),
                user.getRole(),    // DB에서 가져온 실제 역할 반환
                user.getCreatedAt()
        );
    }

    // 새 작업(Task) 등록시 담당자 조회
    @Transactional
    public List<AssigneeResponse> findAllUser() {
        List<User> users = userRepository.findByDeletedFalse();
        List<AssigneeResponse> responseList = new ArrayList<>();

        for (User user : users) {
            responseList.add(new AssigneeResponse(
                    user.getId(),
                    user.getEmail(),
                    user.getName(),
                    user.getRole()
            ));
        }

        return responseList;
    }
}