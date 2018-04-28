package fileupload.homecontroller.controller;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import javax.servlet.ServletContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.FileCopyUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import fileupload.connectionConfig.DatabaseConnection;
import fileupload.datamanager.FileUploadDataManager;
import fileupload.filemodel.FileModel;

@Controller
public class HomeController {
	@Autowired
	   ServletContext context;
	// http://localhost:9970/fileUploadPage
	
	@RequestMapping(value="/welcomePage.htm", method = RequestMethod.GET)
	public ModelAndView homePage() {
		System.out.println("inside controller");
		ModelAndView model  = new ModelAndView("WelcomePage");
		DatabaseConnection dbConn  = new DatabaseConnection();
		Connection conn ;
		ResultSet rs =null;
		PreparedStatement pt = null;
		int count = 0;
		try{
			conn  =(Connection) dbConn.getConnection();
			conn.setAutoCommit(false);
			String query = "select count(*) CNT from FILE_MASTER";
			pt = conn.prepareStatement(query);
			rs = pt.executeQuery();
			while(rs.next()){
			count = rs.getInt("CNT");
			}
			System.out.println("COUNT IS >>>>>>>>>>>>>>>>>>>>>>>>>>>" +  count);
			conn.commit();
		}catch(Exception ex){
			ex.printStackTrace();
		}finally{
			
		}
		return model;
	}
	@RequestMapping(value="/uploadFile.htm",method=RequestMethod.POST)
	public ModelAndView fileUpload(@RequestParam("file") MultipartFile file){
		ModelAndView model = new ModelAndView("UploadSuccess");
		System.out.println("file successfully uploaded.");
		return model;
	}
	
	 @RequestMapping(value = "/fileUploadPage", method = RequestMethod.GET)
	   public ModelAndView fileUploadPage() {
	      FileModel file = new FileModel();
	      ModelAndView modelAndView = new ModelAndView("fileUpload", "command", file);
	      return modelAndView;
	   }
	 
	@RequestMapping(value="/fileUploadPage", method = RequestMethod.POST)
	   public String fileUpload(@Validated FileModel file, BindingResult result, ModelMap model) throws IOException {
	      if (result.hasErrors()) {
	         System.out.println("validation errors");
	         return "fileUploadPage";
	      } else {            
	         System.out.println("Fetching file");
	         MultipartFile multipartFile = file.getFile();
	         String uploadPath = context.getRealPath("") + File.separator + "temp" + File.separator;
	         System.out.println("file path______________"+ uploadPath);
	         //Now do something with file...
	         boolean check = new File(uploadPath+file.getFile().getOriginalFilename()).exists();
	         FileCopyUtils.copy(file.getFile().getBytes(), new File(uploadPath+file.getFile().getOriginalFilename()));
	         String fileName = multipartFile.getOriginalFilename();
	         System.out.print("FileName is___________________" + fileName);
	         
	         System.out.println("file exist in directory? " + check);
	         HashMap param = new HashMap();
	         param.put("FILE_NAME",fileName );
	         param.put("FILE_PATH",uploadPath );
	         FileUploadDataManager datamanager = new FileUploadDataManager();
	         if(check == false){
	        	 try {
					datamanager.insertRecordIntoTable(param);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	         }else if(check == true){
	        	 try {
						datamanager.updateRecordToTable(param);;
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} 
	         }

	         
	         boolean isFileExist = datamanager.isFileExist(param);
	         System.out.println(isFileExist);
	         
	         model.addAttribute("fileName", fileName);
	         return "success";
	      }
	   }
	
}
