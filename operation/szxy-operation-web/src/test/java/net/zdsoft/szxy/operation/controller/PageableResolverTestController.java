package net.zdsoft.szxy.operation.controller;

import net.zdsoft.szxy.plugin.mvc.Response;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author shenke
 * @since 2019/1/14 上午9:38
 */
@RestController
@RequestMapping("/operation/pageable/resolver/test")
public class PageableResolverTestController {

    @GetMapping("")
    public Response execute(Pageable pageable) {

        return Response.ok().data("page", pageable).build();
    }

}
