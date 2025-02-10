package com.project.free.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Where;

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
    private Long commentId;

    private Long userId;

    private Long boardId;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private String writer;

    @ToString.Exclude
    @OneToMany
    private List<CommentEntity> reply;

    public void updateWriter(String writer) {
        this.writer = writer;
    }
}
