package org.galaxy.tasktrackerapi.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "users", schema = "app_data")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @Column(nullable = false, unique = true)
    @Email
    String username;
    @NotNull
    @Column(nullable = false)
    String password;
    @Enumerated(EnumType.STRING)
    Role role;
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    List<Task> tasks;

}
