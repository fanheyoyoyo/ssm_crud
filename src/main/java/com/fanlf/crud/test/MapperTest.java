package com.fanlf.crud.test;

import java.util.List;
import java.util.UUID;

import org.apache.ibatis.session.SqlSession;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.fanlf.crud.bean.Employee;
import com.fanlf.crud.bean.EmployeeExample;
import com.fanlf.crud.dao.DepartmentMapper;
import com.fanlf.crud.dao.EmployeeMapper;

/**
 * ���� dao ��Ĺ��� �Ƽ� Spring ����Ŀ����ʹ�� Spring �ĵ�Ԫ���ԣ������Զ�ע��������Ҫ����� 1������ SpringTest
 * ������ģ�� 2��@ContextConfiguration ָ�� Spring �����ļ���λ�� 3��ֱ�� @autowire Ҫʹ�õ��������
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:applicationContext.xml" })
public class MapperTest {

	@Autowired
	DepartmentMapper departmentMapper;
	@Autowired
	EmployeeMapper employeeMapper;
	@Autowired
	SqlSession sqlSession;

	@Test
	public void testGetEmpAll() {
		EmployeeExample employeeExample = new EmployeeExample();
		employeeExample.setOrderByClause("emp_id");
		List<Employee> selectByExampleWithDept = employeeMapper.selectByExampleWithDept(null);
		for (Employee employee : selectByExampleWithDept) {

			System.out.println(employee.getEmpId());
		}

	}

	@Test
	public void testCRUD() {

		/*
		 * //1������ spring ioc ���� ApplicationContext ioc = new
		 * ClassPathXmlApplicationContext("applicationContext.xml");
		 * //2���������л�ȡmapper DepartMentMapper bean =
		 * ioc.getBean(DepartMentMapper.class); DepartMent department = new
		 * DepartMent(null,"����"); bean.insert(department);
		 */

		System.out.println(departmentMapper);
		// ���벿��
		// departMentMapper.insertSelective(new DepartMent(null,"����"));
		// ����Ա��
		employeeMapper.insertSelective(new Employee(null, "Jcak", "F", "jack@fanlf.com", 1));
		// ��������Ա��
		/*
		 * for(){ employeeMapper.insertSelective(new Employee(null, "Jcak", "F",
		 * "jack@fanlf.com", 1)); }
		 */

		EmployeeMapper mapper = sqlSession.getMapper(EmployeeMapper.class);

		for (int i = 0; i < 1000; i++) {
			String uid = UUID.randomUUID().toString().substring(0, 5);
			mapper.insertSelective(new Employee(null, uid, "M", uid + "@fanlf.com", 1));
		}
		System.out.println("success!");
	}

}
