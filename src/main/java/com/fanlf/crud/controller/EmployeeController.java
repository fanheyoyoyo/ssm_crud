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
 * 处理员工CRUD请求
 * 
 * @author fanlf
 *
 */
@Controller
public class EmployeeController {

	@Autowired
	EmployeeService employeeService;
	
	
	/**
	 * 删除员工信息
	 * 单个删除(1)与批量删除(1-2-3-4)
	 */
	@RequestMapping(value="/emp/{ids}", method=RequestMethod.DELETE)
	@ResponseBody
	public Msg delEmp(@PathVariable("ids") String ids){
		if(ids.contains("-")){
			//批量删除
			String[] str_ids = ids.split("-");
			List<Integer> del_ids = new ArrayList<Integer>();
			for (String id : str_ids) {
				del_ids.add(Integer.parseInt(id));			
			}
			employeeService.deleteBatch(del_ids);
		}else{//this is a git branch test!
			//单一删除 
			Integer id = Integer.parseInt(ids);
			employeeService.delEmp(id);
		}
			
		return Msg.success();
	}
	

	/**
	 * 保存 更新的员工信息 
	 * path路径一定要写对 
	 * 封装的数据 
	 * Employee [empId=5, empName=null,	 * gender=null, email=null, dId=null] 问题： 请求体中有数据，当时 Employee 对象封装不上，
	 *  原因：
	 * Tomcat ： 
	 * 1、将请求体中的数据封住成一个map。
	 *  2、request.getParameter("empName") 就会从这个 map 中取值 
	 * 3、SpringMVC 在封装POJO对象的时候 会把 POJO 中每个属性的值， 通过调用 request.getParameter("empName") 取到值; 
	 * ajax 发送 PUT 请求引发的问题： 
	 * PUT 请求，请求体中的数据，request.getParameter("empName") 拿不到。 
	 * 拿不到的根本原因是： Tomcat 不会把 PUT  请求的数据封装为 map，Tomcat 只会把 POST 请求的数据封装为 map 
	 * package org.apache.catalina.connector.Request -- parseParameters() 
	 * if(!getConnector().isParseBodyMethod(getMethod()) ) { 
	 *  success = true;
	 *  return; 
	 * } 
	 * 解决方案： 
	 * 我们要直接能发送 PUT 之类的请求，还要封装请求体中的数据 
	 * 1、配置上 HttpPutFormContentFilter 
	 * 2、他的作用：将请求体中的数据解析包装成一个map 
	 * 3、request 被重新包装，request.getParameter() 被重写，就会从自己的包装的 map 中取值
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
	 * 获取员工信息
	 */
	@RequestMapping(value = "/emp/{id}", method = RequestMethod.GET)
	@ResponseBody
	public Msg getEmp(@PathVariable("id") Integer id) {
		Employee emp = employeeService.getEmp(id);
		return Msg.success().add("emp", emp);
	}

	/**
	 * 员工名字重复性校验 ajax校验
	 */

	@ResponseBody
	@RequestMapping(value = "/checkEmpName", method = RequestMethod.POST)
	public Msg checkEmpName(@RequestParam(value = "empName") String empName) {
		// 先校验名字合法性
		String regx = "(^[a-zA-Z0-9_-]{6,16}$)|(^[\u2E80-\u9FFF]{2,5})";
		if (!empName.matches(regx)) {
			return Msg.failed().add("va_msg", "用户名可以是6-16位字母与数字的组合或者2-5位汉字！");
		}
		// 在进行重复性校验
		boolean b = employeeService.checkEmpName(empName);
		if (b) {
			return Msg.success();
		} else {
			return Msg.failed().add("va_msg", "用户名不可用！");

		}
	}

	/**
	 * 员工保存 1、支持JSR303校验 2、导入Hibernate Validator包 三、后端校验
	 * 
	 * @param employee
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/emp", method = RequestMethod.POST)
	public Msg saveEmp(@Valid Employee employee, BindingResult result) {
		if (result.hasErrors()) {
			// 校验失败，应该返回失败，同时把错误信息显示在模态框中
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
	 * @ResponseBody 会自动的将返回的数据转换为 json 格式 若要 @ResponseBody 正常工作，需要导入 jackson 包
	 *               这个地方不用把查询出的分页数据放入到请求域了
	 * 
	 * @param pn
	 * @return
	 */
	@RequestMapping(value="/emps",method=RequestMethod.GET)
	@ResponseBody
	public Msg getEmpsWithJson(@RequestParam(value = "pn", defaultValue = "1") Integer pn) {
		// 分页查询，引入 pageHelper 插件
		// 在查询之前只需要调用 startPage，传入页码以及每页的大小
		PageHelper.startPage(pn, 5);
		// startPage 后面紧跟的查询就是一个分页查询
		List<Employee> emps = employeeService.getAll();
		// 用 PageInfo 对结果进行包装,只需要将 PageInfo 交给页面就可以了
		// PageInfo 封装了详细的分页信息，包括我们查询出来的数据,传入连续显示的页数
		PageInfo page = new PageInfo(emps, 5);

		return Msg.success().add("pageInfo", page);
	}

	/**
	 * 查询员工数据（分页查询）
	 * 
	 * @return
	 */
	// @RequestMapping("/emps")
	public String getEmps(@RequestParam(value = "pn", defaultValue = "1") Integer pn, Model model) {
		// 分页查询，引入 pageHelper 插件
		// 在查询之前只需要调用 startPage，传入页码以及每页的大小
		PageHelper.startPage(pn, 5);
		// startPage 后面紧跟的查询就是一个分页查询
		List<Employee> emps = employeeService.getAll();
		// 用 PageInfo 对结果进行包装,只需要将 PageInfo 交给页面就可以了
		// PageInfo 封装了详细的分页信息，包括我们查询出来的数据,传入连续显示的页数
		PageInfo page = new PageInfo(emps, 5);

		model.addAttribute("pageInfo", page);

		return "list";
	}
}
