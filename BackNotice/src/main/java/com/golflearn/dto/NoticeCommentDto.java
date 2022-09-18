package com.golflearn.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.golflearn.domain.entity.NoticeBoardEntity;
import com.golflearn.domain.entity.NoticeCommentEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class NoticeCommentDto {
	private Long noticeCmtNo;
	private String noticeCmtContent;
	@JsonFormat(pattern = "yy/MM/dd", timezone = "Asia/Seoul")
	private Date noticeCmtDt;
	private Long noticeCmtParentNo;
	private String userNickname;
	private NoticeBoardDto noticeBoardDto;
	private Long noticeBoardNo;
	
//	private NoticeCommentDto (Long noticeCmtNo, String noticeCmtContent, Date noticeCmtDt, Long noticeCmtParentNo, String userNickname, NoticeBoardDto noticeBoardDto) {
//		this.noticeCmtDt = noticeCmtDt;
//		this.noticeCmtContent = noticeCmtContent;
//		this.userNickname = userNickname;
//		this.noticeCmtNo = noticeCmtNo;
//		this.noticeCmtParentNo = noticeCmtParentNo;
//	}
	
	public NoticeCommentEntity toEntity() {
//		System.out.println("-----"+noticeBoardDto.getNoticeBoardNo());
		NoticeBoardDto noticeBoard = NoticeBoardDto.builder().noticeBoardNo(noticeBoardDto.getNoticeBoardNo()).build();
		NoticeBoardEntity noticeBoardEntity = noticeBoard.toEntity();
		return NoticeCommentEntity.builder()
				.noticeCmtNo(noticeCmtNo)
				.noticeCmtDt(noticeCmtDt)
				.noticeBoard(noticeBoardEntity)
				.noticeCmtContent(noticeCmtContent)
				.noticeCmtParentNo(noticeCmtParentNo)
				.userNickname(userNickname)
				.build();
	}
	
	public NoticeCommentEntity toEntity1() {
//		System.out.println("-----"+noticeBoardDto.getNoticeBoardNo());
		return NoticeCommentEntity.builder()
				.noticeCmtNo(noticeCmtNo)
				.noticeCmtDt(noticeCmtDt)
				.noticeCmtContent(noticeCmtContent)
				.noticeCmtParentNo(noticeCmtParentNo)
				.userNickname(userNickname)
				.build();
	}
//	public List<NoticeCommentEntity> ListDtoToListEntity(List<NoticeCommentDto> noticeCommentDtoList){
//		List<NoticeCommentDto> cDto = new ArrayList<>();
//		
//		for(NoticeCommentDto noticeCommentDto : noticeCommentDtoList){
//			NoticeCommentDto dto = NoticeCommentDto.builder()
//					.noticeCmtContent(noticeCmtContent)
//					.noticeCmtDt(noticeCmtDt)
//					.noticeCmtNo(noticeCmtNo)
//					.noticeCmtParentNo(noticeCmtParentNo)
//					.userNickname(userNickname)
//					.build();
//			cDto.add(dto);
//        }
//		System.out.println("불러옴 --- "+cDto.get(0).noticeCmtContent);
//		
//        return cDto.stream()
//                .map(NoticeCommentDto::toEntity)
//                .collect(Collectors.toList());
//    }

}
