package com.zing.springboot.demo004_gateway;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.util.Enumeration;
import java.util.Map;
import java.util.Set;

/**
 * create at 2022-01-08 18:04
 *
 * @author Zing
 */
@RestController
public class GatewayController {
    @Value("${demo003.remotePathHost}")
    private String remotePathHost;

    @RequestMapping("/t/**")
    public Object requestTransmit(HttpServletRequest request, HttpMethod method, @RequestBody(required = false) String body) {
        try {
            // 根据request，构造HttpHeaders
            HttpHeaders headers = new HttpHeaders();
            Enumeration<String> headerNames = request.getHeaderNames();
            while (headerNames.hasMoreElements()) {
                String name = headerNames.nextElement();
                String value = request.getHeader(name);
                headers.add(name, value);
            }

            // 复制 request 的参数
            Map<String, String[]> parameterMap = request.getParameterMap();
            MultiValueMap<String, Object> params = new LinkedMultiValueMap<>();
            // 附加参数值
            Set<String> keySet = parameterMap.keySet();
            for (String key : keySet) {
                String[] value = parameterMap.get(key);
                params.add(key, value[0]);
            }
            // 根据body内容填充requestEntity。对于form-data，body为空但parameterMap有值；对于raw，body不为空。
            HttpEntity<Object> requestEntity = (body != null && !body.isEmpty()) ? new HttpEntity<>(body, headers) : new HttpEntity<>(params, headers);
            String remotePath = request.getServletPath().replaceFirst("/t", "");
            String sUrl = remotePathHost + remotePath;

            // 构造URI。必须拼接出String url然后创建URI，否则会出现queryString %符号转%25的问题
            if (request.getQueryString() != null && !request.getQueryString().isEmpty())
                sUrl += "?" + request.getQueryString();
            URI destUri = new URI(sUrl);
            // 向服务请求
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<Object> responseEntity = restTemplate.exchange(destUri, method, requestEntity, Object.class);
            if (responseEntity.getStatusCode().equals(HttpStatus.OK)) {
                return responseEntity.getBody();
            } else {
                return "返回错误1";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "返回错误2";
        }

    }
}
