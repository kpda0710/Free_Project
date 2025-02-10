package com.project.free.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Where;

import java.util.List;

@Table(name = "board")
@Entity
@Getter
@SuperBuilder
@ToString
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Where(clause = "is_deleted=false")
public class BoardEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long boardId;

    private Long userId;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private String writer;

    @Column(nullable = false)
    private Long views;

    @ToString.Exclude
    @OneToMany
    private List<CommentEntity> comments;

    @ToString.Exclude
    @OneToMany
    private List<LikesEntity> likes;

    public void updateWriter(String writer) {
        this.writer = writer;
    }

    public void viewPlus() {
        this.views++;
    }
}
