package com.project.free.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Where;

import java.util.ArrayList;
import java.util.List;

@Table(name = "comment")
@Entity
@Getter
@SuperBuilder
@ToString
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Where(clause = "is_deleted=false")
public class CommentEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, updatable = false, unique = true)
    private Long commentId;

    @Column(nullable = false, updatable = false, unique = true)
    private Long userId;

    @Column(nullable = false, updatable = false, unique = true)
    private Long boardId;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private String writer;

    @ToString.Exclude
    @OneToMany
    private List<CommentEntity> reply = new ArrayList<>();

    public void updateWriter(String writer) {
        this.writer = writer;
    }
}
