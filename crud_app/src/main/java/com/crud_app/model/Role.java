package com.crud_app.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter @Setter @Builder @AllArgsConstructor
@NoArgsConstructor
@Table(name = "role")
public class Role {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING)
    private ERole roleName;


    public Role(ERole roleName) {
        this.roleName = roleName;
    }
}
