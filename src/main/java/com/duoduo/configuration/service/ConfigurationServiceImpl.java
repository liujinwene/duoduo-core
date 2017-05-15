package com.duoduo.configuration.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.duoduo.configuration.dao.ConfigurationDao;
import com.duoduo.schema.tables.records.ConfigurationRecord;

@Component
public class ConfigurationServiceImpl implements ConfigurationService {
	private static Logger LOGGER = LoggerFactory.getLogger(ConfigurationServiceImpl.class);
	
	@Autowired
	private ConfigurationDao configurationDao;

	@Override
	public String getValue(String name) {
		ConfigurationRecord config = configurationDao.findByName(name);
		if(config == null) {
			LOGGER.info("cofiguration not found.name=" + name);
			throw new RuntimeException("cofiguration not found");
		}
		return config.getValue();
	}

	@Override
	public void update(String expiredTime, String value) {
		ConfigurationRecord config = configurationDao.findByName(expiredTime);
		if(config != null) {
			if(!config.getValue().equals(value)) {
				config.setValue(value);
				configurationDao.update(config);
			}
		}
	}
	
	

}
