package com.newdumai.dumai_data.dm.asset;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

public interface DmAssetAcquisitionService {
	public List<Map<String,Object>> listDumaiSources(String[] excludeCodes);
	public List<Map<String,Object>> listDm3rdInterfaces(String[] excludeCodes);
	public InputStream excute(InputStream in,String options) throws IOException;
}
