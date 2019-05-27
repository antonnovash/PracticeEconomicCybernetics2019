package com.UP_11;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.Scanner;
import java.util.StringTokenizer;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class GetFiles extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public GetFiles() {
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}
	
	private File getFile(String genre, String name, File dir) {
		File result = null;
		if(dir.isDirectory()) {
			File[] files = dir.listFiles();
	    	for(int i = 0; i < files.length; i++) {
	    		if(files[i].isDirectory() && genre.equals(files[i].getName())) {
	    			result = getFile(genre, name, files[i]);
	    		} else {
	    			StringTokenizer st = new StringTokenizer(files[i].getName());
		            if(name.equals(st.nextToken("."))) {
		            	result = files[i];
		            	break;
		            }	            	
	    		}    		
	    	} 
		} else {
			StringTokenizer st = new StringTokenizer(dir.getName());
            if(name.equals(st.nextToken(".")))
            	result = dir;	 
		}
    	return result;
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String resp = "<html><head><title>Your File</title></head><body><hr>";
		// изменить путь к файлам, где хранятся ресурсы
		File dir = new File("E:\\Eclipse\\UP_11\\src\\data");
		PrintWriter out = response.getWriter();		
		Enumeration<String> names = request.getParameterNames();
		String genre = request.getParameterValues((String)names.nextElement())[0];
		System.out.println(genre);
		String name = request.getParameterValues((String)names.nextElement())[0];
		System.out.println(name);
		File file = getFile(genre, name, dir);
		if(file != null) {
			StringTokenizer st = new StringTokenizer(file.getName());
            resp = resp + "<p>" + st.nextToken(".") + "</p><div>";
			Scanner in = new Scanner(file);
			while(in.hasNextLine()) {
				resp = resp + in.nextLine() + "<br>";
			}
			resp = resp + "</div></body></html>";
		} else {
			resp = resp + "<p>This file is not found</p></body></html>";
		}
		out.write(resp);
		out.close();
	}

}
