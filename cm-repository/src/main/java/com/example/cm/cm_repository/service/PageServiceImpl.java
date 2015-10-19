//package com.example.cm.cm_repository.service;
//
//import com.example.cm.cm_model.domain.Page;
//import com.example.cm.cm_repository.repository.PageRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.domain.Sort;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
///**
// * @author jvwong
// */
//@Service
//@Transactional
//public class PageServiceImpl implements PageService {
//
//    @Autowired
//    private PageRepository pageRepository;
//
//    public org.springframework.data.domain.Page<Page> pageList(Long articleId, int pageNumber, int pageSize){
//        PageRequest pageRequest =
//                new PageRequest(pageNumber - 1, pageSize, Sort.Direction.DESC, "createdDate");
//        return pageRepository.findAll(pageRequest);
//    }
//}
