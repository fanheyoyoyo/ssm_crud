package com.fanlf.crud.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fanlf.crud.bean.Department;
import com.fanlf.crud.bean.Msg;
import com.fanlf.crud.service.DepartmentService;

@Controller
public class DepartmentController {

	@Autowired
	private DepartmentService departmentService;

	@ResponseBody
	@RequestMapping("/depts")
	public Msg getDept() {

		List<Department> list = departmentService.getDeptAll();

		return Msg.success().add("depts", list);
	}

}
