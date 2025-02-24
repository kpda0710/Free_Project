package com.project.free.service;

import com.project.free.dto.image.PhotoResponse;
import com.project.free.entity.BoardEntity;
import com.project.free.entity.ItemEntity;
import com.project.free.entity.PhotoEntity;
import com.project.free.entity.PhotoStatus;
import com.project.free.exception.BaseException;
import com.project.free.exception.ResponseCode;
import com.project.free.repository.BoardEntityRepository;
import com.project.free.repository.ItemEntityRepository;
import com.project.free.repository.PhotoEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PhotoService {

    private final PhotoEntityRepository photoEntityRepository;
    private final BoardEntityRepository boardEntityRepository;
    private final ItemEntityRepository itemEntityRepository;

    @Transactional
    public List<PhotoResponse> uploadImageByBoard(Long boardId, List<MultipartFile> images, Authentication authentication) {
        List<PhotoEntity> imageEntities = new ArrayList<>();
        try {
            String uploadsDir = "src/main/resources/static/uploads/thumbnails/";

            for (MultipartFile image : images) {
                String dbFilePath = saveImage(image, uploadsDir);
                PhotoEntity photoEntity = PhotoEntity.builder()
                        .targetId(boardId)
                        .photoPath(dbFilePath)
                        .isDeleted(false)
                        .photoStatus(PhotoStatus.BOARD)
                        .build();

                BoardEntity boardEntity = boardEntityRepository.findById(boardId).orElseThrow(() -> new BaseException(ResponseCode.BOARD_NOT_FOUND));
                PhotoEntity saved = photoEntityRepository.save(photoEntity);
                boardEntity.uploadImage(saved);
                imageEntities.add(saved);
                boardEntityRepository.save(boardEntity);
            }
            return imageEntities.stream().map(entity -> PhotoResponse.builder()
                    .photoId(entity.getPhotoId())
                    .targetId(entity.getTargetId())
                    .photoPath(entity.getPhotoPath())
                    .photoStatus(PhotoStatus.BOARD)
                    .build()).collect(Collectors.toList());
        } catch (IOException e) {
            throw new BaseException(ResponseCode.FILE_SAVE_ERROR);
        }
    }

    @Transactional
    public List<PhotoResponse> uploadImageByItem(Long itemId, List<MultipartFile> images, Authentication authentication) {
        List<PhotoEntity> imageEntities = new ArrayList<>();
        try {
            String uploadsDir = "src/main/resources/static/uploads/thumbnails/";

            for (MultipartFile image : images) {
                String dbFilePath = saveImage(image, uploadsDir);
                PhotoEntity photoEntity = PhotoEntity.builder()
                        .targetId(itemId)
                        .photoPath(dbFilePath)
                        .isDeleted(false)
                        .photoStatus(PhotoStatus.ITEM)
                        .build();

                ItemEntity itemEntity = itemEntityRepository.findById(itemId).orElseThrow(() -> new BaseException(ResponseCode.ITEM_NOT_FOUND));
                PhotoEntity saved = photoEntityRepository.save(photoEntity);
                itemEntity.uploadImage(saved);
                imageEntities.add(saved);
                itemEntityRepository.save(itemEntity);
            }
            return imageEntities.stream().map(entity -> PhotoResponse.builder()
                    .photoId(entity.getPhotoId())
                    .targetId(entity.getTargetId())
                    .photoPath(entity.getPhotoPath())
                    .photoStatus(PhotoStatus.ITEM)
                    .build()).collect(Collectors.toList());
        } catch (IOException e) {
            throw new BaseException(ResponseCode.FILE_SAVE_ERROR);
        }
    }

    @Transactional
    // 파일 삭제
    public void deleteImage(Long photoId, Authentication authentication) {
        PhotoEntity photoEntity = photoEntityRepository.findById(photoId).orElseThrow(() -> new BaseException(ResponseCode.FILE_NOT_FOUND));
        BoardEntity boardEntity = boardEntityRepository.findById(photoEntity.getTargetId()).orElseThrow(() -> new BaseException(ResponseCode.BOARD_NOT_FOUND));

        boardEntity.getPhotos().remove(photoId);
        photoEntity.deleteSetting();

        photoEntityRepository.save(photoEntity);
        boardEntityRepository.save(boardEntity);
    }

    @Transactional
    // 파일 아이디로 파일 삭제 - 어드민 전용
    public void deleteImageById(Long photoId, Authentication authentication) {
        PhotoEntity photoEntity = photoEntityRepository.findById(photoId).orElseThrow(() -> new BaseException(ResponseCode.FILE_NOT_FOUND));
        BoardEntity boardEntity = boardEntityRepository.findById(photoEntity.getTargetId()).orElseThrow(() -> new BaseException(ResponseCode.BOARD_NOT_FOUND));

        boardEntity.getPhotos().remove(photoId);
        photoEntity.deleteSetting();

        photoEntityRepository.save(photoEntity);
        boardEntityRepository.save(boardEntity);
    }

    private String saveImage(MultipartFile image, String uploadsDir) throws IOException {
        String filename = UUID.randomUUID().toString().replace("-", "") + "-" + image.getOriginalFilename();
        String filePath = uploadsDir + filename;
        String dbFilePath = "/uploads/thumbnails/" + filename;

        Path path = Paths.get(filePath);
        Files.createDirectories(path.getParent());
        Files.write(path, image.getBytes());

        return dbFilePath;
    }
}
