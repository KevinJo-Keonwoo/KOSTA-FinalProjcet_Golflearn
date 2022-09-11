package com.golflearn.control;


import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.golflearn.dto.LessonLine;
import com.golflearn.dto.Payment;
import com.golflearn.dto.ResultBean;
import com.golflearn.exception.AddException;
import com.golflearn.service.PaymentService;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/pay/*")
public class PaymentController {
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private PaymentService paymentService;
	
	@Autowired 
	private ServletContext sc;
	
//	@PostMapping("paymnet/{lsnNo}") 
//	public String payment(@PathVariable int lsnNo, @RequestBody Payment payment){ 
//		try {
//			paymentService.addPayment(payment);
//		} catch (AddException e) {
//			e.printStackTrace();
//		}
//		return ("결제상태는");
//	} 
	
	@PostMapping("lesson/{lsnNo}")
	public ResultBean<LessonLine> addLsnLine(@PathVariable int lsnNo, 
											 @RequestBody LessonLine lsnLine, 
											 HttpSession session){
		//String loginedId = (String)session.getAttribute("loginInfo");
		String loginedId = "hanmr@nate.com";
		logger.error("세션에 저장된 아이디는"+ lsnLine.getUserInfo().getUserId());
		
		ResultBean<LessonLine> rb = new ResultBean<>();
		
		Payment payment = new Payment();
			lsnLine.getLsn().setLsnNo(lsnNo);
			logger.error("레슨 번호는"+ lsnLine.getLsn().getLsnNo());
			lsnLine.getUserInfo().setUserId(loginedId);
			logger.error("유저 아이디는" + lsnLine.getUserInfo().getUserId());
			try {
				payment = lsnLine.getPayment();
				paymentService.addPayment(payment);
			} catch (AddException e) {
				e.printStackTrace();
			}
			
			try {
				lsnLine.getLsn().setLsnNo(lsnNo);
				lsnLine.getUserInfo().setUserId(loginedId);
				paymentService.addLsnLine(lsnLine);
			} catch (AddException e) {
				e.printStackTrace();
			}
		
			return rb;
	}
	
		
	
//	@PostMapping("/{imp_uid}")
//	public IamportResponse<Payment> paymentByImUid(@PathVariable String imp_uid) throws IamportResponseException, IOException{
////        logger.info("paymentByImpUid 진입");
//        return iamportClient.paymentByImpUid(imp_uid);
    }
	

