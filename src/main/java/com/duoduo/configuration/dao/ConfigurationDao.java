package com.duoduo.configuration.dao;

import com.duoduo.schema.tables.records.ConfigurationRecord;

public interface ConfigurationDao {
	ConfigurationRecord findByName(String name);

	void update(ConfigurationRecord record);

}
