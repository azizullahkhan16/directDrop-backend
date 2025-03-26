package com.aktic.directdropbackend.repository.search;

import com.aktic.directdropbackend.model.entity.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SearchRepository {

    Page<Message> fullTextSearchByChatRoom(Long chatRoomId, String keyword, String username,Pageable pageable);
    Page<Message> fullTextSearch(String keyword, String username,Pageable pageable);
}
