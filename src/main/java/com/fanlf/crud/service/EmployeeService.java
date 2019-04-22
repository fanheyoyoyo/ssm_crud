package com.fanlf.crud.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fanlf.crud.bean.Employee;
import com.fanlf.crud.bean.EmployeeExample;
import com.fanlf.crud.bean.EmployeeExample.Criteria;
import com.fanlf.crud.dao.EmployeeMapper;

@Service
public class EmployeeService {

	@Autowired
	EmployeeMapper employeeMapper;

	/**
	 * ��ѯ����Ա��
	 * 
	 * @return
	 */
	public List<Employee> getAll() {
		EmployeeExample example = new EmployeeExample();
		String orderByClause = "emp_id";
		example.setOrderByClause(orderByClause);;
		return employeeMapper.selectByExampleWithDept(null);
	}

	/**
	 * Ա������
	 * 
	 * @param employee
	 */
	public void saveEmp(Employee employee) {
		employeeMapper.insertSelective(employee);
	}

	public boolean checkEmpName(String empName) {

		EmployeeExample employeeExample = new EmployeeExample();
		Criteria createCriteria = employeeExample.createCriteria();
		createCriteria.andEmpNameEqualTo(empName);
		long example = employeeMapper.countByExample(employeeExample);

		return example == 0;
	}

	/**
	 * ��ѯԱ��
	 * 
	 * @param id
	 * @return
	 */
	public Employee getEmp(Integer id) {

		return employeeMapper.selectByPrimaryKey(id);
	}

	/**
	 * ����Ա����Ϣ
	 * 
	 * @param employee
	 */
	public void updateEmp(Employee employee) {

		employeeMapper.updateByPrimaryKeySelective(employee);

	}

	/**
	 * ɾ��Ա����Ϣ
	 * @param id
	 */
	public void delEmp(Integer id) {

		employeeMapper.deleteByPrimaryKey(id);
		
	}

	public void deleteBatch(List<Integer> ids) {
		EmployeeExample example = new EmployeeExample();
		Criteria criteria = example.createCriteria();
		criteria.andEmpIdIn(ids);
		employeeMapper.deleteByExample(example);
		
	}

}
