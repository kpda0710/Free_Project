package com.project.free.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Where;

@Table(name = "likes")
@Entity
@Getter
@SuperBuilder
@ToString
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Where(clause = "is_deleted=false")
public class LikesEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, updatable = false, unique = true)
    private Long likesId;

    @Column(nullable = false, updatable = false)
    private Long boardId;

    @Column(nullable = false, updatable = false)
    private Long userId;
}
