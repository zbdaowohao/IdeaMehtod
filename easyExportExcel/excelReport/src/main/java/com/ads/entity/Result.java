package com.ads.entity;

import java.io.Serializable;

/**
 * @ClassName Result
 * @Description 前后端交互数据标准
 * @author 守彬
 * @Date 2018年9月21日 下午4:31:18
 * @version 1.0.0
 * @param <T>
 */
public class Result<T> implements Serializable{

    private static final long serialVersionUID = 1L;

    /**
     * 成功标志
     */
    private boolean success;

    /**
     * 失败消息
     */
    private String message;

    /**
     * 返回代码
     */
    private Integer code;

    /**
     * 时间戳
     */
    private long timestamp = System.currentTimeMillis();

    /**
     * 结果对象
     */
    private T result;

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	public T getResult() {
		return result;
	}

	public void setResult(T result) {
		this.result = result;
	}
}
