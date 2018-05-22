package com.newdumai.assetmgr;

import java.util.Map;

public interface AssetService {
	public String list(Map<String, Object> map);

	public int getTotal();

	String getHeadtype();
}
