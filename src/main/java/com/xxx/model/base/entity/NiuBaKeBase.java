package com.xxx.model.base.entity;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class NiuBaKeBase {
	/**
	 * 牛八客企业客户端编号
	 * */
	private String duoduo_client_id = "9442c2e645a644bbb999c4323c52b1fc";

	/**
	 * 牛八客企业客户端秘钥
	 * */
	private String duoduo_client_secret = "80526971f075cd7ddba00f6da76ac41a60448029";

	/**
	 * 牛八客助手企业客户端ID
	 * */
	private String assistant_client_id = "9a44de4b17074534a10698de33767dd7";

	public String getDuoduo_client_id() {
		return duoduo_client_id;
	}

	public void setDuoduo_client_id(String duoduo_client_id) {
		this.duoduo_client_id = duoduo_client_id;
	}

	public String getDuoduo_client_secret() {
		return duoduo_client_secret;
	}

	public void setDuoduo_client_secret(String duoduo_client_secret) {
		this.duoduo_client_secret = duoduo_client_secret;
	}

	public String getAssistant_client_id() {
		return assistant_client_id;
	}

	public void setAssistant_client_id(String assistant_client_id) {
		this.assistant_client_id = assistant_client_id;
	}

	public String getAssistant_client_secret() {
		return assistant_client_secret;
	}

	public void setAssistant_client_secret(String assistant_client_secret) {
		this.assistant_client_secret = assistant_client_secret;
	}

	/***
	 * 牛八客助手企业客户端秘钥
	 * */
	private String assistant_client_secret = "e5e53af67f8d7b341aa94002c22c4393834dd4df ";

	/**
	 * 内部发送post请求
	 * URL 请求的URL
	 * sbParams 参数字符串
	 * */
	public String innerSendPostRequest(URL url,StringBuilder sbParams) {
		HttpURLConnection con = null;
		PrintWriter out = null;
		BufferedReader in = null;
		StringBuffer resultBuffer = null;
		// 发送请求
		try {
			con = (HttpURLConnection) url.openConnection();
			// con.setRequestMethod("POST");
			con.setDoOutput(true);
			con.setDoInput(true);
			con.setUseCaches(false);
			// con.setRequestProperty("Content-Type", "application/json");
			con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

			if (sbParams != null && sbParams.length() > 0) {
				out = new PrintWriter(con.getOutputStream());
				out.write(sbParams.toString());
				out.flush();
			}
			resultBuffer = new StringBuffer();
			in = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
			String temp = null;
			while ((temp = in.readLine()) != null) {
				System.out.println(temp);
				resultBuffer.append(temp);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			try {
				if (out != null) {
					out.close();
				}
				if (in != null) {
					in.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			} finally {
				if (con != null) {
					con.disconnect();
				}
			}
		}
		return resultBuffer.toString();
	}

}
