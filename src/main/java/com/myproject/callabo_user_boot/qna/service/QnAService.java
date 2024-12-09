package com.myproject.callabo_user_boot.qna.service;

import com.myproject.callabo_user_boot.creator.domain.CreatorEntity;
import com.myproject.callabo_user_boot.creator.repository.CreatorRepository;
import com.myproject.callabo_user_boot.customer.domain.CustomerEntity;
import com.myproject.callabo_user_boot.customer.repository.CustomerRepository;
import com.myproject.callabo_user_boot.product.domain.ProductEntity;
import com.myproject.callabo_user_boot.product.repository.ProductRepository;
import com.myproject.callabo_user_boot.qna.domain.QnAEntity;
import com.myproject.callabo_user_boot.qna.domain.QnAImageEntity;
import com.myproject.callabo_user_boot.qna.dto.*;
import com.myproject.callabo_user_boot.qna.repository.QnARepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.myproject.callabo_user_boot.qna.domain.QQnAEntity.qnAEntity;

@Service
@Transactional
@RequiredArgsConstructor
@Log4j2
public class QnAService {

    private final QnARepository qnARepository;
    private final ProductRepository productRepository;
    private final CreatorRepository creatorRepository;
    private final CustomerRepository customerRepository;

    // qna 등록
    public Long registerQnA(QnARegisterDTO qnARegisterDTO) {
        log.info("QnA 등록 요청: {}", qnARegisterDTO);

        // ProductEntity와 CreatorEntity 조회
        ProductEntity product = productRepository.findById(qnARegisterDTO.getProductNo())
                .orElseThrow(() -> new RuntimeException("Product not found"));

        CreatorEntity creator = creatorRepository.findById(qnARegisterDTO.getCreatorId())
                .orElseThrow(() -> new RuntimeException("Creator not found"));

        CustomerEntity customer = customerRepository.findById(qnARegisterDTO.getCustomerId())
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        // QnAEntity 생성
        QnAEntity qnaEntity = addQnAEntity(qnARegisterDTO, customer, product, creator);

        qnARepository.save(qnaEntity);

        log.info("QnA 등록 완료 ID: {}", qnaEntity.getQnaNo());
        return qnaEntity.getQnaNo();
    }

    private QnAEntity addQnAEntity(QnARegisterDTO dto, CustomerEntity customer, ProductEntity product, CreatorEntity creator) {

        return QnAEntity.builder()
                .question(dto.getQuestion())
                .customerEntity(customer)
                .productEntity(product)
                .creatorEntity(creator)
                .build();
    }

    // qna 리스트
    public List<QnAListDTO> getAllQnA(Long qnaNo) {
        return qnARepository.QnAList(qnaNo);
    }

    // qna 조회
    public QnAReadDTO readQnA(Long qnaNo) {
        log.info("QnA 조회 요청 :", qnaNo);

        QnAEntity qnaEntity = qnARepository.findById(qnaNo)
                .orElseThrow(() -> new RuntimeException("Qna not found"));

        return QnAReadDTO.builder()
                .question(qnaEntity.getQuestion())
                .answer(qnaEntity.getAnswer())
                .createdAt(qnaEntity.getCreatedAt())
                .customerId(qnaEntity.getCustomerEntity().getCustomerId())
                .qnaImages(qnaEntity.getQnAImages().stream()
                        .map(image -> QnAImageDTO.builder()
                                .qnaImageNo(image.getQnaImageNo())
                                .qnaImageOrd(image.getQnaImageOrd())
                                .qnaImageUrl(image.getQnaImageUrl())
                                .build())
                        .toList())
                .build();
    }
}