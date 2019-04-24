package com.ads.excelReport;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

@CrossOrigin("*") //此注解解决跨域，可不通过过滤器处理，直接跳过OPTION请求
@RestController
@RequestMapping(value = "/recServeReport")
public class ReceptionServerReport {

	// 日志记录
	private static Logger logger = LoggerFactory.getLogger(ReceptionServerReport.class);

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/uploadTableDataApi", method = RequestMethod.POST)
	public String formTableData(HttpServletRequest request, String tableData) {
		logger.info("获取礼宾员接待日统计下载数据={}", tableData);
		String htmlStart = "<html xmlns:o='urn:schemas-microsoft-com:office:office' xmlns:x='urn:schemas-microsoft-com:office:excel' xmlns='http://www.w3.org/TR/REC-html40'><head><!--[if gte mso 9]><xml><x:ExcelWorkbook><x:ExcelWorksheets><x:ExcelWorksheet><x:Name>{worksheet}</x:Name><x:WorksheetOptions><x:DisplayGridlines/></x:WorksheetOptions></x:ExcelWorksheet></x:ExcelWorksheets></x:ExcelWorkbook></xml><![endif]--></head><body><table>";
		String htmlEnd = "</table></body></html>";
		String tableName = "<tr><th colspan=\"7\">礼宾员接待日统计表</th></tr>";
		String thead = "<thead><th>商户</th><th>路局</th><th>车站</th><th>站厅</th><th>人员</th><th>接待类型</th><th>接待量(人次)</th></thead>";
		String tbodyStart = "<tbody>";
		String tbodyEnd = "</tbody>";

		StringBuffer tbodyContent = new StringBuffer();
		List<Map<String, Object>> datas = JSON.parseObject(tableData, new ArrayList<Map<String, Object>>().getClass());
		for (Map<String, Object> data : datas) {
			tbodyContent.append("<tr>");
			// 商户
			if (null != data.get("companyNamedis") && data.get("companyNamedis").toString().equals("true")
					|| null != data.get("companyNamedis1") && data.get("companyNamedis1").toString().equals("true")) {
			} else {
				tbodyContent.append("<td width=\"10%\"");
				if (null != data.get("companyNamespan")) {
					tbodyContent.append(" rowspan=\"" + data.get("companyNamespan").toString().split("\\.")[0] + "\"");
				}
				if (null != data.get("companyNamecols")) {
					tbodyContent.append(" colspan=\"" + data.get("companyNamecols").toString().split("\\.")[0] + "\"");
				}
				tbodyContent.append(">" + data.get("companyName") + "</td>");
			}
			// 路局
			if (null != data.get("burNamedis") && data.get("burNamedis").toString().equals("true")
					|| null != data.get("burNamedis1") && data.get("burNamedis1").toString().equals("true")) {
			} else {
				tbodyContent.append("<td width=\"10%\"");
				if (null != data.get("burNamespan")) {
					tbodyContent.append(" rowspan=\"" + data.get("burNamespan").toString().split("\\.")[0] + "\"");
				}
				if (null != data.get("burNamecols")) {
					tbodyContent.append(" colspan=\"" + data.get("burNamecols").toString().split("\\.")[0] + "\"");
				}
				tbodyContent.append(">" + data.get("burName") + "</td>");
			}
			// 车站
			if (null != data.get("stationNamedis") && data.get("stationNamedis").toString().equals("true")
					|| null != data.get("stationNamedis1") && data.get("stationNamedis1").toString().equals("true")) {
			} else {
				tbodyContent.append("<td width=\"8%\"");
				if (null != data.get("stationNamespan")) {
					tbodyContent.append(" rowspan=\"" + data.get("stationNamespan").toString().split("\\.")[0] + "\"");
				}
				if (null != data.get("stationNamecols")) {
					tbodyContent.append(" colspan=\"" + data.get("stationNamecols").toString().split("\\.")[0] + "\"");
				}
				tbodyContent.append(">" + data.get("stationName") + "</td>");
			}
			// 站厅
			if (null != data.get("roomNamedis") && data.get("roomNamedis").toString().equals("true")
					|| null != data.get("roomNamedis1") && data.get("roomNamedis1").toString().equals("true")) {
			} else {
				tbodyContent.append("<td width=\"5%\"");
				if (null != data.get("roomNamespan")) {
					tbodyContent.append(" rowspan=\"" + data.get("roomNamespan").toString().split("\\.")[0] + "\"");
				}
				if (null != data.get("roomNamecols")) {
					tbodyContent.append(" colspan=\"" + data.get("roomNamecols").toString().split("\\.")[0] + "\"");
				}
				tbodyContent.append(">" + data.get("roomName") + "</td>");
			}
			// 人员
			if (null != data.get("recistNamedis") && data.get("recistNamedis").toString().equals("true")
					|| null != data.get("recistNamedis1") && data.get("recistNamedis1").toString().equals("true")) {
			} else {
				tbodyContent.append("<td width=\"5%\"");
				if (null != data.get("recistNamespan")) {
					tbodyContent.append(" rowspan=\"" + data.get("recistNamespan").toString().split("\\.")[0] + "\"");
				}
				if (null != data.get("recistNamecols")) {
					tbodyContent.append(" colspan=\"" + data.get("recistNamecols").toString().split("\\.")[0] + "\"");
				}
				tbodyContent.append(">" + data.get("recistName") + "</td>");
			}
			// 接待类型
			if (data.get("receptionType") != null && StringUtils.isNoneBlank(data.get("receptionType").toString())) {
				tbodyContent.append("<td width=\"5%\"");
				tbodyContent.append(">" + data.get("receptionType") + "</td>");
			}
			// 接待量(人次)
			tbodyContent.append("<td width=\"5%\"");
			tbodyContent.append(">" + data.get("recNum") + "</td>");

			tbodyContent.append("</tr>");
		}
		String tableStr = htmlStart + tableName + thead + tbodyStart + tbodyContent.toString() + tbodyEnd + htmlEnd;

		return JSONObject.toJSONString(
				FileUtils.saveTempDownloadFile(request, UUID.randomUUID().toString().replace("-", ""), tableStr));
	}

	/**
	 * 导出礼宾员接待日统计表
	 * 
	 * @param response
	 * @param filePath
	 * @return
	 */
	@RequestMapping(value = "/export", method = RequestMethod.POST)
	public String exportQuickEnterByDayApi(HttpServletResponse response, String filePath) {
		logger.info("导出礼宾员接待日统计表，开始>>>>>>");
		FileUtils.downLodadReport(response, filePath, "礼宾员接待日统计表");
		return JSONObject.toJSONString(true);
	}

}