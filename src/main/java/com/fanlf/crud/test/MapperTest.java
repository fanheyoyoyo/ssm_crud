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
 * 测试 dao 层的工作 推荐 Spring 的项目可以使用 Spring 的单元测试，可以自动注入我们需要的组件 1、导入 SpringTest
 * 的依赖模块 2、@ContextConfiguration 指定 Spring 配置文件的位置 3、直接 @autowire 要使用的组件即可
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
		 * //1、创建 spring ioc 容器 ApplicationContext ioc = new
		 * ClassPathXmlApplicationContext("applicationContext.xml");
		 * //2、从容器中获取mapper DepartMentMapper bean =
		 * ioc.getBean(DepartMentMapper.class); DepartMent department = new
		 * DepartMent(null,"财务部"); bean.insert(department);
		 */

		System.out.println(departmentMapper);
		// 插入部门
		// departMentMapper.insertSelective(new DepartMent(null,"财务部"));
		// 插入员工
		employeeMapper.insertSelective(new Employee(null, "Jcak", "F", "jack@fanlf.com", 1));
		// 批量插入员工
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
