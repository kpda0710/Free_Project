package com.project.free.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Where;

import java.util.ArrayList;
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

    private String title;

    private String content;

    private String writer;

    private Long views;

    @ToString.Exclude
    @OneToMany
    private List<CommentEntity> comments = new ArrayList<>();

    @ToString.Exclude
    @OneToMany
    private List<LikesEntity> likes = new ArrayList<>();

    @ToString.Exclude
    @OneToMany
    private List<PhotoEntity> photos = new ArrayList<>();

    public void updateWriter(String writer) {
        this.writer = writer;
    }

    public void viewPlus() {
        this.views++;
    }

    public void uploadImage(PhotoEntity photoEntity) {
        this.photos.add(photoEntity);
    }
}
