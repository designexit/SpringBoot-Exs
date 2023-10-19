package com.example.teamprojectmk.service;

import com.example.teamprojectmk.entity.LectureImg;
import com.example.teamprojectmk.repository.LectureImgRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.StringUtils;

import javax.persistence.EntityNotFoundException;

@Service
@RequiredArgsConstructor
@Transactional
public class LectureImgService {

    @Value("${lectureImgLocation}")
    private String lectureImgLocation;

    private final LectureImgRepository lectureImgRepository;

    private final FileService fileService;

    public void saveLectureImg(LectureImg lectureImg, MultipartFile lectureImgFile) throws Exception{
        String oriImgName = lectureImgFile.getOriginalFilename();
        String imgName = "";
        String imgUrl = "";

        //파일 업로드
        if(!StringUtils.isEmpty(oriImgName)){
            imgName = fileService.uploadFile(lectureImgLocation, oriImgName,
                    lectureImgFile.getBytes());
            imgUrl = "/images/lecture/" + imgName;
        }

        //상품 이미지 정보 저장
        lectureImg.updateLectureImg(oriImgName, imgName, imgUrl);
        lectureImgRepository.save(lectureImg);
    }

    public void updateLectureImg(Long lectureImgId, MultipartFile lectureImgFile) throws Exception{
        if(!lectureImgFile.isEmpty()){
            LectureImg savedLectureImg = lectureImgRepository.findById(lectureImgId)
                    .orElseThrow(EntityNotFoundException::new);

            //기존 이미지 파일 삭제
            if(!StringUtils.isEmpty(savedLectureImg.getImgName())) {
                fileService.deleteFile(lectureImgLocation+"/"+
                        savedLectureImg.getImgName());
            }

            String oriImgName = lectureImgFile.getOriginalFilename();
            String imgName = fileService.uploadFile(lectureImgLocation, oriImgName, lectureImgFile.getBytes());
            String imgUrl = "/images/lecture/" + imgName;
            savedLectureImg.updateLectureImg(oriImgName, imgName, imgUrl);
        }
    }
}
