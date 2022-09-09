package com.golflearn.domain;

import com.golflearn.dto.Payment;
import com.golflearn.exception.AddException;

public interface PaymentRepository {
	/**
	 * 결제 완료 시 결제 내역 추가
	 * @param payment
	 * @throws AddException
	 */
	public void insertPayment(Payment payment) throws AddException;
	
}
