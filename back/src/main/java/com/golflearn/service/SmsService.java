package com.golflearn.service;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.transaction.Transactional;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.golflearn.dto.Message;
import com.golflearn.dto.SmsRequest;
import com.golflearn.dto.SmsResponse;
import com.golflearn.dto.UserInfo;

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
	
	public String sendRandomMessage(String userPhone) {
		SmsService message = new SmsService();
		Random rnd = new Random();
		String randomKey = "";
		for (int i = 0; i < 6; i++) {
			String ran = Integer.toString(rnd.nextInt(10));
			randomKey += rnd;
		}
		System.out.println("회원가입 문자 인증 : " + randomKey);

		//message.sendSms(null);

		return randomKey;
	}
	
	
	public SmsResponse sendSms(Message msg) throws JsonProcessingException, UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException, URISyntaxException {
		Long time = System.currentTimeMillis();

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.set("x-ncp-apigw-timestamp", time.toString());
		headers.set("x-ncp-iam-access-key", this.accessKey);
		headers.set("x-ncp-apigw-signature-v2", makeSignature(time));
//		String sig = makeSignature(time); //암호화
		
		List<Message> messages = new ArrayList<>();
		messages.add(msg);

		SmsRequest smsRequest = SmsRequest.builder()
				.type("SMS")
				.contentType("COMM")
				.countryCode("82")
				.from(senderPhone)
				.content(msg.getContent())
				.messages(messages)
				.build();

		ObjectMapper objectMapper = new ObjectMapper();
		String jsonBody = objectMapper.writeValueAsString(smsRequest);
		HttpEntity<String> body = new HttpEntity<>(jsonBody, headers);

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