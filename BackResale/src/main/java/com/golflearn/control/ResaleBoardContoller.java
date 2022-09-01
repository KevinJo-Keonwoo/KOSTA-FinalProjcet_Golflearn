package com.golflearn.control;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.golflearn.service.ResaleBoardService;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("resaleboard/*")
public class ResaleBoardContoller {

	@Autowired
	private ResaleBoardService service;
	
}
