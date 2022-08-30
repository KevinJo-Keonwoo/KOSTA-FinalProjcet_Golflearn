package com.golflearn.control;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.golflearn.dto.Lesson;
import com.golflearn.dto.LessonLine;
import com.golflearn.dto.LessonReview;
import com.golflearn.dto.ResultBean;
import com.golflearn.exception.AddException;
import com.golflearn.exception.FindException;
import com.golflearn.exception.ModifyException;
import com.golflearn.service.LessonReviewService;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("review/*")
public class LessonReviewController {
	@Autowired
	private LessonReviewService service;
	
	@GetMapping(value = "new") //userId로 가는것이 맞나? 
	public ResultBean<Lesson> newReview(@RequestParam int lsnLineNo) {
		ResultBean<Lesson> rb = new ResultBean<>();
		try {
			Lesson lesson = service.newReview(lsnLineNo);
			rb.setStatus(1);
			rb.setT(lesson);
		} catch (FindException e) {
			e.printStackTrace();
			rb.setStatus(0);
			rb.setMsg(e.getMessage());
		}
		return rb;
	}
	@GetMapping(value = "previous") 
	public ResultBean<LessonLine> previous(@RequestParam int lsnLineNo){
		ResultBean<LessonLine> rb = new ResultBean<>();
		try {
			LessonLine lessonLine = service.previousReview(lsnLineNo);
			rb.setStatus(1);
			rb.setT(lessonLine);
		} catch (FindException e) {
			e.printStackTrace();
			rb.setStatus(0);
			rb.setMsg(e.getMessage());
		}
		return rb;
	}
	@PostMapping(value = "write", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> write(@RequestBody LessonReview lsnReview) {
		try {
			service.writeReview(lsnReview);
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (AddException e) {
			e.printStackTrace();
			return new ResponseEntity<>("내용은 필수 입력 사항입니다", HttpStatus.BAD_REQUEST);
		}
	}
	@PutMapping(value = "{lsnLineNo}")  //수정하기 코드이상할 수 있음 
	public ResponseEntity<?> modify(@PathVariable int lsnLineNo, @RequestBody LessonReview lsnReview) {
		//front에서 누른 lsnLineNo받아서 가져오기 
//		LessonLine lsnLine = new LessonLine();
		
		try {
			if(lsnReview.getReview() == null || lsnReview.getReview().equals("")) {
				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			}
			service.modifyReview(lsnReview);
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (ModifyException e) {
			e.printStackTrace();
			return new ResponseEntity<>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
//		lsnReview.getReview();
//		lsnReview.getMyStarScore();
		
//		LessonReview lessonReview =
//		
//		lessonLine.setLsnLineNo(lsnRe);
//		int lsnReviewNo = lsnReview.getLsnLine().getLsnLineNo();
		
	}
}
