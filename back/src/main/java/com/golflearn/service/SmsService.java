package com.golflearn.service;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.transaction.Transactional;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.golflearn.dto.Messages;
import com.golflearn.dto.SmsRequest;
import com.golflearn.dto.SmsResponse;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
@Transactional
public class SmsService {

	@Value("${sms.accessKey}")
	private String accessKey;
	@Value("${sms.secretKey}")
	private String secretKey;
	@Value("${sms.serviceId}")
	private String serviceId;
	@Value("${sms.senderPhone}")
	private String senderPhone;

	public SmsResponse sendSms(Messages messages) throws JsonProcessingException, UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException, URISyntaxException {
		Long time = System.currentTimeMillis();
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.set("x-ncp-apigw-timestamp", time.toString());
		headers.set("x-ncp-iam-access-key", this.accessKey);
		headers.set("x-ncp-apigw-signature-v2", makeSignature(time));
//		String sig = makeSignature(time); //암호화
		
		List<Messages> messages = new ArrayList<>();
		messages.add(messages);

		SmsRequest smsRequest = SmsRequest.builder()
				.type("SMS")
				.contetnType("COMM")
				.countryCode("82")
				.from(senderPhone)
				.content(messages.getContent())
				.messages(messages)
				.build();
		ObjectMapper objectMapper = new ObjectMapper();
		String jsonBody = objectMapper.writeValueAsString(smsRequest);


		HttpEntity<String> body = new HttpEntity<>(jsonBody,headers);

		RestTemplate restTemplate = new RestTemplate();
		restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());
		SmsResponse smsResponse = restTemplate.postForObject(new URI("https://sens.apigw.ntruss.com/sms/v2/services/"+this.serviceId+"/messages"), body, SmsResponse.class);

		return smsResponse;

	}
	public String makeSignature(Long time) throws UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException {
		String space = " ";             
		String newLine = "\n";
		String method = "POST";
		String url = "/sms/v2/services/"+ this.serviceId+"/messages";
		String timestamp = time.toString();
		String accessKey = this.accessKey;
		String secretKey = this.secretKey;

		String message = new StringBuilder()
				.append(method)
				.append(space)
				.append(url)
				.append(newLine)
				.append(timestamp)
				.append(newLine)
				.append(accessKey)
				.toString();

		SecretKeySpec signingKey = new SecretKeySpec(secretKey.getBytes("UTF-8"), "HmacSHA256");
		Mac mac = Mac.getInstance("HmacSHA256");
		mac.init(signingKey);

		byte[] rawHmac = mac.doFinal(message.getBytes("UTF-8"));
		String encodeBase64String = Base64.encodeBase64String(rawHmac);

		return encodeBase64String;
	}


}