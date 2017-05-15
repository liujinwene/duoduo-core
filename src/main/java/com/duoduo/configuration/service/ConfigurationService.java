package com.duoduo.configuration.service;

public interface ConfigurationService {
	
	String getValue(String name);

	void update(String name, String value);

}
