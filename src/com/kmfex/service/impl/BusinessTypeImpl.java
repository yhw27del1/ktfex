package com.kmfex.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kmfex.model.BusinessType;
import com.kmfex.service.BusinessTypeService;
import com.wisdoor.core.service.impl.BaseServiceImpl;

@Service
@Transactional
public class BusinessTypeImpl extends BaseServiceImpl<BusinessType> implements BusinessTypeService {

}
