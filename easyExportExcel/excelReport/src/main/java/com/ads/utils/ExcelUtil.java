package com.ads.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import com.ads.entity.Result;

public class ExcelUtil {

	private static final String EXCEL_XLS = "xls";
	private static final String EXCEL_XLSX = "xlsx";

	// 工作空间、是否加粗、字体大小
	private static HSSFCellStyle CreatCellStyle(HSSFWorkbook workbook, boolean isBoldWeight, int fontHeight) {
		HSSFCellStyle cellStyle = workbook.createCellStyle();
		// Cell样式
		cellStyle.setAlignment(HorizontalAlignment.CENTER);// 水平居中
		cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);// 垂直居中
		// 生成一个字体
		HSSFFont cellFont = workbook.createFont();
		cellFont.setFontHeightInPoints((short) fontHeight);
		cellFont.setColor(Font.COLOR_NORMAL);
		cellFont.setBold(isBoldWeight);
		cellFont.setFontName("宋体");
		cellStyle.setFont(cellFont);
		return cellStyle;
	}

	/**
	 * 插入数据
	 * 
	 * 1. 文件格式   <br>
	 * HSSFWorkbook导出的Excel文件以.xls结尾，支持Excel2003
	 * SXSSFWorkbook导出的Excel文件以.xlsx结尾，支持Excel2007 Excel2010之后的版本    <br>
	 * 2.最大支持条数  <br>
	 * HSSFWorkbook最大支持65535条数数据 SXSSFWorkbook还没遇到限制  <br>
	 * 3.执行效率  <br>
	 * SXSSFWorkbook的创建速度比HSSFWorkbook快
	 * 
	 * @param dataList
	 * @param fieldMap
	 * @param sheetName
	 * @param response
	 */
	@SuppressWarnings({ "unchecked" })
	public static <T> void exportExcelData(List<T> dataList, LinkedHashMap<String, String> fieldMap, String sheetName,
			HttpServletResponse response) {

		// 定义存放英文字段名和中文字段名的数组
		String[] enFields = new String[fieldMap.size()];
		String[] cnFields = new String[fieldMap.size()];

		// 填充数组
		int count = 0;
		for (Entry<String, String> entry : fieldMap.entrySet()) {
			enFields[count] = entry.getKey();
			cnFields[count] = entry.getValue();
			count++;
		}
		// 设置默认文件名为当前时间：年月日时分秒
		String fileName = sheetName + "-" + new SimpleDateFormat("yyyyMMddhhmmss").format(new Date()).toString();
		// 设置response头信息
		response.reset();
		response.setContentType("application/vnd.ms-excel");
		response.setCharacterEncoding("utf-8");
		try {
			response.setHeader("Content-disposition",
					"attachment;filename=" + URLEncoder.encode(fileName, "UTF-8") + ".xls");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		HSSFWorkbook workbook = new HSSFWorkbook();
		// 生成一个样式
		HSSFCellStyle titleStyle = CreatCellStyle(workbook, true, 15);
		HSSFCellStyle columnStyle = CreatCellStyle(workbook, true, 10);
		HSSFCellStyle cellStyle = CreatCellStyle(workbook, false, 10);

		try {
			OutputStream out = response.getOutputStream();

			// 因为2003的Excel一个工作表最多可以有65536条记录，除去列头剩下65535条
			// 所以如果记录太多，需要放到多个工作表中，其实就是个分页的过程
			// 1.计算一共有多少个工作表
			int sheetSize = 65534;
			double sheetNum = dataList.size() / sheetSize;
			if (sheetNum == 0.0) {
				sheetNum = 1;
			}
			if (dataList.size() / sheetSize != 0) {
				sheetNum = sheetNum + 1;
			}
			for (int i = 0; i < sheetNum; i++) {
				HSSFSheet sheet = workbook.createSheet();
				if (i != 0) {
					sheetName = (sheetName + (i + 1));
				}
				workbook.setSheetName(i, sheetName);
				// 创建第一行标题行
				CellRangeAddress cra = new CellRangeAddress(0, 0, 0, count);// 合并单元格：参数：起始行, 终止行, 起始列, 终止列
				sheet.addMergedRegion(cra);
				HSSFRow titleRow = sheet.createRow(0);
				HSSFCell titleCell = titleRow.createCell(0);
				titleCell.setCellValue(sheetName);
				titleCell.setCellStyle(titleStyle);
				// 创建第二行列名
				HSSFRow columnRow = sheet.createRow(1);
				for (int j = 0; j < count; j++) {
					HSSFCell columnCell = columnRow.createCell((short) j);
					columnCell.setCellValue(cnFields[j]);
					columnCell.setCellStyle(columnStyle);
				}
				// 创建每一行数据
				for (int k = 0; k < dataList.size(); k++) {
					HSSFRow dataRow = sheet.createRow(2 + k);
					for (int j = 0; j < count; j++) {
						HSSFCell cell = dataRow.createCell((short) j);
						cell.setCellValue(((Map<String, String>) dataList.get(k)).get(enFields[j]));
						cell.setCellStyle(cellStyle);
					}
				}

			}
			workbook.write(out);
			out.flush();
			out.close();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}

	// 判断版本获取Wordboook
	private static Workbook getWorkbook(InputStream in, String fileName) throws IOException {
		Workbook wbook = null;
		if (fileName.endsWith(EXCEL_XLS)) {
			wbook = new HSSFWorkbook(in);
		} else if (fileName.endsWith(EXCEL_XLSX)) {
			wbook = new XSSFWorkbook(in);
		}
		return wbook;
	}

	/**
	 * 判断文件是否是excel
	 * 
	 * @throws Exception
	 */
	public static void checkExcelVaild(MultipartFile file) throws Exception {
		if (file.isEmpty()) {
			throw new Exception("文件不存在");
		}
		if (file.getName().endsWith(EXCEL_XLS) || file.getName().endsWith(EXCEL_XLSX)) {
			throw new Exception("文件不是Excel");
		}
	}

	/**
	 * 读取Excel测试，兼容 Excel 2003/2007/2010
	 * 
	 * @param filedMap
	 * 
	 * @throws Exception
	 */
	public static Result<List<Map<String, String>>> getExcelData(MultipartFile excelFile,
			LinkedHashMap<String, String> filedMap) throws Exception {
		List<Map<String, String>> resDatas = new ArrayList<Map<String, String>>();
		// 定义存放英文字段名和中文字段名的数组
		String[] enFields = new String[filedMap.size()];
		String[] cnFields = new String[filedMap.size()];
		// 填充数组
		int count = 0;
		for (Entry<String, String> entry : filedMap.entrySet()) {
			enFields[count] = entry.getKey();
			cnFields[count] = entry.getValue();
			count++;
		}
		try {
			// 同时支持Excel 2003、2007
			checkExcelVaild(excelFile);
			Workbook workbook = getWorkbook(excelFile.getInputStream(), excelFile.getOriginalFilename());
			// Workbook workbook = WorkbookFactory.create(is); // 这种方式
			// Excel2003/2007/2010都是可以处理的

			int sheetCount = workbook.getNumberOfSheets(); // Sheet的数量

			for (int i = 0; i < sheetCount; i++) {
				Sheet sheet = workbook.getSheetAt(i);
				// 为跳过第一行目录设置count
				for (Row row : sheet) {
					Map<String, String> rowDatas = new HashMap<String, String>();
					if (row.getRowNum() == 0) {
						continue;
					}
					String cellValue = "";
					for (int j = 0; j < row.getLastCellNum(); j++) {
						Cell cell = row.getCell(j);
						// 非必填字段为空填“”
						if (cnFields[j].equals("false")) {
							if (cell == null || cell.getCellType().name() == "BLANK") {
								cellValue = "";
							} else {
								cellValue = getValue(cell).toString();
							}
							rowDatas.put(enFields[j], cellValue);
						} else if (cnFields[j].equals("true") && null != cell) {
							cellValue = getValue(cell).toString();
							rowDatas.put(enFields[j], cellValue);
						} else {
							// 必填字段为空报错
							return new ResultUtil<List<Map<String, String>>>().setErrorMsg("导入文件非空字段不能为空");
						}
					}
					resDatas.add(rowDatas);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ResultUtil<List<Map<String, String>>>().setData(resDatas);
	}

	private static Object getValue(Cell cell) {
		Object obj = null;
		switch (cell.getCellTypeEnum()) {
		case BOOLEAN:
			obj = cell.getBooleanCellValue();
			break;
		case ERROR:
			obj = cell.getErrorCellValue();
			break;
		case NUMERIC:
			if (HSSFDateUtil.isCellDateFormatted(cell)) {
				obj = new SimpleDateFormat("yyyyMMdd").format(cell.getDateCellValue());
			} else {
				DecimalFormat format = new DecimalFormat("#");
				obj = String.valueOf(format.format(cell.getNumericCellValue()));
			}
			break;
		case STRING:
			obj = cell.getStringCellValue();
			break;
		default:
			break;
		}
		return obj;
	}

}
