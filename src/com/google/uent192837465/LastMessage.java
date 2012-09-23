package com.google.uent192837465;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

public class LastMessage extends HttpServlet {
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		Velocity.init();
		
		VelocityContext vContext = new VelocityContext();
		
		Template template = Velocity.getTemplate("WEB-INF/lastmsg.vm");
		
		template.merge(vContext, resp.getWriter());
		
		HttpSession session = req.getSession();
		session.invalidate();
	}
}
