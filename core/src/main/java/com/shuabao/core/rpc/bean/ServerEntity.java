package com.shuabao.core.rpc.bean;


import org.apache.commons.lang3.math.NumberUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ServerEntity implements Comparable<ServerEntity>{
	private String path;
	private String name;
	private String protocol;//协议
	private String ip;
	private int port;
	private int sort;
	private String serverId;
	
	public ServerEntity(String path, String url){
		this.path = path;
		analyzeUrl(url);
	}
	
	public void updateUrl(String url){
		analyzeUrl(url);
	}

	private static final Pattern r = Pattern.compile("(\\S+)://(\\S+):(\\d+)\\?(\\S+)");

	private final void analyzeUrl(String url){
		Matcher m = r.matcher(url);
		if (m.find()) {
			this.protocol = m.group(1);
			this.ip = m.group(2);
			this.port = NumberUtils.toInt(m.group(3));
			Map<String,String> para = getParam(m.group(4));
			this.name = para.get("name");
			this.sort = NumberUtils.toInt(para.get("sort"));
			this.serverId = ip+":"+port;
		}
	}
	
	private final Map<String,String> getParam(String param){
		Map<String,String> ret = new HashMap<String, String>();
		for(String arr : param.split("&")){
			String[] pair = arr.split("=");
			ret.put(pair[0].toLowerCase().trim(), pair[1].toLowerCase().trim());
		}
		return ret;
	}
	
	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getProtocol() {
		return protocol;
	}
	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}
	public int getSort() {
		return sort;
	}
	public void setSort(int sort) {
		this.sort = sort;
	}
	public String getServerId(){
		return this.serverId;
	}

	
	
	@Override
	public String toString() {
	    return protocol+"://"+ip+":"+port+"?name="+name+"&sort="+sort;
	}
	
	
/*	public static void main(String[] args) {
		ServerEntity s = new ServerEntity("","dubbo://127.0.0.1:8080?name=room1&sort=0");
		System.out.println(s);
    }*/

    @Override
    public int compareTo(ServerEntity o) {
        return this.name.compareTo(o.getName());
    }
}
