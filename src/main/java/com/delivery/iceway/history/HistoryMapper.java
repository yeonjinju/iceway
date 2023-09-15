package com.delivery.iceway.history;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;

import com.delivery.iceway.domain.History;

@Mapper
public interface HistoryMapper {
	List<History> getHistory(History dto);
	int getCount(History dto);
}