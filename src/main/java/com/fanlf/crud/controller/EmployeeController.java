package com.fanlf.crud.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fanlf.crud.bean.Employee;
import com.fanlf.crud.bean.Msg;
import com.fanlf.crud.service.EmployeeService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

/**
 * ����Ա��CRUD����
 * 
 * @author fanlf
 *
 */
@Controller
public class EmployeeController {

	@Autowired
	EmployeeService employeeService;
	
	
	/**
	 * ɾ��Ա����Ϣ
	 * ����ɾ��(1)������ɾ��(1-2-3-4)
	 */
	@RequestMapping(value="/emp/{ids}", method=RequestMethod.DELETE)
	@ResponseBody
	public Msg delEmp(@PathVariable("ids") String ids){
		if(ids.contains("-")){
			//����ɾ��
			String[] str_ids = ids.split("-");
			List<Integer> del_ids = new ArrayList<Integer>();
			for (String id : str_ids) {
				del_ids.add(Integer.parseInt(id));			
			}
			employeeService.deleteBatch(del_ids);
		}else{//this is a git branch test!
			//��һɾ�� 
			Integer id = Integer.parseInt(ids);
			employeeService.delEmp(id);
		}
			
		return Msg.success();
	}
	

	/**
	 * ���� ���µ�Ա����Ϣ 
	 * path·��һ��Ҫд�� 
	 * ��װ������ 
	 * Employee [empId=5, empName=null,	 * gender=null, email=null, dId=null] ���⣺ �������������ݣ���ʱ Employee �����װ���ϣ�
	 *  ԭ��
	 * Tomcat �� 
	 * 1�����������е����ݷ�ס��һ��map��
	 *  2��request.getParameter("empName") �ͻ����� map ��ȡֵ 
	 * 3��SpringMVC �ڷ�װPOJO�����ʱ�� ��� POJO ��ÿ�����Ե�ֵ�� ͨ������ request.getParameter("empName") ȡ��ֵ; 
	 * ajax ���� PUT �������������⣺ 
	 * PUT �����������е����ݣ�request.getParameter("empName") �ò����� 
	 * �ò����ĸ���ԭ���ǣ� Tomcat ����� PUT  ��������ݷ�װΪ map��Tomcat ֻ��� POST ��������ݷ�װΪ map 
	 * package org.apache.catalina.connector.Request -- parseParameters() 
	 * if(!getConnector().isParseBodyMethod(getMethod()) ) { 
	 *  success = true;
	 *  return; 
	 * } 
	 * ��������� 
	 * ����Ҫֱ���ܷ��� PUT ֮������󣬻�Ҫ��װ�������е����� 
	 * 1�������� HttpPutFormContentFilter 
	 * 2���������ã����������е����ݽ�����װ��һ��map 
	 * 3��request �����°�װ��request.getParameter() ����д���ͻ���Լ��İ�װ�� map ��ȡֵ
	 * 
	 */

	@ResponseBody
	@RequestMapping(value = "/emp/{empId}", method = RequestMethod.PUT)
	public Msg updateEmp(Employee employee, HttpServletRequest request) {
		/*System.out.println("HttpServletRequest empName: " + request.getParameter("empName"));
		System.out.println(employee);*/
		employeeService.updateEmp(employee);
		return Msg.success();
	}

	/**
	 * ��ȡԱ����Ϣ
	 */
	@RequestMapping(value = "/emp/{id}", method = RequestMethod.GET)
	@ResponseBody
	public Msg getEmp(@PathVariable("id") Integer id) {
		Employee emp = employeeService.getEmp(id);
		return Msg.success().add("emp", emp);
	}

	/**
	 * Ա�������ظ���У�� ajaxУ��
	 */

	@ResponseBody
	@RequestMapping(value = "/checkEmpName", method = RequestMethod.POST)
	public Msg checkEmpName(@RequestParam(value = "empName") String empName) {
		// ��У�����ֺϷ���
		String regx = "(^[a-zA-Z0-9_-]{6,16}$)|(^[\u2E80-\u9FFF]{2,5})";
		if (!empName.matches(regx)) {
			return Msg.failed().add("va_msg", "�û���������6-16λ��ĸ�����ֵ���ϻ���2-5λ���֣�");
		}
		// �ڽ����ظ���У��
		boolean b = employeeService.checkEmpName(empName);
		if (b) {
			return Msg.success();
		} else {
			return Msg.failed().add("va_msg", "�û��������ã�");

		}
	}

	/**
	 * Ա������ 1��֧��JSR303У�� 2������Hibernate Validator�� �������У��
	 * 
	 * @param employee
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/emp", method = RequestMethod.POST)
	public Msg saveEmp(@Valid Employee employee, BindingResult result) {
		if (result.hasErrors()) {
			// У��ʧ�ܣ�Ӧ�÷���ʧ�ܣ�ͬʱ�Ѵ�����Ϣ��ʾ��ģ̬����
			Map<String, Object> map = new HashMap<String, Object>();
			List<FieldError> errors = result.getFieldErrors();
			for (FieldError fieldError : errors) {
				map.put(fieldError.getField(), fieldError.getDefaultMessage());
			}
			return Msg.failed().add("errorFields", map);
		} else {
			employeeService.saveEmp(employee);
			return Msg.success();
		}
	}

	/**
	 * @ResponseBody ���Զ��Ľ����ص�����ת��Ϊ json ��ʽ ��Ҫ @ResponseBody ������������Ҫ���� jackson ��
	 *               ����ط����ðѲ�ѯ���ķ�ҳ���ݷ��뵽��������
	 * 
	 * @param pn
	 * @return
	 */
	@RequestMapping(value="/emps",method=RequestMethod.GET)
	@ResponseBody
	public Msg getEmpsWithJson(@RequestParam(value = "pn", defaultValue = "1") Integer pn) {
		// ��ҳ��ѯ������ pageHelper ���
		// �ڲ�ѯ֮ǰֻ��Ҫ���� startPage������ҳ���Լ�ÿҳ�Ĵ�С
		PageHelper.startPage(pn, 5);
		// startPage ��������Ĳ�ѯ����һ����ҳ��ѯ
		List<Employee> emps = employeeService.getAll();
		// �� PageInfo �Խ�����а�װ,ֻ��Ҫ�� PageInfo ����ҳ��Ϳ�����
		// PageInfo ��װ����ϸ�ķ�ҳ��Ϣ���������ǲ�ѯ����������,����������ʾ��ҳ��
		PageInfo page = new PageInfo(emps, 5);

		return Msg.success().add("pageInfo", page);
	}

	/**
	 * ��ѯԱ�����ݣ���ҳ��ѯ��
	 * 
	 * @return
	 */
	// @RequestMapping("/emps")
	public String getEmps(@RequestParam(value = "pn", defaultValue = "1") Integer pn, Model model) {
		// ��ҳ��ѯ������ pageHelper ���
		// �ڲ�ѯ֮ǰֻ��Ҫ���� startPage������ҳ���Լ�ÿҳ�Ĵ�С
		PageHelper.startPage(pn, 5);
		// startPage ��������Ĳ�ѯ����һ����ҳ��ѯ
		List<Employee> emps = employeeService.getAll();
		// �� PageInfo �Խ�����а�װ,ֻ��Ҫ�� PageInfo ����ҳ��Ϳ�����
		// PageInfo ��װ����ϸ�ķ�ҳ��Ϣ���������ǲ�ѯ����������,����������ʾ��ҳ��
		PageInfo page = new PageInfo(emps, 5);

		model.addAttribute("pageInfo", page);

		return "list";
	}
}
