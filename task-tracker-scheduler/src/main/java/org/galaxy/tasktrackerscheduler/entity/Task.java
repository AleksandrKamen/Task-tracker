package org.galaxy.tasktrackerscheduler.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "tasks", schema = "app_data")
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String title;
    String description;
    @Builder.Default
    Boolean iscompleted = false;
    @CreationTimestamp
    LocalDateTime createdAt;
    LocalDateTime completed_at;
    @JoinColumn(name = "id_user")
    @ManyToOne(fetch = FetchType.LAZY)
    User user;
}
