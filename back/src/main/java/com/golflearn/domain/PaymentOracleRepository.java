package com.golflearn.domain;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.golflearn.dto.Payment;
import com.golflearn.exception.AddException;
@Repository(value="paymentRepository")
public class PaymentOracleRepository implements PaymentRepository {

	@Autowired
	private SqlSessionFactory sqlSessionFactory;
	
	 //결제 추가
	@Override
	public void insertPayment(Payment payment) throws AddException {
		SqlSession session = null;
		
		try {
			session = sqlSessionFactory.openSession();
			session.insert("com.golflearn.mapper.LessonLineMapper.insertPayment", payment);
		} catch(Exception e) {
			e.printStackTrace();
			throw new AddException(e.getMessage());
		}finally {
			if(session !=null) {
				session.close();
			}
		}
	}

}
