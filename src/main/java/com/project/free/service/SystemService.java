package com.project.free.service;

import com.project.free.entity.*;
import com.project.free.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SystemService {

    private final UserEntityRepository userEntityRepository;
    private final BoardEntityRepository boardEntityRepository;
    private final CommentEntityRepository commentEntityRepository;
    private final LikesEntityRepository likesEntityRepository;
    private final PhotoEntityRepository photoEntityRepository;
    private final SellerEntityRepository sellerEntityRepository;
    private final ItemEntityRepository itemEntityRepository;

    @Scheduled(cron = "0 0 0 * * *")
    public void clearDeletedEntities() {
        LocalDateTime deletedTime = LocalDateTime.now().minusDays(30);

        List<UserEntity> userEntityList = userEntityRepository.findByIsDeletedAndDeletedAtBefore(true, deletedTime);
        userEntityRepository.deleteAll(userEntityList);

        List<BoardEntity> boardEntityList = boardEntityRepository.findByIsDeletedAndDeletedAtBefore(true, deletedTime);
        boardEntityRepository.deleteAll(boardEntityList);

        List<CommentEntity> commentEntityList = commentEntityRepository.findByIsDeletedAndDeletedAtBefore(true, deletedTime);
        commentEntityRepository.deleteAll(commentEntityList);

        List<LikesEntity> likesEntityList = likesEntityRepository.findByIsDeletedAndDeletedAtBefore(true, deletedTime);
        likesEntityRepository.deleteAll(likesEntityList);

        List<PhotoEntity> photoEntityList = photoEntityRepository.findByIsDeletedAndDeletedAtBefore(true, deletedTime);
        photoEntityRepository.deleteAll(photoEntityList);

        List<SellerEntity> sellerEntityList = sellerEntityRepository.findByIsDeletedAndDeletedAtBefore(true, deletedTime);
        sellerEntityRepository.deleteAll(sellerEntityList);

        List<ItemEntity> itemEntityList = itemEntityRepository.findByIsDeletedAndDeletedAtBefore(true, deletedTime);
        itemEntityRepository.deleteAll(itemEntityList);
    }
}
