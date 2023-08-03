package com.example.mailplugboard.repository.post;


import com.example.mailplugboard.model.post.PostDto;

import java.util.List;

public interface PostRepository {
    List<PostDto> selectPostListByBoardId(Long boardId);
}
