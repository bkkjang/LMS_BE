package com.aivle.bookapp.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "genre")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class Genre {

    @Id
    @Column(nullable = false, length = 20)
    private String code;

    @Column(nullable = false, length = 100)
    private String label;

    @Column(name = "parent_code", length = 20)
    private String parentCode;
}
