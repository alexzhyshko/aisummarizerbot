package io.github.zhyshko.model;

import jakarta.persistence.*;
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
    @OneToOne(cascade=CascadeType.ALL)
    @JoinColumn(name = "token_balance_id", referencedColumnName = "id")
    private TokenBalance tokenBalance;

}
