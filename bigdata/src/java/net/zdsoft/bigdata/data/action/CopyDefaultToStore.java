package net.zdsoft.bigdata.data.action;

import net.zdsoft.system.constant.Constant;
import net.zdsoft.system.remote.service.SysOptionRemoteService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author shenke
 * @since 2018/8/7 上午9:36
 */
@Component
@Lazy(false)
public class CopyDefaultToStore {

    private static final String default_chart_icon = "thumbnail-default.png";
    private static final String default_cockpit_icon = "cockpit_default.png";
    public static final String default_cockpit_icon_path = "/bigdata/" + default_cockpit_icon;
    public static final String default_chart_icon_path = "/bigdata/" + default_chart_icon;
    private Logger logger = LoggerFactory.getLogger(CopyDefaultToStore.class);

    @Autowired
    private SysOptionRemoteService sysOptionRemoteService;
    @Autowired
    private ServletContext servletContext;

    @PostConstruct
    public void copy() {
        copyChartDefaultThumbnail();
        copyCockpitDefaultThumbnail();
    }

    private void copyCockpitDefaultThumbnail() {
        Path destinationPath = Paths.get(sysOptionRemoteService.findValue(Constant.FILE_PATH), default_cockpit_icon_path);
        if (Files.exists(destinationPath, LinkOption.NOFOLLOW_LINKS)) {
            logger.info("default cockpit icon already exists, copy over");
            return;
        }

        Path source = Paths.get(servletContext.getRealPath("/static/bigdata/images/cockpit_default.png"));
        if (!Files.exists(source)) {
            logger.error("default cockpit icon not exists, can't copy to store dirs ");
            return;
        }
        try {
            Files.copy(source, destinationPath);
        } catch (IOException e) {
            logger.warn(e.getMessage());
        }
    }

    private void copyChartDefaultThumbnail() {
        Path destinationPath = Paths.get(sysOptionRemoteService.findValue(Constant.FILE_PATH), "/bigdata/" + default_chart_icon);
        if (Files.exists(destinationPath, LinkOption.NOFOLLOW_LINKS)) {
            logger.info("default chart icon already exists, copy over");
            return;
        }
        Path source = Paths.get(servletContext.getRealPath("/static/bigdata/images/" + default_chart_icon));
        if (!Files.exists(source)) {
            logger.error("default chart icon not exists, can't copy to store dirs ");
            return;
        }
        try {
            Files.copy(source, destinationPath);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }
}
