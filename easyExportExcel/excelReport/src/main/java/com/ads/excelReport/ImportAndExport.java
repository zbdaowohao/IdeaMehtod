package com.ads.excelReport;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ads.entity.Employee;
import com.ads.entity.Result;
import com.ads.utils.CommonUtil;
import com.ads.utils.ExcelUtil;
import com.ads.utils.ResultUtil;
import com.google.gson.Gson;

@CrossOrigin("*") // 此注解解决跨域，可不通过过滤器处理，直接跳过OPTION请求
@RestController
@RequestMapping(value = "/excelData")
public class ImportAndExport {

	// 日志记录
	private static Logger logger = LoggerFactory.getLogger(ImportAndExport.class);

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/exportExcel", method = RequestMethod.POST)
	public Result<String> exportExcel(String tableData, HttpServletRequest request, HttpServletResponse response) {
		logger.info("导出快捷进站统计明细，入参>>> tableData:{}", tableData);
		// 导出Excel文件
		try {
			List<Employee> dataList = new Gson().fromJson(tableData, new ArrayList<Employee>().getClass());

			// 顺序和Excel文件中列名称保持一致（为无标题导入，有标题则应当起始RowIndex=2）
			LinkedHashMap<String, String> filedMap = new LinkedHashMap<>();
			filedMap.put("employeeName", "姓名");
			filedMap.put("gender", "性别");
			filedMap.put("idCard", "身份证");
			filedMap.put("birthDay", "出生日期");
			filedMap.put("phone", "电话号码");

			ExcelUtil.exportExcelData(dataList, filedMap, "导出table表", response);
		} catch (Exception e) {
			logger.error("导出异常");
			e.printStackTrace();
		}
		return new ResultUtil<String>().setData("导出成功");
	}

	/**
	 * 正常情况下返回类型为Result<String>, 因为不模拟数据库操作将读取的数据在前台push进去实现数据插入
	 * 
	 * @param upfile
	 * @return
	 */
	@RequestMapping(value = "/uploadExcel", method = RequestMethod.POST)
	public Result<List<Employee>> uploadExcel(@RequestParam(value = "upfile", required = true) MultipartFile upfile) {
		logger.info("上传文件......");
		List<Employee> Employees = new ArrayList<Employee>();
		try {
			// 必填字段设置，必填则为true,顺序和Excel文件中列名称保持一致
			LinkedHashMap<String, String> filedMap = new LinkedHashMap<>();
			filedMap.put("employeeName", "true");
			filedMap.put("gender", "false");
			filedMap.put("idCard", "false");
			filedMap.put("birthDay", "false");
			filedMap.put("phone", "false");

			Result<List<Map<String, String>>> resMap = ExcelUtil.getExcelData(upfile, filedMap);
			if (resMap.getCode() == 200) {
				// 数据校验全部通过则进行数据库操作
				for (Map<String, String> map : resMap.getResult()) {
					Employee employee = new Gson().fromJson(new Gson().toJson(map), Employee.class);
					if (!CommonUtil.isPhone(employee.getPhone())) {
						return new ResultUtil<List<Employee>>().setErrorMsg("导入文件电话号码格式错误");
					}
					if (!CommonUtil.isIdCard(employee.getIdCard())) {
						return new ResultUtil<List<Employee>>().setErrorMsg("导入文件身份证号码格式错误");
					}
				}
				for (Map<String, String> map : resMap.getResult()) {
					Employee employee = new Gson().fromJson(new Gson().toJson(map), Employee.class);
					employee.setCreateDate(new Date());
					Employees.add(employee);
					// service.save(employee);
				}
			} else {
				return new ResultUtil<List<Employee>>().setErrorMsg(resMap.getMessage());
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("get:" + e.getMessage() + "上传Excel文件异常");
			return new ResultUtil<List<Employee>>().setErrorMsg("上传Excel文件异常");
		}

		logger.info("upload:" + "上传Excel文件成功" + upfile.getOriginalFilename());
		return new ResultUtil<List<Employee>>().setData(Employees);

	}

}
