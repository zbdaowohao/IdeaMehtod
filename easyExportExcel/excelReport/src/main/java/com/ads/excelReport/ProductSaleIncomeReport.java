package com.ads.excelReport;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

@CrossOrigin("*") // 此注解解决跨域可不用过滤器处理，直接跳过OPTION请求
@RestController
@RequestMapping(value = "/productSaleIncomeReport")
public class ProductSaleIncomeReport {

	// 日志记录
	private static Logger logger = LoggerFactory.getLogger(ProductSaleIncomeReport.class);

	/**
	 * 上传销售收入日统计下载数据
	 * 
	 * @param request
	 * @param tableData
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/uploadSaleDayReportByDayTableDataApi", method = RequestMethod.POST)
	public String formTableData(HttpServletRequest request, String tableData) {
		logger.info("上传销售收入日统计下载数据={}", tableData);
		String htmlStart = "<html xmlns:o='urn:schemas-microsoft-com:office:office' xmlns:x='urn:schemas-microsoft-com:office:excel' xmlns='http://www.w3.org/TR/REC-html40'><head><!--[if gte mso 9]><xml><x:ExcelWorkbook><x:ExcelWorksheets><x:ExcelWorksheet><x:Name>{worksheet}</x:Name><x:WorksheetOptions><x:DisplayGridlines/></x:WorksheetOptions></x:ExcelWorksheet></x:ExcelWorksheets></x:ExcelWorkbook></xml><![endif]--></head><body><table>";
		String htmlEnd = "</table></body></html>";
		String tableName = "<tr><th colspan=\"10\">商品销售日统计</th></tr>";
		String thead = "<thead>" + "<th>商户</th>" + "<th>路局</th>" + "<th>车站</th>" + "<th>站厅</th>" + "<th>日期</th>"
				+ "<th>销售额（元）</th>" + "<th>支付宝（元）</th>" + "<th>微信（元）</th>" + "<th>POS（元）</th>" + "<th>现金（元）</th>"
				+ "</thead>";
		String tbodyStart = "<tbody>";
		String tbodyEnd = "</tbody>";

		StringBuffer tbodyContent = new StringBuffer();
		List<Map<String, Object>> datas = JSON.parseObject(tableData, new ArrayList<Map<String, Object>>().getClass());
		for (Map<String, Object> data : datas) {
			tbodyContent.append("<tr>");
			// 商户
			if (null != data.get("companyAbbreviationdis")
					&& data.get("companyAbbreviationdis").toString().equals("true")
					|| null != data.get("companyAbbreviationdis1")
							&& data.get("companyAbbreviationdis1").toString().equals("true")) {
			} else {
				tbodyContent.append("<td width=\"10%\"");
				if (null != data.get("companyAbbreviationspan")) {
					tbodyContent.append(
							" rowspan=\"" + data.get("companyAbbreviationspan").toString().split("\\.")[0] + "\"");
				}
				if (null != data.get("companyAbbreviationcols")) {
					tbodyContent.append(
							" colspan=\"" + data.get("companyAbbreviationcols").toString().split("\\.")[0] + "\"");
				}
				tbodyContent.append(">" + data.get("companyAbbreviation") + "</td>");
			}
			// 路局
			if (null != data.get("bureauNamedis") && data.get("bureauNamedis").toString().equals("true")
					|| null != data.get("bureauNamedis1") && data.get("bureauNamedis1").toString().equals("true")) {
			} else {
				tbodyContent.append("<td width=\"10%\"");
				if (null != data.get("bureauNamespan")) {
					tbodyContent.append(" rowspan=\"" + data.get("bureauNamespan").toString().split("\\.")[0] + "\"");
				}
				if (null != data.get("bureauNamecols")) {
					tbodyContent.append(" colspan=\"" + data.get("bureauNamecols").toString().split("\\.")[0] + "\"");
				}
				tbodyContent.append(">" + data.get("bureauName") + "</td>");
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
			// 日期
			if (null != data.get("orderDaydis1") && data.get("orderDaydis1").toString().equals("true")) {
			} else {
				tbodyContent.append("<td width=\"5%\"");
				if (null != data.get("orderDaycols")) {
					tbodyContent.append(" colspan=\"" + data.get("orderDaycols").toString().split("\\.")[0] + "\"");
				}
				if (data.get("orderDay") == null) {
					tbodyContent.append(">" + "" + "</td>");
				} else {
					tbodyContent.append(">" + data.get("orderDay") + "</td>");
				}
			}
			// 销售额
			if (null != data.get("amountdis1") && data.get("amountdis1").toString().equals("true")) {
			} else {
				tbodyContent.append("<td width=\"5%\"");
				BigDecimal amount = new BigDecimal(data.get("amount").toString()).divide(new BigDecimal(100));
				tbodyContent.append(">" + amount + "</td>");
			}
			// 支付宝
			if (null != data.get("zfbdis1") && data.get("zfbdis1").toString().equals("true")) {
			} else {
				tbodyContent.append("<td width=\"5%\"");
				BigDecimal zfb = new BigDecimal(data.get("zfb").toString()).divide(new BigDecimal(100));
				tbodyContent.append(">" + zfb + "</td>");
			}
			// 微信
			if (null != data.get("wxdis1") && data.get("wxdis1").toString().equals("true")) {
			} else {
				tbodyContent.append("<td width=\"5%\"");
				BigDecimal wx = new BigDecimal(data.get("wx").toString()).divide(new BigDecimal(100));
				tbodyContent.append(">" + wx + "</td>");
			}
			// POS
			if (null != data.get("posdis1") && data.get("posdis1").toString().equals("true")) {
			} else {
				tbodyContent.append("<td width=\"5%\"");
				BigDecimal pos = new BigDecimal(data.get("pos").toString()).divide(new BigDecimal(100));
				tbodyContent.append(">" + pos + "</td>");
			}
			// 现金
			if (null != data.get("xjdis1") && data.get("xjdis1").toString().equals("true")) {
			} else {
				tbodyContent.append("<td width=\"5%\"");
				BigDecimal xj = new BigDecimal(data.get("xj").toString()).divide(new BigDecimal(100));
				tbodyContent.append(">" + xj + "</td>");
			}
			tbodyContent.append("</tr>");
		}
		String tableStr = htmlStart + tableName + thead + tbodyStart + tbodyContent.toString() + tbodyEnd + htmlEnd;

		return JSONObject.toJSONString(
				FileUtils.saveTempDownloadFile(request, UUID.randomUUID().toString().replace("-", ""), tableStr));
	}

	/**
	 * 导出销售收入日统计表
	 * 
	 * @param response
	 * @param filePath
	 * @return
	 */
	@RequestMapping(value = "/export", method = RequestMethod.POST)
	public void exportRefundDetailTableDataApi(HttpServletResponse response, String filePath) {
		FileUtils.downLodadReport(response, filePath, "销售收入日统计表");
	}

}