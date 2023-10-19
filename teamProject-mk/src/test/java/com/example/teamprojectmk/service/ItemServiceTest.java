package com.example.teamprojectmk.service;


import com.example.teamprojectmk.constant.ItemSellStatus;
import com.example.teamprojectmk.dto.ItemFormDto;
import com.example.teamprojectmk.entity.Item;
import com.example.teamprojectmk.entity.ItemImg;
import com.example.teamprojectmk.repository.ItemImgRepository;
import com.example.teamprojectmk.repository.ItemRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
@TestPropertySource(locations="classpath:application-test.properties")
class ItemServiceTest {

    @Autowired
    ItemService itemService;

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    ItemImgRepository itemImgRepository;

    //일반 파일 데이터 전달시 멀티파트로 전달하는 부분

    // createMultipartFiles -> 파일데이터 5개를 담은 리스트를 반환해 주는 메서드
    // 더미데이터 생성
    List<MultipartFile> createMultipartFiles() throws Exception{
        // 멀티파트 파일만 담을 임시 리스트 생성
        List<MultipartFile> multipartFileList = new ArrayList<>();

        for(int i=0;i<5;i++){
            // 임시 저장소 경로 및 파일면 확장자까지 지정
            String path = "C:/shop/item/";
            String imageName = "image" + i + ".jpg";
            MockMultipartFile multipartFile =
                    new MockMultipartFile(path, imageName, "image/jpg", new byte[]{1,2,3,4});
            multipartFileList.add(multipartFile);
        }

        return multipartFileList;
    }

    @Test
    @DisplayName("상품 등록 테스트")
    @WithMockUser(username = "admin", roles = "ADMIN")
    void saveItem() throws Exception {
        // 엔티티용 클래스와 전달용 DTO클래스
        // 일반 데이터 - 문자열
        ItemFormDto itemFormDto = new ItemFormDto();
        itemFormDto.setItemNm("테스트상품");
        itemFormDto.setItemSellStatus(ItemSellStatus.SELL);
        itemFormDto.setItemDetail("테스트 상품 입니다.");
        itemFormDto.setPrice(1000);
        itemFormDto.setStockNumber(100);

        // 정의된 메서드를 통해서 더미데이터 5개 생성 (멀티파트 타입을 요소로 가지는 리스트)
        List<MultipartFile> multipartFileList = createMultipartFiles();
        // itemFormDto : 일반 데이터 (더미)
        // multipartFileList :  파일 데이터 5개 (더미)
        // 영속성 컨텍스트에 영속화 (중간 테이블에 등록)
        Long itemId = itemService.saveItem(itemFormDto, multipartFileList);
        //findByItemIdOrderByIdAsc : 상품 아이디 검색, 아이디 오름차순, 등록순으로 정렬
        // 해당 아이템의 이미지들을 담은 리스트에 담기
        List<ItemImg> itemImgList = itemImgRepository.findByItemIdOrderByIdAsc(itemId);

        //등록된 상품의 아이디를 통해서 검색
        // 상품을 등록하기 위한 입력폼 (DTO) 사용중
        // 2개를 비교 -> 입력내용과 중간테이블 저장 내용과 일관성 확인
        Item item = itemRepository.findById(itemId)
                .orElseThrow(EntityNotFoundException::new);

        assertEquals(itemFormDto.getItemNm(), item.getItemNm());
        assertEquals(itemFormDto.getItemSellStatus(), item.getItemSellStatus());
        assertEquals(itemFormDto.getItemDetail(), item.getItemDetail());
        assertEquals(itemFormDto.getPrice(), item.getPrice());
        assertEquals(itemFormDto.getStockNumber(), item.getStockNumber());
        assertEquals(multipartFileList.get(0).getOriginalFilename(), itemImgList.get(0).getOriImgName());
    }

}