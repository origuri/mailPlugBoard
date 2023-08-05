# 메일 플러그 게시판 만들기 

# 개발 환경
1. IDE : 인텔리제이
2. Spring boot 3.1.1
3. JDK 17
4. H2 database
5. MyBatis Framework
6. Postman

# 프로젝트(yml) 설정
1. port : 8081
2. H2 database
   - url: jdbc:h2:tcp://localhost/~/mailPlugBoard
   - username : sa
   - password : sa

# 프로젝트 설명
- Rest API 설계 규칙에 따라 api 명세를 작성하고 
메일플러그의 게시판, 게시글, 댓글에 대한 API(CRUD)를 개발
- 게스트 게시판으로써 로그인을 하지 않아도 게시글과 댓글을 작성할 수 있음.
  - 단, 게시글과 댓글 작성 시 password를 입력해야 함
  - 수정, 삭제 시에 password가 일치하는지 확인. 

# 게시판 주요기능 
- 등록 : POST
- 조회 : GET
- 수정 : PUT
- 삭제 : DELETE
####
- 게시판
1. 게시판 등록 (/board/write)
2. 게시판 조회 (/board)
3. 게시판 수정 (/board/{boardId})
4. 게시판 삭제 (/board/{boardId})

- 게시글 
1. 게시글 등록 (/board/{boardId}/post/write)
2. 게시글 전체 조회 (/board/{boardId}/post)
3. 게시글 단건 조회 (/board/{boardId}/post/{postId})
4. 게시글 수정 (/board/{boardId}/post/{postId})
5. 게시글 삭제 (/board/{boardId}/post/{postId})

- 댓글
1. 댓글 등록 (/board/{boardId}/post/{postId}/comment/write)
2. 댓글 전체 조회 (/board/{boardId}/post/{postId}/comment)
3. 댓글 단건 조회 (/board/{boardId}/post/{postId}/comment/{commentId})
4. 댓글 수정 (/board/{boardId}/post/{postId}/comment/{commentId})
5. 댓글 삭제 (/board/{boardId}/post/{postId}/comment/{commentId})

# 댓글 등록, 수정, 삭제 동작 방식 설명 
# 등록 

```java
@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class CommentService {
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;

   /*
    * 특정 게시글에 댓글 등록 메소드
    * 파라미터 : commentDto(boardId, postId, displayName, password, contents)
    * result : 1(정상), 2(파라미터 오류), 3(해당 게시글이 없음)
    *
    * updatePlusPostCountsByCommentDto
    * => 댓글 등록 성공 시 post에 commentCount를 올려주는 메소드
    * */
   public int addCommentByCommentDto(CommentDto commentDto) {

      if(commentDto.getDisplayName() == null
              || commentDto.getDisplayName().trim().equals("")
              || commentDto.getContents() == null
              || commentDto.getContents().trim().equals("")){
         return 2;
      }

      Map<String, Long> BoardIdAndPostId = new HashMap<>();
      BoardIdAndPostId.put("boardId", commentDto.getBoardId());
      BoardIdAndPostId.put("postId", commentDto.getPostId());
      PostDto postDto = postRepository.selectPostByBoradIdAndPostId(BoardIdAndPostId);
      if(postDto == null){
         return 3;
      }

      int plusCommentsCount = 0;
      int result = commentRepository.insertCommentByCommentDto(commentDto);

      if(result == 1){
         plusCommentsCount = postRepository.updatePlusPostCommentsCountByCommentDto(commentDto);
      } else{
         return 2;
      }

      if(result == 1 && plusCommentsCount == 1){
         return result;
      }else {
         return 2;
      }
   }
}

```
- 파라미터 : postDto(boardId, title, displayName, password, contents)
- result : 1(정상), 2(파라미터 오류), 3(해당 게시판이 없음)
####
- 입력 값이 비어있거나 null일 경우 400 BAD_REQUEST 에러 발생
   -     int result = commentRepository.insertCommentByCommentDto(commentDto);
- 해당 게시글이 존재하는 지 확인, 존재하지 않으면 404 에러 발생
   -     PostDto postDto = postRepository.selectPostByBoradIdAndPostId(BoardIdAndPostId);
- 해당 게시글이 존재 되는 것이 확인 되면 댓글 입력
   -     int result = commentRepository.insertCommentByCommentDto(commentDto);
- 댓글이 성공적으로 입력 되면 postDto의 commentsCount(댓글 수) 플러스
   -     if(result == 1){
         plusCommentsCount = postRepository.updatePlusPostCommentsCountByCommentDto(commentDto);
         } 




# 수정
```java
@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class CommentService {
   private final CommentRepository commentRepository;
   private final PostRepository postRepository;

   /*
    * selectCommentDbPasswordByCommentDto =>
    * 내가 입력한 비밀번호와 db의 비밀번하 일치하는 지 확인하는 메소드
    * 파라미터 : commentDto(boardId, postId, commentId)
    * */
   private boolean checkDbPassword(CommentDto commentDto){
      String rawPassword = commentDto.getPassword();
      String dbPassword = commentRepository.selectCommentDbPasswordByCommentDto(commentDto);
      log.info("modifyCommentByCommentDto 비번 확인 -> {}",rawPassword.equals(dbPassword));

      return rawPassword.equals(dbPassword);
   }

   /*
    * 댓글 수정, 삭제 시 해당 댓글이 존재하는 지 확인하는 메소드
    * 파라미터 : boardId, postId, commentId
    * */
   private boolean checkSavedCommentDto(Long boardId, Long postId, Long commentId){
      Map<String, Long> boardIdAndPostIdAndCommentId = new HashMap<>();

      boardIdAndPostIdAndCommentId.put("boardId", boardId);
      boardIdAndPostIdAndCommentId.put("postId", postId);
      boardIdAndPostIdAndCommentId.put("commentId", commentId);

      CommonDto savedCommonDto = commentRepository.
              selectCommentByBoardIdAndPostIdAndCommentId(boardIdAndPostIdAndCommentId);
      if(savedCommonDto == null){
         return false;
      }else{
         return true;
      }
   }

   /*
    * 특정 댓글을 수정하는 메소드
    * 파라미터 : CommentDto(boardId, postId, commentId, content, password)
    * result : 1(정상), 2(파라미터 오류), 3(해당 댓글이 없음)
    * */
   public int modifyCommentByCommentDto(CommentDto commentDto) {

      int result = 0;

      // 먼저 수정하는 댓글이 존재하는지 확인.
      boolean checkSavedCommentDto = checkSavedCommentDto(commentDto.getBoardId(),commentDto.getPostId(),commentDto.getCommentId());
      if(!checkSavedCommentDto){
         result = 3;
         return result;
      }

      if(checkDbPassword(commentDto) && (commentDto.getContents() != null && !commentDto.getContents().trim().equals(""))){
         result = commentRepository.updateCommentByCommentDto(commentDto);
         return result;
      } else {
         result = 2;
         return result;
      }

   }
   
}
```
- 파라미터 : CommentDto(boardId, postId, commentId, content, password)
- result : 1(정상), 2(파라미터 오류), 3(해당 댓글이 없음)
####
- 수정하려는 댓글이 존재하는지 확인 함.
  - 삭제에서도 사용하기 때문에 모듈화 
    -       boolean checkSavedCommentDto = checkSavedCommentDto(commentDto.getBoardId(),commentDto.getPostId(),commentDto.getCommentId());
  - 존재하지 않는다면 404 에러 리턴
- 존재 한다면(true) 입력한 password와 dbPassword를 비교하고, 수정할 내용이 null값이나 비어있지 않은지 확인 후 댓글 수정 메소드 실행
  -      if(checkDbPassword(commentDto) && (commentDto.getContents() != null && !commentDto.getContents().trim().equals(""))){
         result = commentRepository.updateCommentByCommentDto(commentDto);
         return result;
         }

# 삭제
```java
@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class CommentService {
   private final CommentRepository commentRepository;
   private final PostRepository postRepository;

   
}
```