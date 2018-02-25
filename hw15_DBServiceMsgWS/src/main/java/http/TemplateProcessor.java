package http;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Map;

class TemplateProcessor {
    private final Configuration cfg;
    private static final TemplateProcessor instance = new TemplateProcessor();

    private TemplateProcessor() {
        cfg = new Configuration();
//
    }

    static TemplateProcessor getInstance() {
        return instance;
    }

    String getPage(String path, String filename, Map<String, Object> data) throws IOException {

        try (Writer out = new StringWriter()) {
            cfg.setClassForTemplateLoading(this.getClass(), path);
            Template template = cfg.getTemplate(filename);
            template.process(data, out);
            return out.toString();
        } catch (TemplateException e) {
            throw new IOException();
        }
    }
}
