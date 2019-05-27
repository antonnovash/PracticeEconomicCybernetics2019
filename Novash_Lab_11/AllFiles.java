package com.UP_11;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AllFiles extends HttpServlet {
	private static final long serialVersionUID = 1L;

    public AllFiles() {
    }
    
    private static String getData(File dir, String result) {
    	if(dir.isDirectory()) {
	    	File[] files = dir.listFiles();
	    	String tmp = "";
	    	for(int i = 0; i < files.length; i++) {
	    		if(files[i].isDirectory()) {
	    			result = result + "<ul>" + "<li>" + files[i].getName() + ":</li>"+ getData(files[i], result);    			
	    		} else {
	    			tmp = tmp + "<li>" + files[i].getName() + "</li>";
	    			result = tmp;
	    		}    		
	    	} 
    	}    	
    	return result;
    }

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException { // ??????? искать в data разделы, дальше файлы
		try {
			response.setContentType("text/html");
			String resp = "<html><head><title>All Files</title></head><body><hr>";
			// изменить путь к файлам, где хранятся ресурсы
			File dir = new File("E:\\Eclipse\\UP_11\\src\\data");
			resp = resp + getData(dir, "") + "</ul><hr></body></html>";
			PrintWriter out = response.getWriter();
			out.write(resp);
			out.close();
		} catch(Exception e) {
			e.printStackTrace();
		}		
	}
}
