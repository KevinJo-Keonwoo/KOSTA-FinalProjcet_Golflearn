package com.golflearn.service;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.golflearn.domain.LessonLineRepository;
import com.golflearn.domain.PaymentRepository;
import com.golflearn.dto.LessonLine;
import com.golflearn.dto.Payment;
import com.golflearn.exception.AddException;

@Transactional
@Service
public class PaymentService {
	@Autowired
	PaymentRepository paymentRepository;
	
	@Autowired
	LessonLineRepository lsnLineRepository;

	// 결제 정보 추가
	public void addPayment(Payment payment) throws AddException{
		paymentRepository.insertPayment(payment);
	
	}
	public void addLsnLine(LessonLine lsnLine) throws AddException{
		lsnLineRepository.insertLsnLine(lsnLine);	
	}
	
}
