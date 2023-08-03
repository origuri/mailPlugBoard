package com.example.mailplugboard.repository.post;


import com.example.mailplugboard.model.post.PostDto;

import java.util.List;
import java.util.Map;

public interface PostRepository {
    List<PostDto> selectPostListByBoardId(Long boardId);

    PostDto selectPostByBoradIdAndPostId(Map<String, Long> boardIdAndPostId);
}
