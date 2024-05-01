package org.galaxy.tasktrackerapi.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @NotNull
    @Size(min = 3, max = 50)
    String title;
    @Size(max = 1000)
    String description;
    Boolean iscomplited;
    @CreationTimestamp
    LocalDateTime createdAt;
    LocalDateTime completed_at;
    @ManyToOne
    @JoinColumn(name = "id_user")
    User user;
}
