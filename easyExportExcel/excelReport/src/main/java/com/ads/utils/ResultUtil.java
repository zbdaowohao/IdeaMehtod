package com.ads.utils;

import com.ads.entity.Result;

/**
 * @ClassName ResultUtil
 * @Description 返回工具
 * @author 守彬
 * @Date 2018年9月21日 下午4:32:13
 * @version 1.0.0
 * @param <T>
 */
public class ResultUtil<T> {

	private Result<T> result;

	public ResultUtil() {
		result = new Result<>();
		result.setSuccess(true);
		result.setMessage("success");
		result.setCode(200);
	}

	public Result<T> setData(T t) {
		this.result.setResult(t);
		this.result.setCode(200);
		return this.result;
	}

	public Result<T> setSuccessMsg(String msg) {
		this.result.setSuccess(true);
		this.result.setMessage(msg);
		this.result.setCode(200);
		this.result.setResult(null);
		return this.result;
	}

	public Result<T> setData(T t, String msg) {
		this.result.setResult(t);
		this.result.setCode(200);
		this.result.setMessage(msg);
		return this.result;
	}

	public Result<T> setErrorMsg(String msg) {
		this.result.setSuccess(false);
		this.result.setMessage(msg);
		this.result.setCode(500);
		return this.result;
	}

	public Result<T> setErrorMsg(Integer code, String msg) {
		this.result.setSuccess(false);
		this.result.setMessage(msg);
		this.result.setCode(code);
		return this.result;
	}
}
