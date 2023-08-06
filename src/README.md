# 메일 플러그 게시판 만들기 

# 1. 개발 환경
- IDE : 인텔리제이
- Spring boot 3.1.1 
- JDK 17 
- H2 database 
- MyBatis Framework 
- Postman

# 2. 프로젝트(yml) 설정
- port : 8081
- H2 database
   - url: jdbc:h2:tcp://localhost/~/mailPlugBoard
   - username : sa
   - password : sa

# 3. 프로젝트 설명
- Rest API 설계 규칙에 따라 api 명세를 작성하고 
메일플러그의 게시판, 게시글, 댓글에 대한 API(CRUD)를 개발
- 게스트 게시판으로써 로그인을 하지 않아도 게시글과 댓글을 작성할 수 있음.
  - 단, 게시글과 댓글 작성 시 password를 입력해야 함
  - 수정, 삭제 시에 password가 일치하는지 확인. 
- 등록, 수정, 삭제 시 result 리턴 값의 의미
  - 1 : 정상 동작 (200)
  - 2 : 파라미터 오류 (400)
  - 3 : 찾을 수 없음 (404)
###
- **boardId, postId, commentId는 전부 Long 타입이며 1부터 시작**
- **boardType과 status는 전부 소문자이며 해당 값을 가진다.**
  - **boardType : normal(일반), notice(공지)**
  - **boardStatus : activated(활성화), deactivated(비활성화)**


# 4. url 네이밍 룰
- Parameter가 없을 때 작성 요령
    - 단 건 : /post
    - 복수 건 : /posts
- Parameter가 있을 때 작성 요령
    - 단 건 : /post/{parameter}
    - 복수 건 : /posts/{parameter}

# 5. method 네이밍 룰

1. Controller
- xxxList() : 목록 조회 유형의 controller method
- xxxDetail() : 단 건 상세 조회 유형의 controller method
- xxxAdd() : 등록만 하는 유형의 controller method
- xxxModify() : 수정만 하는 유형의 controller method
- xxxRemove() : 삭제만 하는 유형의 controller method

2. Service
- findxxx() : 조회 유형의 service method
- addxxx() : 등록 유형의 service method
- modifyxxx() : 변경(수정) 유형의 service method
- removexxx() : 삭제 유형의 service method


3. Repository and Mapper
- selectxxx() : 조회 유형의 repository and mapper method
- insertxxx() : 등록 유형의 repository and mapper method
- updatexxx() : 변경 유형의 repository and mapper method
- deletexxx() : 삭제 유형의 repository and mapper method
####
- xxx는 구현하는 페이지의 기능 이름
  - ex)게시판 -> boardList(), findBoard(), selectBoard().
####
- Parameter가 있을 경우 method name끝단에 byParameterName을 사용.
  - ex)게시판 -> boardListByBoardId(Long boardId) 등

# 6. 게시판 주요기능 
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
2. 게시글 전체 조회 (/board/{boardId}/posts)
3. 게시글 단건 조회 (/board/{boardId}/post/{postId})
4. 게시글 수정 (/board/{boardId}/post/{postId})
5. 게시글 삭제 (/board/{boardId}/post/{postId})

- 댓글
1. 댓글 등록 (/board/{boardId}/post/{postId}/comment/write)
2. 댓글 전체 조회 (/board/{boardId}/post/{postId}/comments)
3. 댓글 단건 조회 (/board/{boardId}/post/{postId}/comment/{commentId})
4. 댓글 수정 (/board/{boardId}/post/{postId}/comment/{commentId})
5. 댓글 삭제 (/board/{boardId}/post/{postId}/comment/{commentId})


# 7. 댓글 등록, 수정, 삭제 동작 방식 설명 
# 7-1 등록 

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
-     if(commentDto.getDisplayName() == null
      || commentDto.getDisplayName().trim().equals("")
      || commentDto.getContents() == null
      || commentDto.getContents().trim().equals("")){
      return 2;
      }
  - 댓글 등록 시 필수 입력값이 없다면 400 에러 발생 
-       Map<String, Long> BoardIdAndPostId = new HashMap<>();
        BoardIdAndPostId.put("boardId", commentDto.getBoardId());
        BoardIdAndPostId.put("postId", commentDto.getPostId());
        PostDto postDto = postRepository.selectPostByBoradIdAndPostId(BoardIdAndPostId);
        if(postDto == null){
        return 3;
        }
  - 넘어온 boardId가 유효한지 확인 후 삭제된 게시글이라면 404 에러 발생
-     int result = commentRepository.insertCommentByCommentDto(commentDto);
  - 모든 조건이 유효하다면 등록 메소드 실행
-     if(result == 1){
      plusCommentsCount = postRepository.updatePlusPostCommentsCountByCommentDto(commentDto);
      }
  - 댓글이 성공적으로 입력 되면 postDto의 commentsCount(댓글 수) 플러스 로직 실행  
-       if(result == 1 && plusCommentsCount == 1){
        return result;
        }
  - 댓글입력 성공과 댓글 수 플러스 로직 둘 다 성공하면 200 리턴



# 7-2 수정
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
      
      // db의 비밀번호와 체크해서 다르다면 400 에러 리턴
       // 수정할 댓글 내용이 null값이나 비어있지 않은지 확인 후 수정 진행
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

-       boolean checkSavedCommentDto = checkSavedCommentDto(commentDto.getBoardId(),commentDto.getPostId(),commentDto.getCommentId());
        if(!checkSavedCommentDto){
            result = 3;
            return result;
        }
  - 수정하려는 댓글이 존재하는지 확인 함.
    - 삭제에서도 사용하기 때문에 모듈화
  - 존재하지 않는다면 404 에러 리턴
-      if(checkDbPassword(commentDto) && (commentDto.getContents() != null && !commentDto.getContents().trim().equals(""))){
       result = commentRepository.updateCommentByCommentDto(commentDto);
       return result;
       }
  - 존재 한다면(true) 입력한 password와 dbPassword를 비교하고, 수정할 내용이 null값이나 비어있지 않은지 확인 후 댓글 수정 메소드 실행
    - 비밀번호가 다르다면 400 에러 리턴

# 7-3 삭제
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
     * 댓글 삭제 메소드
     * 파라미터 CommentDto(boardId, postId, commentId, password)
     * result : 1(정상), 2(파라미터 오류), 3(해당 댓글이 없음)
     *
     * updateMinusPostCommentsCountByCommentDto
     * => 댓글 삭제 시 post의 commentsCount를 감소시킴.
     * 파라미터 CommentDto(boardId, postId)
     * */
    public int removeCommentByCommentDto(CommentDto commentDto) {

        int result = 0;
        int minusCommentCount = 0;
        // 먼저 해당 댓글이 있는지 확인 후
        boolean checkSavedCommentDto = checkSavedCommentDto(commentDto.getBoardId(),commentDto.getPostId(),commentDto.getCommentId());
        if(!checkSavedCommentDto){
            result = 3;
            return result;
        }

        // db 비번 체크
        if(checkDbPassword(commentDto)){
            result = commentRepository.deleteCommentByCommentDto(commentDto);
        }else {
            // 비번 오류
            return 2;
        }

        // commentsCount 감소
        if(result == 1){
            minusCommentCount = postRepository.updateMinusPostCommentsCountByCommentDto(commentDto);
        }

        log.info("result, deleteCommentCount => {}, {}",result,minusCommentCount);

        if(result == 1 && minusCommentCount == 1){
            return result;
        }else{
            return 2;
        }
    }
   
}
```
- 파라미터 CommentDto(boardId, postId, commentId, password)
- result : 1(정상), 2(파라미터 오류), 3(해당 댓글이 없음) 

-       boolean checkSavedCommentDto = checkSavedCommentDto(commentDto.getBoardId(),commentDto.getPostId(),commentDto.getCommentId());
        if(!checkSavedCommentDto){
            result = 3;
            return result;
        }
    - 삭제하려는 댓글이 존재하는지 확인 함.
        - 수정에서도 사용하기 때문에 모듈화
    - 존재하지 않는다면 404 에러 리턴
-      if(checkDbPassword(commentDto)){
       result = commentRepository.deleteCommentByCommentDto(commentDto);
       }else {
       // 비번 오류
       return 2;
       }
  - 삭제 하려는 댓글의 비밀번호가 일치하는 지 확인 후 삭제 메소드 실행
  - 비밀번호 불일치시 400 에러 리턴
-       if(result == 1){
        minusCommentCount = postRepository.updateMinusPostCommentsCountByCommentDto(commentDto);
        }
  - 삭제가 성공적으로 이루어졌으면 commentsCount를 -1 하는 메소드 실행
-       if(result == 1 && minusCommentCount == 1){
        return result;
        }else{
        return 2;
        }
  - 삭제와 commentsCount를 -1하는 메소드가 정상 작동 되었을 시에만 리턴 
