package io.github.zhyshko.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="users")
public class User {

    @Id
    private Long id;
    private String username;
    private String firstName;
    private String lastName;
    private String languageCode;
    private UserRole userRole = UserRole.USER;

}
