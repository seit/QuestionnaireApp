package com.google.uent192837465;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.FilterOperator;

public class InsAccountName extends HttpServlet {
	
	DatastoreService dss = DatastoreServiceFactory.getDatastoreService();
	
	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		
//		Key firstKey = KeyFactory.createKey("Questions","1");
//		Entity firstEntity = new Entity(firstKey);
//		firstEntity.setProperty("name", "seit");
//		dss.put(firstEntity);
		
		String name = req.getParameter("account");
		
		if(name == ""){			
			// 未入力の場合
			resp.sendRedirect("/questionnaireapp");
		}
		
		// キーを取得して最新のキーを生成
		Query query = new Query("Questions");
		query.setKeysOnly();
		PreparedQuery pq = dss.prepare(query);
		List keyList = new ArrayList();
		
		String lastKey = "";
		String lastId = "";
		Integer maxId = 0;
		Integer intId = 0;
		// idを作成する
		for(Entity entity : pq.asIterable()){
            // 取り出したエンティティーからキーを取得する
			lastKey = entity.getKey().toString();
			lastId = lastKey.substring(lastKey.indexOf("(")+2,(lastKey.indexOf(")")-1));
			intId = Integer.parseInt(lastId);			
			if(maxId <= intId){
				// 最大値を探して取得
				maxId = intId;
			}
        }
		// 最大値に1足して今回の登録idとする
		Integer nextId = maxId + 1;
		
		Key key = KeyFactory.createKey("Questions",nextId.toString());
		Entity entity = null;
		
		// 入力されたidが登録済でないか確認
		Query asrtQuery = new Query("Questions");
		asrtQuery.addFilter("name", FilterOperator.EQUAL, name);
		PreparedQuery pQuery = dss.prepare(asrtQuery);
		ArrayList<Entity> querylist = new ArrayList<Entity>();
        for(Entity asrtEntity: pQuery.asIterable()){
            querylist.add(asrtEntity);
        }        
        if(querylist.size() > 0){
        	// １件以上登録されていた場合
        	entity = querylist.get(0);
        	
        	// idを取得したEntityのものに更新
			lastKey = entity.getKey().toString();
			lastId = lastKey.substring(lastKey.indexOf("(")+2,(lastKey.indexOf(")")-1));
			intId = Integer.parseInt(lastId);			
			nextId = intId;
			
			// ２個以上見つかった場合はそれらをデータストアから削除する
			for(int i=1;i<querylist.size();i++){
				Entity delEntity = querylist.get(i);
				dss.delete(delEntity.getKey());
			}
        }else{
        	// 新規の場合
        	entity = new Entity(key);        	
        }
        	        
		entity.setProperty("name", name);		
		dss.put(entity);
		
		HttpSession session = req.getSession();
		session.setAttribute("id", nextId);
		
		resp.setContentType("text/plain");
		resp.getWriter().println(nextId.toString());
		
		resp.sendRedirect("/secondqestion");
	}
}
