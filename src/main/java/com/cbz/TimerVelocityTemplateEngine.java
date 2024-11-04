package com.cbz;

import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.engine.VelocityTemplateEngine;

import java.io.File;
import java.net.URL;
import java.util.Map;

/**
 * @author HuangYiCheng
 * @since 2024/11/1
 */
public class TimerVelocityTemplateEngine extends VelocityTemplateEngine {
    @Override
    protected void outputCustomFile(Map<String, String> customFile, TableInfo tableInfo, Map<String, Object> objectMap) {
        String otherPath = getPathInfo(OutputFile.other);
        customFile.forEach((key, value) -> {
            String fileName = String.format((otherPath + File.separator + "%s"), key);
            outputFile(new File(fileName), objectMap, value);
        });
    }

}

