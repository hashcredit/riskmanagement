package com.newdumai.util;

import com.newdumai.dumai_data.dm_3rd_interface.util.CommonUtil;

/**
 * Created by zhang on 2017/10/17.
 */
public class yyyy {

    public static void main(String[] args) {
        boolean success = CommonUtil.checkBaseCondition("{“idNo”:\"65900119970xxxxxxx\",\"name\":\"xx\",“custName\":\"aqxx\",\"mobile\":\"18299078xxx\",\"linkedMobile1\":\"13999737xxx\",\"sign\":\"xxxxxxxxxxxx\"}", "1==1");

        System.out.println(success);
    }
}
