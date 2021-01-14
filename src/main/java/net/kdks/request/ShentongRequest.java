package net.kdks.request;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSON;

import cn.hutool.http.Header;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;
import net.kdks.config.ShentongConfig;
import net.kdks.constant.ShentongMethod;
import net.kdks.constant.YuantongMethod;
import net.kdks.utils.DateUtils;
import net.kdks.utils.DigestUtils;
import net.kdks.utils.StringUtils;

/**
 * <p>申通请求封装</p>
 * <p>date: 2021-01-13 17:23:55</p>
 * 
 * @author Ze.Wang
 * @since 0.0.1
 */
public class ShentongRequest {
	
	private String secretKey;
	private String appkey;
	private String code;
	
	private String requestUrl;
	
	public ShentongRequest(ShentongConfig shentongConfig) {
		this.secretKey = shentongConfig.getSecretKey();
		this.appkey = shentongConfig.getAppkey();
		this.code = shentongConfig.getAppkey();
		this.requestUrl = ShentongMethod.URL;
    	if(shentongConfig.getIsProduct() == 0) {
    		this.requestUrl = ShentongMethod.URL_TEST;
		}
	}
	/**
	 * 路由查询
	 * @param param
	 * @param format
	 * @return
	 */
	public String queryRouteRequest(String param,String format) {
		return request(ShentongMethod.QUERY_ROUTE_API_NAME,
				ShentongMethod.QUERY_ROUTE_TO_APPKEY,
				ShentongMethod.QUERY_ROUTE_TO_CODE,
				param,
				format);
	}
	/**
	 * 运费预估
	 * @param param
	 * @param format
	 * @return
	 */
	public String queryPriceRequest(String param,String format) {
		return request(ShentongMethod.QUERY_PRICE_API_NAME,
				ShentongMethod.QUERY_PRICE_TO_APPKEY,
				ShentongMethod.QUERY_PRICE_TO_CODE,
				param,
				format);
	}
	
	/**
	 * 圆通通用请求
	 * @param method
	 * @param param
	 * @param format
	 * @return
	 */
	public String request(String method, String toAppkey, String toCode, String param,String format) {
		Map<String, Object> paramMap = new HashMap<>(7);
		paramMap.put("content", param);
		paramMap.put("api_name", method);
		paramMap.put("from_appkey", appkey);
		paramMap.put("from_code", code);
		paramMap.put("to_appkey", toAppkey);
		paramMap.put("to_code", toCode);
		String beforeDigestStr = param + secretKey;
		String dataDigest = Base64.getEncoder().encodeToString(DigestUtils.md5Digest(beforeDigestStr));
		paramMap.put("data_digest", dataDigest);

		String responseData = HttpUtil.post(requestUrl, paramMap);
		return responseData;
	}
}