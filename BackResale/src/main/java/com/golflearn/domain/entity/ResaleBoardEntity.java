package com.golflearn.domain.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
@Entity 
@Table(name = "resale_board")
@SequenceGenerator(name ="resale_board_generator",
					sequenceName="resale_board_no_seq",
					initialValue=28, // 28부터 시작(샘플데이터가 27까지 있음)
					allocationSize=1) // 1씩 증가
@DynamicInsert
@DynamicUpdate
public class ResaleBoardEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE,
					generator = "resale_board_generator")
	@Column(name="resale_board_no")
	private Long resaleBoardNo;
	
	@Column(name="user_nickname")
	private String userNickname;
	
	@Column(name="resale_board_title")
	@NotBlank(message="제목은 필수 입력값입니다.")
	private String resaleBoardTitle;
	
	@Column(name="resale_board_content")
	@NotBlank(message="내용은 필수 입력값입니다.")
	private String resaleBoardContent;
	
	@Column(name="resale_board_dt")
	@ColumnDefault(value="sysdate")
	@JsonFormat(pattern = "yy/MM/dd", timezone = "Asia/Seoul")
	private Date resaleBoardDt;
	
	@Column(name="resale_board_view_cnt")
	@ColumnDefault(value="0")
	private Integer resaleBoardViewCnt;
	
	@Column(name="resale_board_like_cnt")
	@ColumnDefault(value="0")
	private Integer resaleBoardLikeCnt;
	
	@Column(name="resale_board_cmt_cnt")
	@ColumnDefault(value="0")
	private Integer resaleBoardCmtCnt;

	@JsonManagedReference
    @NotFound(action = NotFoundAction.IGNORE)
	@OneToMany(mappedBy="resaleBoard", fetch=FetchType.EAGER,cascade=CascadeType.REMOVE) //EAGER
	// @JoinColumn(name = "resale_board_no") //referencedColumnName="resale_board_no")
	// OneToMany , ManyToOne -> 유스케이스별로 만듦. 게시글을 조회할 때 댓글도 같이 불러 오는 경우가 훨씬 많음
	private List<ResaleCommentEntity> resaleComment;
	
	
	@JsonManagedReference//연관관계의 주인이 아닌 쪽에 선언. 정상적으로 직렬화 수행
//    @NotFound(action = NotFoundAction.IGNORE)
	@OneToMany(mappedBy="resaleBoard",fetch=FetchType.LAZY, cascade=CascadeType.REMOVE) //LAZY
	//@JoinColumn(name = "resale_board_no")
	private List<ResaleLikeEntity> resaleLike;


//	public void addComment(ResaleCommentEntity commentEntity) {
//		this.resaleCommentEntity.add(commentEntity);
		
//		commentEntity.setResaleBoard(this);
//		resaleCommentEntity.add(commentEntity);
//	}
	
}
// @NonNull : 롬복 어노테이션. Setter, Getter와 관련 있기 때문에 DB와는 아무 관계가 없음
// Set, Get 할 때 null이 들어가지 않도록 하는 것