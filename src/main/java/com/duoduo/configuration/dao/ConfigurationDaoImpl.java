package com.duoduo.configuration.dao;

import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.duoduo.schema.Tables;
import com.duoduo.schema.tables.records.ConfigurationRecord;

@Component
public class ConfigurationDaoImpl implements ConfigurationDao {

	@Autowired
	private DSLContext dsl;
	
	@Override
	public ConfigurationRecord findByName(String name) {
		return dsl.selectFrom(Tables.CONFIGURATION).where(Tables.CONFIGURATION.NAME.eq(name)).fetchOne();
	}

	@Override
	public void update(ConfigurationRecord record) {
		dsl.executeUpdate(record);
	}

}
