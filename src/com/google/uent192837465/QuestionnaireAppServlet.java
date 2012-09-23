package com.google.uent192837465;

import java.io.IOException;
import javax.servlet.http.*;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;


public class QuestionnaireAppServlet extends HttpServlet {
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		Velocity.init();
		
		VelocityContext vContext = new VelocityContext();

		resp.setContentType("text/html");
        resp.setCharacterEncoding("utf-8");
        
		Template template = Velocity.getTemplate("WEB-INF/accountname.vm", "Shift-JIS");
		
		template.merge(vContext, resp.getWriter());
		
	}
}
