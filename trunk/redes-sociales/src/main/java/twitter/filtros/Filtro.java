package twitter.filtros;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import twitter.beans.UsuarioBean;

/**
 * @author Rafael de los Santos Guirado
 * 
 */
public class Filtro implements Filter {

	FilterConfig fc;

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		fc = filterConfig;
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		
		UsuarioBean userManager = (UsuarioBean) req.getSession().getAttribute("usuario");

		try {
			System.out.println(userManager.isLogin());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(userManager != null && userManager.isLogin()){
			System.out.println("todo correcto");
			chain.doFilter(request, response);
		}else{
			System.out.println("llega");
			HttpServletResponse resp = (HttpServletResponse) response;
			resp.sendRedirect(req.getContextPath() + "/index.jsf");
		}
		
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}

}
