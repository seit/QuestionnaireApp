package com.google.uent192837465;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;

public class InsSecondQuestion extends HttpServlet {
	
	DatastoreService dss = DatastoreServiceFactory.getDatastoreService();
	
	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		
		HttpSession session = req.getSession();
		Integer id = (Integer) session.getAttribute("id");
		
		String number = req.getParameter("number");
		
		Key key = KeyFactory.createKey("Questions",id.toString());
		Entity updateEntity = null;
		try {
			updateEntity = dss.get(key);
		} catch (EntityNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String strNoNm = "";
		if(updateEntity == null){
//			 updateEntity.getProperty("name").toString()
			strNoNm = "NoName";	
			updateEntity.setProperty("name", strNoNm);
		}
		
		updateEntity.setProperty("secondquestion", number);
		dss.put(updateEntity);
		
		resp.setContentType("text/plain");
		resp.getWriter().println(id.toString());
		
		resp.sendRedirect("/thirdquestion");
	}
}
