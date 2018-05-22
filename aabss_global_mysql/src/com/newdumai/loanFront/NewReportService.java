package com.newdumai.loanFront;

import java.util.Map;

public interface NewReportService {
	
	public Map<String,Object> genReport(Map<String,Object> param);

	public Map<String, Object> getScoreResult(String orderCode, String type);
}
