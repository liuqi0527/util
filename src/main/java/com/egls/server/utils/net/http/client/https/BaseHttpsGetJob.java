package com.egls.server.utils.net.http.client.https;

import com.egls.server.utils.net.http.HttpUtil;
import com.egls.server.utils.net.http.SimpleHttpResponse;
import com.egls.server.utils.net.http.client.BaseHttpJob;

/**
 * @author mayer - [Created on 2018-09-13 18:29]
 */
public abstract class BaseHttpsGetJob extends BaseHttpJob {

    @Override
    protected void request(String url) throws Exception {
            handleResponse(HttpUtil.doHttpsGet(url, getRequestHeaders()));
    }

    /**
     * 处理http的返回.
     * Note: 判断状态是否是200
     *
     * @param httpResponse 返回的结果
     */
    protected abstract void handleResponse(final SimpleHttpResponse httpResponse);

}