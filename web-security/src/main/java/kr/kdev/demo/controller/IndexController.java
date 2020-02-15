package kr.kdev.demo.controller;

import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.ast.Node;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.GetMapping;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.nio.charset.StandardCharsets;


@Controller
public class IndexController {

    private String README;

    public IndexController() {

    }

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("README", this.README);
        return "index";
    }

    @PostConstruct
    private void load() throws IOException {
        String realPath = getClass().getResource("/").getPath().replaceAll("/out/production/classes", "") + "README.md";

        FileSystemResource fileSystemResource = new FileSystemResource(realPath);
        String copyToString = StreamUtils.copyToString(fileSystemResource.getInputStream(), StandardCharsets.UTF_8);
        Parser parser = Parser.builder().build();
        Node document = parser.parse(copyToString);
        HtmlRenderer renderer = HtmlRenderer.builder().build();
        this.README = renderer.render(document);
    }
}
