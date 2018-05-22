package com.newdumai.dumai_data.dm_out;

import java.util.Map;
import java.util.Set;

public interface Dm_outService {

	Map<String, Object> getDmSources();

	Map<String, Object> getOutParasByDmSourceCode(Map<String, Object> params);

	Map<String, Object> getAllDmSourceOutParas();

	Map<String, Object> getAuditOrderResult(Map<String, Object> params);

	Map<String, Object> getAuditOrderResult(Map<String, Object> orderMap, Set<String> dm_source_codeSet);
}
