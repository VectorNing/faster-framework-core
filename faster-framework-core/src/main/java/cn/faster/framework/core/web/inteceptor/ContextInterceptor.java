package cn.faster.framework.core.web.inteceptor;

import cn.faster.framework.core.utils.NetworkUtil;
import cn.faster.framework.core.web.context.RequestContext;
import cn.faster.framework.core.web.context.WebContextFacade;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by zhangbowen on 2018/5/14.
 */
public class ContextInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response, Object handler) {
        setRequestContext(request);
        return true;
    }

    private void setRequestContext(HttpServletRequest request) {
        RequestContext requestContext = WebContextFacade.getRequestContext();
        requestContext.setIp(NetworkUtil.getIp(request));
        requestContext.setUri(request.getRequestURI());
        WebContextFacade.setRequestContext(requestContext);
    }

    @Override
    public void afterCompletion(HttpServletRequest request,
                                HttpServletResponse response, Object handler, Exception ex) {
        WebContextFacade.removeRequestContext();
    }
}