package com.delivery.iceway.history;

import java.util.List;

import com.delivery.iceway.domain.History;

public interface HistoryService {
	List<History> getHistory(int pageNum , String keyword , String condition , History dto);
	int[] getPagination(int pageNum , History dto);
	String[] getKeyword(History dto , String keyword , String condition);
	String[] recallArray(History dto);
}