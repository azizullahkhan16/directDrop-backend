package com.aktic.directdropbackend.repository.search;

import com.aktic.directdropbackend.model.entity.ChatRoom;
import com.aktic.directdropbackend.model.entity.User;
import com.aktic.directdropbackend.model.response.MessageInfoResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SearchRepository {

    Page<MessageInfoResponse> fullTextSearchIncludingChatRoom(ChatRoom chatRoom, String keyword, String username, Pageable pageable);
    Page<MessageInfoResponse> fullTextSearchExcludingChatRoom(User user, String keyword, String username, Pageable pageable);
}
