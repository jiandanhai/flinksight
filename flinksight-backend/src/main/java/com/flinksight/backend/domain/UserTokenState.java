package com.flinksight.backend.domain;

import jakarta.persistence.*;
import lombok.*;

import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;

@Entity
@Table(name = "user_token_state")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserTokenState {

    @Id
    @Column(name = "user_id", nullable = false, updatable = false)
    private Long userId;

    @Column(name = "token_version", nullable = false)
    private int tokenVersion;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    /** 行级版本，用于乐观锁并发控制 */
    @Version
    @Column(name = "row_version")
    private Long rowVersion;
}