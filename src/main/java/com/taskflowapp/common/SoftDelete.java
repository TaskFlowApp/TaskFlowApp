package com.taskflowapp.common;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;

@Getter
// 상속받은 엔티티에 deleted 컬럼 추가
@MappedSuperclass
public class SoftDelete {

    // Soft Delete용 컬럼
    @Column(name = "deleted", nullable = false)
    private boolean deleted;

    // Soft Delete 메서드
    public void softDelete() {
        this.deleted = true;
    }
}
