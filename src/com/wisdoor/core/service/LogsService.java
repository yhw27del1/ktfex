package com.wisdoor.core.service;

import com.wisdoor.core.model.Logs;
import com.wisdoor.core.model.User;

public interface LogsService extends BaseService<Logs>{
/**
 * 记录日志
 * @param operate
 * @return
 * @throws Exception
 */
	public Logs log(String operate) throws Exception;
	public Logs log(String operate,User user) throws Exception;
}
