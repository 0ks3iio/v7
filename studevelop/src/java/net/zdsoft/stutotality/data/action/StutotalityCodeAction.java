package net.zdsoft.stutotality.data.action;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.lowagie.text.Image;
import com.lowagie.text.*;
import com.lowagie.text.rtf.RtfWriter2;
import net.zdsoft.basedata.constant.BaseConstants;
import net.zdsoft.basedata.entity.Course;
import net.zdsoft.basedata.entity.Semester;
import net.zdsoft.basedata.entity.StorageDir;
import net.zdsoft.basedata.remote.service.CourseRemoteService;
import net.zdsoft.basedata.remote.service.GradeRemoteService;
import net.zdsoft.basedata.remote.service.SemesterRemoteService;
import net.zdsoft.basedata.remote.service.StorageDirRemoteService;
import net.zdsoft.framework.action.BaseAction;
import net.zdsoft.framework.annotation.ControllerInfo;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.*;
import net.zdsoft.stutotality.data.dto.CodeExportDto;
import net.zdsoft.stutotality.data.entity.StutotalityCode;
import net.zdsoft.stutotality.data.entity.StutotalityItem;
import net.zdsoft.stutotality.data.service.StutotalityCodeService;
import net.zdsoft.stutotality.data.service.StutotalityItemService;
import net.zdsoft.stutotality.data.util.LogoPngUtil;
import net.zdsoft.stutotality.data.util.StutotalityConstant;
import net.zdsoft.stutotality.remote.service.StutotalityRemoteService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.List;
import java.util.*;

@Controller
@RequestMapping("/stutotality")
public class StutotalityCodeAction extends BaseAction{
	@Autowired
	private StutotalityCodeService stutotalityCodeService;
	@Autowired
	private SemesterRemoteService semesterRemoteService;
	@Autowired
	private GradeRemoteService gradeRemoteService;
	@Autowired
	private StutotalityRemoteService stutotalityRemoteService;
	@Autowired
	private StutotalityItemService stutotalityItemService;
	@Autowired
	private StorageDirRemoteService storageDirRemoteService;
	@Autowired
	private CourseRemoteService courseRemoteService;


	@RequestMapping("/permission/index/page")
	@ControllerInfo(value = "人员权限index")
	public String permissionIndex(ModelMap map,HttpServletRequest request) {
		map.put("subsystem", StutotalityConstant.STUTOTALITY_SUBSYSTEM);
		return "/stutotality/permission/stutotalityPerIndex.ftl";
	}
	@RequestMapping("/code/getItemList")
	@ControllerInfo(value = "通过年纪获取项目")
	public @ResponseBody List<StutotalityItem> getItemList(ModelMap map,String gradeId) {
		String unitId=getLoginInfo().getUnitId();
		Semester se=SUtils.dc(semesterRemoteService.getCurrentSemester(1,unitId), Semester.class);
		return stutotalityItemService.getItemListByParams(unitId,se.getAcadyear(),se.getSemester().toString(),gradeId,1);
	}
	@RequestMapping("/code/index/page")
	@ControllerInfo(value = "二维码index")
	public String codeIndex(ModelMap map,HttpServletRequest request) {
		String unitId=getLoginInfo().getUnitId();
//		String gradeId=request.getParameter("gradeId");
		Semester se=SUtils.dc(semesterRemoteService.getCurrentSemester(1,unitId), Semester.class);
		List<StutotalityItem> itemList=stutotalityItemService.getItemListByParams(unitId,se.getAcadyear(),se.getSemester().toString(),null,1);
		if(CollectionUtils.isNotEmpty(itemList)){
			List<String> checkList=new ArrayList<>();
			List<StutotalityItem> lastList=new ArrayList<>();
			//获取缩写
			List<Course> courseList = SUtils.dt(courseRemoteService.findByUnitIdAndTypeAndLikeSection
					(unitId, BaseConstants.SUBJECT_TYPE_BX,BaseConstants.SECTION_PRIMARY.toString()), Course.class);
			Map<String,String> courseShortNameMap=EntityUtils.getMap(courseList,Course::getId,Course::getShortName);
			itemList.forEach(item->{
				if(item.getItemName().equals("音乐")){
					System.err.println("");
				}
				if(!checkList.contains(item.getItemName())){
					String shortName=courseShortNameMap.get(item.getSubjectId());
					if(StringUtils.isNotBlank(shortName) && shortName.length()==1){
						item.setShortName(shortName);
					}
					lastList.add(item);
					checkList.add(item.getItemName());
				}
			});
			map.put("itemList",lastList);
		}
//		stutotalityRemoteService.checkCode("6FEA83C09E7A4490BDCA1F80DEB8DC33","2c91808a6e8db0a2016e8dcd98f402fc");
//		stutotalityRemoteService.getStutotalityItemResult("402880956d1f89ac016d1f9341b00019","1");
		return "/stutotality/code/stutotalityCodeIndex.ftl";
	}

	@RequestMapping("/code/export")
	@ControllerInfo(value = "二维码export")
	public void exportCode(ModelMap map, CodeExportDto exportDto,HttpServletResponse response) throws IOException {
//		exportDto.setItemId("123");
		if(StringUtils.isBlank(exportDto.getItemId()) ||
				(exportDto.getNumber1()==null &exportDto.getNumber2()==null &
						exportDto.getNumber3()==null &exportDto.getNumber4()==null &
						exportDto.getNumber5()==null) ){
		}else{
			String unitId=getLoginInfo().getUnitId();
//			Grade grade=SUtils.dc(gradeRemoteService.findOneById(exportDto.getGradeId()),Grade.class);
//			String gradeName=grade==null?"":grade.getGradeName();
			StutotalityItem item=stutotalityItemService.findOne(exportDto.getItemId());
			String itemName=item==null?"":item.getItemName();
//			String pathName=exportDto.getName()+"二维码";
			List<String> nameList=new ArrayList<>();
			Map<String,String> nameMap=new HashMap<>();
			//组装出对应的名称
			getName(exportDto,nameList,nameMap);
			//获取dir
			List<StorageDir> dirs = SUtils.dt(storageDirRemoteService.findByTypeAndActiove(0, "1"), new TR<List<StorageDir>>(){});
			String dirStr="";
			if(!CollectionUtils.isEmpty(dirs)){
				dirStr= dirs.get(0).getDir();
			}
			ByteArrayOutputStream baos =null;
			BufferedImage image= null;
			try {
				//获取logo地址，当不存在时生成对应的logo(会有空图设置文字)
				Map<String,String> logoMap=getLogoMap(dirStr,unitId,nameList);
				List<Image> imageList=null;
				String pathId=UuidUtils.generateUuid();
				List<StutotalityCode> insertList=new ArrayList<>();
				for(Map.Entry<String,String> entry:nameMap.entrySet()){
					imageList=new ArrayList<>();
					StringBuilder targetImgUrl= null;
					StutotalityCode code=null;
					File logoFile=new File(logoMap.get(entry.getKey()));
					String[] ss=entry.getValue().split("_");
					int number=Integer.parseInt(ss[0]);
					for(int i=0;i<number;i++){
						code=new StutotalityCode();
						code.setId(UuidUtils.generateUuid());
						code.setUnitId(unitId);
						code.setType(1);
						code.setItemId(exportDto.getItemId());//由于不与年级有关联且有不是学科的项目，此时的itemId已无实际性作用。直接通过名称匹配
						code.setHasUsing(0);
						code.setName(entry.getKey());
						code.setItemName(itemName);
						code.setScore(Float.valueOf(ss[1]));
						code.setCreationTime(new Date());
						insertList.add(code);
						//获取二维码 带有logo
						image = getQRCode(code.getId(),110,115,logoFile);
						baos = new ByteArrayOutputStream();
						ImageIO.write(image,"png",baos);
						byte[] by = baos.toByteArray();

						imageList.add(Image.getInstance(by));
//					imageList.add(Image.getInstance(targetImgUrl.toString()));
					}
					createCodeDoc(imageList,dirStr,exportDto.getColumn(),entry.getKey(),pathId,response);
				}
				String zipName = ZipUtils.makeZipWithFile(dirStr+File.separator+"stutotality/zip"+File.separator+pathId+File.separator);
				File zip = new File(zipName);
				InputStream in = new FileInputStream(zip);
				ServletUtils.download(in, null, response, itemName+".zip");
				deleteAllFile(dirStr+File.separator+"stutotality/zip");
				//记录每个二维码对应 id的数据内容
				stutotalityCodeService.saveAll(insertList.toArray(new StutotalityCode[0]));
			} catch (Exception e) {
				e.printStackTrace();
			}finally {
				if(baos!=null){
					baos.flush();
					baos.close();
				}
				if(image!=null){
					image=null;
				}
			}
		}
	}
	public void createCodeDoc(List<Image> imageList,String dirStr,Integer column,String name, String pathName,HttpServletResponse response) throws IOException {
		String blankPath=getBlankDoc(dirStr,name,pathName);
		Document document =null;
		RtfWriter2 writer2=null;
		OutputStream stream=null;
		try {
//			File docFile=new File(UrlUtils.getPrefix(getRequest())+"/stutotality/code/blank.doc");
			File docFile=new File(blankPath);
			document = new Document();
			// 建立一个书写器(Writer)与document对象关联，通过书写器(Writer)可以将文档写入到磁盘中
			stream=new FileOutputStream(docFile);
			writer2=RtfWriter2.getInstance(document, stream);
			document.open();
			Paragraph title = new Paragraph("二维码信息");
			//设置标题格式对齐方式
			title.setAlignment(Element.ALIGN_CENTER);
			document.add(title);
			// 设置 Table 表格
			//设置表格，将图片加到表格中进行方便定位
			Table aTable = new Table(column);
			aTable.setBorderColor(new Color(255,255,255));
			// 设置每列所占比例
			// 占页面宽度 90%
			aTable.setWidth(100);
			// 自动填满
			aTable.setAutoFillEmptyCells(true);
			//这里是imagelist集合，就是图片字节流的集合，图片从流中去获取放到word中
			for (int i = 0; i < imageList.size(); i++) {
				//设置图片等比例缩小
				imageList.get(i).scalePercent(55);
				Cell cell=new Cell(imageList.get(i));
				cell.setBorderColor(new Color(255,255,255));
				aTable.addCell(cell);
//				document.add(imageList.get(i));
			}
			document.add(aTable);
			document.add(new Paragraph("\n"));
			writer2.close();
			writer2=null;
			document.close();
			//响应浏览器 返回下载
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(writer2!=null){
				writer2.close();
				writer2=null;
			}
			if(document!=null){
				document.close();
				document=null;
			}
			if(stream!=null){
				stream.close();
				stream=null;
			}
		}
	}
	public String  getBlankDoc(String dirStr,String name,String pathName) throws IOException {
		String docPath=dirStr+File.separator+"stutotality/zip/"+pathName+"/";
//		String docPath=dirStr+File.separator+"stutotality/zip/code/";
		File docPathFile=new File(docPath);
//		docPathFile.mkdirs();
		if(!docPathFile.exists()){
			docPathFile.mkdirs();
		}
		docPath+=name+".doc";
		Document document=new Document(PageSize.A4);
		RtfWriter2 writer2=null;
		OutputStream stream=null;
		try {
			stream=new FileOutputStream(docPath);
			writer2=RtfWriter2.getInstance(document,stream);
//            document.open();
			writer2.close();
			writer2=null;
//			document.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}finally {
			if(document!=null){
				document.close();
				document=null;
			}
			if(writer2!=null){
				writer2.close();
				writer2=null;
			}
			if(stream!=null){
				stream.close();
				stream=null;
			}
			docPathFile=null;
		}
		return docPath;
	}
	/**
	 * 获取logo地址，当不存在时生成对应的logo
	 * @param dirStr
	 * @param unitId
	 * @param nameList
	 * @return
	 */
	public Map<String,String> getLogoMap(String dirStr,String unitId,List<String> nameList){
		Map<String,String> logoMap=new HashMap<>();
		//获取logo，logo空时重新生成
		List<StutotalityCode> codeList=stutotalityCodeService.findListByUnitIdAndTypeAndNameIn(unitId,2,nameList.toArray(new String[0]));
		Map<String,StutotalityCode> codeMap=EntityUtils.getMap(codeList,StutotalityCode::getName);

		List<StutotalityCode> insertCodeList=new ArrayList<>();
		//logo地址
		StringBuilder targetImgUrl= null;
		StutotalityCode code=null;
		String path=UrlUtils.getPrefix(getRequest());
		try {
			for(String nameStr:nameList){
				targetImgUrl = new StringBuilder(dirStr);
				if(!codeMap.containsKey(nameStr)){
					code=new StutotalityCode();
					code.setId(UuidUtils.generateUuid());
					code.setUnitId(unitId);
					code.setName(nameStr);
					code.setType(2);
					code.setCreationTime(new Date());
					insertCodeList.add(code);
				}else{
					code=codeMap.get(nameStr);
				}
				targetImgUrl.append(File.separator+"stutotality"+File.separator);
				targetImgUrl.append("logo"+File.separator);
				targetImgUrl.append(unitId);
				File targetFile=new File(targetImgUrl.toString()+File.separator+code.getId()+".png");
				if(!targetFile.exists()){
					//为图片添加图片文字
					LogoPngUtil.markImgMark(dirStr+"/stutotality/blank",targetImgUrl.toString(),code.getId()+".png",nameStr);
				}
				logoMap.put(nameStr,targetImgUrl.append(File.separator+code.getId()+".png").toString());
			}
			if(CollectionUtils.isNotEmpty(insertCodeList)){
				stutotalityCodeService.saveAll(insertCodeList.toArray(new StutotalityCode[0]));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return logoMap;
	}

	/**
	 * 组装出对应的名称
	 * @param exportDto
	 * @param nameList
	 * @param nameMap
	 */
	public void getName(CodeExportDto exportDto,List<String> nameList,Map<String,String> nameMap){
		String name=exportDto.getName();
		int column=exportDto.getColumn();
		if(exportDto.getNumber1()!=null){
			if(exportDto.getNumber1()%column!=0){
				exportDto.setNumber1((exportDto.getNumber1()/column+1)*column);
			}
			nameMap.put(name+"1",exportDto.getNumber1()+"_1");
			nameList.add(name+"1");
		}
		if(exportDto.getNumber2()!=null){
			if(exportDto.getNumber2()%column!=0){
				exportDto.setNumber2((exportDto.getNumber2()/column+1)*column);
			}
			nameMap.put(name+"2",exportDto.getNumber2()+"_2");
			nameList.add(name+"2");
		}
		if(exportDto.getNumber3()!=null){
			if(exportDto.getNumber3()%column!=0){
				exportDto.setNumber3((exportDto.getNumber3()/column+1)*column);
			}
			nameMap.put(name+"3",exportDto.getNumber3()+"_3");
			nameList.add(name+"3");
		}
		if(exportDto.getNumber4()!=null){
			if(exportDto.getNumber4()%column!=0){
				exportDto.setNumber4((exportDto.getNumber4()/column+1)*column);
			}
			nameMap.put(name+"4",exportDto.getNumber4()+"_4");
			nameList.add(name+"4");
		}
		if(exportDto.getNumber5()!=null){
			if(exportDto.getNumber5()%column!=0){
				exportDto.setNumber5((exportDto.getNumber5()/column+1)*column);
			}
			nameMap.put(name+"5",exportDto.getNumber5()+"_5");
			nameList.add(name+"5");
		}
	}
	public  BufferedImage getQRCode(String contents, int width,int height,File logoFile) throws WriterException, IOException {
		Map<EncodeHintType, Object> hints = new Hashtable<>();
		hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.M);
		hints.put(EncodeHintType.CHARACTER_SET, "UTF8");
		BitMatrix bitMatrix = new MultiFormatWriter().encode(contents, BarcodeFormat.QR_CODE, width, height, hints);
//		bitMatrix=deleteWhite(bitMatrix);
		BufferedImage image = toBufferedImage(bitMatrix);
		if(logoFile!=null && logoFile.exists()) {
			Graphics2D g = image.createGraphics();
			BufferedImage logo = ImageIO.read(logoFile);
//			int widthLogo = logo.getWidth(null)>image.getWidth()*3/20?(image.getWidth()*3/20):logo.getWidth(null);
//			int heightLogo = logo.getHeight(null)>image.getHeight()*3/20?(image.getHeight()*3/20):logo.getWidth(null);
			int widthLogo = logo.getWidth(null);
			int heightLogo = logo.getHeight(null);
			/**logo放在中心 */
			int x = (image.getWidth() - widthLogo) / 2;
			int y = (image.getHeight() - heightLogo) / 2;
			//开始绘制图片
			g.drawImage(logo, x, y, widthLogo, heightLogo, null);
			g.drawRoundRect(x, y, widthLogo, heightLogo, 15, 15);
			g.drawRect(x, y, widthLogo, heightLogo);
			g.dispose();
			logo.flush();
		}
		return image;
	}
	private  BitMatrix deleteWhite(BitMatrix matrix) {
		int[] rec = matrix.getEnclosingRectangle();
		int resWidth = rec[2] + 1;
		int resHeight = rec[3] + 1;

		BitMatrix resMatrix = new BitMatrix(resWidth, resHeight);
		resMatrix.clear();
		for (int i = 0; i < resWidth; i++) {
			for (int j = 0; j < resHeight; j++) {
				if (matrix.get(i + rec[0], j + rec[1]))
					resMatrix.set(i, j);
			}
		}
		return resMatrix;
	}
	private  BufferedImage toBufferedImage(BitMatrix matrix) {
		int width = matrix.getWidth();
		int height = matrix.getHeight();
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				image.setRGB(x, y, matrix.get(x, y) ? 0xFF000000 : 0xFFFFFFFF);
			}
		}
		return image;
	}
	public void deleteAllFile(String path){
		File file=new File(path);
		if(file.isFile()){
			file.delete();
		}else{
			File[] files=file.listFiles();
			if(files==null){
				file.delete();
			}else{
				for(File file1:files){
					deleteAllFile(file1.getAbsolutePath());
				}
				file.delete();
			}
		}
	}
	/*//将二维码写入对应的路径
	targetImgUrl=new StringBuilder(dirStr);
	targetImgUrl.append(File.separator+"stutotality"+File.separator);
	targetImgUrl.append("code"+File.separator);
	targetImgUrl.append(unitId+File.separator);
	String codePath=targetImgUrl.toString();
	File codeFile=new File(codePath);
	if(!codeFile.exists()){
		codeFile.mkdirs();
	}
	targetImgUrl.append(code.getId()+".png");
	if (!ImageIO.write(image, "png", new File(targetImgUrl.toString()))) {
		throw new IOException("Could not write an image of format " + "png" + " to file"  );
	}*/
}
