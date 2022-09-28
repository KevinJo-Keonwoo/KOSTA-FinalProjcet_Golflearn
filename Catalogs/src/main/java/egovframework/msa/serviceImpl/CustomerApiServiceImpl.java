package egovframework.msa.serviceImpl;

import org.springframework.stereotype.Service;

import egovframework.msa.service.CustomerApiService;

@Service
public class CustomerApiServiceImpl implements CustomerApiService {

//	@Override
//	public String getCustomerDetail(String customerId) {
//		return restTemplate.getForObject("http://localhost:8082/customers/" + customerId, String.class);
//	}
	@Override
	public String getCustomerDetail(String customerId) {
		return customerId;
	}
}
