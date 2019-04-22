package com.fanlf.crud.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fanlf.crud.bean.Department;
import com.fanlf.crud.dao.DepartmentMapper;

@Service
public class DepartmentService {
	@Autowired
	private DepartmentMapper departmentMapper;

	public List<Department> getDeptAll() {
		return departmentMapper.selectByExample(null);
	}
}
