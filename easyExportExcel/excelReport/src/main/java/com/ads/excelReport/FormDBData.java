package com.ads.excelReport;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;

/**
 * 此类为后台给前台封装json用，列合并在后台处理，对于名称相同的则进行上下合并，
 * 不同层级类同名的避免合并可通过添加！<p style='display:none;'>类名称</p>！前台识别去进行处理
 * 行合并通过前台代码进行合并
 * ！！！因为前台行合并所取的属性是第一行(后台封装数据未反转的最后一行) for (let field in list[0])，
 * 所以这一行应当保证是最全属性，将全部的mergeRolName都要加上即便全部为false
 */
public class FormDBData {

	private static org.slf4j.Logger logger = LoggerFactory.getLogger(FormDBData.class);

	@SuppressWarnings("unchecked")
	public Boolean queryServeInfo(String params) {
		logger.info("查询礼宾员接待日统计表，入参>>> params:{}", params);
		List<Map<String, String>> tableDatas = new ArrayList<Map<String, String>>();
		try {
			Map<String, Object> filterParams = JSON.parseObject(params, new HashMap<String, Object>().getClass());
			if (null != filterParams.get("operateTimeStr")
					&& StringUtils.isNoneBlank(filterParams.get("operateTimeStr").toString())) {
				List<String> dateList = (ArrayList<String>) filterParams.get("operateTimeStr");
				filterParams.put("startDate", dateList.get(0));
				filterParams.put("endDate", dateList.get(1));
			}

			// 全部商户合计
			String totalCount = receptionDayReportMapper.totalCount(filterParams);
			if (totalCount.equals("0")) {
				return false;
				// return ResultData.success(tableDatas);
			}
			Map<String, String> totalMap = new LinkedHashMap<String, String>();
			totalMap.put("companyName", "合计");
			totalMap.put("burName", "");
			totalMap.put("stationName", "");
			totalMap.put("roomName", "");
			totalMap.put("recistName", "");
			totalMap.put("receptionType", "");
			totalMap.put("recNum", totalCount);
			// 列合并标志
			totalMap.put("totalMerge", "true");
			totalMap.put("companyMerge", "false");
			totalMap.put("burMerge", "false");
			totalMap.put("stationMerge", "false");
			totalMap.put("roomMerge", "false");
			totalMap.put("recNameMerge", "false");
			tableDatas.add(totalMap);
			// 找到全部权限的商户
			List<Map<String, String>> companys = receptionDayReportMapper.findCompanys(filterParams);
			for (Map<String, String> company : companys) {
				filterParams.put("company_id", company.get("company_id"));
				String companyCount = receptionDayReportMapper.companyCount(filterParams);
				if (companyCount.equals("0")) {
					continue;
				}
				Map<String, String> companyMap = new LinkedHashMap<String, String>();
				companyMap.put("companyName", company.get("company_name") + "总计");
				companyMap.put("burName", "");
				companyMap.put("stationName", "");
				companyMap.put("roomName", "");
				companyMap.put("recistName", "");
				companyMap.put("receptionType", "");
				companyMap.put("recNum", companyCount);
				companyMap.put("totalMerge", "false");
				companyMap.put("companyMerge", "true");
				companyMap.put("burMerge", "false");
				companyMap.put("stationMerge", "false");
				companyMap.put("roomMerge", "false");
				companyMap.put("recNameMerge", "false");
				tableDatas.add(companyMap);
				// 找到全部权限的路局
				List<Map<String, String>> burs = receptionDayReportMapper.findBurs(filterParams);
				for (Map<String, String> bur : burs) {
					filterParams.put("bureau_code", bur.get("bureau_code"));
					String burCount = receptionDayReportMapper.burCount(filterParams);
					if (burCount.equals("0")) {
						continue;
					}
					Map<String, String> burMap = new LinkedHashMap<String, String>();
					burMap.put("companyName", company.get("company_name"));
					burMap.put("burName", bur.get("bureau_name") + "合计");
					burMap.put("stationName", "");
					burMap.put("roomName", "");
					burMap.put("recistName", "");
					burMap.put("receptionType", "");
					burMap.put("recNum", burCount);
					burMap.put("totalMerge", "false");
					burMap.put("companyMerge", "false");
					burMap.put("burMerge", "true");
					burMap.put("stationMerge", "false");
					burMap.put("roomMerge", "false");
					burMap.put("recNameMerge", "false");
					tableDatas.add(burMap);
					// 找到全部权限的车站
					List<Map<String, String>> stations = receptionDayReportMapper.findStations(filterParams);
					for (Map<String, String> station : stations) {
						filterParams.put("station_telecode", station.get("station_telecode"));
						String stationCount = receptionDayReportMapper.stationCount(filterParams);
						if (stationCount.equals("0")) {
							continue;
						}
						Map<String, String> stationMap = new LinkedHashMap<String, String>();
						stationMap.put("companyName", company.get("company_name"));
						stationMap.put("burName", bur.get("bureau_name"));
						stationMap.put("stationName", station.get("station_name") + "合计");
						stationMap.put("roomName", "");
						stationMap.put("recistName", "");
						stationMap.put("receptionType", "");
						stationMap.put("recNum", stationCount);
						stationMap.put("totalMerge", "false");
						stationMap.put("companyMerge", "false");
						stationMap.put("burMerge", "false");
						stationMap.put("stationMerge", "true");
						stationMap.put("roomMerge", "false");
						stationMap.put("recNameMerge", "false");
						tableDatas.add(stationMap);
						// 找到全部权限的站厅
						List<Map<String, String>> rooms = receptionDayReportMapper.findRooms(filterParams);
						for (Map<String, String> room : rooms) {
							filterParams.put("merchant_room_id", room.get("merchant_room_id"));
							String roomCount = receptionDayReportMapper.roomCount(filterParams);
							if (roomCount.equals("0")) {
								continue;
							}
							Map<String, String> roomMap = new LinkedHashMap<String, String>();
							roomMap.put("companyName", company.get("company_name"));
							roomMap.put("burName", bur.get("bureau_name"));
							roomMap.put("stationName", station.get("station_name"));
							roomMap.put("roomName", room.get("room_name") + "小计");
							roomMap.put("recistName", "");
							roomMap.put("receptionType", "");
							roomMap.put("recNum", roomCount);
							roomMap.put("totalMerge", "false");
							roomMap.put("companyMerge", "false");
							roomMap.put("burMerge", "false");
							roomMap.put("stationMerge", "false");
							roomMap.put("roomMerge", "true");
							roomMap.put("recNameMerge", "false");
							tableDatas.add(roomMap);
							// 找到站厅下的全部礼宾员
							List<Map<String, String>> recNames = receptionDayReportMapper.findRecNames(filterParams);
							for (Map<String, String> recName : recNames) {
								filterParams.put("receptionist_id", recName.get("receptionist_id"));
								String recNameCount = receptionDayReportMapper.recNameCount(filterParams);
								if (recNameCount.equals("0")) {
									continue;
								}
								Map<String, String> recNameMap = new LinkedHashMap<String, String>();
								recNameMap.put("companyName", company.get("company_name"));
								recNameMap.put("burName", bur.get("bureau_name"));
								recNameMap.put("stationName", station.get("station_name"));
								recNameMap.put("roomName", room.get("room_name"));
								recNameMap.put("recistName", recName.get("receptionist_name") + "小计");
								recNameMap.put("receptionType", "");
								recNameMap.put("recNum", recNameCount);
								recNameMap.put("totalMerge", "false");
								recNameMap.put("companyMerge", "false");
								recNameMap.put("burMerge", "false");
								recNameMap.put("stationMerge", "false");
								recNameMap.put("roomMerge", "false");
								recNameMap.put("recNameMerge", "true");
								tableDatas.add(recNameMap);
								// 礼宾员接待量
								List<Map<String, String>> recInfos = receptionDayReportMapper.findRecInfos(filterParams);
								for (Map<String, String> recInfo : recInfos) {
									filterParams.put("receptionType", recInfo.get("receptionType"));
									Map<String, String> recInfoMap = new LinkedHashMap<String, String>();
									recInfoMap.put("companyName", company.get("company_name"));
									recInfoMap.put("burName", bur.get("bureau_name"));
									recInfoMap.put("stationName", station.get("station_name"));
									recInfoMap.put("roomName", room.get("room_name"));
									recInfoMap.put("recistName", recName.get("receptionist_name"));
									recInfoMap.put("receptionType", recInfo.get("receptiontype"));
									recInfoMap.put("recNum", recInfo.get("recnum"));
									recInfoMap.put("totalMerge", "false");
									recInfoMap.put("companyMerge", "false");
									recInfoMap.put("burMerge", "false");
									recInfoMap.put("stationMerge", "false");
									recInfoMap.put("roomMerge", "false");
									recInfoMap.put("recNameMerge", "false");
									tableDatas.add(recInfoMap);
								}
							}
						}
					}
				}
			}
			Collections.reverse(tableDatas);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
			// return ResultData.error("礼宾员接待日统计表");
		}
		return true;
		// return ResultData.success(tableDatas);
	}
}