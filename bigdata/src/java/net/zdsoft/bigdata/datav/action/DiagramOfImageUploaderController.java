package net.zdsoft.bigdata.datav.action;

import com.alibaba.fastjson.JSONObject;
import net.zdsoft.bigdata.data.vo.Response;
import net.zdsoft.bigdata.datav.entity.Diagram;
import net.zdsoft.bigdata.datav.enu.DiagramEnum;
import net.zdsoft.bigdata.datav.render.crete.custom.ImageOption;
import net.zdsoft.bigdata.datav.service.DiagramService;
import net.zdsoft.bigdata.v3.index.action.BigdataBaseAction;
import net.zdsoft.system.constant.Constant;
import net.zdsoft.system.remote.service.SysOptionRemoteService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.ServletContext;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 单张图片和轮播图上传图片
 * 彻底乱了
 * @author shenke
 * @since 2018/10/31 上午10:51
 */
@Lazy(false)
@Controller
@RequestMapping("/bigdata/datav/diagram-image/uploader")
public class DiagramOfImageUploaderController extends BigdataBaseAction {

    private Logger logger = LoggerFactory.getLogger(DiagramOfImageUploaderController.class);

    @Resource
    private DiagramService diagramService;
    @Resource
    private SysOptionRemoteService sysOptionRemoteService;
    @Resource
    private ServletContext servletContext;
    private String filePath;
    private String slideFile = ImageOption.PATH + File.separator + "demo" + File.separator + "slide.png";
    private String singleFile = ImageOption.PATH + File.separator + "demo" + File.separator + "single.png";

    @PostConstruct
    public void init() {

        filePath = sysOptionRemoteService.findValue(Constant.FILE_PATH);
        /*String slidePath = servletContext.getRealPath("/static/bigdata/datav/images/slide-pic-grey.png");
        String singlePath = servletContext.getRealPath("/static/bigdata/datav/images/single-noname-pic-grey.png");
        try {
            String dir = filePath + File.separator + ImageOption.PATH + File.separator + "demo";
            if (!Files.exists(Paths.get(dir))) {
                new File(dir).mkdirs();
            }
            if (Files.exists(Paths.get(slidePath))) {

                if (!Files.exists(Paths.get(filePath + File.separator + slideFile))) {
                    Files.copy(Paths.get(slidePath), Paths.get(filePath + File.separator + slideFile));
                }
            }
            if (Files.exists(Paths.get(singlePath))) {

                if (!Files.exists(Paths.get(filePath + File.separator + singleFile))) {
                    Files.copy(Paths.get(singlePath), Paths.get(filePath + File.separator + singleFile));
                }
            }
        } catch (IOException e) {
            logger.error("Copy 单张图片、轮播图片的默认图片失败", e);
        }*/
    }

    @ResponseBody
    @PostMapping("/{diagramId}")
    public Response doUpload(@PathVariable("diagramId") String diagramId,
                             @RequestParam("image") MultipartFile image,
                             @RequestParam("index") String index,
                             @RequestParam("suffix") String suffix) {
        Diagram diagram = diagramService.findOne(diagramId);
        if (diagram == null) {
            return Response.error().message("图表已经不存在了").build();
        }
        String humanPath = ImageOption.PATH + File.separator + getLoginInfo().getUnitId() + File.separator + diagramId;
        if (!Files.exists(Paths.get(filePath + File.separator + humanPath))) {
            new File(filePath + File.separator + humanPath).mkdirs();
        }
        try {
            if (DiagramEnum.SINGLE_IMAGE.getType().equals(diagram.getDiagramType())) {
                //Base64ConvertUtils.convert2Path(image, , suffix);
                image.transferTo(new File(filePath + File.separator + humanPath + File.separator + "1." + suffix));
                ImageOption imageOption = new ImageOption();
                imageOption.setImagePath(humanPath + File.separator + "1." + suffix);
                imageOption.setIndex("1");
                diagram.setDatasourceValueJson(JSONObject.toJSONString(Collections.singleton(imageOption)));
                diagramService.save(diagram);
                return Response.ok().message("").data(humanPath + File.separator + "1." + suffix).build();
            } else {
                //Base64ConvertUtils.convert2Path(image, filePath + File.separator + humanPath + File.separator + index + "." + suffix, suffix);
                image.transferTo(new File(filePath + File.separator + humanPath + File.separator + index + "." + suffix));
                List<ImageOption> imageOptions = JSONObject.parseArray(diagram.getDatasourceValueJson(), ImageOption.class);
                if (imageOptions.stream().noneMatch(imageOption -> imageOption.getIndex().equals(index))) {
                    ImageOption imageOption = new ImageOption();
                    imageOption.setIndex(index);
                    imageOption.setImagePath(humanPath + File.separator + index + "." + suffix);
                    imageOptions.add(imageOption);
                    diagram.setDatasourceValueJson(JSONObject.toJSONString(imageOptions));
                    diagramService.save(diagram);
                } else {
                    boolean update = false;
                    for (ImageOption imageOption : imageOptions) {
                        if (index.equals(imageOption.getIndex())) {
                            imageOption.setImagePath(humanPath + File.separator + index + "." + suffix);
                            update = true;
                            break;
                        }
                    }
                    if (update) {
                        diagram.setDatasourceValueJson(JSONObject.toJSONString(imageOptions));
                        diagramService.save(diagram);
                    }
                }
                return Response.ok().message("").data(humanPath + File.separator + index + "." + suffix).build();
            }
        } catch (IOException e) {
            logger.error("Upload Image error", e);
            return Response.error().message(e.getMessage()).build();
        }
    }

    @ResponseBody
    @GetMapping("/delete/{diagramId}")
    public Response doDeleteImage(@PathVariable("diagramId") String diagramId,
                                  @RequestParam("index") String index) {
        Diagram diagram = diagramService.findOne(diagramId);
        if (diagram == null) {
            return Response.error().message("图表已经不存在了").build();
        }
        List<ImageOption> imageOptions = JSONObject.parseArray(diagram.getDatasourceValueJson(), ImageOption.class);
        List<ImageOption> newOptions = new ArrayList<>(imageOptions.size());
        String deletePath = null;
        for (ImageOption imageOption : imageOptions) {
            if (!imageOption.getIndex().equals(index)) {
                newOptions.add(imageOption);
            } else {
                deletePath = imageOption.getImagePath();
            }
        }
        //delete file
        if (!"/bigdata/diagram-image/demo/slide.png".equals(deletePath)) {
            try {
                Files.deleteIfExists(Paths.get(filePath + File.separator + deletePath));
            } catch (IOException e) {
                logger.error("delete image error: {}", e.getMessage());
            }
        }
        diagram.setDatasourceValueJson(JSONObject.toJSONString(newOptions));
        diagramService.save(diagram);
        return Response.ok().build();
    }
}
