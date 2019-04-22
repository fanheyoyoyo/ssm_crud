<%-- <jsp:forward page="/emps"></jsp:forward>    --%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE HTML>
<!-- <!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd"> -->
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>员工列表</title>
<%
	pageContext.setAttribute("APP_PATH", request.getContextPath());
%>
<!-- 
	web路径：
		以/开始的相对路径，找资源，以当前资源路径为基准，容易出问题的。
		不以/开始的相对路径，找资源，以服务器的路径为标准(http://localhost:3306);需要加上项目名http://localhost:3306/crud
 -->
<script type="text/javascript"
	src="${APP_PATH}/static/js/jquery-3.3.1.min.js"></script>
<link
	href="${APP_PATH}/static/bootstrap-3.3.7-dist/css/bootstrap.min.css"
	rel="stylesheet">
<script
	src="${APP_PATH}/static/bootstrap-3.3.7-dist/js/bootstrap.min.js"></script>
</head>
<body>

	<!-- 员工修改模态框 -->
	<div class="modal fade" id="empUpdateModal" tabindex="-1" role="dialog"
		aria-labelledby="myModalLabel">
		<div class="modal-dialog" role="document">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
					<h4 class="modal-title">员工修改</h4>
				</div>
				<div class="modal-body">
					<form class="form-horizontal">
						<div class="form-group">
							<label for="empName_update_input" class="col-sm-2 control-label">empName</label>
							<div class="col-sm-10">
								<p class="form-control-static" id="epmName_update_static"></p>
							</div>
						</div>
						<div class="form-group">
							<label for="email_update_input" class="col-sm-2 control-label">email</label>
							<div class="col-sm-10">
								<input type="text" name="email" class="form-control"
									id="email_update_input" placeholder="email@fanlf.com"> <span
									class="help-block"></span>
							</div>
						</div>
						<div class="form-group">
							<label for="gender_update_input" class="col-sm-2 control-label">gender</label>
							<div class="col-sm-10">
								<label class="radio-inline"> <input type="radio"
									name="gender" id="gender1_update_input" value="M"
									checked="checked"> 男
								</label> <label class="radio-inline"> <input type="radio"
									name="gender" id="gender2_update_input" value="F"> 女
								</label>
							</div>
						</div>
						<div class="form-group">
							<label for="deptName_update_input" class="col-sm-2 control-label">deptName</label>
							<div class="col-sm-4">
								<select class="form-control" name="dId" id="dept_update_select">
								</select>
							</div>
						</div>
					</form>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
					<button type="button" class="btn btn-primary" id="emp_update_btn">更新</button>
				</div>
			</div>
		</div>
	</div>

	<!-- 员工添加模态框 -->
	<div class="modal fade" id="empAddModal" tabindex="-1" role="dialog"
		aria-labelledby="myModalLabel">
		<div class="modal-dialog" role="document">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
					<h4 class="modal-title" id="myModalLabel">员工添加</h4>
				</div>
				<div class="modal-body">
					<form class="form-horizontal">
						<div class="form-group">
							<label for="empName_add_input" class="col-sm-2 control-label">empName</label>
							<div class="col-sm-10">
								<input type="text" name="empName" class="form-control"
									id="empName_add_input" placeholder="empName"> <span
									class="help-block"></span>
							</div>
						</div>
						<div class="form-group">
							<label for="email_add_input" class="col-sm-2 control-label">email</label>
							<div class="col-sm-10">
								<input type="text" name="email" class="form-control"
									id="email_add_input" placeholder="email@fanlf.com"> <span
									class="help-block"></span>
							</div>
						</div>
						<div class="form-group">
							<label for="gender_add_input" class="col-sm-2 control-label">gender</label>
							<div class="col-sm-10">
								<label class="radio-inline"> <input type="radio"
									name="gender" id="gender1_add_input" value="M"
									checked="checked"> 男
								</label> <label class="radio-inline"> <input type="radio"
									name="gender" id="gender2_add_input" value="F"> 女
								</label>
							</div>
						</div>
						<div class="form-group">
							<label for="deptName_add_input" class="col-sm-2 control-label">deptName</label>
							<div class="col-sm-4">
								<select class="form-control" name="dId" id="dept_add_select">
								</select>
							</div>
						</div>
					</form>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
					<button type="button" class="btn btn-primary" id="emp_save_btn">保存</button>
				</div>
			</div>
		</div>
	</div>

	<!-- 搭建list员工页面 -->
	<div class="container">
		<!-- 标题 -->
		<div class="row">
			<div class="col-md-12">
				<h1>SSM-CRUD</h1>
			</div>
		</div>
		<!-- 增删 -->
		<div class="row">
			<div class="col-md-4 col-md-offset-8">
				<button class="btn btn-primary" id="emp_add_modal_btn">新增</button>
				<button class="btn btn-danger" id="emp_delete_all_btn">删除</button>
			</div>
		</div>
		<!-- list表格 -->
		<div class="row">
			<table class="table table-hover" id="emps_table">
				<thead>
					<tr>
						<th>
							<input type="checkbox" id="check_all"/>
						</th>
						<th>Emp_Id</th>
						<th>lastName</th>
						<th>email</th>
						<th>gender</th>
						<th>deptName</th>
						<th>操作</th>
					</tr>
				</thead>
				<tbody>
				</tbody>

			</table>
		</div>
		<!-- 页码 -->
		<div class="row">
			<!-- 分页信息 -->
			<div class="col-md-6" id="page_info_area"></div>
			<!-- 分页 -->
			<div class="col-md-6" id="page_nav_area"></div>
		</div>
	</div>
	<script type="text/javascript">
		//定义一个全局的总记录数变量
		var totalRecord, currentPageNum;

		//1、页面加载完毕后，直接发送一个ajax请求，拿到分页数据
		$(function() {
			//去首页 
			to_page(1);
		});

		//
		function to_page(pn) {
			//每次请求前都清空上次页面显示数据
			empty();
			$.ajax({
				url : "${APP_PATH}/emps",
				data : "pn=" + pn,
				type : "get",
				success : function(result) {
					//console.log(result);
					//1、解析并显示员工数据
					build_emps_table(result);
					//2、解析并显示分页信息
					build_page_info(result);
					//3、解析分页条数据
					build_page_nav(result);
				}
			});
		}

		//清空上次页面显示数据
		function empty() {
			$("#emps_table tbody").empty();
			$("#page_info_area").empty();
			$("#page_nav_area").empty();
		}

		//1、解析并显示员工数据
		function build_emps_table(result) {
			var emps = result.extend.pageInfo.list;
			$.each(emps, function(index, item) {
				//alert(item.empName);
				var checkBoxTd = $("<td><input type='checkbox', class='check_item'/></td>");
				var empIdTd = $("<td></td>").append(item.empId);
				var empNameTd = $("<td></td>").append(item.empName);
				var emailTd = $("<td></td>").append(item.email);
				var genderTd = $("<td></td>").append(
						item.gender == "M" ? "男" : "女");
				var departmentTd = $("<td></td>").append(
						item.department.deptName);
				var editBtn = $("<button></button>").addClass(
						"btn btn-primary btn-sm edit_btn").append("<span></span>")
						.addClass("glyphicon glyphicon-pencil").append("编辑");
				//为编辑按钮添加自定义属性
				editBtn.attr("edit-id",item.empId);
				var delBtn = $("<button></button>").addClass(
						"btn btn-danger btn-sm delete_btn").append("<span></span>")
						.addClass("glyphicon glyphicon-trash").append("删除");
				//为删除按钮添加自定义属性
				delBtn.attr("del-id",item.empId);
				var btnTd = $("<td></td>").append(editBtn).append(" ").append(
						delBtn);
				$("<tr></tr>").append(checkBoxTd).append(empIdTd).append(empNameTd)
						.append(emailTd).append(genderTd).append(departmentTd)
						.append(btnTd).appendTo("#emps_table tbody");
			});
		}
		//2、解析并显示分页信息
		function build_page_info(result) {
			//当前页码
			var pageNum = result.extend.pageInfo.pageNum;
			//总页数
			var pages = result.extend.pageInfo.pages;
			//总记录数
			var total = result.extend.pageInfo.total;
			$("#page_info_area").append(
					"当前 " + pageNum + " 页，总 " + pages + " 页，总 " + total
							+ " 条记录");
			totalRecord = result.extend.pageInfo.total;
			currentPageNum = result.extend.pageInfo.pageNum;
		}
		//3、解析分页条数据,并实现超链接
		function build_page_nav(result) {
			var firstPageLi = $("<li></li>").append(
					$("<a></a>").attr("href", "#").append("首页"));
			var prePageLi = $("<li></li>").append(
					$("<a></a>").attr("href", "#").append("&laquo;"));
			//如果没有前一条数据是，首页和前一页按钮不能点击
			if (result.extend.pageInfo.hasPreviousPage == false) {
				firstPageLi.addClass("disabled");
				prePageLi.addClass("disabled");
			} else {
				//为元素添加点击事件
				firstPageLi.click(function() {
					to_page(1);
				});
				prePageLi.click(function() {
					to_page(result.extend.pageInfo.pageNum - 1);
				});
			}

			var nextPageLi = $("<li></li>").append(
					$("<a></a>").attr("href", "#").append("&raquo;"));
			var lastPageLi = $("<li></li>").append(
					$("<a></a>").attr("href", "#").append("末页"));
			//如果没有下一条数据是，末页和下一页按钮不能点击
			if (result.extend.pageInfo.hasNextPage == false) {
				nextPageLi.addClass("disabled");
				lastPageLi.addClass("disabled");
			} else {
				//为元素添加点击事件
				nextPageLi.click(function() {
					to_page(result.extend.pageInfo.pageNum + 1);
				});
				lastPageLi.click(function() {
					to_page(result.extend.pageInfo.pages);
				});
			}

			var ul = $("<ul></ul>").addClass("pagination");
			ul.append(firstPageLi).append(prePageLi);
			$.each(result.extend.pageInfo.navigatepageNums, function(index,
					item) {
				var numLi = $("<li></li>").append(
						$("<a></a>").attr("href", "#").append(item));
				if (result.extend.pageInfo.pageNum == item) {
					numLi.addClass("active");
				}
				numLi.click(function() {
					to_page(item);
				});
				ul.append(numLi);
			});
			ul.append(nextPageLi).append(lastPageLi);
			var navEle = $("<nav></nav>").append(ul);
			navEle.appendTo("#page_nav_area");
		}
		
		//表单完全清除
		function form_reset(ele){
			$(ele)[0].reset();
			$(ele).find("*").removeClass("has-error has-success");
			$(ele).find(".help-block").text("");
		}
		
		//点击新增按钮弹出模态框
		$("#emp_add_modal_btn").click(function() {
			/* //清除表单，表单重置,通过调用 DOM 中的reset方法来重置表单。
			$("#empAddModal form")[0].reset();
			//登录数据前清空状态
			$("#empAddModal input").parent().removeClass("has-error has-success");
			$("#empAddModal input").next("span").text(""); */
			form_reset("#empAddModal form");
			//发送ajax请求，查出部门信息，显示在下拉列表中
			getDepts("#empAddModal select");
			//弹出模态框
			$("#empAddModal").modal({
				backdrop : 'static'
			});
		});
		function getDepts(ele) {
			$(ele).empty();
			$.ajax({
				url : "${APP_PATH}/depts",
				type : "get",
				success : function(result) {
					//console.log(result);
					//"extend":{"depts":[{"deptId":1,"deptName":"开发部"},{"deptId":2,"deptName":"测试部"},{"deptId":6,"deptName":"财务部"}]}
					//显示部门信息到下拉列表中
					//$("#empAddModal select").append();
					$.each(result.extend.depts, function() {
						var optionEle = $("<option></option>").append(
								this.deptName).attr("value", this.deptId);
						optionEle.appendTo(ele);
					});
				}
			});
		}
		
		
		//一、jquery 校验form表单提交的数据
		function validate_add_form() {
			//1、拿到要校验的数据，使用正则表达式
			//校验名字
			var empName = $("#empName_add_input").val();
			//var regName = /(^[a-zA-Z0-9_-]{6,16}$)|(^[\u2E80-\u9FFF]{2,5}$)/;
			var regName = /(^[a-zA-Z0-9_-]{6,16}$)|(^[\u2E80-\u9FFF]{2,5})/;
			if (!regName.test(empName)) {
				//alert("用户名可以是2-5位汉字或者6-16位字母与数字的组合！")
				show_validate_msg("#empName_add_input", "error",
						"用户名可以是2-5位汉字或者6-16位字母与数字的组合！");
				return false;
			} else {
				show_validate_msg("#empName_add_input", "success", "");
			}
			//邮箱校验
			var email = $("#email_add_input").val();
			var regEmail = /^([a-z0-9_\.-]+)@([\da-z\.-]+)\.([a-z\.]{2,6})$/;
			if (!regEmail.test(email)) {
				//alert("邮箱格式不正确！");
				show_validate_msg("#email_add_input", "error", "邮箱格式不正确！");
				return false;
			} else {
				show_validate_msg("#email_add_input", "success", "");
			}
			return true;
		}
		//显示校验结果信息
		function show_validate_msg(ele, status, msg) {
			//校验信息前清空状态
			$(ele).parent().removeClass("has-error has-success");
			$(ele).next("span").text("");
			if ("error" == status) {
				$(ele).parent().addClass("has-error");
				$(ele).next("span").text(msg);
			} else if ("success" == status) {
				$(ele).parent().addClass("has-success");
				$(ele).next("span").text(msg);
			}
		}
		//员工名字重复性校验
		//二、前端ajax check
		$("#empName_add_input").change(function(){
			//1、拿到员工名字
			//var empName = $("#empName_add_input").val();
			var empName = this.value;
			//console.log("empName: " +empName);
			//2、发送ajax请求，查询员工名字在数据库中是否存在
			$.ajax({
				url:"${APP_PATH}/checkEmpName",
				type:"POST",
				data:"empName="+empName,
				success: function(result){
					if(result.code!="100"){
						show_validate_msg("#empName_add_input", "error", result.extend.va_msg);
						$("#emp_save_btn").attr("ajax-va","error");
					}else{
						show_validate_msg("#empName_add_input", "success", "用户名可用");
						$("#emp_save_btn").attr("ajax-va","success");
					}
				}
			});
			//3、返回校验结果
		});
		
		//模态框新增员工的信息保存按钮
		$("#emp_save_btn").click(function() {
			//1、模态框中填写的表单数据提交给服务器进行保存
			//1、先对提交给服务器的数据进行校验
			 if (!validate_add_form()) {
				return false;
			} 
			//1、判断之前的ajax用户名校验是否成功，成功的话在进行保存
			if($(this).attr("ajax-va")=="error"){
				//console.log("$(this) " + $(this).attr("ajax-va"));
				return false;
			}
			//2、发送ajax请求，保存员工
			$.ajax({
				url : "${APP_PATH}/emp",
				type : "POST",
				data : $("#empAddModal form").serialize(),
				success : function(result) {
					//alert(result.msg);
					
					if(result.code == 100){
						//1、保存成功后，要关闭模态框
						$('#empAddModal').modal('hide');
						//2、来到最后一页，显示刚才添加的信息
						to_page(totalRecord);
					}else{
						//显示失败信息
						console.log(result);
						if(undefined != result.extend.errorFields.email){
							//显示邮箱错误信息
							show_validate_msg("#email_add_input", "error", result.extend.errorFields.email);
						}
						if(undefined != result.extend.errorFields.empName){
							//显示员工名字错误信息
							show_validate_msg("#empName_add_input", "error", result.extend.errorFields.empName);
						}
					}
					
					
				}
			});
		});
		
		//添加编辑按钮的事件
		//1、我们是创建按钮之前就绑定了 click 事件，所以绑不上
		//2、可以再创建按钮时绑定， 绑定点击.live()
		//jquery 新版没有 live ，使用 on 代替
		$(document).on("click", ".edit_btn", function(){
			//alert("edit");
			//1、查询员工信息，显示员工信息
			getEmp($(this).attr("edit-id"));
			//2、查询部门信息，显示部门列表
			getDepts("#empUpdateModal select");
			//3、把员工id传递给模态框的更新按钮
			$("#emp_update_btn").attr("edit-id",$(this).attr("edit-id"))
			//弹出模态框
			$("#empUpdateModal").modal({
				backdrop : 'static'
			});
			
		});
		
		//添加单个删除按钮的事件
		//jquery 新版没有 live ，使用 on 代替
		$(document).on("click", ".delete_btn", function(){
			//alert("del");
			//1、弹出是否删除确认对话框
			var empName = $(this).parents("tr").find("td:eq(2)").text();
			var empId = $(this).attr("del-id");
			if(confirm("确认删除【 " +empName+ "】吗？")){
				//确定，发送 ajax 请求，删除
				$.ajax({
					url:"${APP_PATH}/emp/" + empId,
					type:"DELETE",
					success:function(result){
						alert(result.msg);
						to_page(currentPageNum);
					}
				});
			}
			
		});
		
		//查询员工信息
		function getEmp(id){
			$.ajax({
				url:"${APP_PATH}/emp/"+ id,
				type:"GET",
				success:function(result){
					//console.log(result);
					if(result.code == 100){
						//处理取得的员工信息
						var empData = result.extend.emp;
						$("#epmName_update_static").text(empData.empName);
						$("#email_update_input").val(empData.email);
						$("#empUpdateModal input[name=gender]").val([empData.gender]);
						$("#empUpdateModal select").val([empData.dId]);
					}else{
						//信息取得失败
					}
					
				}
			});
		}
		
		//点击更新按钮，更新员工信息
		$("#emp_update_btn").click(function(){
			//1、邮箱校验
			//console.log("emp_update_btn==========");
			var email = $("#email_update_input").val();
			var regEmail = /^([a-z0-9_\.-]+)@([\da-z\.-]+)\.([a-z\.]{2,6})$/;
			if (!regEmail.test(email)) {
				//alert("邮箱格式不正确！");
				show_validate_msg("#email_update_input", "error", "邮箱格式不正确！");
				return false;
			} else {
				show_validate_msg("#email_update_input", "success", "");
			}
			//console.log("emp_update_btn==========After");
			//2、发送 ajax 请求， 保存更新的员工信息
			//console.log($(this).attr("edit-id"));
			$.ajax({
				url:"${APP_PATH}/emp/"+$(this).attr("edit-id"),
				type:"PUT",
				data:$("#empUpdateModal form").serialize(),
				success:function(result){
					//alert(result.msg);
					if(result.code == 100){
						//1、关闭对话框
						$("#empUpdateModal").modal('hide');
						//2、回到本页，显示更新信息
						to_page(currentPageNum);
					}else{
						//显示失败信息
					}
					
				}
			});
		});
		
		//删除的全选，全不选功能
		$("#check_all").click(function(){
			//attr 获取 checked 属性是 undefined，因为 dom 原生的属性值没有
			// attr 是用来获取自定义的属性值
			// 以后用 prop 属性来读取和修改 dom 原生的属性值
			//alert($(this).prop("checked"));
			$(".check_item").prop("checked", $(this).prop("checked"));
		});
		
		//check_item
		$(document).on("click", ".check_item", function(){
			//判断当前选中的元素个数
			var flag = $(".check_item:checked").length == $(".check_item").length;
			$("#check_all").prop("checked", flag);
		});
		
		//点击删除按钮，全部删除
		$("#emp_delete_all_btn").click(function(){
			var empNames="";
			var del_ids="";
			$.each($(".check_item:checked"), function(){
				//alert($(this).parents("tr").find("td:eq(2)").text());
				empNames += $(this).parents("tr").find("td:eq(2)").text()+", ";
				del_ids += $(this).parents("tr").find("td:eq(1)").text()+"-";
			});
			//去除字符串多余的逗号
			empNames = empNames.substring(0, empNames.length-2);
			del_ids = del_ids.substring(0, del_ids.length-1);
			if(confirm("确认删除【 " +empNames+ "】吗？")){
				//确认删除
				$.ajax({
					url:"${APP_PATH}/emp/" +del_ids,
					type:"DELETE",
					success:function(result){
						if(result.code == 100){
							to_page(currentPageNum);
						}else{
							
						}
					}
				});
			}
		});
	</script>
</body>
</html>